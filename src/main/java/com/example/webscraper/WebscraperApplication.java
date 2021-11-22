package com.example.webscraper;

import com.example.services.CompanyScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebscraperApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WebscraperApplication.class, args);
        CompanyScraperService companyScraperService = new CompanyScraperService();
        companyScraperService.scrape();
    }

}
