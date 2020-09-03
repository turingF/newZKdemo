package demo;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;


public class myProducer <K,V>

{

    public static void main(String[] args) {

        //set property of producer
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("acks","all"); //
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 生成producer实例,<string>-<string>为键值对
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        JSONObject message = new JSONObject();
        message.put("moduleName","hello");
        message.put("status","1");

        producer.send(new ProducerRecord<String, String>("feedback","feedback",message.toString()));



        producer.close();


    }
}
