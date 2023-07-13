package scalibrary.Repository;

import java.io.IOException;
import java.util.Optional;

import com.scalar.db.api.ConditionBuilder;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Scan;
import com.scalar.db.api.MutationCondition;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;

import java.util.ArrayList;
import java.util.List;

import scalibrary.Entities.User;
import scalibrary.Entities.Document;
import scalibrary.Utils.ScalarNewId;

public class DocumentRepository extends Repository {
    public DocumentRepository() throws IOException{
        super();
        this.namespace = "document";
        this.table = "documents";
        this.partitionKey = "document_id";
    }

    public List<Result> getDocumentByName(String name) throws TransactionException{
        List<Result> r = getAll();
        List<Result> documents = new ArrayList<Result>();
        for (Result result : r){
            if (result.getText("name").contains(name)){
                documents.add(result);
            }
        }
        return documents;

    }
    
    public List<Result> getDocumentsByLocation(int location_id) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                    .namespace("document")
                    .table("documents")
                    .indexKey(Key.ofInt("location_id", location_id))
                    .build();
            List<Result> documents = transaction.scan(scan);
            transaction.commit();
            return documents;
        }catch(TransactionException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }
    }

    public void newDocument(
        String name,
        String author,
        int type_id,
        int location_id
      ) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            // get new id
            int new_id = (new ScalarNewId()).newId(manager, "document", "documents", "document_id");
            transaction = manager.start();
            Put put = Put.newBuilder()
            .namespace("document")
            .table("documents")
            .partitionKey(Key.ofInt("document_id",new_id))
            .textValue("name", name)
            .textValue("author", author)
            .intValue("user_id", -1)
            .intValue("type_id", type_id)
            .intValue("location_id", location_id)
            .build();
            transaction.put(put);
            transaction.commit();
        }catch(TransactionException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }
    }

    public void loanDocument(int user_id, int document_id) throws TransactionException, CrudException, RuntimeException {
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get getUser = Get.newBuilder()
                .namespace("user")
                .table("users")
                .partitionKey(Key.ofInt("user_id",user_id))
                .build();
            User user = new User(transaction.get(getUser));
            Get getDocument = Get.newBuilder()
                .namespace("document")
                .table("documents")
                .partitionKey(Key.ofInt("document_id",document_id))
                .build();
            Document document = new Document(transaction.get(getDocument));
            
            if (document.user_id != -1){
                throw new RuntimeException("Document already loaned");
            }

            if (user.document_total >= user.document_limit){
                throw new RuntimeException("User has reached document limit");
            }

            document.user_id = user.user_id;
            Put putDocument = Put.newBuilder()
                    .namespace("document")
                    .table("documents")
                    .partitionKey(Key.ofInt("document_id",document.document_id))
                    .intValue("user_id",document.user_id)
                    .build();
            Put putUser = Put.newBuilder()
                    .namespace("user")
                    .table("users")
                    .partitionKey(Key.ofInt("user_id", user.user_id))
                    .intValue("document_total",user.document_total + 1)
                    .build();
            transaction.put(putDocument);
            transaction.put(putUser);
            transaction.commit();
        }catch(CrudException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }catch(TransactionException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }catch(RuntimeException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }
    }

    public void returnDocument(int user_id, int document_id) throws TransactionException, RuntimeException {
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get userGet = Get.newBuilder()
                .namespace("user")
                .table("users")
                .partitionKey(Key.ofInt("user_id", user_id))
                .build();
            User user = new User(transaction.get(userGet));
            Get documentGet = Get.newBuilder()
                .namespace("document")
                .table("documents")
                .partitionKey(Key.ofInt("document_id", document_id))
                .build();
            Document document = new Document(transaction.get(documentGet));
            if (document.user_id != user.user_id){
                throw new RuntimeException("Document not loaned by user");
            }
            Put documentPut = Put.newBuilder()
                .namespace("document")
                .table("documents")
                .partitionKey(Key.ofInt("document_id", document.document_id))
                .intValue("user_id", -1)
                .build();
            Put userPut = Put.newBuilder()
                .namespace("user")
                .table("users")
                .partitionKey(Key.ofInt("user_id", user.user_id))
                .intValue("document_total", user.document_total - 1)
                .build();
            transaction.put(documentPut);
            transaction.put(userPut);
            transaction.commit();
        }catch(TransactionException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }catch(RuntimeException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }
    }

    

    
}
