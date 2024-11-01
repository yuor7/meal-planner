import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private Connection connection;

    public Connection connectToDatabase() {
        try {
            // Replace with your database URL, username, and password
            String url = "jdbc:mysql://localhost:3306/meal_planner";
            String user = "root"; // Replace with your MySQL username
            String password = "375674"; // Replace with your MySQL password

            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
