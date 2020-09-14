package Demo.Kioto;

import Demo.Kioto.custom.CustomProducer;
import Demo.Kioto.plain.PlainProcessor;
import Demo.Kioto.plain.PlainProducer;
import com.github.javafaker.Faker;

import java.util.concurrent.TimeUnit;

public class MAIN {
    public static void main(String[] args) {
//        new PlainProducer("localhost:9092").produce(2);

        new CustomProducer("localhost:9092").produce(2);
    }

}
