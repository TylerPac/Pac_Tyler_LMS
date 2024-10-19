import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import org.junit.jupiter.api.Test;

class AddBookToDatabase {

    @Test
    void test() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the LMS database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LMS", "root", "Kora4386!");

            // Prepare the SQL insert statement for adding a book, including DueDate column
            String sql = "INSERT INTO Books (Author, Title, Barcode, CheckIn_Status, DueDate) VALUES (?, ?, ?, ?, ?)";

            // Create a PreparedStatement for the first book
            preparedStatement = connection.prepareStatement(sql);

            // Set the values for the first book
            String expectedAuthor1 = "J.K. Rowling";
            String expectedTitle1 = "Harry Potter and the Sorcerer's Stone";
            String expectedBarcode1 = "123456789";
            boolean expectedCheckInStatus1 = true;

            preparedStatement.setString(1, expectedAuthor1);
            preparedStatement.setString(2, expectedTitle1);
            preparedStatement.setString(3, expectedBarcode1);
            preparedStatement.setBoolean(4, expectedCheckInStatus1);
            preparedStatement.setNull(5, java.sql.Types.DATE);  // Set DueDate to null

            // Execute the insert query for the first book
            int rowsInserted1 = preparedStatement.executeUpdate();
            assertEquals(1, rowsInserted1, "First book insertion failed!");

            // Set the values for the second book (reuse the same PreparedStatement)
            String expectedAuthor2 = "J.K. Rowling";
            String expectedTitle2 = "Harry Potter and the Chamber of Secrets";
            String expectedBarcode2 = "987654321";
            boolean expectedCheckInStatus2 = true;

            preparedStatement.setString(1, expectedAuthor2);
            preparedStatement.setString(2, expectedTitle2);
            preparedStatement.setString(3, expectedBarcode2);
            preparedStatement.setBoolean(4, expectedCheckInStatus2);
            preparedStatement.setNull(5, java.sql.Types.DATE);  // Set DueDate to null

            // Execute the insert query for the second book
            int rowsInserted2 = preparedStatement.executeUpdate();
            assertEquals(1, rowsInserted2, "Second book insertion failed!");

            // Verify that both books were actually inserted by querying the database
            String selectQuery = "SELECT * FROM Books WHERE Barcode = ?";
            
            // Verify the first book
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, expectedBarcode1);
            resultSet = preparedStatement.executeQuery();

            // Assert that the first book was found in the database
            assertTrue(resultSet.next(), "First book was not found in the database!");
            assertEquals(expectedAuthor1, resultSet.getString("Author"), "First book's author does not match!");
            assertEquals(expectedTitle1, resultSet.getString("Title"), "First book's title does not match!");
            assertNull(resultSet.getDate("DueDate"), "First book's DueDate is not null as expected!");

            // Verify the second book
            preparedStatement.setString(1, expectedBarcode2);
            resultSet = preparedStatement.executeQuery();

            // Assert that the second book was found in the database
            assertTrue(resultSet.next(), "Second book was not found in the database!");
            assertEquals(expectedAuthor2, resultSet.getString("Author"), "Second book's author does not match!");
            assertEquals(expectedTitle2, resultSet.getString("Title"), "Second book's title does not match!");
            assertNull(resultSet.getDate("DueDate"), "Second book's DueDate is not null as expected!");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during the test: " + e.getMessage());
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
