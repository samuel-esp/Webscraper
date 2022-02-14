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

    /*
    private final String URL = "https://www.yelp.it/search?cflt=contractors&find_loc=Los%20Angeles%2C%20CA%2C%20Stati%20Uniti&start=";

    private final String yelpURL = "https://www.yelp.it";
    */

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
        List<String> yelpSeeds = initializeSeeds();
        Collections.shuffle(yelpSeeds);

        int i = 0;
        for (String URL: yelpSeeds) {
            i = 0;
            while (companyLinks.size() <= 1050 && i <= 25) {
                StringBuilder stringBuilder = new StringBuilder();
                String newUrl = stringBuilder.append(URL).append(i).toString();
                driver.get(newUrl);
                if(i%5==0 || i==0) {
                    Thread.sleep(5000);
                }else{
                    Thread.sleep(3000);
                }
                List<WebElement> refList = driver.findElementsByXPath("//span[@class=' css-1uq0cfn']//a");
                for (WebElement element : refList) {
                    String companyURL = element.getAttribute("href");
                    System.out.println(companyURL);
                    companyLinks.add(companyURL);
                }
                System.out.println("Company URLs Scraped: " + companyLinks.size());
                i = i + 1;
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
                workingHours = driver.findElementByXPath("//span[@class=' display--inline__09f24__c6N_k margin-l1__09f24__m8GL9 border-color--default__09f24__NPAKY']//span[@class=' css-1ccncw']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                companyType = driver.findElementByXPath("//span[@class=' css-1h7ysrc']//a").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                companyAddress = driver.findElementByXPath("//div[@class=' css-1vhakgw border--top__09f24__exYYb border-color--default__09f24__NPAKY']//p[@class=' css-1ccncw']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                companyWebsite = driver.findElementByXPath("//div[@class=' arrange-unit__09f24__rqHTg arrange-unit-fill__09f24__CUubG border-color--default__09f24__NPAKY']//p[@class=' css-1nv8jdk' and contains(.,  \"Sito web dell'attività\")]/following-sibling::p//a").getAttribute("href");
            } catch (NoSuchElementException e) {

            }
            try {
                companyMobileNumber = driver.findElementByXPath("//div[@class=' css-1vhakgw border--top__09f24__exYYb border-color--default__09f24__NPAKY']//p[@class=' css-1nv8jdk' and contains(., 'Numero di telefono')]/following-sibling::p").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                String companyStars = driver.findElementByXPath("//div[@class=' arrange__09f24__LDfbs gutter-1-5__09f24__vMtpw vertical-align-middle__09f24__zU9sE margin-b2__09f24__CEMjT border-color--default__09f24__NPAKY']//div[@role='img'][1]").getAttribute("aria-label");
                companyStars = StringUtils.substringBefore(companyStars, " ");
                companyStarsConverted = Double.parseDouble(companyStars);
            } catch (NoSuchElementException e) {

            }
            try {
                String companyReviews = driver.findElementByXPath("//div[@class=' arrange-unit__09f24__rqHTg arrange-unit-fill__09f24__CUubG border-color--default__09f24__NPAKY nowrap__09f24__lBkC2']//span").getText();
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

    public List<String> initializeSeeds(){

        List<String> yelpSeeds = new LinkedList<>();
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Los%20Angeles%2C%20CA%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Phoenix%2C%20AZ%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Dallas%2C%20TX%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Austin%2C%20TX%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Atlanta%2C%20GA%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Chicago%2C%20IL%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Houston%2C%20TX%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Tulsa%2C%20OK%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Miami%2C%20FL%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Boise%2C%20ID%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Detroit%2C%20MI%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Seattle%2C%20WA%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Portland%2C%20OR%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Sacramento%2C%20CA%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Minneapolis%2C%20MN%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?cflt=contractors&find_loc=Columbus%2C%20WA%2C%20Stati%20Uniti&start=");
        yelpSeeds.add("https://www.yelp.it/search?find_desc=contractors&find_loc=Salt+Lake+City%2C+UT&start=");
        yelpSeeds.add("https://www.yelp.it/search?find_desc=contractors&find_loc=New+York%2C+NY&start=");




        return yelpSeeds;


    }

}
