package scalibrary.Repository;

import io.javalin.http.Context;
import java.util.*;
import com.scalar.db.service.TransactionFactory;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Result;
import com.scalar.db.api.Get;
import com.scalar.db.io.Key;
import com.scalar.db.exception.transaction.TransactionException;
import io.javalin.validation.Validator;
import java.io.IOException;

public class UserRepository implements AutoCloseable{
    private final DistributedTransactionManager manager;

    public UserRepository() throws IOException{
        TransactionFactory factory = TransactionFactory.create("scalardb.properties");
        manager = factory.getTransactionManager();
    }
    
    public Optional<Result> getUser(int user_id)throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                    .namespace("user")
                    .table("users")
                    .partitionKey(Key.ofInt("user_id", user_id))
                    .build();
            Optional<Result> user = transaction.get(get);
            if (!user.isPresent()){
                throw new RuntimeException("User not found");
            }
            transaction.commit();
            return user;
        }catch(TransactionException e){
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    @Override
    public void close() throws Exception{
        manager.close();
    }
}
