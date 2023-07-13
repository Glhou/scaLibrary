package scalibrary.Controller;

import java.util.ArrayList;
import java.util.List;

import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import scalibrary.Annotation.Docs;
import scalibrary.Entities.Status;
import scalibrary.Repository.StatusRepository;

public class StatusController extends Controller {
    @Docs(route = "/status", method = "get", input = "{}", output = "[{\"status_id\": \"int:status_id\",\"name\": \"name\"}]", description = "Get all status.")
    public static void getAll(Context ctx, StatusRepository statusRepository) throws TransactionException {
        try {
            List<Result> status_result = statusRepository.getAll();
            List<Status> status_list = new ArrayList<Status>();
            for (Result r : status_result) {
                status_list.add(new Status(r));
            }
            ctx.json(status_list);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    @Docs(route = "/status/{id}", method = "get", input = "{}", output = "{\"status_id\": \"int:status_id\",\"name\": \"name\"}", description = "Get a status by id.")
    public static void getStatus(Context ctx, StatusRepository statusRepository) throws TransactionException {
        Validator<Integer> status_id = ctx.pathParamAsClass("id", Integer.class);
        try {
            Status status = new Status(statusRepository.get(status_id.get()));
            ctx.json(status);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\": \"Failed to get status\"}");
            throw e;
        }
    }

}
