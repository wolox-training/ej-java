package wolox.training.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByAuthor(String author);

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE (:publisher IS '' OR b.publisher = :publisher) AND"
        + " (:genre IS '' OR b.genre = :genre) AND (:year IS '' OR b.year = :year)")
    List<Book> findByPublisherAndGenreAndYear(
        @Param("publisher") String publisher,
        @Param("genre") String genre,
        @Param("year") String year);

    @Query("SELECT b FROM Book b WHERE (:id IS NULL OR b.id = :id) AND "
        + "(:title IS NULL OR b.title = :title) AND "
        + "(:author IS NULL OR b.author = :author) AND "
        + "(b.genre LIKE %:genre%) AND "
        + "(:image IS NULL OR b.image = :image) AND "
        + "(:subtitle IS NULL OR b.subtitle = :subtitle) AND "
        + "(:year IS NULL OR b.year = :year) AND "
        + "(:publisher IS NULL OR b.publisher = :publisher) AND "
        + "(:isbn IS NULL OR b.isbn = :isbn) AND "
        + "(:pages IS NULL OR b.pages = :pages)")
    Page<Book> findAllByFilter(
        @Param("id") Long id,
        @Param("title") String title,
        @Param("author") String author,
        @Param("genre") String genre,
        @Param("image") String image,
        @Param("subtitle") String subtitle,
        @Param("year") String year,
        @Param("publisher") String publisher,
        @Param("isbn") String isbn,
        @Param("pages") Integer pages,
        Pageable pageable);
}
