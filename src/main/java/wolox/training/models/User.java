package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.common.base.Preconditions;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.services.PasswordEncoderService;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String userName;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private List<Book> books = new ArrayList<Book>();

    public User(){}

    public User(String userName, String name, LocalDate birthDate) {
        setUserName(userName);
        setName(name);
        setBirthDate(birthDate);
    }

    public User(long id, String userName, String name, LocalDate birthDate) {
        this.id = id;
        setUserName(userName);
        setName(name);
        setBirthDate(birthDate);
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        Preconditions.checkArgument(userName != null && !userName.isEmpty());
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkArgument(name != null && !name.isEmpty());
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        Preconditions.checkNotNull(birthDate);
        this.birthDate = birthDate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Preconditions.checkArgument(password != null && !password.isEmpty());

        this.password = PasswordEncoderService.encode(password);
    }

    public void addBook(Book book) {
        if (books.contains(book)) {
            throw new BookAlreadyOwnedException();
        }
        this.books.add(book);
    }

    public void deleteBook(Book book) {
        books.remove(book);
    }
}
