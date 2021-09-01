package com.zhf.tkmapperstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TKMapperStudyApplication {

    public static void main(String[] args) {
        // SpringApplication.run(UnileverSpcApplication.class, args);
        SpringApplication application = new SpringApplication(TKMapperStudyApplication.class);
        // application.addListeners(new ApplicationStartedEventListener());
        application.run(args);
    }

}
