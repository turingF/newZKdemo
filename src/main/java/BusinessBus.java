import kafkaAPI.ProducerApi;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.List;

/**
 * @author xuyang
 *
 * 业务总线负责各模块之间的数据传输
 * 当两个模块需要传输消息时，业务总线需要创建对应producer和consumer
 * 同时需要创建主题来作为消息传输的中介
 */
public class BusinessBus {

    List<ZkModule> modules;
    List<String> ZkNames;
    public static AdminClient client;
    private static String broker = "localhost:9092";

    public BusinessBus(List<ZkModule> list){
        this.modules =list;

        for (ZkModule module:modules)
            ZkNames.add(module.getModuleName());
    }

    public void business(String produce,String consumer){

        if (!ZkNames.contains(produce)|| !ZkNames.contains(consumer)){
            System.out.println("module(built-in) not find");
            return;
        }

        
    }


}
