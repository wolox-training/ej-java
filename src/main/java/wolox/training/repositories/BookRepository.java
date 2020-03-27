package wolox.training.repositories;
import wolox.training.models.Book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByAuthor(String author);
}

