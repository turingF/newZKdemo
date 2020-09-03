package demo;

import org.apache.kafka.clients.producer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class myProdCallBack

{

    public static void main(String[] args) {

        //set property of producer
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("acks","all"); //
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //设置拦截
        List<String> interceptors = new ArrayList<String>();
        interceptors.add("myInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,interceptors);

        // 生成producer实例
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        for(int i =0; i<100; i++)
            producer.send(new ProducerRecord<String, String>("test001", "hello " + i), new Callback() {

                //当topic存储message成功后(包括follower)会回馈给producer一个ack
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(recordMetadata !=null){
                        System.err.println(recordMetadata.partition()+":"+recordMetadata.offset());
                    }
                }
            });



        producer.close();


    }
}

