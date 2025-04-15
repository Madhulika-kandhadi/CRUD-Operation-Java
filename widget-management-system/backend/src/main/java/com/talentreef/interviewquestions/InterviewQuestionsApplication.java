package com.talentreef.interviewquestions;

import com.talentreef.interviewquestions.takehome.config.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.talentreef")
@EntityScan(basePackages = "com.talentreef")
@EnableAutoConfiguration
@EnableConfigurationProperties(CorsProperties.class)
public class InterviewQuestionsApplication {

  public static void main(String[] args) {
    SpringApplication.run(InterviewQuestionsApplication.class, args);
  }

}
