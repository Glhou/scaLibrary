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
    public int status_id;

    public Document(
        int document_id,
        String name,
        String author,
        int user_id,
        int location_id,
        int type_id,
        int status_id
    ){
        this.document_id = document_id;
        this.name = name;
        this.author = author;
        this.user_id = user_id;
        this.location_id = location_id;
        this.type_id = type_id;
        this.status_id = status_id;
    }

    public Document(Optional<Result> result){
        this.document_id = result.get().getInt("document_id");
        this.name = result.get().getText("name");
        this.author = result.get().getText("author");
        this.user_id = result.get().getInt("user_id");
        this.location_id = result.get().getInt("location_id");
        this.type_id = result.get().getInt("type_id");
        this.status_id = result.get().getInt("status_id");
    }
    
    public Document(Result result){
        this.document_id = result.getInt("document_id");
        this.name = result.getText("name");
        this.author = result.getText("author");
        this.user_id = result.getInt("user_id");
        this.location_id = result.getInt("location_id");
        this.type_id = result.getInt("type_id");
        this.status_id = result.getInt("status_id");
    }
    public String toString(){
        return "Document: " + this.document_id + " " + this.name + " " + this.author + " " + this.user_id + " " + this.location_id + " " + this.type_id + " " + this.status_id;
    }
    
}
