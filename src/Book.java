/*
Library Management System
Tyler Pac
CEN 3024C
14320
10/6/2024

Book Object helps set attributed for books and has functions to return status for check in/out 
as well as text for the book such as barcode author title.
 */
import java.time.*;
public class Book {
    private String title;
    private String author;
    private int barcode;
    private boolean checkedOut;
    private LocalDate dueDate;

    // Constructor
    public Book(String title, String author, int barcode) {
        this.title = title;
        this.author = author;
        this.barcode = barcode;
        this.checkedOut = false;
        this.dueDate = null;
    }

    // Getters and setters
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getBarcode() {
        return barcode;
    }
    //returns check out status
    public boolean isCheckedOut() {
        return checkedOut;
    }
    //sets checkout status to true and sets due date to 4 weeks
    public void checkOut() {
        this.checkedOut = true;
        this.dueDate = LocalDate.now().plusWeeks(4);
    }

    //sets checkout status to false and reset due date
    public void checkIn() {
        this.checkedOut = false;
        this.dueDate = null;  
    }

    // to string method to print 
    @Override
    public String toString() {
        return barcode + ", " + title + ", " + author + ", " + checkedOut + ", " + dueDate;
    }
}
