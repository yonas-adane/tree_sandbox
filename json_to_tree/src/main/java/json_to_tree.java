import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class json_to_tree {


    public static void main(String[] args) {


        try {

           json_to_tree j = new json_to_tree();
//            j.xmlToObject();

            InputStream sourceStream = json_to_tree.class.getClassLoader().getResourceAsStream("source.json");
            InputStream targetStream = json_to_tree.class.getClassLoader().getResourceAsStream("target.json");

            InputStream mapStream = json_to_tree.class.getClassLoader().getResourceAsStream("map.json");

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode sourceJsonNode = objectMapper.readValue(sourceStream, JsonNode.class);
            JsonNode targetJsonNode = objectMapper.readValue(targetStream, JsonNode.class);

            JsonNode mapJsonNode = objectMapper.readValue(mapStream, JsonNode.class);

            j.listPath(sourceJsonNode);
            j.listPath(targetJsonNode);

            for (JsonNode mapNode: mapJsonNode) {

                ArrayNode sourcePath = (ArrayNode) mapNode.get("sourcePath");
                String targetPath = mapNode.get("targetPath").textValue();

                String transformFunction = mapNode.get("transformFunction").textValue();

                HashMap<String, JsonNode> sourceValues = extractPathValue(sourceJsonNode, sourcePath);

                setPathValue(targetJsonNode, targetPath, sourceValues);

                String sds = "";
            }

            Object f = sourceJsonNode.get("batters").get("batter");

            String ss = "";

        } catch (Exception ex) {


            ex.printStackTrace();
        }


    }

    private void xmlToObject() throws IOException {
        InputStream routeStream = json_to_tree.class.getClassLoader().getResourceAsStream("myRoute.xml");

        XmlMapper xmlMapper = new XmlMapper();

        try {
            route xmlToObject = xmlMapper.readValue(routeStream, route.class);

            String xml = xmlMapper.writeValueAsString(xmlToObject);

            String xx = "";
        }
        catch (Exception ex){
            System.out.printf("");
        }
    }

    private static HashMap<String, JsonNode> extractPathValue(JsonNode sourceJsonNode, ArrayNode sourcePath) {

        HashMap<String, JsonNode> sourceValues =  new HashMap<>();

        for (JsonNode path: sourcePath) {

            JsonNode sourceValue=null;

            String[] pathList = path.textValue().split("/");

            Integer i = 0;
            for(String parent : pathList){

                String xx = parent;

                //root
                if(pathList.length == 1){
                    sourceValue = sourceJsonNode.get(parent);
                }
                else {
                    //build path
                    if (i == 0) {
                        sourceValue = sourceJsonNode.get(parent);
                    } else {
                        sourceValue = sourceValue.get(parent);
                    }
                }

                i++;

                sourceValues.put(parent,sourceValue);

            }

        }

        return sourceValues;
    }

    private static void setPathValue(JsonNode sourceJsonNode, String sourcePath, HashMap<String, JsonNode> values) {

        for (Map.Entry<String, JsonNode> entry : values.entrySet()) {
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            JsonNode sourceValue = null;

            ObjectMapper mapper = new ObjectMapper();

            String[] pathList = sourcePath.split("/");

            Integer i = 0;
            for (String parent : pathList) {

                //root
                if (pathList.length == 1) {
                    //((ObjectNode) sourceJsonNode.findParent(parent)).put(parent, value);
                    sourceValue = sourceJsonNode.get(parent);
                } else {
                    //build path
                    if (i == 0) {
                        sourceValue = sourceJsonNode.get(parent);
                    } else {
                        sourceValue = sourceValue.get(parent);
                    }


                }

                i++;

            }

            ((ObjectNode) sourceJsonNode.findParent(pathList[pathList.length - 1])).put(pathList[pathList.length - 1], value);

        }

    }


    private static void listPath(JsonNode json) {
        getNextObject("", json);
    }

    private static void getNextObject(String parent, JsonNode data) {
        if (data.isObject()) {
            jsonObject(parent, (ObjectNode)data);
        } else if (data.isArray()) {
            jsonArray(parent, (ArrayNode) data);
        } else {
            jsonPrimitive(parent, data);
        }
    }

    private static void jsonObject(String parent, JsonNode json) {


        Iterator it = json.fields();

        while (it.hasNext()) {
            String key = ((Map.Entry<String, JsonNode>) it.next()).getKey();
            JsonNode child = json.get(key);
            String childKey = parent.isEmpty() ? key : parent + "/" + key;
            getNextObject(childKey, child);
        }

    }

    private static void jsonArray(String parent, ArrayNode json) {

        int i = 0;
//        for (JsonNode node: json) {
//
//            JsonNode data = json.get(i);
//            listObject(parent + "[" + i + "]", data);
//
//            i++;
//        }

        JsonNode data = json.get(i);
        getNextObject(parent , data);


    }

    private static void jsonPrimitive(String parent, Object obj) {
        //System.out.println(parent + ":"  + obj);
        System.out.println(parent);
    }







}

