package Demo.Kioto.plain;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class PlainConsumer {
    private Consumer<String,String> consumer;

    public PlainConsumer(String brokers){
        Properties props = new Properties();

        props.put("group.id","healthcheck-processor");
        props.put("bootstrap.servers",brokers);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer",StringDeserializer.class);

        consumer = new KafkaConsumer<String, String>(props);
    }

    public Consumer<String, String> getConsumer() {
        return consumer;
    }
}
