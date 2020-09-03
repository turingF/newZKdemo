package demo;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        myThread t1 = new myThread();
        myThread t2 = new myThread();
        myThread t3 = new myThread();
        myThread t4 = new myThread();

        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);


        pool.shutdown();
    }
}


class myThread extends Thread{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}
