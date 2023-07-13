package scalibrary.Controller;

import java.util.ArrayList;
import java.util.List;

import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import scalibrary.Repository.TypeRepository;
import scalibrary.Annotation.Docs;
import scalibrary.Entities.Type;

public class TypeController extends Controller {
    @Docs(route = "/type", method = "get", input = "{}", output = "[{\"type_id\": \"int:type_id\",\"name\": \"name\"}]", description = "Get all types.")
    public static void getAll(Context ctx, TypeRepository typeRepository) throws TransactionException {
        try {
            List<Result> type_result = typeRepository.getAll();
            List<Type> types = new ArrayList<Type>();
            for (Result r : type_result) {
                types.add(new Type(r));
            }
            ctx.json(types);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    @Docs(route = "/type/{id}", method = "get", input = "{}", output = "{\"type_id\": \"int:type_id\",\"name\": \"name\"}", description = "Get a type by id.")
    public static void getType(Context ctx, TypeRepository typeRepository) throws TransactionException {
        Validator<Integer> type_id = ctx.pathParamAsClass("id", Integer.class);
        try {
            Type type = new Type(typeRepository.get(type_id.get()));
            ctx.json(type);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\": \"Failed to get type\"}");
            throw e;
        }
    }
}
