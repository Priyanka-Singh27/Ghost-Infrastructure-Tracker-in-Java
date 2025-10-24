package gui;
import javax.swing.*;
import java.sql.*;

public class InfrastructureTrackerGUI {

    static Connection conn;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/infrastructure_tracker?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "Priyanka@7083"
            );
            System.out.println("Connected to MySQL");

            SwingUtilities.invokeLater(() -> new LoginFrame(conn));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed:\n" + e.getMessage());
        }
    }
}
