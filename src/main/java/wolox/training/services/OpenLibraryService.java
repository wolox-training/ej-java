package wolox.training.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.dto.OpenLibraryBook;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Service
public class OpenLibraryService {
    @Autowired
    private BookRepository bookRepository;

    private ObjectMapper objectMapper;

    public OpenLibraryBook bookInfo(String isbn) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://openlibrary.org/api/books";
        String url = baseUrl + String.format("?bibkeys=ISBN:%s&format=json&jscmd=data", isbn);

        ResponseEntity<String> response = restTemplate.getForEntity(
            url,
            String.class
        );

        String result = response.getBody();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);

        if (jsonNode.size() == 0) {
            throw new BookNotFoundException();

        }

        return buildDTO(jsonNode.path("ISBN:" + isbn), isbn);
    }

    public Book saveBook(OpenLibraryBook openLibraryBook) {
        Book book =  new Book(openLibraryBook.getTitle(), openLibraryBook.getAuthors(), "No genre",
            openLibraryBook.getImage(), openLibraryBook.getSubtitle(), openLibraryBook.getPublishDate(),
            openLibraryBook.getPublishers(), openLibraryBook.getIsbn(), openLibraryBook.getNumberOfPages());

        bookRepository.save(book);

        return book;
    }

    public OpenLibraryBook buildDTO(JsonNode jsonNode, String isbn) {
        OpenLibraryBook book = new OpenLibraryBook();
        book.setTitle(jsonNode.path("title").asText());
        book.setSubtitle("No subtitle");
        book.setAuthors(jsonNode.path("authors").findPath("name").asText());
        book.setIsbn(isbn);
        book.setNumberOfPages(Integer.parseInt(jsonNode.path("number_of_pages").asText()));
        book.setPublishers(jsonNode.path("publishers").findPath("name").asText());
        book.setPublishDate(jsonNode.path("publish_date").asText());
        book.setImage(jsonNode.path("cover").path("medium").asText());

        return book;
    }
}
