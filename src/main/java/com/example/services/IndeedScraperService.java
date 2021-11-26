package com.example.services;

import com.example.configuration.SeleniumConfiguration;
import com.example.entities.IndeedCompany;
import com.example.entities.TennisPlayer;
import com.example.entities.YelpCompany;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IndeedScraperService {

    private final String URLPrefix = "https://www.indeed.com/jobs?q=Software%20Engineer&start=";
    private final String URLSuffix = "&vjk=b3abf389027891db";

    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();

    public void scrape() throws InterruptedException, IOException {

        System.out.println("prova");
        ChromeDriver driver = new ChromeDriver();

        //mi estraggo tutti i link delle imprese edilizie da Yelp e la salvo sul set (1)
        Set<String> companyLinks = extractCompanyLinks(driver);

        //estraggo informazione da tutti i link presenti nel set "companyLinks" (2)
        List<IndeedCompany> indeedCompanyList = extractInformation(driver, companyLinks);

        //mappo tutta la lista della companies su un json da mandare in output sulla cartella target (3)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("target/indeed.json"), indeedCompanyList);
        driver.quit();


    }

    public Set<String> extractCompanyLinks(ChromeDriver driver) throws InterruptedException {

        Set<String> companyLinks = new HashSet<>();
        int i = 0;

        while (companyLinks.size()<=30){
            StringBuilder stringBuilder = new StringBuilder();
            String newUrl = stringBuilder.append(URLPrefix).append(i).append(URLSuffix).toString();
            driver.get(newUrl);
            Thread.sleep(3000);
            List<WebElement> refList = driver.findElementsByXPath("//span[@class='companyName']//a");
            for (WebElement element : refList) {
                String companyURL = element.getAttribute("href").toString() + "/about";
                System.out.println(companyURL);
                companyLinks.add(companyURL);
            }
            i = i+10;
        }

        return companyLinks;

    }

    public List<IndeedCompany> extractInformation(ChromeDriver driver, Set<String> companyLinks) throws InterruptedException {

        List<IndeedCompany> indeedCompanyList = new ArrayList<>();

        for (String link : companyLinks) {
            driver.get(link);
            Thread.sleep(3500);
            System.out.println(link + "\n\n");

            IndeedCompany c;

            if(driver.findElementsByXPath("//div[@id='company-details-section']").isEmpty()){
                 c = processOldLayout(driver);
            }else{
                 c = processNewLayout(driver);
            }

            /*
            String name = null;
            String headquarters = null;
            String employeesSize = null;
            String industryType = null;
            List<String> websiteList = new LinkedList<>();
            String happinessScore = null;
            Double reviewScore = null;
            Integer jobOffersCount = null;

            try {
                name = driver.findElementByXPath("//span[@class='cmp-CompactHeaderCompanyName']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                industryType = driver.findElementByXPath("//a[@class='cmp-AboutBasicCompanyDetailsWidget-industryLink']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                happinessScore = driver.findElementByXPath("//span[@class='cmp-HappinessCompanyRating-happinessScore']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                reviewScore = Double.parseDouble(driver.findElementByXPath("//span[@class='cmp-CompactHeaderCompanyRatings-value']").getText());
            } catch (NoSuchElementException e) {

            }
            try {
                headquarters = driver.findElementByXPath("//div[@data-testid='headquarters']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                employeesSize = driver.findElementByXPath("//div[@data-testid='employees']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                List<WebElement> websites = driver.findElementsByXPath("//a[@class='cmp-AboutBasicCompanyDetailsWidget-companyLink']");
                if(!websites.isEmpty()) {
                    for (WebElement element : websites) {
                        String website = element.getAttribute("href");
                        websiteList.add(website);
                    }
                }
            } catch (NoSuchElementException e) {

            }
            try {
                String jobOffersString = driver.findElementByXPath("//div[@class='cmp-AboutBasicJobsWidget-allJobsText']").getText();
                jobOffersString = jobOffersString.replaceAll("\\D+", "");
                jobOffersCount = Integer.parseInt(jobOffersString);
            } catch (NoSuchElementException e) {

            }

            IndeedCompany c = new IndeedCompany();
            c.setId(UUID.randomUUID().toString());
            c.setName(name);
            c.setIndustryType(industryType);
            c.setHeadquarters(headquarters);
            c.setWebsiteList(websiteList);
            c.setEmployeesSize(employeesSize);
            c.setHappinessScore(happinessScore);
            c.setReviewScore(reviewScore);
            c.setJobOffersCount(jobOffersCount);

            System.out.println(c.getName());
            System.out.println(c.getIndustryType());
            System.out.println(c.getHeadquarters());
            System.out.println(c.getWebsiteList());
            System.out.println(c.getEmployeesSize());
            System.out.println(c.getHappinessScore());
            System.out.println(c.getReviewScore());
            System.out.println(c.getJobOffersCount() + "\n\n");*/
            indeedCompanyList.add(c);


        }

        return indeedCompanyList;

    }

    public IndeedCompany processNewLayout(ChromeDriver driver){

        String name = null;
        String headquarters = null;
        String employeesSize = null;
        String industryType = null;
        List<String> websiteList = new LinkedList<>();
        String happinessScore = null;
        Double reviewScore = null;
        Integer jobOffersCount = null;

        try {
            name = driver.findElementByXPath("//span[@itemprop='name']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            industryType = driver.findElementByXPath("//div[@class='cmp-CompanyDetails']//a[@rel='nofollow']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            happinessScore = driver.findElementByXPath("//span[@class='cmp-HappinessCompanyRating-happinessScore']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            reviewScore = Double.parseDouble(driver.findElementByXPath("//span[@class='cmp-CompactHeaderCompanyRatings-value']").getText());
        } catch (NoSuchElementException e) {

        }
        try {
            headquarters = driver.findElementByXPath("//div[@class='cmp-CompanyDetailSection-content']//span[@class='cmp-NewLineToBr-text'][1]").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            employeesSize = driver.findElementByXPath("//div[@class='cmp-CompanyDetailSection'][3]//div[@class='cmp-CompanyDetailSection-content']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            List<WebElement> websites = driver.findElementsByXPath("//div[@class='cmp-CompanyDetails']//a[@rel='noopener nofollow']");
            if(!websites.isEmpty()) {
                for (WebElement element : websites) {
                    String website = element.getAttribute("href");
                    websiteList.add(website);
                }
            }
        } catch (NoSuchElementException e) {

        }
        try {
            String jobOffersString = driver.findElementByXPath("//div[@class='cmp-JobSection-linkText']").getText();
            jobOffersString = jobOffersString.replaceAll("\\D+", "");
            jobOffersCount = Integer.parseInt(jobOffersString);
        } catch (NoSuchElementException e) {

        }

        IndeedCompany c = new IndeedCompany();
        c.setId(UUID.randomUUID().toString());
        c.setName(name);
        c.setIndustryType(industryType);
        c.setHeadquarters(headquarters);
        c.setWebsiteList(websiteList);
        c.setEmployeesSize(employeesSize);
        c.setHappinessScore(happinessScore);
        c.setReviewScore(reviewScore);
        c.setJobOffersCount(jobOffersCount);

        System.out.println(c.getName());
        System.out.println(c.getIndustryType());
        System.out.println(c.getHeadquarters());
        System.out.println(c.getWebsiteList());
        System.out.println(c.getEmployeesSize());
        System.out.println(c.getHappinessScore());
        System.out.println(c.getReviewScore());
        System.out.println(c.getJobOffersCount() + "\n\n");

        return c;


    }

    public IndeedCompany processOldLayout(ChromeDriver driver){

        String name = null;
        String headquarters = null;
        String employeesSize = null;
        String industryType = null;
        List<String> websiteList = new LinkedList<>();
        String happinessScore = null;
        Double reviewScore = null;
        Integer jobOffersCount = null;

        try {
            name = driver.findElementByXPath("//span[@class='cmp-CompactHeaderCompanyName']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            industryType = driver.findElementByXPath("//a[@class='cmp-AboutBasicCompanyDetailsWidget-industryLink']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            happinessScore = driver.findElementByXPath("//span[@class='cmp-HappinessCompanyRating-happinessScore']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            reviewScore = Double.parseDouble(driver.findElementByXPath("//span[@class='cmp-CompactHeaderCompanyRatings-value']").getText());
        } catch (NoSuchElementException e) {

        }
        try {
            headquarters = driver.findElementByXPath("//div[@data-testid='headquarters']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            employeesSize = driver.findElementByXPath("//div[@data-testid='employees']").getText();
        } catch (NoSuchElementException e) {

        }
        try {
            List<WebElement> websites = driver.findElementsByXPath("//a[@class='cmp-AboutBasicCompanyDetailsWidget-companyLink']");
            if(!websites.isEmpty()) {
                for (WebElement element : websites) {
                    String website = element.getAttribute("href");
                    websiteList.add(website);
                }
            }
        } catch (NoSuchElementException e) {

        }
        try {
            String jobOffersString = driver.findElementByXPath("//div[@class='cmp-AboutBasicJobsWidget-allJobsText']").getText();
            jobOffersString = jobOffersString.replaceAll("\\D+", "");
            jobOffersCount = Integer.parseInt(jobOffersString);
        } catch (NoSuchElementException e) {

        }

        IndeedCompany c = new IndeedCompany();
        c.setId(UUID.randomUUID().toString());
        c.setName(name);
        c.setIndustryType(industryType);
        c.setHeadquarters(headquarters);
        c.setWebsiteList(websiteList);
        c.setEmployeesSize(employeesSize);
        c.setHappinessScore(happinessScore);
        c.setReviewScore(reviewScore);
        c.setJobOffersCount(jobOffersCount);

        System.out.println(c.getName());
        System.out.println(c.getIndustryType());
        System.out.println(c.getHeadquarters());
        System.out.println(c.getWebsiteList());
        System.out.println(c.getEmployeesSize());
        System.out.println(c.getHappinessScore());
        System.out.println(c.getReviewScore());
        System.out.println(c.getJobOffersCount() + "\n\n");

        return c;

    }


}