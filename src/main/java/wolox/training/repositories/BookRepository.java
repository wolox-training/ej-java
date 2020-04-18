package wolox.training.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findByAuthor(String author);
    List<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year);
}
