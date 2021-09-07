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
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

public class json_to_tree {


    public static void main(String[] args) {


        try {

            json_to_tree j = new json_to_tree();
            j.xmlToObject();

            InputStream sourceStream = json_to_tree.class.getClassLoader().getResourceAsStream("source.json");
            InputStream targetStream = json_to_tree.class.getClassLoader().getResourceAsStream("target.json");

            InputStream mapStream = json_to_tree.class.getClassLoader().getResourceAsStream("map.json");

            ObjectMapper objectMapper = new ObjectMapper();


            JsonNode sourceJsonNode = objectMapper.readValue(sourceStream, JsonNode.class);
            JsonNode targetJsonNode = objectMapper.readValue(targetStream, JsonNode.class);

            JsonNode mapJsonNode = objectMapper.readValue(mapStream, JsonNode.class);


            for (JsonNode node: mapJsonNode) {

                String sourcePath = node.get("sourcePath").textValue();
                String targetPath = node.get("targetPath").textValue();

                String transformFunction = node.get("transformFunction").textValue();

                JsonNode sourceValue = extractPathValue(sourceJsonNode, sourcePath);
                setPathValue(targetJsonNode, targetPath,sourceValue);

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

    private static JsonNode extractPathValue(JsonNode sourceJsonNode, String sourcePath) {

        JsonNode sourceValue=null;

        String[] pathList = sourcePath.split("/");

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

        }

        return sourceValue;
    }

    private static void setPathValue(JsonNode sourceJsonNode, String sourcePath, JsonNode value) {

        JsonNode sourceValue=null;

        ObjectMapper mapper = new ObjectMapper();

        String[] pathList = sourcePath.split("/");

        Integer i = 0;
        for(String parent : pathList){

            //root
            if(pathList.length == 1) {
                //((ObjectNode) sourceJsonNode.findParent(parent)).put(parent, value);
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

        }

        ((ObjectNode) sourceJsonNode.findParent(pathList[pathList.length-1])).put(pathList[pathList.length-1], value);

    }






}

