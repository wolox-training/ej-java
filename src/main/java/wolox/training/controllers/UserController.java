package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Iterable findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());
        return userRepository.save(user);
    }

    @PostMapping("/{user_id}/books/{book_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User addBookToUser(@PathVariable Long user_id, @PathVariable Long book_id) {
        User user = userRepository.findById(user_id)
            .orElseThrow(() -> new UserNotFoundException());

        Book book = bookRepository.findById(book_id)
            .orElseThrow(() -> new BookNotFoundException());

        user.addBook(book);
        return userRepository.save(user);
    }

    @DeleteMapping("/{user_id}/books/{book_id}")
    public User deleteBookToUser(@PathVariable Long user_id, @PathVariable Long book_id) {
        User user = userRepository.findById(user_id)
            .orElseThrow(() -> new UserNotFoundException());

        Book book = bookRepository.findById(book_id)
            .orElseThrow(() -> new BookNotFoundException());

        user.deleteBook(book);
        return userRepository.save(user);
    }
}