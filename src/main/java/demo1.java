import com.alibaba.fastjson.JSONObject;
import kafkaAPI.ConsumerApi;
import kafkaAPI.ProducerApi;
import neo4jApi.Neo4jDriver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author xuyang
 * 业务总线模块
 * 控制模块间发送消息
 */
public class demo1 {


    private static String broker = "localhost:9092";
    private static String topicName = "test002";


    public static void main(String[] args) {

        Neo4jDriver nd = new Neo4jDriver();

        demo(nd);

    }

    public static void demo(Neo4jDriver nd){
        //假设A要给B发一个message(JSON文件)

        JSONObject message = new JSONObject();
//        //需要携带自身模块参数 *
        message.put("appId","A");
        message.put("name","xuyang");
        message.put("school","hust");
        message.put("id","M2019");

        //需要创建Topic节点(neo4j)
        nd.CreateNode(topicName,"topic");

        KafkaProducer<String,String> A = ProducerApi.CreateProducer(broker);
        ProducerApi.PubTopic(A,topicName,message);

        nd.CreateNode("A", "module1");
        nd.CreateEdge("A", "module1",topicName,"topic");


        KafkaConsumer B = ConsumerApi.CreateConsumer(broker,"B");
        B.subscribe(Collections.singletonList(topicName));


        nd.CreateNode("B", "module1");
        nd.CreateEdge(topicName,"topic","B", "module1");

        while(true) {
            ConsumerRecords<String, String> records = B.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.key().toString() + "---" + record.value().toString());
            }
        }


    }



}

