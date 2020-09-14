package Demo.monedero;

import org.apache.kafka.common.protocol.types.Field;

public class ProcessingEngine {
    public static void main(String[] args) {
        String servers = "localhost:9092";
        String groupId = "test";
        String sourceTopic = "test1";
        String targetTopic = "test2";
        Reader reader = new Reader(servers,groupId,sourceTopic);
        Writter writter = new Writter(servers,targetTopic);

        reader.run(writter);
    }
}
