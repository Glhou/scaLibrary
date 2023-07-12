package scalibrary.Repository;

import java.io.IOException;
import java.util.Optional;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Get;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;

public class LocationRepository extends Repository {
    public LocationRepository() throws IOException{
        super();
    }

    public Optional<Result> getLocation(int location_id) throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                .namespace("document")
                .table("location")
                .partitionKey(Key.ofInt("location_id",location_id))
                .build();
            Optional<Result> result = transaction.get(get);
            transaction.commit();
            return result;
        }catch (TransactionException e){
            if(transaction != null){
                transaction.abort();
            }
            throw e;
        }
    }
    
}
