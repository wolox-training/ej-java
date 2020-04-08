package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;


@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository repository;
    private Book book;
    private Book bookWithId;


    @Before
    public void setUp() {
        book = new Book("Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);

        bookWithId = new Book(1, "Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);
    }

    @Test
    public void givenBooks_whenGetBooks_thenReturnJsonArray() throws Exception {
        List<Book> allBooks = Arrays.asList(book);

        given(repository.findAll()).willReturn(allBooks);

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].title", is(book.getTitle())));
    }

    @Test
    public void givenNoneBook_whenGetBooks_thenReturnEmptyJsonArray() throws Exception {
        List<Book> allBooks = new ArrayList<Book>();

        given(repository.findAll()).willReturn(allBooks);

        mvc.perform(get("/api/books")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenBook_whenGetABook_thenReturnJson() throws Exception {
        given(repository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(get("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    public void givenNoneBook_whenGetABook_thenReturnJsonError() throws Exception {
        mvc.perform(get("/api/books/5")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAValidBook_whenCreatesABook_thenReturnJson() throws Exception {
        mvc.perform(post("/api/books")
            .content(objectMapper.writeValueAsString(book))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void givenAValidBook_whenUpdatesABook_thenReturnOk() throws Exception {
        given(repository.findById(1L)).willReturn(Optional.of(bookWithId));

        bookWithId.setImage("image2.png");

        mvc.perform(put("/api/books/1")
            .content(objectMapper.writeValueAsString(bookWithId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMismatchingIds_whenUpdatesABook_thenReturnBadRequest() throws Exception {
        given(repository.findById(1L)).willReturn(Optional.of(bookWithId));

        bookWithId.setImage("image2.png");

        mvc.perform(put("/api/books/2")
            .content(objectMapper.writeValueAsString(bookWithId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenAValidId_whenDeletesABook_thenReturnOk() throws Exception {
        given(repository.findById(1L)).willReturn(Optional.of(bookWithId));

        mvc.perform(delete("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidId_whenDeletesABook_thenReturnNotFound() throws Exception {
        mvc.perform(delete("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
