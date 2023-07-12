package scalibrary.Repository;

import java.io.IOException;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.service.TransactionFactory;


public class Repository implements AutoCloseable{
    public final DistributedTransactionManager manager;

    public Repository() throws IOException{
        TransactionFactory factory = TransactionFactory.create("scalardb.properties");
        manager = factory.getTransactionManager();
    }

    @Override
    public void close() throws Exception{
        manager.close();
    }
}
