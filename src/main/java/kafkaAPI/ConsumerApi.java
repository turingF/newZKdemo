package kafkaAPI;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author xuyang
 *
 * 消费类api
 */
public class ConsumerApi {
    /**
     *创立消费者实例
     * @param broker 节点端口
     * @param moduleName 模块名称作为Id，以此设置group Id
     * @return
     */
    public static KafkaConsumer<String,String> CreateConsumer(String broker,String moduleName){

        Properties props = new Properties();
        //bootstrap means create
        props.put("bootstrap.servers",broker);
        //等待所有副本应答
        props.put("acks","all");
        //每个consumer应该有自己的group id
        props.put("group.id",moduleName);
        //反序列化
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


        // 生成producer实例,<string>-<string>为键值对

        return new KafkaConsumer<String, String>(props);
    }

    }


