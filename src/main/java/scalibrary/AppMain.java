package scalibrary;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.Javalin; 

import scalibrary.Repository.UserRepository;
import scalibrary.Repository.DocumentRepository;


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
        // Creating the server with javalin
        Javalin app = Javalin.create(config -> {
          config.plugins.enableDevLogging();
          })
          .get("/", ctx -> ctx.json("{\"status\": \"Server On\"}"))
          .get("/document/{id}",ctx -> DocumentController.getDocument(ctx,documentRepository))
          .post("/document/create", DocumentController::createDocument)
          .post("/document/{id}/loan", DocumentController::loanDocument)
          .post("/document/{id}/return", DocumentController::returnDocument)
          .get("/document/location/{id}", DocumentController::getDocumentByLocation)
          .post("/user/create", UserController::createUser)
          .get("/user/{id}", ctx -> UserController.getUser(ctx,userRepository))
          .post("/user/{id}/location", UserController::editUserLocation)
          .get("/user/{id}/documents", UserController::getLoanedDocument)
          .start(7000);  
      }catch(IOException e){
        logger.error("Failed to create repository instances", e);
      }
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}
