import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
/*
Library Management System
Tyler Pac
CEN 3024C
14320
10/6/2024

Main function is to read in file and add to library array, after that we go through options for different actions in a menu 
to print, add/remove, and check in/out books for a library
 */
public class Main {
    public static void main(String[] args) {
    	//ask user for input
    	Scanner userInput = new Scanner(System.in);
        // create instance for library
    	Library library = new Library();

        System.out.println("Enter the file name:");
        String filename = userInput.nextLine();

        // Load books from file into library and error handle
        List<String> bookLines = ReadFile(filename);
        for (String line : bookLines) {
            String[] details = line.split(",");
            if (details.length == 3) {
                try {
                	//get Barcode
                    int barcode = Integer.parseInt(details[0].trim());  
                    // Get Title
                    String title = details[1].trim();
                    // Get Author
                    String author = details[2].trim();
                    // Add the book to the library
                    library.addBook(new Book(title, author, barcode)); 
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid barcode format for line: " + line);
                    continue; 
                }
            } else {
                System.out.println("Error: Incorrect file format on line: " + line);
            }
        }
        //print books
        System.out.println("Printing all books:");
        library.printBooks();
        //menu
        int choice = -1;
        do {
            System.out.println("AddBook = 0");
            System.out.println("RemoveBookByBarcode = 1");
            System.out.println("RemoveBookByTitle = 2");
            System.out.println("PrintBooks = 3");
            System.out.println("CheckOutBook = 4");
            System.out.println("CheckInBook = 5");
            System.out.println("Quit = 6");
            System.out.println("------------");
            System.out.println("Enter your choice:");

            try {
                choice = userInput.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter a valid number.");
                userInput.next();  // Clear invalid input
                continue;
            }

            userInput.nextLine();  // Consume newline

            switch (choice) {
                case 0:
                	// enter info for books
                    System.out.println("Enter title:");
                    String title = userInput.nextLine();
                    System.out.println("Enter author:");
                    String author = userInput.nextLine();
                    System.out.println("Enter barcode:");
                    int barcode = userInput.nextInt();
                    //add to library array
                    library.addBook(new Book(title, author, barcode));
                    System.out.println("Book added successfully.");
                    //print books after adding
                    System.out.println("Printing all books:");
                    library.printBooks();
                    break;
                case 1:
                	//ask for barcode
                    System.out.println("Enter barcode of the book to remove:");
                    int removeBarcode = userInput.nextInt();
                    //remove book by barcode
                    library.removeBookByBarcode(removeBarcode);
                    System.out.println("Book with" + removeBarcode +" Barcode removed.");
                    //print books after removing a book
                    System.out.println("Printing all books:");
                    library.printBooks();
                    break;
                case 2:
                    System.out.println("Enter title of the book to remove:");
                    String removeTitle = userInput.nextLine();
                    //remove book by title
                    library.removeBookByTitle(removeTitle);
                    System.out.println("Book with" + removeTitle +" Title removed.");
                    //print books after removing a book
                    System.out.println("Printing all books:");
                    library.printBooks();
                    break;
                case 3:
                	//print all books
                    System.out.println("Printing all books:");
                    library.printBooks();
                    break;
                case 4:
                	//ask for book to check out
                    System.out.println("Enter title to check out:");
                    String checkoutTitle = userInput.nextLine();
                	//find book to check out
                    Book bookToCheckout = library.findBookByTitle(checkoutTitle);
                	//error handle
                    if (bookToCheckout != null && !bookToCheckout.isCheckedOut()) {
                    	//set status of book
                        bookToCheckout.checkOut();
                        System.out.println(checkoutTitle + "checked out.");
                    } else {
                        System.out.println(checkoutTitle + " not available to be checked out.");
                    }
                    //print all books to show updated status
                    System.out.println("Printing all books:");
                    library.printBooks();
                    break;
                case 5:
                	//ask for book to check in
                    System.out.println("Enter title to check in:");
                    String checkinTitle = userInput.nextLine();
                	//find book to check in
                    Book bookToCheckIn = library.findBookByTitle(checkinTitle);
                	//error handle
                    if (bookToCheckIn != null && bookToCheckIn.isCheckedOut()) {
                    	//set status of book
                        bookToCheckIn.checkIn();
                        System.out.println(checkinTitle + " checked in.");
                    } else {
                        System.out.println(checkinTitle + " has not been checkout out cant checkin.");
                    }
                    //print all books to show updated status
                    System.out.println("Printing all books:");
                    library.printBooks();
                    break;
                case 6:
                    System.out.println("Quitting the program.");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }

        } while (choice != 6);

        userInput.close();
    }

    // Method to read file content and return as a list of strings
    public static List<String> ReadFile(String filename) {
        List<String> readLines = new ArrayList<>();
        try {
            // Read lines from file and assign to list
            readLines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch (IOException x) {
            System.err.format("Read file error: %s%n", x);
        }
        return readLines;
    }
}
