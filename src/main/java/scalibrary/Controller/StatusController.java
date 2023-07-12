package scalibrary.Controller;

import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import scalibrary.Entities.Status;
import scalibrary.Repository.StatusRepository;

public class StatusController {
    public static void getStatus(Context ctx, StatusRepository statusRepository) throws TransactionException{
        Validator<Integer> status_id = ctx.pathParamAsClass("id", Integer.class);
        try{
            Status status = new Status(statusRepository.getStatus(status_id.get()));
            ctx.json(status);
        }catch (TransactionException e){
            ctx.status(500);
            ctx.json("{\"status\": \"Failed to get status\"}");
            throw e;
        }
    }
    
}
