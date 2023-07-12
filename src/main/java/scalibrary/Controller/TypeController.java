package scalibrary.Controller;

import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import scalibrary.Repository.TypeRepository;
import scalibrary.Entities.Type;

public class TypeController {
    public static void getType(Context ctx, TypeRepository typeRepository) throws TransactionException{
        Validator<Integer> type_id = ctx.pathParamAsClass("id", Integer.class);
        try{
            Type type = new Type(typeRepository.getType(type_id.get()));
            ctx.json(type);
        }catch (TransactionException e){
            ctx.status(500);
            ctx.json("{\"status\": \"Failed to get type\"}");
            throw e;
        }
    }
}
