import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
/*
Library Management System
Tyler Pac
CEN 3024C
14320
9/8/2024
 */
public class Main {

    public static void main(String[] args) {
        //scan for input from user
        Scanner userInput = new Scanner(System.in);
        int choice = -1;

        do {
            // Menu
            System.out.println("AddBook = 0");
            System.out.println("RemoveBook = 1");
            System.out.println("PrintBooks = 2");
            System.out.println("Quit = 3");
            System.out.println("------------");
            System.out.println("Enter your choice:");

            // Validate the user input
            try {
                choice = userInput.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please enter a valid number (0, 1, 2, or 3).");
                // Clear the invalid input
                userInput.next();
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Please Enter Book Title");

                    userInput.nextLine(); // Consume the newline character after nextInt
                    //create strings for format framework
                    String title = "";
                    String author = "";
                    String authorAndtitle = "";

                    System.out.println("Enter the title:");
                    title = userInput.nextLine();
                    System.out.println("Please Enter Book Author");
                    author = userInput.nextLine();
                    //framework for formatting
                    authorAndtitle = title + "," + author;

                    // Read the file and sort the lines
                    List<String> linesList = ReadFile("src/sortedData.txt");
                    //check for empty authorAndTitle
                    if (!authorAndtitle.isEmpty()) {
                        linesList.add(authorAndtitle); // Add the title to the list before sorting
                    }
                    // Sort alphabetically
                    Collections.sort(linesList);

                    // Write the sorted lines to the output file
                    WriteFile(linesList, "src/sortedData.txt");
                    break;

                case 1:
                    System.out.println("What Book do you want to remove? (enter Book ID)");
                    int BookID = -1;

                    // Validate the user input for the book ID
                    try {
                        BookID = userInput.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input, please enter a valid number (integer).");
                        // Clear the invalid input
                        userInput.next();
                        continue;
                    }
                    //call remove line function
                    RemoveLineByNumber("src/sortedData.txt", BookID);
                    // read file to list
                    List<String> resortlines = ReadFile("src/sortedData.txt");
                    // sort list
                    Collections.sort(resortlines);

                    // Write the sorted lines to the output file
                    WriteFile(resortlines, "src/sortedData.txt");
                    break;

                case 2:
                    System.out.println("Displaying all Books");
                    // call printfile
                    PrintFile("src/sortedData.txt");
                    break;

                case 3:
                    System.out.println("Quitting the program.");
                    break;

                default:
                    //incase of bad input
                    System.out.println("Invalid choice, please enter 0, 1, 2, or 3.");
                    break;
            }

        } while (choice != 3); // Loop until choice is 3

        //close input
        userInput.close();
    }

    public static List<String> ReadFile(String filename) {

        List<String> readLines = new ArrayList<>();
        try {
            //read lines from file assign to list readLines
            readLines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());

            // Remove any leading numbers and commas from the lines
            for (int i = 0; i < readLines.size(); i++) {
                String line = readLines.get(i).trim();
                line = line.replaceFirst("^\\d+,", ""); // This regex removes leading numbers followed by a comma
                readLines.set(i, line);
            }

        } catch (IOException x) {
            System.err.format("Read file error: %s%n", x);
        }

        return readLines;
    }

    public static void PrintFile(String filename) {
        List<String> readLines = new ArrayList<>();
        try {
            //read lines from file assign to readLines list
            readLines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());

            // Print the lines with numbering
            for (String line : readLines) {
                System.out.println(line.trim());
            }

        } catch (IOException x) {
            System.err.format("Read file error: %s%n", x);
        }
    }

    public static void WriteFile(List<String> linesList, String filename) {
        List<String> numberedLines = new ArrayList<>();
        //initialize ID/LineNumber
        int lineNumber = 1;
        for (String line : linesList) {
            // formating for ID and adding to list
            numberedLines.add(lineNumber + "," + line);
            lineNumber++;
        }
        try {
            //writing to file
            Files.write(Paths.get(filename), numberedLines, Charset.defaultCharset());
        } catch (IOException x) {
            System.err.format("Write file error: %s%n", x);
        }
    }

    public static void RemoveLineByNumber(String filename, int lineNumber) {
        List<String> readLines = new ArrayList<>();
        try {
            // Read all lines from the file
            readLines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());

            // Check if the line number is valid
            if (lineNumber > 0 && lineNumber <= readLines.size()) {
                // Remove the line at the given index (lineNumber - 1)
                readLines.remove(lineNumber - 1);

                // Write the updated lines back to the file
                Files.write(Paths.get(filename), readLines, Charset.defaultCharset());

                System.out.println("Line " + lineNumber + " removed successfully.");
            } else {
                System.out.println("Invalid line number.");
            }

        } catch (IOException x) {
            System.err.format("File operation error: %s%n", x);
        }
    }
}
