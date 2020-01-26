package kz.oku.kerek.controller;

import kz.oku.kerek.model.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookRestControllerIntergrationTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testFindById_whenBookExists_thenReturnBook() {
        Long bookId = 24L;
        ResponseEntity<Book> entity = this.template.getForEntity("/book/"+bookId, Book.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, entity.getHeaders().getContentType());
        Book response = entity.getBody();
        assertEquals(bookId, response.getId());
    }
}