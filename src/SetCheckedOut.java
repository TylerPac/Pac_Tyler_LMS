import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class SetCheckedOut {

    @Test
    void testSetCheckedOutAndDueDate() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String expectedBarcode = "123456789";  // Example barcode of the book
        boolean expectedCheckInStatus = false; // The expected status after checking out
        LocalDate expectedDueDate = LocalDate.now().plusWeeks(2); // 2 weeks from now

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the LMS database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LMS", "root", "Kora4386!");

            // Prepare the SQL query to update CheckIn_Status and DueDate
            String updateSql = "UPDATE Books SET CheckIn_Status = ?, DueDate = ? WHERE Barcode = ?";

            // Create a PreparedStatement for updating
            preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setBoolean(1, expectedCheckInStatus); // false = checked out
            preparedStatement.setDate(2, java.sql.Date.valueOf(expectedDueDate)); // Set the new due date
            preparedStatement.setString(3, expectedBarcode); // Set the book's barcode

            // Execute the update query
            int rowsUpdated = preparedStatement.executeUpdate();
            assertEquals(1, rowsUpdated, "Book checkout failed, no rows updated!");

            // Verify that the book has been updated with the correct status and due date
            String selectQuery = "SELECT CheckIn_Status, DueDate FROM Books WHERE Barcode = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, expectedBarcode);
            resultSet = preparedStatement.executeQuery();

            // Assert that the book exists and the values are correct
            assertTrue(resultSet.next(), "Book not found in the database after update!");
            assertEquals(expectedCheckInStatus, resultSet.getBoolean("CheckIn_Status"), "Check-in status does not match!");
            assertEquals(java.sql.Date.valueOf(expectedDueDate), resultSet.getDate("DueDate"), "Due date does not match!");

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
