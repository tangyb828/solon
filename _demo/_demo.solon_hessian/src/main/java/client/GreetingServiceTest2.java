package client;

import org.noear.nami.Nami;
import org.noear.nami.coder.hession.HessianDecoder;
import org.noear.nami.coder.snack3.SnackEncoder;
import server.dso.IGreetingService;

public class GreetingServiceTest2 {
    public static void main(String[] args) throws Exception {
        //接口的动态代理工厂
        IGreetingService service = Nami.builder()
                .encoder(SnackEncoder.instance)
                .decoder(HessianDecoder.instance)
                .upstream(()-> "http://localhost:8080")
                .create(IGreetingService.class);


        String result = service.greeting("tom");

        //远程方法调用
        System.out.println("hello(), " + result);
    }
}
