package scalibrary.Repository;

import io.javalin.http.Context;
import java.util.*;
import com.scalar.db.service.TransactionFactory;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.io.Key;
import com.scalar.db.exception.transaction.TransactionException;
import io.javalin.validation.Validator;
import java.io.IOException;

import scalibrary.Utils.ScalarNewId;
import scalibrary.Entities.User;
import scalibrary.Entities.Document;
public class UserRepository extends Repository{
    public UserRepository() throws IOException{
        super();
        this.namespace = "user";
        this.table = "users";
        this.partitionKey = "user_id";
    }    


    public void newUser(
        String name,
        int location_id,
        int document_limit 
        ) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            // get new id
            int new_id = (new ScalarNewId()).newId(manager, "user", "users", "user_id");
            transaction = manager.start();
            Put put = Put.newBuilder()
            .namespace("user")
            .table("users")
            .partitionKey(Key.ofInt("user_id",new_id))
            .textValue("name", name)
            .intValue("location_id", location_id)
            .intValue("document_limit", document_limit)
            .intValue("document_total", 0)
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

    public void setLocation(
        int user_id,
        int location_id
    ) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                    .namespace("user")
                    .table("users")
                    .partitionKey(Key.ofInt("user_id", user_id))
                    .build();
            User user = new User(transaction.get(get));
            Put put = Put.newBuilder()
            .namespace("user")
            .table("users")
            .partitionKey(Key.ofInt("user_id",user.user_id))
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

    public List<Result> getLoanedDocument(
        int user_id
    ) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                    .namespace("user")
                    .table("users")
                    .partitionKey(Key.ofInt("user_id", user_id))
                    .build();
            User user = new User(transaction.get(get));

            Scan scan = Scan.newBuilder()
                .namespace("document")
                .table("documents")
                .indexKey(Key.ofInt("user_id", user_id))
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

}
