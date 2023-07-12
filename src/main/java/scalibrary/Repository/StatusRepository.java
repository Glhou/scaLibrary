package scalibrary.Repository;

import java.io.IOException;
import java.util.Optional;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Get;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import scalibrary.Entities.Status;

public class StatusRepository extends Repository{
    public StatusRepository() throws IOException{
        super();
    }

    public Optional<Result> getStatus(int status_id) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                    .namespace("document")
                    .table("status")
                    .partitionKey(Key.ofInt("status_id", status_id))
                    .build();
            Optional<Result> status = transaction.get(get);
            transaction.commit();
            return status;
        }catch (TransactionException e){
            if(transaction != null){
                transaction.abort();
            }
            throw e;
        }
    }
    
}
