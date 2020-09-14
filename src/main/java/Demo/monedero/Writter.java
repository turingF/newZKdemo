package Demo.monedero;

import org.apache.kafka.clients.producer.KafkaProducer;

public class Writter implements Producer{
    private final KafkaProducer<String,String> producer;
    private final String topic;

    Writter(String servers,String topic){
        this.producer = new KafkaProducer<String, String>(Producer.createConfig(servers));
        this.topic = topic;
    }

    @Override
    public void process(String message) {
        Producer.write(this.producer,this.topic,message);
    }
}
