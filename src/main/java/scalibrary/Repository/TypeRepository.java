package scalibrary.Repository;

import java.io.IOException;
import java.util.Optional;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Get;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;

public class TypeRepository extends Repository{
    public TypeRepository() throws IOException{
        super();
    }

    public Optional<Result> getType(int type_id) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                .namespace("document")
                .table("type")
                .partitionKey(Key.ofInt("type_id",type_id))
                .build();
            Optional<Result> result = transaction.get(get);
            transaction.commit();
            return result;
        }catch (TransactionException e ){
            if(transaction != null){
                transaction.abort();
            }
            throw e;
        }

    }
}
