package com.justtest;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.CountDownLatch;

/**
 * 启动类
 *
 */
@SpringBootApplication
public class App
{
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
        logger.info("项目启动!");
    }

}
