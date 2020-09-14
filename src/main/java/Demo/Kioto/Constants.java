package Demo.Kioto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * 常量类
 */
public final class Constants {
    private static final ObjectMapper jsonMapper;
    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        jsonMapper = mapper;
    }

    public static String getHealthChecksTopic(){
        return "healthchecks";
    }

    public static String getHealthChecksAvroTopic(){
        return  "healthchecks-avro";
    }

    public static String getUptimesTopic(){
        return "uptimes";
    }

    public enum machineType {GEOTHERMAL,HYDROELECTRIC,NUCLEAR,WIND,SOLAR}

    public enum machineStates {STARTING,RUNNING,SHUTTING_DOWN,SHUT_DOWN}

    public static ObjectMapper getJsonMapper(){
        return jsonMapper;
    }

}
