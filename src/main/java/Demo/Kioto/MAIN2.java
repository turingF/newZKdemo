package Demo.Kioto;

import Demo.Kioto.custom.CustomProcessor;
import Demo.Kioto.plain.PlainProcessor;

public class MAIN2 {
    public static void main(String[] args) {
//        new PlainProcessor("localhost:9092").process();
        new CustomProcessor("localhost:9092").process();
    }
}
