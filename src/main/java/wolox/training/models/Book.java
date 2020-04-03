package wolox.training.models;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
@ApiModel(description = "Books from the library")
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

    @ApiModelProperty(notes = "Unique identification code for book")
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
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
