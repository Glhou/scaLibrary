package scalibrary;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.service.TransactionFactory;
import com.scalar.db.exception.transaction.TransactionException;
import java.io.IOException;


public class AppMain {
  public static void main(String[] args) {
    try(DataLoader loader = new DataLoader()){
      DistributedTransactionManager manager = loader.getManager();
      // Loading the fake data
      try {
        loader.loadInitialData();
        loader.loadFixtureData();
      } catch (TransactionException e) {
        e.printStackTrace();
      }
      // Creating the server with javalin
          
    }catch (IOException e){
      e.printStackTrace();
    }
  } 
}
