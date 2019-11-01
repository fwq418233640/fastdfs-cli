package com.ch.fastdfsapi;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;

@EnableScheduling
@SpringBootApplication
@EnableSwagger2
@Slf4j
public class Run extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Run.class, args);
        ConfigurableEnvironment environment = run.getEnvironment();
        String file_path = environment.getProperty("file_path");
        log.info("============= TRACKER SERVER ADDR : {} {}"
                , environment.getProperty("com.ikingtech.fastdfs-client.tracker_server")
                , " =============");

        if (StringUtils.isEmpty(file_path)) {
            file_path = System.getProperty("java.io.tmpdir");
        }
        File file = new File(file_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        log.info("初始化目录成功");
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Run.class);
    }

}


