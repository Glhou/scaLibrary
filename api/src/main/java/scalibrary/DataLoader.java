package scalibrary;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Delete;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.service.TransactionFactory;
import com.scalar.db.io.Key;
import com.github.javafaker.Faker;
import java.util.Optional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class DataLoader implements AutoCloseable {
  private final DistributedTransactionManager manager;
  private final Faker faker;

  private List<String> locations = Arrays.asList(
      "Hiyoshi", "Yagami", "Mita", "SFC");
  private List<String> types = Arrays.asList(
      "Book", "Magazine", "Newspaper", "Journal");
  private List<String> statuses = Arrays.asList(
      "Available", "Borrowed");

  public DataLoader() throws IOException {
    TransactionFactory factory = TransactionFactory.create("scalardb.properties");
    manager = factory.getTransactionManager();
    faker = new Faker();
  }

  public DistributedTransactionManager getManager() {
    return manager;
  }

  public void loadInitialData() throws TransactionException {
    DistributedTransaction transaction = null;
    try {
      transaction = manager.start();
      // Loading Locations
      for (int i = 0; i < locations.size(); i++) {
        loadLocationIfNotExists(transaction, i, locations.get(i));
      }
      // Loading Types
      for (int i = 0; i < types.size(); i++) {
        loadTypeIfNotExists(transaction, i, types.get(i));
      }
      // Loading Statuses
      for (int i = 0; i < statuses.size(); i++) {
        loadStatusIfNotExists(transaction, i, statuses.get(i));
      }
      transaction.commit();
    } catch (TransactionException e) {
      if (transaction != null) {
        transaction.abort();
      }
      throw e;
    }
  }

  public void loadFixtureData() throws TransactionException {
    DistributedTransaction transaction = null;
    try {
      int number_users = 100;
      int number_documents = 1000;
      transaction = manager.start();
      Map<Integer, Integer> user_not_null_ids = new HashMap<>();
      // Loading Documents
      for (int i = 0; i < number_documents; i++) {
        Integer user_id = -1;
        Random r = new Random();
        if (r.nextInt(3) != 0) {
          user_id = i % number_users;
          user_not_null_ids.put(user_id, user_not_null_ids.getOrDefault(user_id, 0));
        }
        loadDocumentIfNotExists(transaction, i, faker.book().title(), faker.book().author(), user_id, i % types.size(),
            i % statuses.size(), i % locations.size());
      }
      // Loading Users
      for (int i = 0; i < number_users; i++) {
        loadUserIfNotExists(transaction, i, faker.name().fullName(), i % locations.size(), 100,
            user_not_null_ids.get(i));
      }
      transaction.commit();
    } catch (TransactionException e) {
      if (transaction != null) {
        transaction.abort();
      }
      throw e;
    }
  }

  private void loadUserIfNotExists(
      DistributedTransaction transaction,
      int user_id,
      String name,
      int location_id,
      int document_limit,
      int document_total)
      throws TransactionException {
    Optional<Result> user = transaction.get(
        Get.newBuilder()
            .namespace("user")
            .table("users")
            .partitionKey(Key.ofInt("user_id", user_id))
            .build());
    if (!user.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace("user")
              .table("users")
              .partitionKey(Key.ofInt("user_id", user_id))
              .textValue("name", name)
              .intValue("location_id", location_id)
              .intValue("document_limit", document_limit)
              .intValue("document_total", document_total)
              .build());
    }
  }

  private void loadDocumentIfNotExists(
      DistributedTransaction transaction,
      int document_id,
      String name,
      String author,
      int user_id,
      int type_id,
      int status_id,
      int location_id)
      throws TransactionException {
    Optional<Result> document = transaction.get(
        Get.newBuilder()
            .namespace("document")
            .table("documents")
            .partitionKey(Key.ofInt("document_id", document_id))
            .build());
    if (!document.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace("document")
              .table("documents")
              .partitionKey(Key.ofInt("document_id", document_id))
              .textValue("name", name)
              .textValue("author", author)
              .intValue("user_id", user_id)
              .intValue("type_id", type_id)
              .intValue("status_id", status_id)
              .intValue("location_id", location_id)
              .build());
    }
  }

  private void loadLocationIfNotExists(
      DistributedTransaction transaction,
      int location_id,
      String name)
      throws TransactionException {
    Optional<Result> location = transaction.get(
        Get.newBuilder()
            .namespace("document")
            .table("location")
            .partitionKey(Key.ofInt("location_id", location_id))
            .build());
    if (!location.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace("document")
              .table("location")
              .partitionKey(Key.ofInt("location_id", location_id))
              .textValue("name", name)
              .build());
    }
  }

  private void loadTypeIfNotExists(
      DistributedTransaction transaction,
      int type_id,
      String name) throws TransactionException {
    Optional<Result> type = transaction.get(
        Get.newBuilder()
            .namespace("document")
            .table("type")
            .partitionKey(Key.ofInt("type_id", type_id))
            .build());
    if (!type.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace("document")
              .table("type")
              .partitionKey(Key.ofInt("type_id", type_id))
              .textValue("name", name)
              .build());
    }
  }

  private void loadStatusIfNotExists(
      DistributedTransaction transaction,
      int status_id,
      String name) throws TransactionException {
    Optional<Result> status = transaction.get(
        Get.newBuilder()
            .namespace("document")
            .table("status")
            .partitionKey(Key.ofInt("status_id", status_id))
            .build());
    if (!status.isPresent()) {
      transaction.put(
          Put.newBuilder()
              .namespace("document")
              .table("status")
              .partitionKey(Key.ofInt("status_id", status_id))
              .textValue("name", name)
              .build());
    }
  }

  @Override
  public void close() {
    manager.close();
  }
}
