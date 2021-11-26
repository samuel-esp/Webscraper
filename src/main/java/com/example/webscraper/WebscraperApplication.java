package com.example.webscraper;

import com.example.services.IndeedScraperService;
import com.example.services.YelpScraperService;
import com.example.services.AtpScraperService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class WebscraperApplication {

    public static void main(String[] args) throws InterruptedException, IOException {

        SpringApplication.run(WebscraperApplication.class, args);

        System.out.println("TYPE 1 TO SCRAPE YELP.COM ...\n");
        System.out.println("TYPE 2 TO SCRAPE ATPTOUR.COM ...\n");
        System.out.println("TYPE 3 TO SCRAPE INDEED.COM ...\n");
        System.out.println("TYPE 4 TO SCRAPE FANTACALCIO.IT ...\n");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();

            if (Integer.parseInt(str) == 1) {

                YelpScraperService yelpScraperService = new YelpScraperService();
                yelpScraperService.scrape();

            } else if (Integer.parseInt(str) == 2) {

                AtpScraperService atpScraperService = new AtpScraperService();
                atpScraperService.scrape();

            } else if (Integer.parseInt(str) == 3) {

                IndeedScraperService indeedScraperService = new IndeedScraperService();
                indeedScraperService.scrape();


            } else {
                System.out.println("YOUR INPUT IS NOT VALID ...");
            }

    }

}
