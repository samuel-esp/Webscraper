package com.example.services;

import com.example.configuration.SeleniumConfiguration;
import com.example.entities.Lawyer;
import com.example.entities.LawyerOffice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class JustiaScraperService {

    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();

    public void scrape() throws InterruptedException, IOException {

        System.out.println("prova");
        ChromeDriver driver = new ChromeDriver();
        //mi estraggo tutti i link delle imprese edilizie da Yelp e la salvo sul set (1)
        Set<String> lawyersLinks = extractLawyerLinks(driver);

        //estraggo informazione da tutti i link presenti nel set "companyLinks" (2)
        List<Lawyer> lawyerList = extractInformation(driver, lawyersLinks);
        driver.quit();

        //mappo tutta la lista della companies su un json da mandare in output sulla cartella target (3)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("target/lawyers.json"), lawyerList);


    }

    public Set<String> extractLawyerLinks(ChromeDriver driver) throws InterruptedException {

        Set<String> lawyersLinks = new HashSet<>();
        List<String> justiaSeeds = initializeSeeds();
        Collections.shuffle(justiaSeeds);

        int i = 0;

        for (String URL: justiaSeeds) {
            i = 0;
            while (lawyersLinks.size() <= 1050 && i <= 25) {
                StringBuilder stringBuilder = new StringBuilder();
                String newUrl = stringBuilder.append(URL).append(i).toString();
                driver.get(newUrl);
                if(i%5==0 || i==0) {
                    Thread.sleep(25000);
                }else{
                    Thread.sleep(3000);
                }
                List<WebElement> refListPremium = driver.findElementsByXPath("//a[@class='url mainprofilelink']");
                List<WebElement> refList = driver.findElementsByXPath("//a[@class='url main-profile-link']");
                for (WebElement element : refListPremium) {
                    String lawyerURL = element.getAttribute("href");
                    System.out.println(lawyerURL);
                    lawyersLinks.add(lawyerURL);
                }
                for (WebElement element : refList) {
                    String lawyerURL = element.getAttribute("href");
                    System.out.println(lawyerURL);
                    lawyersLinks.add(lawyerURL);
                }
                System.out.println("Lawyers URLs Scraped: " + lawyersLinks.size());
                i = i + 1;
            }
        }

        return lawyersLinks;

    }

    public List<Lawyer> extractInformation(ChromeDriver driver, Set<String> lawyersLinks) throws InterruptedException {

        List<Lawyer> lawyersList = new ArrayList<>();
        int i = 0;

        for (String link : lawyersLinks) {
            if (i == 100) {
                Thread.sleep(15000);
            }
            driver.get(link);
            Thread.sleep(3000);
            System.out.println(link + "\n\n");

            String name = null;
            Double yearsOfExperience = null;
            List<String> specializationList = new LinkedList<>();
            List<String> statesOfAbilitation = new LinkedList<>();
            String website = null;
            Set<LawyerOffice> lawyerOfficeSet = new HashSet<>();

            try {
                name = driver.findElementByXPath("//h1[@class='fn lawyer-name']").getText();
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                String yearsOfExperienceString = driver.findElementByXPath("//li[@class='iconed-line']//time").getText();
                yearsOfExperience = Double.parseDouble(yearsOfExperienceString.replaceAll("\\D+", ""));
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                List<WebElement> specializationWebElementList = driver.findElementsByXPath("//div[@id='practice_areas']//dt[@class='dsc-term']");
                for (WebElement element : specializationWebElementList) {
                    String field = element.getText();
                    specializationList.add(field);
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                List<WebElement> statesOfAbilitationWebElementList = driver.findElementsByXPath("//li[@class='iconed-line']//span[@class='jicon jicon-jurisdictions jicon-inline']/parent::*");
                for (WebElement element : statesOfAbilitationWebElementList) {
                    String field = element.getText();
                    List<String> splittedString = Arrays.asList(field.split(", "));
                    statesOfAbilitation = splittedString;
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                website = driver.findElementByXPath("//a[@aria-label='Website 1']").getAttribute("href");
            } catch (org.openqa.selenium.NoSuchElementException e) {

            }
            try {
                List<WebElement> officeWebElementList = driver.findElementsByXPath("//div[@class='office']");
                for(WebElement element: officeWebElementList){

                    LawyerOffice office = new LawyerOffice();

                    String address = element.findElement(By.xpath("//div[@class='street-address']")).getText();
                    String locality = element.findElement(By.xpath("//span[@class='locality']")).getText();
                    String state = element.findElement(By.xpath("//span[@class='region']")).getText();;
                    String postalCode = element.findElement(By.xpath("//span[@class='postal-code']")).getText();;
                    String telephone = element.findElement(By.xpath("//span[@class='value']//strong[contains(text(),'Telephone:')]/parent::*//span")).getText();

                    office.setAddress(address);
                    office.setCity(locality);
                    office.setState(state);
                    office.setPostalcode(postalCode);
                    office.setTelephone(telephone);

                    lawyerOfficeSet.add(office);

                }
        } catch (org.openqa.selenium.NoSuchElementException e) {

        }

            Lawyer l = new Lawyer();
            l.setId(UUID.randomUUID().toString());
            l.setName(name);
            l.setSpecialization(specializationList);
            l.setStatesOfAbilitation(statesOfAbilitation);
            l.setWebsite(website);
            l.setYearsOfExperience(yearsOfExperience);
            l.setLawyerOfficeSet(lawyerOfficeSet);

            System.out.println(l.getName());
            System.out.println(l.getYearsOfExperience());
            System.out.println(l.getStatesOfAbilitation());
            System.out.println(l.getSpecialization());
            System.out.println(l.getWebsite());
            System.out.println(l.getLawyerOfficeSet() + "\n\n");

            lawyersList.add(l);
            i = i + 1;

        }

        return lawyersList;

    }


    public List<String> initializeSeeds(){

        List<String> justiaSeeds = new LinkedList<>();
        justiaSeeds.add("https://www.justia.com/lawyers/arizona?page=");
        justiaSeeds.add("https://www.justia.com/lawyers/california?page=");

        return justiaSeeds;


    }

}
