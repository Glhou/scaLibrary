package scalibrary.Entities;

import java.util.Optional;

import com.scalar.db.api.Result;

public class Location {
    public int location_id;
    public String name;

    public Location(
        int location_id,
        String name
    ){
        this.location_id = location_id;
        this.name = name;
    }
    
    public Location(Optional<Result> r){
        this.location_id = r.get().getInt("location_id");
        this.name = r.get().getText("name");
    }

    public Location(Result r){
        this.location_id = r.getInt("location_id");
        this.name = r.getText("name");
    }

    public String toString(){
        return "Location: " + this.location_id + " " + this.name;
    }
    
}
