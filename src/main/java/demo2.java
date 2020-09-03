import com.alibaba.fastjson.JSONObject;
import kafkaAPI.ProducerApi;
import neo4jApi.Neo4jDriver;
import org.apache.kafka.clients.producer.KafkaProducer;

public class demo2 {

    private static String broker = "localhost:9092";
    private static String topicName = "test002";

    public static void main(String[] args) {


            Neo4jDriver nd = new Neo4jDriver();

            demo(nd);

    }

    private static void demo(Neo4jDriver nd){

        JSONObject message = new JSONObject();
        //需要携带自身模块参数 *
        message.put("appId","A");
        message.put("name","xuyang");
        message.put("school","hust");
        message.put("id","M2019");

        KafkaProducer<String,String> A = ProducerApi.CreateProducer(broker);
        ProducerApi.PubTopic(A,topicName,message);

        A.close();
    }
}



