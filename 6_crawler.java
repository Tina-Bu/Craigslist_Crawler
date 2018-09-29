import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.net.*;

//import org.apache.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CraigslistCrawler {

    private static final String CRAIGSLIST_URL = "https://sfbay.craigslist.org/d/apts-housing-for-rent/search/apa";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final String authUser = "bittiger";
    private final String authPassword = "cs504";
    private List<String> proxyList;
    private List<String> titleList;
    private List<String> categoryList;
    private List<String> detailUrlList;
    private HashSet crawledUrl;
    BufferedWriter logBFWriter;

    public static void crawl(String requestUrl) throws IOException {
        HashMap<String,String> headers = new HashMap<String,String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.8");
        //Document doc = Jsoup.connect(requestUrl).userAgent(USER_AGENT).timeout(1000).get();
        Document doc = Jsoup.connect(requestUrl).headers(headers).userAgent(USER_AGENT).timeout(1000).get();

        // to crawl: title/rent price/detail url/hood/
        Element title = doc.getElementByAttribute("title");
        String pageTitle = titleEle.get(0).text();
        System.out.println("page title " + pageTitle);

        Element rentRow = doc.getElementByClass("result-row");
        for(int i = 0;i < rentRow.size();i++) {
            Float rentPrice = rentRow.get(i).attr("result-price");
            String detailRrl = rentRow.get(i).attr("href");
            String hood = rentRow.get(i).attr("result-hood");
            System.out.println("detail url from property = " + detailRrl);
        }

        Element rentPrice = doc.getElementByAttribute("rentPrice");
        String

    }
    public static void main(String[] args) {
        CraigslistCrawler.Crawl("https://sfbay.craigslist.org/d/apts-housing-for-rent/search/apa")

    }
    public static void Crawl(String url) {
        // starts crawling this url
        // TODO

    }
}
