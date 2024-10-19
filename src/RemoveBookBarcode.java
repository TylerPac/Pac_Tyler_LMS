import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class RemoveBookBarcode {

    @Test
    void test() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String expectedBarcode = "987654321";

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the LMS database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LMS", "root", "Kora4386!");

            // Now, remove the book using its barcode
            String deleteSql = "DELETE FROM Books WHERE Barcode = ?";
            preparedStatement = connection.prepareStatement(deleteSql);
            preparedStatement.setString(1, expectedBarcode);

            // Execute the delete query
            int rowsDeleted = preparedStatement.executeUpdate();
            assertEquals(1, rowsDeleted, "Book deletion failed!");

            // Verify that the book has been removed by querying the database again
            String selectQuery = "SELECT * FROM Books WHERE Barcode = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, expectedBarcode);
            resultSet = preparedStatement.executeQuery();

            // Assert that the book is no longer in the database
            assertFalse(resultSet.next(), "Book was not successfully removed!");

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
