package com.example.services;

import com.example.configuration.SeleniumConfiguration;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.sql.Driver;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CompanyScraperService {

    private final String URL =  "https://www.yelp.it/search?cflt=contractors&find_loc=Los%20Angeles%2C%20CA%2C%20Stati%20Uniti&start=";

    private final String yelpURL = "https://www.yelp.it";
    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();

    public void scrape() throws InterruptedException {

        System.out.println("prova");
        ChromeDriver driver = new ChromeDriver();

        //mi estraggo tutti i link delle imprese edilizie da Yelp e la salvo sul set (1)
        Set<String> companyLinks = extractCompanyLinks(driver);


        driver.quit();

    }

    //Funzione che estrae tutti i link da Yelp e li salva su un set (1)
    public Set<String> extractCompanyLinks(ChromeDriver driver) throws InterruptedException {

        Set<String> companyLinks = new HashSet<>();

        for (int i = 0; i < 101; i=i+10) {
            int j = 0;
            StringBuilder stringBuilder = new StringBuilder();
            String newUrl = stringBuilder.append(URL).append(i).toString();
            driver.get(newUrl);
            Thread.sleep(10000);
            List<WebElement> refList = driver.findElementsByClassName("css-1422juy");
            for(int k=11; k<=20; k = k+1){
                if(refList.get(k)!=null) {
                    System.out.println(refList.get(k).getAttribute("href"));
                    j = j+1;
                    companyLinks.add(refList.get(k).getAttribute("href"));
                }
            }
            System.out.println(j);
        }

        return companyLinks;

    }




}
