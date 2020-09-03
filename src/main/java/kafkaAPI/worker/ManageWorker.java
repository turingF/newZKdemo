package kafkaAPI.worker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.time.Duration;

public class ManageWorker implements Runnable{

    private KafkaConsumer<String,String> consumer;
    private KafkaProducer<String,String> producer;


    public ManageWorker(KafkaProducer<String,String> producer,KafkaConsumer<String,String> consumer){
        this.consumer = consumer;
        this.producer = producer;
    }

    @Override
    public void run() {

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    JSONObject message = JSON.parseObject(record.value().toString());

                    //case1:收到的是集成请求
                    if (record.topic().equals("register")) {

                        String name = (String)message.get("moduleName");
                            System.out.println("管理总线:"+"NEW TOPIC "+name);

                    }

                    //case2:收到的是反馈信息
                    else if (record.topic().equals("feedback")) {
                        System.out.println("管理总线:"+"HAS SET " + message.get("moduleName") + " STATUS TO " + message.get("status"));
                    }

                    //case3:收到的是心跳信号
                    else if (record.topic().equals("beatHeart")) {
                        System.out.println("管理总线:"+"RECEIVE BEAT " + message.get("moduleName"));
                    }

                    //TODO: MORE CONDITIONS
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                consumer.commitSync();
            }finally {
                consumer.close();
            }

        }
    }
}
