package Demo.Kioto.plain;


import Demo.Kioto.Constants;
import Demo.Kioto.HealthCheck;
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
import java.util.Properties;
import java.util.concurrent.Future;

public class PlainProcessor {

    private Consumer <String,String> consumer;
    private Producer <String,String> producer;

    public PlainProcessor(String brokers){
        consumer = new PlainConsumer(brokers).getConsumer();
        producer = new PlainProducer(brokers).getProducer();
    }

    public final void process(){
        consumer.subscribe(Collections.singletonList(Constants.getHealthChecksTopic()));

        while(true){
            ConsumerRecords records =
                    consumer.poll(Duration.ofSeconds(1L));
            for (Object record : records){
                ConsumerRecord it = (ConsumerRecord) record;
                String healthCheckString = (String)it.value();

//                System.out.println("->"+healthCheckString);

                HealthCheck healthCheck = null;

                try{ //将consumer轮询得到的字符串变成pojo
                    healthCheck = Constants.getJsonMapper().readValue(healthCheckString,HealthCheck.class);
                }catch (Exception e){
                    e.printStackTrace();
                }

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
