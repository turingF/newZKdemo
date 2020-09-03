package demo;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;


public class myInterceptor implements ProducerInterceptor<String,String> {

    //发送前控制record的data,比如在前面加上timestamp
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return new ProducerRecord<String, String>(producerRecord.topic(),producerRecord.partition(),producerRecord.timestamp(),producerRecord.key(),System.currentTimeMillis()+","+producerRecord.value().toString());
    }

    private int error = 0;
    private int succ = 0;
    //获得回馈ack后的定制操作，比如计数
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

        if (e == null)
            succ++;
        else
            error++;
    }


    public void close() {
        System.out.println("succ count: "+succ);
        System.out.println("error count: "+error);
    }

    //default config
    public void configure(Map<String, ?> map) {

    }
}
