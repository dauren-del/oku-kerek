package kz.oku.kerek.listener;

import kz.oku.kerek.event.BookDownloadEvent;
import kz.oku.kerek.service.BookService;
import kz.oku.kerek.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class BookDownloadEventListener {

    private final BookService bookService;
    private final FileStorageService fileStorageService;

    @Async
    @EventListener(BookDownloadEvent.class)
    public void onBookDownloadEvent(BookDownloadEvent event) throws IOException {

        Long bookId = event.getBookId();
        Path tempFile = Files.createTempFile("book-", ".pdf");
        String filename = String.format("%d/%d.pdf", bookId, bookId);

        if (!fileStorageService.exists(filename)) {
            bookService.downloadPages(bookId);
            bookService.generatePdf(bookId, tempFile);
            fileStorageService.putFile(tempFile, filename);
        }
        URL accessLink = fileStorageService.generateAccessUrl(filename);
        bookService.setStatus(bookId, accessLink.toString());
        fileStorageService.deleteByExtensions(bookId.toString(), "png");
    }
}
