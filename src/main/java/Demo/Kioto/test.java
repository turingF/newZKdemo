package Demo.Kioto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;

import java.util.concurrent.TimeUnit;

public class test {
    public static void main(String[] args) {

        Faker faker = new Faker();

        HealthCheck fakeCheck = new HealthCheck(
                "Health_Check",
                faker.address().city(),
                faker.bothify("??##-??##",true),
                Constants.machineType.values()
                        [faker.number().numberBetween(0,4)].toString(),
                Constants.machineStates.values()
                        [faker.number().numberBetween(0,3)].toString(),
                faker.date().past(100, TimeUnit.DAYS),
                faker.number().numberBetween(100L,0L),
                faker.internet().ipV4Address()
        );

        try {
            byte [] temp1 = Constants.getJsonMapper().writeValueAsBytes(fakeCheck);

            byte [] temp2 = JSONObject.toJSONBytes(fakeCheck);

            Object it = Constants.getJsonMapper().readValue(temp1, HealthCheck.class);
            Object it2 = JSON.parseObject(temp2,HealthCheck.class);


            HealthCheck hc = (HealthCheck)it;
            HealthCheck hc2 = (HealthCheck)it2;

            System.out.println(hc2.getSerialNumber());


        }catch (Exception e){
            e.printStackTrace();

        }


    }
}
