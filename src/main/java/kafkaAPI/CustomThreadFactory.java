package kafkaAPI;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这个类没用
 */
public class CustomThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNum = new AtomicInteger(0);

    public CustomThreadFactory(){
        SecurityManager s = System.getSecurityManager();
        group = (s!=null) ? s.getThreadGroup():
                Thread.currentThread().getThreadGroup();

    }

    public Thread newThread (Runnable r){

        //创建线程
        Thread t = new Thread(group,r,""+threadNum.getAndIncrement(),0);

        if(t.isDaemon()){
            t.setDaemon(false);
        }

        if(t.getPriority() != Thread.NORM_PRIORITY){
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
