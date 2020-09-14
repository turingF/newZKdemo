package Demo.Kioto.plain;

import Demo.Kioto.Constants;
import Demo.Kioto.HealthCheck;
import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class PlainProducer {

    private final Producer<String,String> producer;

    public PlainProducer(String brokers){
        Properties props = new Properties();
        props.put("bootstrap.servers",brokers);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer",StringSerializer.class);

        producer = new KafkaProducer<String, String>(props);

    }

    public Producer<String, String> getProducer() {
        return producer;
    }

    public void produce(int ratePerSecond){
        long waitTimeBetweenIterationMS = 1000L/(long)ratePerSecond;
        Faker faker = new Faker();
        while (true){
            HealthCheck fakeCheck = new HealthCheck(
                    "Health_Check",
                    faker.address().city(),
                    faker.bothify("??##-??##",true),
                    Constants.machineType.values()
                            [faker.number().numberBetween(0,4)].toString(),
                    Constants.machineStates.values()
                            [faker.number().numberBetween(0,3)].toString(),
                    faker.date().past(100, TimeUnit.DAYS),
                    faker.number().numberBetween(100L,0L),
                    faker.internet().ipV4Address()
            );

            String fakeCheckString = null;

            try{
                fakeCheckString = Constants.getJsonMapper().writeValueAsString(fakeCheck);
            }catch (Exception e){
                e.printStackTrace();
            }

            Future futureResult = producer.send(new ProducerRecord<>(Constants.getHealthChecksTopic(),fakeCheckString));

            try {
                Thread.sleep(waitTimeBetweenIterationMS);
                futureResult.get();
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

}
