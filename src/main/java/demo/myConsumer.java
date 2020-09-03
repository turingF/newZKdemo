package demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.sound.midi.SoundbankResource;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class myConsumer {
    public static void main(String[] args) {

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //可以看到在生成producer/consumer时不需要bound到topic，但需要绑到broker上(kafka port)
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        //订阅topic，可同时指定多个
        consumer.subscribe(Collections.singletonList("register"));

        while (true) {
            // 读取数据，读取超时时间为100ms
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                if(record.topic().equals("register"))
                    System.out.println(record.topic() + "---" + record.partition() + "---" + record.offset() + "---" + record.value());
            }
        }
    }

}
