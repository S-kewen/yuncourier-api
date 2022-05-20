package com.boot.yuncourier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;

/**
 * @Author: skwen
 * @Description: YuncourierApplication-SpringBoot項目啟動類
 * @Date: 2020-01-22
 */

@SpringBootApplication
public class YuncourierApplication {
    //    @Value("${http.port}")
//    private int httpPort;
//    @Value("${server.port}")
//    private int port;
    public static void main(String[] args) throws ParseException {
        SpringApplication.run(YuncourierApplication.class, args);
        System.out.println("YuncourierApplication-启动完成!!!!");
    }
//    /**
//     * http重定向到https
//     * @return
//     */
//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//    }
//
//    @Bean
//    public Connector httpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        //Connector监听的http的端口号
//        connector.setPort(httpPort);
//        connector.setSecure(false);
//        //监听到http的端口号后转向到的https的端口号
//        connector.setRedirectPort(port);
//        return connector;
//    }
}
