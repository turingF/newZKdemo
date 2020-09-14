package Demo.Kioto.serde;

import Demo.Kioto.Constants;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public final class HealthCheckSerializer implements Serializer {

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    //必须实现的方法
    @Override
    public byte[] serialize(String s, Object data) {
        if (data ==null){
            return null;
        }

        try{
            //将对象(json)转换成json byte

            return Constants.getJsonMapper().writeValueAsBytes(data);
        }catch (Exception e){
            return null;
        }
    }
}
