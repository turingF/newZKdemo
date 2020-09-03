package kafkaAPI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author xuyang
 *
 * 供生成者所用的函数
 */
public class ProducerApi {

    /**
     * 创立生产者实例
     * @param broker 节点端口
     * @return 生产者实例
     */
    public static KafkaProducer<String,String> CreateProducer(String broker){

        Properties props = new Properties();
        //bootstrap means create
        props.put("bootstrap.servers",broker);
        //等待所有副本应答
        props.put("acks","all");

        //保证发送数据序列化(同一个分区)
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        // 生成producer实例,<string>-<string>为键值对

        return new KafkaProducer<String, String>(props);
    }

    /**
     * producer向topic发布主题
     * 发送消息类型随着发送主题不同而区分，故这里不需要单独设置类型
     * 发送信息需包含自身模块信息
     *
     * @param producer 生产者实例
     * @param topicName 发布信息的主题
     * @param message 需要发送的json文件
     */
    public static void PubTopic(KafkaProducer<String,String> producer,String topicName, JSONObject message){ //假设json内部无嵌套

        //消息格式为 <topic,json>
        producer.send(new ProducerRecord<>(topicName, topicName, message.toString()));

        }

    //调用NewTopic  API的demo
    public static void createTopics(String broker,String topic) {
        try {
            Properties properties = new Properties();
            properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, broker);
            AdminClient client = AdminClient.create(properties);

            NewTopic newTopic = new NewTopic(topic, 3, (short) 1);
            Collection<NewTopic> newTopics = new ArrayList<>();
            newTopics.add(newTopic);
            System.out.println(newTopics);
            // 创建主题
            CreateTopicsResult result=client.createTopics(newTopics);
            result.all().get();
            System.out.println("Topic创建成功！");
        } catch (InterruptedException e) {
            System.out.println("创建失败：" + e.getMessage());
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }





}
