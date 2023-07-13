package scalibrary.Controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.javalin.http.Context;
import scalibrary.Annotation.Docs;
import scalibrary.Entities.Doc;

public class DocsController extends Controller {
    public static void makeDocs(Context ctx) {
        DocumentController documentController = new DocumentController();
        LocationController locationController = new LocationController();
        StatusController statusController = new StatusController();
        UserController userController = new UserController();
        TypeController typeController = new TypeController();

        String docs = "";
        List<Doc> data = new ArrayList<Doc>();

        data = getDocsFromController(documentController, data);
        data = getDocsFromController(locationController, data);
        data = getDocsFromController(statusController, data);
        data = getDocsFromController(userController, data);
        data = getDocsFromController(typeController, data);

        docs = buildDocsFromListDocs(data);

        ctx.json("{" + docs + "}");
    }

    private static List<Doc> getDocsFromController(Controller controller, List<Doc> data) {
        Class<?> clazz = controller.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Docs.class)) {
                Docs docsAnnotation = method.getAnnotation(Docs.class);
                String route = docsAnnotation.route();
                String method_name = docsAnnotation.method();
                String input = docsAnnotation.input();
                String output = docsAnnotation.output();
                String description = docsAnnotation.description();
                data.add(new Doc(route, method_name, input, output, description));
            }
        }
        return data;
    }

    private static String buildDocsFromListDocs(List<Doc> data) {
        Map<String, Map<String, Map<String, String>>> result = new HashMap<String, Map<String, Map<String, String>>>();
        // group doc by matching route
        Map<String, List<Doc>> route_groups = new HashMap<String, List<Doc>>();
        for (Doc doc : data) {
            List<Doc> group = route_groups.getOrDefault(doc.route, new ArrayList<Doc>());
            group.add(doc);
            route_groups.put(doc.route, group);
        }
        // for each group, build a json object with inside a json object with doc method
        // as key to store the rest of the data of the doc
        for (String group_key : route_groups.keySet()) {
            List<Doc> group = route_groups.get(group_key);
            Map<String, Map<String, String>> group_json = new HashMap<String, Map<String, String>>();
            for (Doc doc : group) {
                Map<String, String> doc_json = new HashMap<String, String>();
                doc_json.put("input", doc.input);
                doc_json.put("output", doc.output);
                doc_json.put("description", doc.description);
                group_json.put(doc.method, doc_json);
            }
            result.put(group_key, group_json);
        }
        return resultMapToString(result);
    }

    private static String resultMapToString(Map<String, Map<String, Map<String, String>>> result) {
        String docs = "";
        for (String route : result.keySet()) {
            Map<String, Map<String, String>> group = result.get(route);
            docs += "\"" + route + "\":{";
            for (String method : group.keySet()) {
                Map<String, String> doc = group.get(method);
                docs += "\"" + method + "\":{";
                docs += "\"input\":" + doc.get("input") + ",";
                docs += "\"output\":" + doc.get("output") + ",";
                docs += "\"description\":\"" + doc.get("description") + "\"";
                docs += "},";
            }
            docs = docs.substring(0, docs.length() - 1);
            docs += "},";
        }
        docs = docs.substring(0, docs.length() - 1);
        return docs;
    }
}
