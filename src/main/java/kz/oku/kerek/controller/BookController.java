package kz.oku.kerek.controller;

import kz.oku.kerek.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
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

}
