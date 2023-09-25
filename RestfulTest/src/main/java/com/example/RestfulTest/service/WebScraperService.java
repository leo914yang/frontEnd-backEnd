package com.example.RestfulTest.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class WebScraperService {
    @Autowired
    private DataDAO dataDAO;

    public Map<String, List<String>> scrapeWebsite(Map<String, String> jsonFile) {
        int number = Integer.parseInt(jsonFile.get("number"));
        String url = jsonFile.get("url");
        String name = jsonFile.get("name");
        Map<String, List<String>> scrapedMap = new HashMap<>();
        List<String> hrefList = new ArrayList<>();
        List<String> textList = new ArrayList<>();

        if (name.equals("C_CHAT")) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements titleElements = document.select("div.title");
                for (int i = 0; i < Math.min(number, titleElements.size()); i++) {
                    Element anchorElement = titleElements.get(i).selectFirst("a");
                    if (anchorElement != null) {
                        hrefList.add("https://www.ptt.cc/" + anchorElement.attr("href"));
                        textList.add(anchorElement.text());
                    }
                }
                scrapedMap.put("href", hrefList);
                scrapedMap.put("text", textList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (name.equals("News")) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements articleElements = document.select(".UwIKyb");
                for (int i = 0; i < Math.min(number, articleElements.size()); i++) {
                    Element hrefDivElement = articleElements.get(i).selectFirst(".XlKvRb");
                    Element aElement = hrefDivElement.selectFirst("a");
                    hrefList.add("https://news.google.com/" + aElement.attr("href"));

                    Element h4Element = articleElements.get(i).selectFirst("h4.gPFEn");
                    textList.add(h4Element.text());
                }
                scrapedMap.put("href", hrefList);
                scrapedMap.put("text", textList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("scrapedData: " + scrapedMap);
        return scrapedMap;
    }

    public String savePages(Map<String, List<String>> jsonFile){
        List<String> hrefList = jsonFile.get("href");
        List<String> textList = jsonFile.get("text");
        dataDAO.saveNews(hrefList, textList);
        return "Success";
    }
}
