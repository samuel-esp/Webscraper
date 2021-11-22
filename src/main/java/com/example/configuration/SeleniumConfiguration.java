package com.example.configuration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Configuration
public class SeleniumConfiguration {

    @PostConstruct
    public void PostConstruct(){
        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(getClass().getClassLoader().getResource("ChromeDriver/chromedriver")).getFile());
    }

    @Bean
    public ChromeDriver driver(){
        return new ChromeDriver();

    }


}
