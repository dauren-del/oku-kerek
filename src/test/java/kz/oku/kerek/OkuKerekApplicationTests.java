package kz.oku.kerek;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OkuKerekApplicationTests {

	@Test
	public void contextLoads() throws IOException {

		Document bookPage = Jsoup.parse(new URL("http://kazneb.kz/bookView/view/?brId=1518279&simple=true&lang=kk"), 5000);
		Elements elements = bookPage.select("script");

		for (Element element : elements) {
			StringTokenizer tokenizer = new StringTokenizer(element.data());
			while (tokenizer.hasMoreTokens()) {
				System.out.println(tokenizer.nextElement());
			}
		}

	}

}
