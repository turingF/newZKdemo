package demo;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;

public class JsonTest {
    public static void main(String[] args) {
        JSONObject jObject = new JSONObject();
        jObject.put("String","hello");
        jObject.put("int",2);
        jObject.put("List", Arrays.asList("hello",",","world"));

        String demo = jObject.getString("String");

        //list extract format
        List<String> demo2 = JSONObject.parseArray(jObject.getJSONArray("List").toString(),String.class);


        System.out.println(demo2);
    }


}
