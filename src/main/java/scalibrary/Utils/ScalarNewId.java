package scalibrary.Utils;

import com.scalar.db.exception.transaction.TransactionException;


import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Scan;
import com.scalar.db.api.Result;

import java.util.List;

public class ScalarNewId{
    public int newId(DistributedTransactionManager manager,String namespace,String table, String partitionKey)throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                    .namespace(namespace)
                    .table(table)
                    .all()
                    .build();
            List<Result> elements = transaction.scan(scan);
            int max_id = 0;
            for(Result element : elements){
                int element_id = element.getInt(partitionKey);
                if (element_id > max_id){
                    max_id = element_id;
                }
            }
            transaction.commit();
            return max_id + 1;
        }catch(TransactionException e){
            if (transaction != null){
                transaction.abort();
            }
        }
        return -1;
    }
}