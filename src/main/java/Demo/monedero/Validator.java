package Demo.monedero;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;

public class Validator implements Producer{

    private final KafkaProducer<String,String> producer;
    private final String validMessages;
    private final String invalidMessages;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Validator(String servers,String validMessages,String invalidMessages){
        this.producer = new KafkaProducer<String, String>(Producer.createConfig(servers));
        this.validMessages = validMessages;
        this.invalidMessages = invalidMessages;
    }

    @Override
    public void process(String message) {
        try {
            JsonNode root = MAPPER.readTree(message);
            String error = "";
            //concat 链接两个数据 or 字符串
            error = error.concat(validate(root,"event"));
            error = error.concat(validate(root,"customer"));
            error = error.concat(validate(root,"currency"));
            error = error.concat(validate(root,"timestamp"));

            if (error.length()>0){ //若是json但不含上述字段
                Producer.write(this.producer,this.invalidMessages,"{\"error\":\""+error+"\"}");

            }else {
                Producer.write(this.producer,this.validMessages,MAPPER.writeValueAsString(root));
            }

        }catch (Exception e){ //若不是json类型则放在此处处理
            Producer.write(this.producer,this.invalidMessages,"{\"error\":\""+e.getClass().getSimpleName() + ":"+e.getMessage()+"\"}");
        }


    }

    //validate即验证json数据中是否存在path字段，以及path是否为空节点
    private String validate(JsonNode root,String path){
        if (!root.has(path)){
            return path.concat(" is missing");
        }
        JsonNode node = root.path(path);

        if (node.isMissingNode()){
            return path.concat(" is missing");
        }
        return "";
    }


}
