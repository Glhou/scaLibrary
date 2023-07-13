package scalibrary.Entities;

import java.util.ArrayList;
import java.util.List;

public class Doc {
    public String route;
    public String method;
    public String input;
    public String output;
    public String description;

    public Doc(String route, String method, String input, String output, String description) {
        this.route = route;
        this.method = method;
        this.input = input;
        this.output = output;
        this.description = description;
    }

    public List<String> routes() {
        String[] routes_raw = route.split("/");
        List<String> routes = new ArrayList<String>();
        for (int i = 1; i < routes_raw.length; i++) {
            routes.add(routes_raw[i]);
        }
        return routes;
    }

    public String toJson() {
        return "{\"route\":\"" + route + "\",\"method\":\"" + method + "\",\"input\":\"" + input + "\",\"output\":\""
                + output + "\",\"description\":\"" + description + "\"}";
    }

}
