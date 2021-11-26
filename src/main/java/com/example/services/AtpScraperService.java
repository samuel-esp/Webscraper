package com.example.services;

import com.example.configuration.SeleniumConfiguration;
import com.example.entities.TennisPlayer;
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
public class AtpScraperService {

    private final String URL = "https://www.atptour.com/en/rankings/singles/?rankDate=2021-11-22&countryCode=all&rankRange=";

    private SeleniumConfiguration seleniumConfiguration = new SeleniumConfiguration();

    public void scrape() throws InterruptedException, IOException {

        System.out.println("prova");

        //mi estraggo tutti i link delle imprese edilizie da Yelp e la salvo sul set (1)
        Set<String> playersLinks = extractCompanyLinks();

        //estraggo informazione da tutti i link presenti nel set "companyLinks" (2)
        List<TennisPlayer> playersList = extractInformation(playersLinks);

        //mappo tutta la lista della companies su un json da mandare in output sulla cartella target (3)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("target/players.json"), playersList);
        //driver.quit();


    }

    public Set<String> extractCompanyLinks() throws InterruptedException {

        Set<String> playersLinks = new HashSet<>();

        for (int i = 0; i < 100; i = i + 100) {
            ChromeDriver driver = new ChromeDriver();
            StringBuilder stringBuilder = new StringBuilder();
            String newUrl = stringBuilder.append(URL).append(i).append("-").append(i+100).toString();
            driver.get(newUrl);
            Thread.sleep(3000);
            List<WebElement> refList = driver.findElementsByXPath("//span[@class='player-cell-wrapper']//a[1]");
            for (WebElement element : refList) {
                System.out.println(element.getAttribute("href"));
                playersLinks.add(element.getAttribute("href"));
            }
            driver.quit();
        }

        return playersLinks;

    }

    public List<TennisPlayer> extractInformation(Set<String> playerLinks) throws InterruptedException {

        List<TennisPlayer> playersList = new ArrayList<>();

        for (String link : playerLinks) {
            ChromeDriver driver = new ChromeDriver();
            driver.get(link);
            Thread.sleep(3000);
            System.out.println(link + "\n\n");

            String name = null;
            String surname = null;
            String careerHighRanking = null;
            Integer age = null;
            String birthplace = null;
            String residence = null;
            String staff = null;
            String styleOfPlay = null;
            Integer turnedPro = null;
            Integer weight = null;
            Integer height = null;
            Integer titlesCount = null;
            Integer wonGamesCount = null;
            Integer lostGamesCount = null;
            Double prizeMoney = null;



            try {
                name = driver.findElementByXPath("//div[@class='first-name']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                surname = driver.findElementByXPath("//div[@class='last-name']").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                careerHighRanking = driver.findElementByXPath("//td[@colspan=2]//div[@data-singles]").getText();
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[1]//div[@class='table-big-value'][1]").getText().equals("")){
                    age = null;
                }else {
                    String ageString = driver.findElementByXPath("(//div[@class='wrap'])[1]//div[@class='table-big-value'][1]").getText();
                    ageString = StringUtils.substringBefore(ageString, "(");
                    ageString = ageString.replaceAll("\\D+", "");
                    age = Integer.parseInt(ageString);
                }
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[5]//div[@class='table-value']").getText().equals("")){
                    birthplace = null;
                }else {
                    birthplace = driver.findElementByXPath("(//div[@class='wrap'])[5]//div[@class='table-value']").getText();
                }
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[6]//div[@class='table-value']").getText().equals("")){
                    residence = null;
                }else {
                    residence = driver.findElementByXPath("(//div[@class='wrap'])[6]//div[@class='table-value']").getText();
                }
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[8]//div[@class='table-value']").getText().equals("")){
                    staff = null;
                }else {
                    staff = driver.findElementByXPath("(//div[@class='wrap'])[8]//div[@class='table-value']").getText();
                }
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[7]//div[@class='table-value']").getText().equals("")){
                    styleOfPlay = null;
                }else {
                    styleOfPlay = driver.findElementByXPath("(//div[@class='wrap'])[7]//div[@class='table-value']").getText();
                }
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[2]//div[@class='table-big-value']").getText().equals("")){
                    turnedPro = null;
                }else{
                    turnedPro = Integer.parseInt(driver.findElementByXPath("(//div[@class='wrap'])[2]//div[@class='table-big-value']").getText());
                };
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[3]//div[@class='table-big-value']//span[@class='table-weight-kg-wrapper']").getText().equals("")){
                    weight = null;
                }else {
                    String weightString = driver.findElementByXPath("(//div[@class='wrap'])[3]//div[@class='table-big-value']//span[@class='table-weight-kg-wrapper']").getText();
                    weightString = weightString.replaceAll("\\D+", "");
                    weight = Integer.parseInt(weightString);
                }
            } catch (NoSuchElementException e) {

            }
            try {
                if(driver.findElementByXPath("(//div[@class='wrap'])[4]//div[@class='table-big-value']//span[@class='table-height-cm-wrapper']").getText().equals("")){
                    height = null;
                }else {
                    String heightString = driver.findElementByXPath("(//div[@class='wrap'])[4]//div[@class='table-big-value']//span[@class='table-height-cm-wrapper']").getText();
                    heightString = heightString.replaceAll("\\D+", "");
                    height = Integer.parseInt(heightString);
                }
            } catch (NoSuchElementException e) {

            }
            try {
                titlesCount = Integer.parseInt(driver.findElementByXPath("(//td[@colspan=1])[7]//div").getText());
            } catch (NoSuchElementException e) {

            }
            try {
                String wonGames = driver.findElementByXPath("(//td[@colspan=1])[6]//div[1]").getText();
                wonGames = StringUtils.substringBefore(wonGames, "-");
                wonGamesCount = Integer.parseInt(wonGames);
            } catch (NoSuchElementException e) {

            }
            try {
                String lostGames = driver.findElementByXPath("(//td[@colspan=1])[6]//div[1]").getText();
                lostGames = StringUtils.substringAfter(lostGames, "-");
                lostGamesCount = Integer.parseInt(lostGames);
            } catch (NoSuchElementException e) {

            }
            try {
                String prize = driver.findElementByXPath("(//td[@colspan=1])[8]//div").getAttribute("data-singles");
                prize = prize.replaceAll("\\D+","");
                prizeMoney = Double.parseDouble(prize);
            } catch (NoSuchElementException e) {

            }

            TennisPlayer p = new TennisPlayer();
            p.setId(UUID.randomUUID().toString());
            p.setName(name);
            p.setSurname(surname);
            p.setAge(age);
            p.setBirthplace(birthplace);
            p.setResidence(residence);
            p.setHeight(height);
            p.setWeight(weight);
            p.setCareerHighRanking(careerHighRanking);
            p.setLostGamesCount(lostGamesCount);
            p.setStaff(staff);
            p.setStyleOfPlay(styleOfPlay);
            p.setWonGamesCount(wonGamesCount);
            p.setTitlesCount(titlesCount);
            p.setPrizeMoney(prizeMoney);
            p.setTurnedPro(turnedPro);

            System.out.println(p.getName());
            System.out.println(p.getSurname());
            System.out.println(p.getAge());
            System.out.println(p.getBirthplace());
            System.out.println(p.getResidence());
            System.out.println(p.getHeight());
            System.out.println(p.getWeight());
            System.out.println(p.getCareerHighRanking());
            System.out.println(p.getLostGamesCount());
            System.out.println(p.getStaff());
            System.out.println(p.getStyleOfPlay());
            System.out.println(p.getWonGamesCount());
            System.out.println(p.getTitlesCount());
            System.out.println(p.getPrizeMoney());
            System.out.println(p.getTurnedPro() + "\n\n");
            playersList.add(p);

            driver.quit();

        }

        return playersList;

    }

}
