package com.example.webscraper;

import com.example.services.CompanyScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WebscraperApplication {

    public static void main(String[] args) throws InterruptedException, IOException {
        SpringApplication.run(WebscraperApplication.class, args);
        CompanyScraperService companyScraperService = new CompanyScraperService();
        companyScraperService.scrape();
    }

}
