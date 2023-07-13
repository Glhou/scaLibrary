package scalibrary.Controller;

import java.util.ArrayList;
import java.util.List;

import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import scalibrary.Annotation.Docs;
import scalibrary.Entities.Location;
import scalibrary.Repository.LocationRepository;

public class LocationController extends Controller {

    @Docs(route = "/location", method = "get", input = "{}", output = "[{\"location_id\": \"int:location_id\",\"name\": \"name\"}]", description = "Get all locations.")
    public static void getAll(Context ctx, LocationRepository locationRepository) throws TransactionException {
        try {
            List<Result> location_result = locationRepository.getAll();
            List<Location> locations = new ArrayList<Location>();
            for (Result r : location_result) {
                locations.add(new Location(r));
            }
            ctx.json(locations);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\":\"" + e.getLocalizedMessage() + "\"}");
            throw e;
        }
    }

    @Docs(route = "/location/{id}", method = "get", input = "{}", output = "{\"location_id\": \"int:location_id\",\"name\": \"name\"}", description = "Get a location by id.")
    public static void getLocation(Context ctx, LocationRepository locationRepository) throws TransactionException {
        Validator<Integer> location_id = ctx.pathParamAsClass("id", Integer.class);
        try {
            Location location = new Location(locationRepository.get(location_id.get()));
            ctx.json(location);
        } catch (TransactionException e) {
            ctx.status(500);
            ctx.json("{\"status\": \"Failed to get location\"}");
            throw e;
        }

    }
}
