package Demo.Kioto.serde;

import Demo.Kioto.Constants;
import Demo.Kioto.HealthCheck;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public final class HealthCheckDeserializer implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public Object deserialize(String s, byte[] data) {
        if (data == null){
            return null;
        }
        try{
            return Constants.getJsonMapper().readValue(data, HealthCheck.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
