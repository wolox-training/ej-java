package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Book book = new Book("Mi isla", "Elisabet Benavent", "romance",
            "image.png", "De la autora de En los Zapatos de Valeria", "2018",
            "Suma de Letras", "9789877390957", 536);
        entityManager.persist(book);
        entityManager.flush();

        // when
        Optional<Book> found = bookRepository.findByAuthor(book.getAuthor());

        // then
        assertThat(found.get().getAuthor())
            .isEqualTo(book.getAuthor());
    }
}
