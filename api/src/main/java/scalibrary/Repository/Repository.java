package scalibrary.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;


public class Repository implements AutoCloseable{
    public final DistributedTransactionManager manager;
    public String namespace;
    public String table;
    public String partitionKey;

    public Repository() throws IOException{
        TransactionFactory factory = TransactionFactory.create("scalardb.properties");
        manager = factory.getTransactionManager();
    }

    public List<Result> getAll() throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                .namespace(this.namespace)
                .table(this.table)
                .all()
                .build();
            List<Result> r = transaction.scan(scan);
            transaction.commit();
            return r;
        }catch (TransactionException e){
            if (transaction != null){
                transaction.abort();
            }
            throw e;
        }
    }

    public Optional<Result> get(int id)throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                    .namespace(this.namespace)
                    .table(this.table)
                    .partitionKey(Key.ofInt(this.partitionKey, id))
                    .build();
            Optional<Result> r = transaction.get(get);
            if (!r.isPresent()){
                throw new RuntimeException(this.namespace+"."+this.table+":"+this.partitionKey+"="+id+" not found");
            }
            transaction.commit();
            return r;
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
