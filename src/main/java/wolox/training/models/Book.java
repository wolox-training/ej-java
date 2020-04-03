package wolox.training.models;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    private String genre;

    @NotNull
    private String image;

    @NotNull
    private String subtitle;

    @NotNull
    private String year;

    @NotNull
    private String publisher;

    @NotNull
    private String isbn;

    @NotNull
    private int pages;

    @ManyToMany(mappedBy = "books")
    private List<User> users;

    public Book(){}

    public Book(String bookTitle, String bookAuthor, String bookGenre, String bookImage, String bookSubtitle, String bookYear, String bookPublisher, String bookIsbn, int bookPages) {
        setTitle(bookTitle);
        setAuthor(bookAuthor);
        setGenre(bookGenre);
        setImage(bookImage);
        setSubtitle(bookSubtitle);
        setYear(bookYear);
        setPublisher(bookPublisher);
        setIsbn(bookIsbn);
        setPages(bookPages);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Preconditions.checkNotNull(title);
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        Preconditions.checkNotNull(author);
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        Preconditions.checkNotNull(genre);
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkNotNull(image);
        this.image = image;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkNotNull(subtitle);
        this.subtitle = subtitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        Preconditions.checkNotNull(year);
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkNotNull(publisher);
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkNotNull(isbn);
        this.isbn = isbn;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
