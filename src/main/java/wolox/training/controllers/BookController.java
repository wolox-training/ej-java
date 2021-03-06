package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.dto.OpenLibraryBook;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;


@RestController
@Api
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    @ApiOperation(value = "Retrieves a list of books", response = Iterable.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public Iterable findAll(@RequestParam(required = false) Long id,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(defaultValue = "") String genre,
        @RequestParam(required = false) String image,
        @RequestParam(required = false) String subtitle,
        @RequestParam(required = false) String year,
        @RequestParam(required = false) String publisher,
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) Integer pages, Pageable pageable) {

        return bookRepository.findAllByFilter(id, title, author, genre, image, subtitle,
            year, publisher, isbn, pages, pageable);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieves a book", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved book"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Creates a book", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Book successfully created"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes a book")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted book"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates a book")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated book"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException());
        return bookRepository.save(book);
    }

    @GetMapping("/find_by_publisher_year_and_genre")
    public List<Book> customGetBook(@RequestParam(name="publisher", defaultValue = "") String publisher,
        @RequestParam(name="genre", defaultValue = "") String genre,
        @RequestParam(name="year", defaultValue = "") String year) {

        return bookRepository.findByPublisherAndGenreAndYear(publisher, genre, year);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Retrieves a book from an ISBN")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved book"),
        @ApiResponse(code = 200, message = "Successfully created book"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Book> search(@RequestParam(name="isbn", required = true) String isbn) throws IOException {
        Optional<Book> book = bookRepository.findByIsbn(isbn);

        if (book.isPresent()) {

            return ResponseEntity.status(HttpStatus.OK).body(book.get());
        } else {
            OpenLibraryBook openLibraryBook = openLibraryService.bookInfo(isbn);

            return ResponseEntity.status(HttpStatus.CREATED).body(openLibraryService.saveBook(openLibraryBook));
        }
    }
}
