package kafkaAPI.worker;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Map;
import java.util.Set;

/**
 * 管理已经集成的模块，实际应该用sql存储
 */
public  class ModuleList {

    public static Set<String> controlModule;
    public static Map<String, Integer> moduleState;

}
