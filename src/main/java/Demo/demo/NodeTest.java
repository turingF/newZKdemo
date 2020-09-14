package Demo.demo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeTest {

    public static void main(String[] args) {
        JSONObject object = new JSONObject();
        object.put("uid",1);
        object.put("name","liming");
        object.put("age",18);
        object.put("school",null);

        ObjectMapper MAPPER = new ObjectMapper();

        try {
            JsonNode node = MAPPER.readTree(object.toString());
            System.out.println(node.has("age"));
            System.out.println(node.path("age"));
            System.out.println(node.path("age").isMissingNode());


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
