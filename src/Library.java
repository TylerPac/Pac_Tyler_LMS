import java.util.*;

/*
Library Management System
Tyler Pac
CEN 3024C
14320
10/6/2024

	this is a place holder for the sql database for the library management system. We use an array in place of the sql database.
 */
public class Library {
	//array for books in database
    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }
    // add book to library array
    public void addBook(Book book) {
        books.add(book);
    }

    // remove book from library array by barcode
    public void removeBookByBarcode(int barcode) {
        books.removeIf(book -> book.getBarcode() == barcode);
    }

    // remove book from library array by title
    public void removeBookByTitle(String title) {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }
    
    //search for book by title
    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
    //print all books
    public void printBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
}
