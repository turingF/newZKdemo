import com.alibaba.fastjson.JSONObject;
import kafkaAPI.ConsumerApi;
import kafkaAPI.CustomThreadFactory;
import kafkaAPI.ProducerApi;
import kafkaAPI.worker.BeatMessage;
import kafkaAPI.worker.ModuleWorker;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xuyang
 *
 * 作为模块的代替测试类，就现在得出模块功能大致为：
 * 1.向registerTopic发送集成请求(管理总线订阅)
 * 2.模块运行状态，需要受到管理总线控制.回馈信号需要返回(管理总线订阅)
 * 3.模块间传输数据(业务总线) ?
 * 4.周期性发布心跳报文
 */
public class ZkModule {

    private String localConn;
    //should add app_id
    private String moduleName;
    private KafkaProducer <String,String> mProducer;
    private KafkaConsumer <String,String> mConsumer;
    ExecutorService executor = Executors.newFixedThreadPool(3);

    public String getModuleName() {
        return moduleName;
    }

    /**
     * 代表系统运行状态
     * 0: stop
     * 1: start
     * 2: pause
     */
    private int status;

    /**
     * 默认模块要做的事放在构造函数里
     */
    public ZkModule(String name){
        //默认配置
        localConn = "localhost:9092";
        moduleName= name;
        status = 1;
        mProducer = ProducerApi.CreateProducer(localConn);
        mConsumer = ConsumerApi.CreateConsumer(localConn,moduleName);
        mConsumer.subscribe(Collections.singletonList("controlAdmin"));

        //新模块申请
        newTopicCall();


        executor.execute(new ModuleWorker(mProducer,mConsumer,moduleName));
        BeatMessage();
    }


    /**
     *每五秒发送一次心跳报文
     */
    public void BeatMessage(){

        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(
                1,new CustomThreadFactory()
        );

        scheduledPool.scheduleWithFixedDelay(new BeatMessage(mProducer,moduleName),1,5, TimeUnit.SECONDS);

    }



    public void newTopicCall(){
        //发布new请求
        JSONObject message = new JSONObject();
        message.put("moduleName",this.moduleName);
        ProducerApi.PubTopic(mProducer,"register",message);
    }

    }

