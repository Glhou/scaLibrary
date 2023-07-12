package scalibrary.Entities;

import java.util.Optional;

import com.scalar.db.api.Result;

public class Type {
    public int type_id;
    public String name;

    public Type(
        int type_id,
        String name
    ){
        this.type_id = type_id;
        this.name = name;
    }

    public Type(Optional<Result> r){
        this.type_id = r.get().getInt("type_id");
        this.name = r.get().getText("name");
    }

    public Type(Result r){
        this.type_id = r.getInt("type_id");
        this.name = r.getText("name");
    }

    public String toString(){
        return "Type: " + this.type_id + " " + this.name;
    }
    
}
