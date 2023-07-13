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
        this.namespace = "document";
        this.table = "type";
        this.partitionKey = "type_id";
    }
}
