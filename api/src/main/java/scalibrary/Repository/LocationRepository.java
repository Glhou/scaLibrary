package scalibrary.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Get;
import com.scalar.db.api.Result;
import com.scalar.db.api.Scan;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;

public class LocationRepository extends Repository {
    public LocationRepository() throws IOException{
        super();
        this.namespace = "document";
        this.table = "location";
        this.partitionKey = "location_id";
    }
}
