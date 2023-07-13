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
        this.namespace = "document";
        this.table = "status";
        this.partitionKey = "status_id";
    }
}
