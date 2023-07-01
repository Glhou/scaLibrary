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



public class DocumentRepository implements AutoCloseable {
    private final DistributedTransactionManager manager;

    public DocumentRepository() throws IOException{
        TransactionFactory factory = TransactionFactory.create("scalardb.properties");
        manager = factory.getTransactionManager();
    }
    
    public Optional<Result> getDocument(int document_id)throws TransactionException{
        DistributedTransaction transaction = null;
        try{
            transaction = manager.start();
            Get get = Get.newBuilder()
                    .namespace("document")
                    .table("documents")
                    .partitionKey(Key.ofInt("document_id", document_id))
                    .build();
            Optional<Result> document = transaction.get(get);
            if (!document.isPresent()){
                throw new RuntimeException("Document not found");
            }
            transaction.commit();
            return document;
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
