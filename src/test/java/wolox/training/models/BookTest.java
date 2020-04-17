package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private Book book;

    @Before
    public void setUp() {
        book = new Book("Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);
    }

    @Test
    public void whenFindByAuthor_thenReturnBook() {
        entityManager.persist(book);
        entityManager.flush();

        Optional<Book> found = bookRepository.findByAuthor(book.getAuthor());

        assertThat(found.get().getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutTitle_thenThrowException() {
        book.setTitle(null);
        bookRepository.save(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutAuthor_thenThrowException() {
        book.setAuthor(null);
        bookRepository.save(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutImage_thenThrowException() {
        book.setImage(null);
        bookRepository.save(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutSubtitle_thenThrowException() {
        book.setSubtitle(null);
        bookRepository.save(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutPublisher_thenThrowException() {
        book.setPublisher(null);
        bookRepository.save(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutYear_thenThrowException() {
        book.setYear(null);
        bookRepository.save(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutIsbn_thenThrowException() {
        book.setIsbn(null);
        bookRepository.save(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutPages_thenThrowException() {
        book.setPages(null);
        bookRepository.save(book);
    }
}
