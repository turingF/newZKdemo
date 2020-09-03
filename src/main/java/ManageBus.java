
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kafkaAPI.ConsumerApi;
import kafkaAPI.ProducerApi;
import kafkaAPI.worker.ManageWorker;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author xuyang
 * 管理总线模块
 * 对集成模块状态进行管理
 */
public class ManageBus {

    private KafkaProducer<String, String> mbusProducer;
    private KafkaConsumer<String, String> mbusConsumer;
    private ExecutorService pool;
    private final int MAX_POOL_SIZE = 3;
    private final String broker = "localhost:9092";

    public ManageBus() {

        mbusProducer = ProducerApi.CreateProducer(broker);
        mbusConsumer = ConsumerApi.CreateConsumer(broker, "mbus");
        pool = Executors.newFixedThreadPool(MAX_POOL_SIZE);

        //subscribe default topic
        mbusConsumer.subscribe(Arrays.asList("register", "feedback", "beatHeart"));
        pool.execute(new ManageWorker(mbusProducer, mbusConsumer));

    }

    public void adminControl(String moduleName,int status){
        JSONObject message = new JSONObject();
        message.put("moduleName",moduleName);
        message.put("status",status);

        ProducerApi.PubTopic(mbusProducer,"controlAdmin",message);
    }
}

