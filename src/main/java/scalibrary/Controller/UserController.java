package scalibrary.Controller;

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
import scalibrary.Entities.Document;

import org.json.JSONObject;

public class UserController{
    public static void createUser(Context ctx, UserRepository userRepository) throws TransactionException{
        String request_body = ctx.body();
        JSONObject json = new JSONObject(request_body);
        try{
            String name = (String) json.get("name");
            Integer location_id = (Integer) json.get("location_id");
            Integer document_limit = (Integer) json.get("document_limit");
            userRepository.newUser(name, location_id, document_limit);
            ctx.json("{\"status\": \"success\"}");
        } catch(TransactionException e){
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    public static void getUser(Context ctx,UserRepository userRepository) throws TransactionException{
        Validator<Integer> user_id = ctx.pathParamAsClass("id", Integer.class);
        try{
            Optional<Result> user = userRepository.getUser(user_id.get());
            ctx.json(new User(user));
        }catch(TransactionException e){
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    public static void editUserLocation(Context ctx, UserRepository userRepository) throws TransactionException{
        Validator<Integer> user_id = ctx.pathParamAsClass("id", Integer.class);
        String request_body = ctx.body();
        JSONObject json = new JSONObject(request_body);
        int location_id = (int) json.get("location_id");
        try{
            userRepository.setLocation(user_id.get(), location_id);
            ctx.json("{\"status\":\"success\"}");
        }catch(TransactionException e){
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    public static void getLoanedDocument(Context ctx, UserRepository userRepository) throws TransactionException{
        Validator<Integer> user_id = ctx.pathParamAsClass("id", Integer.class);
        try{
            List<Result> result_documents = userRepository.getLoanedDocument(user_id.get());
            List<Document> list = new ArrayList<Document>();
            for (Result result : result_documents){
                list.add(new Document(result));
            }
            ctx.json(list);
        }catch(TransactionException e){
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }
}