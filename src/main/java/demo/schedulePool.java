package demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class schedulePool {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(
            10,new CustomThreadFactory("Thread-")
        );

        Runnable task = new Runnable() {
            @Override
            public void run() {
                String pattern;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                System.out.println(format.format(new Date()));
            }
        };

        scheduledPool.scheduleWithFixedDelay(task,1,5, TimeUnit.SECONDS);
    }
}

class CustomThreadFactory implements ThreadFactory{

    private String namePrefix;
    private final ThreadGroup group;
    private final AtomicInteger threadNum = new AtomicInteger(0);

    public CustomThreadFactory(String namePrefix){
        SecurityManager s = System.getSecurityManager();
        group = (s!=null) ? s.getThreadGroup():
                Thread.currentThread().getThreadGroup();

        this.namePrefix = namePrefix;
    }

    public Thread newThread (Runnable r){
        Thread t = new Thread(group,r,namePrefix+threadNum.getAndIncrement(),0);

        if(t.isDaemon()){
            t.setDaemon(false);
        }

        if(t.getPriority() != Thread.NORM_PRIORITY){
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
