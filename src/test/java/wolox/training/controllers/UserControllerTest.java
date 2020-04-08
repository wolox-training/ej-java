package wolox.training.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        User user = new User("bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        List<User> allUsers = Arrays.asList(user);

        given(userRepository.findAll()).willReturn(allUsers);

        mvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].userName", is(user.getUserName())));
    }

    @Test
    public void givenNoneUser_whenGetUsers_thenReturnEmptyJsonArray() throws Exception {
        List<User> allUsers = new ArrayList<User>();

        given(userRepository.findAll()).willReturn(allUsers);

        mvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenUser_whenGetAnUser_thenReturnJson() throws Exception {
        User user = new User("bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(get("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName", is(user.getUserName())));
    }

    @Test
    public void givenNoneUser_whenGetAnUser_thenReturnJsonError() throws Exception {
        mvc.perform(get("/api/users/5")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAValidUser_whenCreatesAnUser_thenReturnJson() throws Exception {
        User user = new User("bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        mvc.perform(post("/api/users")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void givenAValidUser_whenUpdatesAnUser_thenReturnOk() throws Exception {
        User user = new User(1,"bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        user.setName("Bob Parker III");

        mvc.perform(put("/api/users/1")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMismatchingIds_whenUpdatesAnUser_thenReturnBadRequest() throws Exception {
        User user = new User(1, "bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        user.setName("Bob Parker III");

        mvc.perform(put("/api/users/2")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenAValidId_whenDeletesAnUser_thenReturnOk() throws Exception {
        User user = new User("bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(delete("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidId_whenDeletesAnUser_thenReturnNotFound() throws Exception {
        mvc.perform(delete("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAnUser_whenAddingABook_thenReturnOk() throws Exception {
        User user = new User(1,"bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        Book book = new Book(1,"Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(post("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void givenAnUserAndInvalidBook_whenAddingABook_thenReturnNotFoundError() throws Exception {
        User user = new User(1,"bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(post("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenNoneUser_whenAddingABook_thenReturnNotFoundError() throws Exception {
        Book book = new Book(1,"Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(post("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenAnUser_whenDeletingABook_thenReturnOk() throws Exception {
        User user = new User(1,"bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        Book book = new Book(1,"Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(delete("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenAnUserAndInvalidBook_whenDeletingABook_thenReturnNotFoundError() throws Exception {
        User user = new User(1,"bparker", "Bob Parker", LocalDate.of(1990, 3, 2));

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mvc.perform(delete("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenNoneUser_whenDeletingABook_thenReturnNotFoundError() throws Exception {
        Book book = new Book(1,"Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        mvc.perform(delete("/api/users/1/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
