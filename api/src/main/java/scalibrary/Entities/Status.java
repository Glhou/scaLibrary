package scalibrary.Entities;

import java.util.Optional;

import com.scalar.db.api.Result;

public class Status {
    public int status_id;
    public String name;

    public Status(
        int status_id,
        String name
    ){
        this.status_id = status_id;
        this.name = name;
    }
    
    public Status(Optional<Result> r){
        this.status_id = r.get().getInt("status_id");
        this.name = r.get().getText("name");
    }

    public Status(Result r){
        this.status_id = r.getInt("status_id");
        this.name = r.getText("name");
    }

    public String toString(){
        return "Status: " + this.status_id + " " + this.name;
    }
    
}
