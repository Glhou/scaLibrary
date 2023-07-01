package scalibrary.Entities;

import java.util.Optional;
import com.scalar.db.api.Result;

public class User {
    public int user_id;
    public String name;
    public int location_id;
    public int document_limit;
    public int document_total;

    public User(
        int user_id,
        String name,
        int location_id,
        int document_limit,
        int document_total
    ){
        this.user_id = user_id;
        this.name = name;
        this.location_id = location_id;
        this.document_limit = document_limit;
        this.document_total = document_total;
    }

    public User(Optional<Result> result){
        this.user_id = result.get().getInt("user_id");
        this.name = result.get().getText("name");
        this.location_id = result.get().getInt("location_id");
        this.document_limit = result.get().getInt("document_limit");
        this.document_total = result.get().getInt("document_total");
    }
    
    public String toString(){
        return "User: " + this.user_id + " " + this.name + " " + this.location_id + " " + this.document_limit + " " + this.document_total;
    }
}
