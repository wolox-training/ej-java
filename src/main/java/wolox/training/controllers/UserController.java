package wolox.training.controllers;

import java.time.LocalDate;
import java.util.List;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public Iterable findAll(@RequestParam(required = false) Long id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String userName,
        @RequestParam(required = false) LocalDate birthDate, Pageable pageable) {

        return userRepository.findAllByFilter(id, name, userName, birthDate, pageable);
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
    public User updateUser(@RequestBody User editedUser, @PathVariable Long id) {
        if (editedUser.getId() != id) {
            throw new UserIdMismatchException();
        }

        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());

        return userService.updateUser(user, editedUser);
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

    @PutMapping("/{id}/password")
    public User updatePassword(@RequestHeader(value = "Password") String password,
        @PathVariable Long id) {

        User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        return userService.updatePassword(user, password);
    }

    @GetMapping("/find_by_birth_date_and_name")
    public List<User> getUserByBirthDateAndName(@RequestParam(name="startDate", defaultValue = "") String startDate,
        @RequestParam(name="endDate", defaultValue = "") String endDate,
        @RequestParam(name="name", defaultValue = "") String name) {

        LocalDate firstDate;
        LocalDate lastDate;

        if (startDate.isEmpty()) {
            firstDate = null;

        } else {
            firstDate = LocalDate.parse(startDate);

        }

        if (endDate.isEmpty()) {
            lastDate = null;

        } else {
            lastDate = LocalDate.parse(endDate);

        }

        return userRepository
            .findByBirthDateBetweenAndNameIgnoreCaseContaining(firstDate, lastDate, name);
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
}
