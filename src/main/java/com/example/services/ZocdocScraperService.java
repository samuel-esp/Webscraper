package com.example.services;

import com.example.configuration.SeleniumConfiguration;
import com.example.entities.Doctor;
import com.example.entities.Lawyer;
import com.example.entities.LawyerOffice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ZocdocScraperService {

    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();

    public void scrape() throws InterruptedException, IOException {

        System.out.println("prova");
        ChromeDriver driver = new ChromeDriver();
        //mi estraggo tutti i link delle imprese edilizie da Yelp e la salvo sul set (1)
        Set<String> doctorLinks = extractDoctorLinks(driver);

        //estraggo informazione da tutti i link presenti nel set "companyLinks" (2)
        List<Doctor> doctorList = extractInformation(driver, doctorLinks);
        driver.quit();

        //mappo tutta la lista della companies su un json da mandare in output sulla cartella target (3)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("target/doctors.json"), doctorList);


    }

    public Set<String> extractDoctorLinks(ChromeDriver driver) throws InterruptedException {

        Set<String> doctorLinks = new HashSet<>();
        List<String> zocdocSeeds = initializeSeeds();
        Collections.shuffle(zocdocSeeds);

        int i = 0;
        for (String URL: zocdocSeeds) {
            i = 0;
            while (doctorLinks.size() <= 100 && i <= 25) {
                StringBuilder stringBuilder = new StringBuilder();
                String newUrl = stringBuilder.append(URL).append(i).toString();
                driver.get(newUrl);
                if(i%5==0 || i==0) {
                    Thread.sleep(25000);
                }else{
                    Thread.sleep(15000);
                }
                List<WebElement> refList = driver.findElementsByXPath("//a[@data-test='doctor-card-info-name']");
                for (WebElement element : refList) {
                    String lawyerURL = element.getAttribute("href");
                    System.out.println(lawyerURL);
                    doctorLinks.add(lawyerURL);
                }
                System.out.println("Doctors URLs Scraped: " + doctorLinks.size());
                i = i + 1;
            }
        }

        return doctorLinks;

    }

    public List<Doctor> extractInformation(ChromeDriver driver, Set<String> doctorLinks) throws InterruptedException {

        List<Doctor> doctorList = new ArrayList<>();
        int i = 0;

        for (String link : doctorLinks) {
            if (i == 100) {
                Thread.sleep(25000);
            }
            driver.get(link);
            Thread.sleep(15000);
            System.out.println(link + "\n\n");

            String name = null;
            String NPINumber = null;
            List<String> specializationList = new LinkedList<>();
            List<String> spokenLanguagesList = new LinkedList<>();
            String address = null;
            String city = null;
            String state = null;
            String postalCode = null;
            Double rating = null;

            try {
                name = driver.findElementByXPath("//span[@data-uem-id='provider-name']").getText();
                name = StringUtils.substringBefore(name, ",");
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                NPINumber = driver.findElementByXPath("//p[@class='krbmlv-8 efvsSS']").getText();
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                List<WebElement> specializationWebElementList = driver.findElementsByXPath("//ul[@data-test='specialities-list-items']//li");
                for (WebElement element : specializationWebElementList) {
                    String field = element.getText();
                    specializationList.add(field);
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                List<WebElement> addressList = driver.findElementsByXPath("//div[@data-test='location-card-address-container'][1]//span[@class='g58yd9-5 hElRrV']");
                StringBuilder s = new StringBuilder();
                for (WebElement element: addressList) {
                    String currentString = element.getText();
                    s.append(currentString);
                    s.append(" ");
                }
                address = s.toString();
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            /*
            try {
                address = driver.findElementByXPath("//span[@itemprop='streetAddress']").getText();
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                city = driver.findElementByXPath("//span[@itemprop='addressLocality']").getText();
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                state = driver.findElementByXPath("//span[@itemprop='addressRegion']").getText();
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                postalCode = driver.findElementByXPath("//span[@itemprop='postalCode']").getText();
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }*/
            try {
                rating = Double.parseDouble(driver.findElementByXPath("//div[@class='sc-15uikgc-1 iisYfV']").getText());
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }try {
                List<WebElement> spokenLanguagesWebElementList = driver.findElementsByXPath("//section[@data-test='Languages-section']//ul//li");
                for(WebElement element: spokenLanguagesWebElementList){
                    String language = element.getText();
                    spokenLanguagesList.add(language);
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            /*
            try {
                // //div[@class='dx0sxs-0 nCTkH']//div[@class='dx0sxs-1 fotQIw']
                WebElement viewAll = driver.findElementByXPath("//a[@data-test='popular-in-network-insurances-view-all-plans']");
                Thread.sleep(1000);
                List<WebElement> insuranceWebElementList = driver.findElementsByXPath("//div[@class='dx0sxs-0 nCTkH']//div[@class='dx0sxs-1 fotQIw']") ;
                for(WebElement element: insuranceWebElementList){
                    String insurance = element.getText();
                    acceptedInsuranceList.add(insurance);

                }
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }*/

            Doctor d = new Doctor();
            d.setId(UUID.randomUUID().toString());
            d.setName(name);
            d.setNPINumber(NPINumber);
            d.setSpokenLanguages(spokenLanguagesList);
            d.setSpecialization(specializationList);
            d.setAddress(address);
            /*
            d.setCity(city);
            d.setState(state);
            */
            d.setRating(rating);

            System.out.println(d.getName());
            System.out.println(d.getNPINumber());
            System.out.println(d.getSpokenLanguages());
            System.out.println(d.getSpecialization());
            System.out.println(d.getAddress());/*
            System.out.println(d.getCity());
            System.out.println(d.getState());*/
            System.out.println(d.getRating());

            doctorList.add(d);
            System.out.println("Doctor List Size: " + doctorList.size());
            i = i + 1;


        }

        return doctorList;

    }


    public List<String> initializeSeeds(){

        List<String> zocdocSeeds = new LinkedList<>();
        zocdocSeeds.add("https://www.zocdoc.com/search?address=Los+Angeles%2C+California%2C+Stati+Uniti&city=Los+Angeles&date_searched_for=2021-12-28&&offset=");
        zocdocSeeds.add("https://www.zocdoc.com/search?address=Atlanta%2C+Georgia%2C+Stati+Uniti&city=Atlanta&date_searched_for=2021-12-28&&offset=");
        zocdocSeeds.add("https://www.zocdoc.com/search?address=Phoenix%2C+Arizona%2C+Stati+Uniti&city=Phoenix&date_searched_for=2021-12-28&&offset=");
        zocdocSeeds.add("https://www.zocdoc.com/search?address=Dallas%2C+Texas%2C+Stati+Uniti&city=Dallas&date_searched_for=2021-12-28&&offset=");
        zocdocSeeds.add("https://www.zocdoc.com/search?address=Houston%2C+Texas%2C+Stati+Uniti&city=Dallas&date_searched_for=2021-12-28&&offset=");
        zocdocSeeds.add("https://www.zocdoc.com/search?address=Chicago%2C+Illinois%2C+Stati+Uniti&city=Dallas&date_searched_for=2021-12-28&&offset=");


        return zocdocSeeds;


    }


}
