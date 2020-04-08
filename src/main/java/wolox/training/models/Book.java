package wolox.training.models;
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
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
    private Integer pages;

    @ManyToMany(mappedBy = "books")
    private List<User> users = new ArrayList<User>();;

    public Book(){}

    public Book(String bookTitle, String bookAuthor, String bookGenre, String bookImage, String bookSubtitle, String bookYear, String bookPublisher, String bookIsbn, Integer bookPages) {
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

    public Book(long id, String bookTitle, String bookAuthor, String bookGenre, String bookImage, String bookSubtitle, String bookYear, String bookPublisher, String bookIsbn, Integer bookPages) {
        this.id = id;
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
        Preconditions.checkArgument(title != null && !title.isEmpty());
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        Preconditions.checkArgument(author != null && !author.isEmpty());
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre != null) {
            Preconditions.checkArgument(!genre.isEmpty());
        }
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkArgument(image != null && !image.isEmpty());
        this.image = image;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkArgument(subtitle != null && !subtitle.isEmpty());
        this.subtitle = subtitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        Preconditions.checkArgument(year != null && !year.isEmpty());
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkArgument(publisher != null && !publisher.isEmpty());
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkArgument(isbn != null && !isbn.isEmpty());
        this.isbn = isbn;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        Preconditions.checkArgument(pages != null && pages > 0);
        this.pages = pages;
    }
}
