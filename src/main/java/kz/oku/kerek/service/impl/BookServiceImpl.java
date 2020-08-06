package kz.oku.kerek.service.impl;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import kz.oku.kerek.service.BookService;
import kz.oku.kerek.service.FileStorageService;
import kz.oku.kerek.util.HtmlParseUtils;
import lombok.RequiredArgsConstructor;
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
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final FileStorageService fileStorageService;
    private final ConcurrentHashMap<Long, String> bookStatuses = new ConcurrentHashMap<>();

    public void downloadPages(Long bookId) throws IOException {
        String baseUrl = "http://kazneb.kz";
        String url = format("http://kazneb.kz/bookView/view/?brId=%d&simple=true&lang=kk", bookId);

        setStatus(bookId, "Requesting information from kazneb.kz");
        List<String> pagesPath = HtmlParseUtils.parseBookPagesPath(url);

        if (CollectionUtils.isEmpty(pagesPath)) {
            setStatus(bookId, "No pages found for this book");
            return;
        }

        setStatus(bookId, "Starting download book pages");
        for (String pagePath : pagesPath) {

            String pageName = HtmlParseUtils.parsePageImageName(pagePath);
            String filename = format("%d/%s", bookId, pageName);

            if (fileStorageService.exists(filename)) {
                continue;
            }

            URL fileUrl = URI.create(baseUrl + pagePath).toURL();
            ReadableByteChannel readableByteChannel = Channels.newChannel(fileUrl.openStream());

            Path tempFile = Files.createTempFile("book-page-", ".png");
            FileChannel fileChannel = FileChannel.open(tempFile, StandardOpenOption.WRITE);
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            fileStorageService.putFile(tempFile, filename);
            setStatus(bookId, format("Downloading book pages (%d/%d)", pagesPath.indexOf(pagePath), pagesPath.size()));
        }
    }

    public void generatePdf(Long bookId, Path pdfPath) throws FileNotFoundException {
        setStatus(bookId, "Starting generate PDF document");

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
            setStatus(bookId, format("Generating PDF document (%d/%d)", i+1, files.size()));
        }
        document.close();
    }

    @Override
    public String getStatus(Long bookId) {
        return this.bookStatuses.getOrDefault(bookId, format("There is no status for book with ID %d", bookId));
    }

    @Override
    public synchronized void setStatus(Long bookId, String status) {
        this.bookStatuses.put(bookId, status);
    }

}
