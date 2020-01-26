package kz.oku.kerek.service;

import kz.oku.kerek.model.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class BookServiceImpl {

    public Book loadInfo(String url) throws IOException {
        Document bookPage = Jsoup.parse(new URL(url), 5000);
        Elements scriptElements = bookPage.getElementsByTag("script");
        return new Book();
    }

}
