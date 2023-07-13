package scalibrary.Controller;

import io.javalin.http.Context;
import java.util.*;

import org.json.JSONObject;

import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Result;
import io.javalin.validation.Validator;
import io.javalin.validation.BodyValidator;
import scalibrary.Annotation.Docs;
import scalibrary.Entities.Document;
import scalibrary.Repository.DocumentRepository;

public class DocumentController extends Controller {
    @Docs(route = "/document", method = "post", input = "{\"name\": \"name\",\"author\": \"author\",\"type_id\": \"int:type_id\",\"location_id\": \"int:location_id\"}", output = "{}", description = "Create a new document.")
    public static void createDocument(Context ctx, DocumentRepository documentRepository) throws TransactionException {
        String request_body = ctx.body();
        JSONObject json = new JSONObject(request_body);
        try {
            String name = (String) json.get("name");
            String author = (String) json.get("author");
            Integer type_id = (Integer) json.get("type_id");
            Integer location_id = (Integer) json.get("location_id");
            documentRepository.newDocument(name, author, type_id, location_id);
            ctx.json("{\"status\": \"success\"}");
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    @Docs(route = "/document", method = "get", input = "{}", output = "[{\"document_id\": \"int:document_id\",\"name\": \"name\",\"author\": \"author\",\"type_id\": \"int:type_id\",\"location_id\": \"int:location_id\"}]", description = "Get all documents.")
    public static void getAll(Context ctx, DocumentRepository documentRepository) throws TransactionException {
        try {
            List<Result> document_result = documentRepository.getAll();
            List<Document> documents = new ArrayList<Document>();
            for (Result r : document_result) {
                documents.add(new Document(r));
            }
            ctx.json(documents);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    @Docs(route = "/document/{id}", method = "get", input = "{}", output = "{\"id\": \"int:id\",\"name\": \"name\",\"author\": \"author\",\"type_id\": \"int:type_id\",\"location_id\": \"int:location_id\"}", description = "Get a document by id.")
    public static void getDocument(Context ctx, DocumentRepository documentRepository) throws TransactionException {
        Validator<Integer> document_id = ctx.pathParamAsClass("id", Integer.class);
        try {
            Optional<Result> document = documentRepository.get(document_id.get());
            ctx.json(new Document(document));
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    @Docs(route = "/document/name/{name}", method = "get", input = "{}", output = "[{\"document_id\": \"int:document_id\",\"name\": \"name\",\"author\": \"author\",\"type_id\": \"int:type_id\",\"location_id\": \"int:location_id\"}]", description = "Get documents by name (even incomplete).")
    public static void getDocumentByName(Context ctx, DocumentRepository documentRepository)
            throws TransactionException {
        Validator<String> document_name = ctx.pathParamAsClass("name", String.class);
        try {
            List<Result> documents_result = documentRepository.getDocumentByName(document_name.get());
            List<Document> documents = new ArrayList<Document>();
            for (Result r : documents_result) {
                documents.add(new Document(r));
            }
            ctx.json(documents);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    @Docs(route = "/document/{id}/loan", method = "post", input = "{\"user_id\":\"int:user_id\"}", output = "{}", description = "Loan document {id} for user with user_id.")
    public static void loanDocument(Context ctx, DocumentRepository documentRepository)
            throws TransactionException, CrudException {
        Validator<Integer> document_id = ctx.pathParamAsClass("id", Integer.class);
        String request_body = ctx.body();
        JSONObject json = new JSONObject(request_body);
        int user_id = (int) json.get("user_id");
        try {
            documentRepository.loanDocument(user_id, document_id.get());
            ctx.json("{\"status\":\"success\"}");
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        } catch (RuntimeException e) {
            ctx.status(400);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
        }
    }

    @Docs(route = "/document/{id}/return", method = "post", input = "{\"user_id\":\"int:user_id\"}", output = "{}", description = "Return document {id} for user with user_id.")
    public static void returnDocument(Context ctx, DocumentRepository documentRepository) throws TransactionException {
        Validator<Integer> document_id = ctx.pathParamAsClass("id", Integer.class);
        String request_body = ctx.body();
        JSONObject json = new JSONObject(request_body);
        int user_id = (int) json.get("user_id");
        try {
            documentRepository.returnDocument(user_id, document_id.get());
            ctx.json("{\"status\":\"success\"}");
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        } catch (RuntimeException e) {
            ctx.status(400);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
        }
    }

    @Docs(route = "/document/{id}/location", method = "get", input = "{}", output = "[{\"document_id\": \"int:document_id\",\"name\": \"name\",\"author\": \"author\",\"type_id\": \"int:type_id\",\"location_id\": \"int:location_id\"}]", description = "Get document by location_id.")
    public static void getDocumentByLocation(Context ctx, DocumentRepository documentRepository)
            throws TransactionException {
        Validator<Integer> location_id = ctx.pathParamAsClass("id", Integer.class);
        try {
            List<Result> documents = documentRepository.getDocumentsByLocation(location_id.get());
            List<Document> list = new ArrayList();
            for (Result document : documents) {
                list.add(new Document(document));
            }
            ctx.json(list);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }
}