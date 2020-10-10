package kz.oku.kerek.controller;

import kz.oku.kerek.event.BookDownloadEvent;
import kz.oku.kerek.model.Book;
import kz.oku.kerek.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ApplicationEventPublisher applicationEventPublisher;

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
    public RedirectView downloadBook(@RequestParam("bookId") Long bookId) {
        this.applicationEventPublisher.publishEvent(new BookDownloadEvent(this, bookId));
        return new RedirectView(String.format("/book/%d/status", bookId));
    }

    @GetMapping("/book/{bookId}/status")
    public String status(@PathVariable("bookId") Long bookId, Model model) {
        String status = bookService.getStatus(bookId);

        if (status.startsWith("http")) {
            return "redirect:" + status;
        }

        model.addAttribute("status", status);
        return "status";
    }

}
