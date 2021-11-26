package com.example.services;
import com.example.configuration.SeleniumConfiguration;
import com.example.entities.YelpCompany;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
public class YelpScraperService {

    private final String URL = "https://www.yelp.it/search?cflt=contractors&find_loc=Los%20Angeles%2C%20CA%2C%20Stati%20Uniti&start=";

    private final String yelpURL = "https://www.yelp.it";
    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();

    public void scrape() throws InterruptedException, IOException {

        System.out.println("prova");
        ChromeDriver driver = new ChromeDriver();

        //mi estraggo tutti i link delle imprese edilizie da Yelp e la salvo sul set (1)
        Set<String> companyLinks = extractCompanyLinks(driver);

        //estraggo informazione da tutti i link presenti nel set "companyLinks" (2)
        List<YelpCompany> yelpCompanyList = extractInformation(driver, companyLinks);

        //mappo tutta la lista della companies su un json da mandare in output sulla cartella target (3)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("target/companies.json"), yelpCompanyList);
        driver.quit();


        }


    //Funzione che estrae tutti i link dalla pagina di ricerca di Yelp sezione aziende edili e li salva su un set (1)
    public Set<String> extractCompanyLinks(ChromeDriver driver) throws InterruptedException {

        Set<String> companyLinks = new HashSet<>();

        for (int i = 0; i < 100; i = i + 10) {
            StringBuilder stringBuilder = new StringBuilder();
            String newUrl = stringBuilder.append(URL).append(i).toString();
            driver.get(newUrl);
            Thread.sleep(3000);
            List<WebElement> refList = driver.findElementsByXPath("//span[@class=' css-1uq0cfn']//a");
            for (WebElement element : refList) {
                System.out.println(element.getAttribute("href"));
                companyLinks.add(element.getAttribute("href"));
            }
        }

        return companyLinks;

    }

    //Funzione che estrae l'informazione da ogni pagina Yelp salvata in companyLinks. L'informazione estratta viene codificata sulla entity "Company" e aggiunta ad una lista.
    // Il processo termina quando tutto il set CompanyLinks e' visitato e ogni entity e' mappata e aggiunta alla lista(1)
    public List<YelpCompany> extractInformation(ChromeDriver driver, Set<String> companyLinks) throws InterruptedException {

        List<YelpCompany> yelpCompanyList = new ArrayList<>();

        for (String link : companyLinks) {
            driver.get(link);
            Thread.sleep(3500);
            System.out.println(link + "\n\n");

            String workingHours = null;
            String companyName = null;
            String companyType = null;
            String companyWebsite = null;
            String companyMobileNumber = null;
            String companyAddress = null;
            Integer companyReviewsConverted = null;
            Double companyStarsConverted = null;

            try {
                companyName = driver.findElementByClassName("css-1x9iesk").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                workingHours = driver.findElementByXPath("//div[@class=' display--inline-block__373c0__39WKb margin-r1-5__373c0__zJ1ZR border-color--default__373c0__2s5dW']//span[@class=' css-v2vuco']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                companyType = driver.findElementByXPath("//span[@class= ' display--inline__373c0__3d-lf margin-r1__373c0__7ZINV border-color--default__373c0__2s5dW']//span[@class=' css-oe5jd3']//a").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                companyAddress = driver.findElementByXPath("//div[@class=' css-1vhakgw border--top__373c0__1YJkA border-color--default__373c0__r305k']//p[@class=' css-v2vuco']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                companyWebsite = driver.findElementByXPath("//div[@class=' css-1vhakgw border--top__373c0__1YJkA border-color--default__373c0__r305k']//p[@class=' css-1u2njw' and contains(., \"Sito web dell'attività\")]/following-sibling::p//a").getAttribute("href");
            } catch (NoSuchElementException e) {

            }
            try {
                companyMobileNumber = driver.findElementByXPath("//div[@class=' css-1vhakgw border--top__373c0__1YJkA border-color--default__373c0__r305k']//p[@class=' css-1u2njw' and contains(., 'Numero di telefono')]/following-sibling::p").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                String companyStars = driver.findElementByXPath("//span[@class=' display--inline__373c0__3d-lf border-color--default__373c0__2s5dW']//div").getAttribute("aria-label");
                companyStars = StringUtils.substringBefore(companyStars, " ");
                companyStarsConverted = Double.parseDouble(companyStars);
            } catch (NoSuchElementException e) {

            }
            try {
                String companyReviews = driver.findElementByXPath("//div[@class=' arrange-unit__373c0__2u2cR arrange-unit-fill__373c0__3cIO5 border-color--default__373c0__2s5dW nowrap__373c0__AzEKB']//span").getText();
                companyReviews = StringUtils.substringBefore(companyReviews, " ");
                companyReviewsConverted = Integer.parseInt(companyReviews);
            } catch (NoSuchElementException e) {

            }

            YelpCompany c = new YelpCompany();
            c.setId(UUID.randomUUID().toString());
            c.setName(companyName);
            c.setType(companyType);
            c.setAddress(companyAddress);
            c.setWebsite(companyWebsite);
            c.setMobileNumber(companyMobileNumber);
            c.setWorkingHours(workingHours);
            c.setStarsCount(companyStarsConverted);
            c.setReviewsCount(companyReviewsConverted);

            System.out.println(c.getName());
            System.out.println(c.getType());
            System.out.println(c.getAddress());
            System.out.println(c.getWebsite());
            System.out.println(c.getMobileNumber());
            System.out.println(c.getStarsCount());
            System.out.println(c.getReviewsCount());
            System.out.println(c.getWorkingHours() + "\n\n");
            yelpCompanyList.add(c);


        }

        return yelpCompanyList;

    }

}
