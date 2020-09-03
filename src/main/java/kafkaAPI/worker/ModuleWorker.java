package kafkaAPI.worker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kafkaAPI.ProducerApi;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.time.Duration;

public class ModuleWorker implements Runnable {

    private KafkaConsumer<String,String> consumer;
    private KafkaProducer<String,String> producer;
    private String name;

    public ModuleWorker(KafkaProducer<String,String> producer, KafkaConsumer<String,String> consumer, String moduleName){
        this.consumer = consumer;
        this.producer = producer;
        this.name = moduleName;

    }

    private void setStatus(int status){

        JSONObject statusMessage = new JSONObject();
        statusMessage.put("moduleName",name);
        statusMessage.put("status",status);
        statusMessage.put("desc","normal");
        //发送回馈信号
        ProducerApi.PubTopic(producer,"feedback",statusMessage);

    }

    @Override
    public void run() {

        try {
            while (true) {
                // 读取数据，读取超时时间为1000ms
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {

                    //如果收到消息为setStatus
                    if (record.topic().equals("controlAdmin")) {

                        JSONObject message = JSON.parseObject(record.value().toString());
                        if (message.get("moduleName").equals(this.name)){
                            setStatus((Integer) message.get("status"));
                            System.out.println("模块:"+this.name + " set to "+message.get("status"));
                        }

                    }

                    // TODO: more operator
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
