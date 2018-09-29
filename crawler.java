
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

import java.io.Serializable;

public class CraigslistCrawler {
    private static final String CRAIGSLIST_URL = "https://sfbay.craigslist.org/d/apts-housing-for-rent/search/apa";
    
    // setting up proxy configuration
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private static String PROXY_FILE = "./proxylist_bittiger.csv";
    private static String LOG_FILE = "./log.txt";
    private final String authUser = "bittiger";
    private final String authPassword = "cs504";
    private static List<String> proxyList;

    BufferedWriter logBFWriter;
    private int index = 0;

    // use lists to hold crawled properties
    private List<String> titleList;
    private List<String> rentPriceList;
    private List<String> detailUrlList;
    private List<String> hoodList;

    public static void main(String[] args) {
        initProxyList(PROXY_FILE);
        setProxy();
        initLog(LOG_FILE);
        CraigslistCrawler.Crawl(CRAIGSLIST_URL);
        cleanup();
    }

    private static void Crawl(String url) {
        // Start crawling this url
        HashMap<String,String> headers = new HashMap<String,String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.8");
        Document doc = Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).timeout(100000).get();

        // initiate new array lists
        titleList = new ArrayList<String>();
        rentPriceList = new ArrayList<String>();
        detailUrlList = new ArrayList<String>();
        hoodList = new ArrayList<String>();

        Elements houses = doc.getElementsByClass("result-info");

        // loop through all crawled houses
        // If some fields don’t exist, use NULL instead.
        for (house : houses) {
            Elements link = house.select("p[class] > a[href]");
            // add detail URL
            detailUrlList.add(link.attr("href"));

            // add title
            titleList.add(link.text());

            // add rent_price

            // add hood
        }
        // write to log
        for (int i = 0; i < titleList.size(); i ++) {
            logBFWriter.write("=========================");
            logBFWriter.write(titleList.get(i) + "\n");
            logBFWriter.write(detailUrlList.get(i) + "\n");
            logBFWriter.write(rentPriceList.get(i) + "\n");
            logBFWriter.write(hoodList.get(i) + "\n");
        } 
        // print to screen
        for (int i = 0; i < titleList.size(); i ++) {
            System.out.println("=========================");
            System.out.println(titleList.get(i));
            System.out.println(detailUrlList.get(i));
            System.out.println(rentPriceList.get(i));
            System.out.println(hoodList.get(i));
        } 
    }


    public void cleanup() {
        if (logBFWriter != null) {
            try {
                logBFWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initProxyList(String proxy_file) {
        proxyList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(proxy_file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String ip = fields[0].trim();
                proxyList.add(ip);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                }
        );

        System.setProperty("http.proxyUser", authUser);
        System.setProperty("http.proxyPassword", authPassword);
        System.setProperty("socksProxyPort", "61336"); // set proxy port
    }


    private void initLog(String log_path) {
        try {
            File log = new File(log_path);
            // if file doesnt exists, then create it
            if (!log.exists()) {
                log.createNewFile();
            }
            FileWriter fw = new FileWriter(log.getAbsoluteFile());
            logBFWriter = new BufferedWriter(fw);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setProxy() {
        //rotate, round robbin
        if (index == proxyList.size()) {
            index = 0;
        }
        String proxy = proxyList.get(index);
        System.setProperty("socksProxyHost", proxy); // set proxy server
        index++;
    }

}
