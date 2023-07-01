package scalibrary.Entities;

import java.util.Optional;
import com.scalar.db.api.Result;

public class Document {
    public int document_id;
    public String name;
    public String author;
    public int user_id;
    public int location_id;
    public int type_id;

    public Document(
        int document_id,
        String name,
        String author,
        int user_id,
        int location_id,
        int type_id
    ){
        this.document_id = document_id;
        this.name = name;
        this.author = author;
        this.user_id = user_id;
        this.location_id = location_id;
        this.type_id = type_id;
    }

    public Document(Optional<Result> result){
        this.document_id = result.get().getInt("document_id");
        this.name = result.get().getText("name");
        this.author = result.get().getText("author");
        this.user_id = result.get().getInt("user_id");
        this.location_id = result.get().getInt("location_id");
        this.type_id = result.get().getInt("type_id");
    }

    public String toString(){
        return "Document: " + this.document_id + " " + this.name + " " + this.author + " " + this.user_id + " " + this.location_id + " " + this.type_id;
    }
    
}
