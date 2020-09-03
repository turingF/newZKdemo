package kafkaAPI.worker;

import com.alibaba.fastjson.JSONObject;
import kafkaAPI.ProducerApi;
import org.apache.kafka.clients.producer.KafkaProducer;

public class BeatMessage implements Runnable {

    private String moduleName;
    private KafkaProducer<String,String> producer;
    private JSONObject beatMessage;

    /**
     * 发送心跳信号线程
     * @param producer 模块的生产者
     * @param name 模块的名称
     */
    public BeatMessage(KafkaProducer<String,String> producer,String name){

        this.producer = producer;
        moduleName = name;
        beatMessage = new JSONObject();
        beatMessage.put("moduleName",moduleName);

    }

    @Override
    public void run() {
        ProducerApi.PubTopic(this.producer,"beatHeart",beatMessage);
    }
}
