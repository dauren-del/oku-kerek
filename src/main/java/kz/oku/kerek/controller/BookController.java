package kz.oku.kerek.controller;

import kz.oku.kerek.model.Book;
import kz.oku.kerek.service.BookService;
import kz.oku.kerek.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final FileStorageService fileStorageService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);

        return "greeting";
    }

    @GetMapping("/book/{bookId}")
    @ResponseBody
    public Book findById(@PathVariable("bookId") Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setName("Book with id " + bookId);
        return book;
    }

    @GetMapping("/book/download")
    public RedirectView downloadBook(@RequestParam("bookId") Long bookId) throws IOException {

        Path tempFile = Files.createTempFile("book-", ".pdf");
        String filename = String.format("%d/%d.pdf", bookId, bookId);

        bookService.downloadPages(bookId);
        if (!fileStorageService.exists(filename)) {
            bookService.generatePdf(bookId, tempFile);
            fileStorageService.putFile(tempFile, filename);
        }
        URL accessLink = fileStorageService.generateAccessUrl(filename);

        return new RedirectView(accessLink.toString());
    }

}
