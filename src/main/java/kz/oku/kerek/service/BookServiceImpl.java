package kz.oku.kerek.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import kz.oku.kerek.model.Book;
import kz.oku.kerek.util.HtmlParseUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final FileStorageService fileStorageService;

    public Book loadInfo(String url) throws IOException {
        Document bookPage = Jsoup.parse(new URL(url), 5000);
        Elements scriptElements = bookPage.getElementsByTag("script");
        return new Book();
    }

    public void downloadPages(Long bookId) throws IOException {
        String baseUrl = "http://kazneb.kz";
        String url = String.format("http://kazneb.kz/bookView/view/?brId=%d&simple=true&lang=kk", bookId);
        List<String> pagesPath = HtmlParseUtils.parseBookPagesPath(url);

        if (CollectionUtils.isEmpty(pagesPath)) {
            return;
        }

        //todo temporary limitation to meet AWS Free Tier requirements
        if (pagesPath.size() > 50) {
            pagesPath = pagesPath.subList(0, 10);
        }

        for (String pagePath : pagesPath) {

            String pageName = HtmlParseUtils.parsePageImageName(pagePath);
            String filename = String.format("%d/%s", bookId, pageName);

            if (fileStorageService.exists(filename)) {
                continue;
            }

            URL fileUrl = URI.create(baseUrl + pagePath).toURL();
            ReadableByteChannel readableByteChannel = Channels.newChannel(fileUrl.openStream());

            Path tempFile = Files.createTempFile("book-page-", ".png");
            FileChannel fileChannel = FileChannel.open(tempFile, StandardOpenOption.WRITE);
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            fileStorageService.putFile(tempFile, filename);
        }
    }

    public void generatePdf(Long bookId, Path pdfPath) throws FileNotFoundException {

        List<String> files = fileStorageService.list(bookId.toString());
        byte[] cover = fileStorageService.getFile(files.get(0));

        Image image = new Image(ImageDataFactory.create(cover));
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfPath.toFile()));
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument,
                new PageSize(image.getImageWidth(), image.getImageHeight()));

        for (int i = 0; i < files.size(); i++) {
            byte[] imageData = fileStorageService.getFile(files.get(i));
            image = new Image(ImageDataFactory.create(imageData));

            pdfDocument.addNewPage(new PageSize(image.getImageWidth(), image.getImageHeight()));
            image.setFixedPosition(i+1, 0,0 );
            document.add(image);
        }
        document.close();
    }

}
