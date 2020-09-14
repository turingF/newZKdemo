package Demo.Kioto.custom;

import Demo.Kioto.Constants;
import Demo.Kioto.HealthCheck;
import Demo.Kioto.plain.PlainConsumer;
import Demo.Kioto.plain.PlainProducer;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.concurrent.Future;

public class CustomProcessor {
    private Consumer<String,String> consumer;
    private Producer<String,String> producer;

    public CustomProcessor(String brokers){
        consumer = new CustomConsumer(brokers).getConsumer();
        producer = new CustomProducer(brokers).getProducer();
    }

    public final void process(){
        consumer.subscribe(Collections.singletonList(Constants.getHealthChecksTopic()));

        while(true){
            ConsumerRecords records =
                    consumer.poll(Duration.ofSeconds(1L));
            for (Object record : records){
                ConsumerRecord it = (ConsumerRecord) record;
                HealthCheck healthCheck = (HealthCheck)it.value();

                if(healthCheck!=null){
                    LocalDate startTime = healthCheck.getLastStartAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    int uptime = Period.between(startTime,LocalDate.now()).getDays(); //计算从startTime到now的运行时间

//                System.out.println(healthCheck.getSerialNumber());
                    Future future =
                            producer.send(new ProducerRecord<>(
                                    Constants.getUptimesTopic(),
                                    healthCheck.getSerialNumber(),
                                    String.valueOf(uptime)
                            ));

                    try{
                        future.get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        }
    }
}
