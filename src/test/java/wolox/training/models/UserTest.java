package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private User user;

    @Before
    public void setUp() {
        user = new User("bparker", "Bob Parker", LocalDate.of(1990, 3, 2));
    }

    @Test
    public void whenFindByUserName_thenReturnUser() {
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByUserName(user.getUserName());

        assertThat(found.get().getUserName()).isEqualTo(user.getUserName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateUserWithoutUserName_thenThrowException() {
        user.setUserName(null);
        userRepository.save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateUserWithoutName_thenThrowException() {
        user.setName(null);
        userRepository.save(user);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithoutBirthDate_thenThrowException() {
        user.setBirthDate(null);
        userRepository.save(user);
    }
}
