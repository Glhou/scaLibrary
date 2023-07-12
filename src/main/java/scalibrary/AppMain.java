package scalibrary;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.Javalin; 

import scalibrary.Repository.UserRepository;
import scalibrary.Controller.DocumentController;
import scalibrary.Controller.UserController;
import scalibrary.Repository.DocumentRepository;
import scalibrary.Controller.LocationController;
import scalibrary.Repository.LocationRepository;
import scalibrary.Controller.StatusController;
import scalibrary.Repository.StatusRepository;
import scalibrary.Controller.TypeController;
import scalibrary.Repository.TypeRepository;


public class AppMain {
  private static Logger logger = LoggerFactory.getLogger(AppMain.class);
  public static void main(String[] args) {
    try(DataLoader loader = new DataLoader()){
      DistributedTransactionManager manager = loader.getManager();
      // Loading the fake data
      try {
        loader.loadInitialData();
        loader.loadFixtureData();
      } catch (TransactionException e) {
        logger.error("Data loading has failed", e);
      }
      try{
        // Creating repository instance
        DocumentRepository documentRepository = new DocumentRepository();
        UserRepository userRepository = new UserRepository();
        LocationRepository locationRepository = new LocationRepository();
        TypeRepository typeRepository = new TypeRepository();
        StatusRepository statusRepository = new StatusRepository();
        // Creating the server with javalin
        Javalin app = Javalin.create(config -> {
          config.plugins.enableDevLogging();
          })
          .get("/", ctx -> ctx.json("{\"status\": \"Server On\"}"))
          .get("/document/{id}",ctx -> DocumentController.getDocument(ctx,documentRepository))
          .post("/document",ctx -> DocumentController.createDocument(ctx, documentRepository))
          .post("/document/{id}/loan", ctx -> DocumentController.loanDocument(ctx, documentRepository))
          .post("/document/{id}/return",ctx -> DocumentController.returnDocument(ctx, documentRepository))
          .get("/document/location/{id}",ctx -> DocumentController.getDocumentByLocation(ctx, documentRepository))
          .post("/user",ctx -> UserController.createUser(ctx, userRepository))
          .get("/user/{id}", ctx -> UserController.getUser(ctx,userRepository))
          .post("/user/{id}/location",ctx -> UserController.editUserLocation(ctx, userRepository))
          .get("/user/{id}/documents",ctx -> UserController.getLoanedDocument(ctx, userRepository))
          .get("/location/{id}", ctx -> LocationController.getLocation(ctx, locationRepository))
          .get("/type/{id}", ctx -> TypeController.getType(ctx, typeRepository))
          .get("/status/{id}", ctx -> StatusController.getStatus(ctx, statusRepository))
          .start(7000);  
      }catch(IOException e){
        logger.error("Failed to create repository instances", e);
      }
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}
