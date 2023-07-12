package scalibrary.Controller;

import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import scalibrary.Entities.Location;
import scalibrary.Repository.LocationRepository;

public class LocationController {
    public static void getLocation(Context ctx, LocationRepository locationRepository) throws TransactionException{
        Validator<Integer> location_id = ctx.pathParamAsClass("id",Integer.class);
        try{
            Location location = new Location(locationRepository.getLocation(location_id.get()));
            ctx.json(location);
        }catch (TransactionException e){
            ctx.status(500);
            ctx.json("{\"status\": \"Failed to get location\"}");
            throw e;
        }

    }
}
