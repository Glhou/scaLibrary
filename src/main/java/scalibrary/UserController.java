package scalibrary;

import io.javalin.http.Context;

import java.io.IOException;
import java.util.*;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Result;
import com.scalar.db.api.Get;
import com.scalar.db.io.Key;
import com.scalar.db.exception.transaction.TransactionException;
import io.javalin.validation.Validator;
import scalibrary.Repository.UserRepository;
import scalibrary.Entities.User;

public class UserController{
    public static void createUser(Context ctx){
        ctx.result("Create User");
    }

    public static void getUser(Context ctx,UserRepository userRepository) throws TransactionException{
        DistributedTransaction transaction = null;
        Validator<Integer> user_id = ctx.pathParamAsClass("id", Integer.class);
        try{
            Optional<Result> user = userRepository.getUser(user_id.get());
            ctx.json(new User(user));
        }catch(TransactionException e){
            throw e;
        }
    }

    public static void editUserLocation(Context ctx){
        ctx.result("Edit User Location");
    }

    public static void getLoanedDocument(Context ctx){
        ctx.result("Get Loaned Document");
    }
}