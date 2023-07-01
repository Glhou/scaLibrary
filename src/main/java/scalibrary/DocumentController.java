package scalibrary;

import io.javalin.http.Context;
import java.util.*;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Result;
import io.javalin.validation.Validator;


import scalibrary.Entities.Document;
import scalibrary.Repository.DocumentRepository;

public class DocumentController{
    public static void createDocument(Context ctx){
        ctx.result("Create Document");
    }

    public static void getDocument(Context ctx,DocumentRepository documentRepository) throws TransactionException{
        DistributedTransaction transaction = null;
        Validator<Integer> document_id = ctx.pathParamAsClass("id", Integer.class);
        try{
            Optional<Result> document = documentRepository.getDocument(document_id.get());
            ctx.json(new Document(document));
        }catch(TransactionException e){
            throw e;
        }
    }

    public static void loanDocument(Context ctx){
        ctx.result("Loan Document");
    }

    public static void returnDocument(Context ctx){
        ctx.result("Return Document");
    }

    public static void getDocumentByLocation(Context ctx){
        ctx.result("Get Document by Location");
    }
}