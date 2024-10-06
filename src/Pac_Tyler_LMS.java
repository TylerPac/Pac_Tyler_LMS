import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;

public class Pac_Tyler_LMS {

    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        int choice = -1;

        do {
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
                userInput.next(); // Clear the invalid input
                continue; // Go back to the start of the loop
            }

            switch (choice) {
                case 0:
                    System.out.println("Please Enter Book Title");

                    // Use the same userInput Scanner instead of creating a new one
                    userInput.nextLine(); // Consume the newline character after nextInt
                    String title = "";
                    String author = "";
                    String authorAndtitle = "";

                    System.out.println("Enter the title:");
                    title = userInput.nextLine();
                    System.out.println("Please Enter Book Author");
                    author = userInput.nextLine();
                    authorAndtitle = title + "," + author;

                    // Read the file and sort the lines
                    List<String> linesList = ReadFile("src/sortedData.txt");

                    if (!authorAndtitle.isEmpty()) {
                        linesList.add(authorAndtitle); // Add the title to the list before sorting
                    }

                    Collections.sort(linesList); // Sort alphabetically

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
                        userInput.next(); // Clear the invalid input
                        continue; // Go back to the start of the loop
                    }

                    RemoveLineByNumber("src/sortedData.txt", BookID);

                    List<String> resortlines = ReadFile("src/sortedData.txt");

                    Collections.sort(resortlines); // Sort alphabetically

                    // Write the sorted lines to the output file
                    WriteFile(resortlines, "src/sortedData.txt");
                    break;

                case 2:
                    System.out.println("Displaying all Books");
                    PrintFile("src/sortedData.txt");
                    break;

                case 3:
                    System.out.println("Quitting the program.");
                    break;

                default:
                    System.out.println("Invalid choice, please enter 0, 1, 2, or 3.");
                    break;
            }

        } while (choice != 3); // Loop until choice is 3

        System.out.println("END OF PROGRAM");

        userInput.close();
    }

    public static List<String> ReadFile(String filename) {
        List<String> readLines = new ArrayList<>();
        try {
            readLines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());

            // Remove any leading numbers and periods from the lines
            for (int i = 0; i < readLines.size(); i++) {
                String line = readLines.get(i).trim();
                line = line.replaceFirst("^\\d+\\.\\s*", ""); // This regex removes leading numbers followed by a dot and spaces
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
        int lineNumber = 1;
        for (String line : linesList) {
            numberedLines.add(lineNumber + ". " + line);
            lineNumber++;
        }
        try {
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
                // Remove the line at the given index (lineNumber - 1 because list is 0-based)
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
