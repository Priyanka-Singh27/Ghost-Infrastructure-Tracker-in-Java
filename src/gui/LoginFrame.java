package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private final JTextField nameField;
    private final JPasswordField passField;
    private final Connection conn;

    public LoginFrame(Connection conn) {
        this.conn = conn;

        setTitle("Ghost Infrastructure Tracker - Login");
        setSize(400, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Form panel (center)
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons panel (south)
        JPanel buttonPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");
        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handling
        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> {
            dispose();
            new SignupFrame(conn);
        });

        setVisible(true);
    }


//    public LoginFrame(Connection conn) {
//        this.conn = conn;
//
//        setTitle("Ghost Infrastructure Tracker - Login");
//        setSize(500, 250);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new GridLayout(5, 2));
//
//        JLabel title = new JLabel("LOGIN", SwingConstants.CENTER);
//        add(title);
//
//        nameField = new JTextField();
//        passField = new JPasswordField();
//
//        add(new JLabel("Name:"));
//        add(nameField);
//        add(new JLabel("Password:"));
//        add(passField);
//        System.out.println();
//        JPanel buttons = new JPanel();
//        JButton loginBtn = new JButton("Login");
//        JButton signupBtn = new JButton("Sign Up");
//        buttons.add(loginBtn);
//        buttons.add(signupBtn);
//        add(buttons);
//
//        loginBtn.addActionListener(e -> login());
//        signupBtn.addActionListener(e -> {
//            dispose();
//            new SignupFrame(conn);
//        });
//
//        setVisible(true);
//    }

    private void login() {
        try {
            String name = nameField.getText();
            String password = new String(passField.getPassword());

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, role FROM stakeholders WHERE name=? AND password=?"
            );
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");

                JOptionPane.showMessageDialog(this, "Welcome " + role + "!");
                dispose();

                switch (role.toLowerCase()) {
                    case "authority" -> new AuthorityDashboard(conn, id, name);
                    case "contractor" -> new ContractorDashboard(conn, id, name);
                    case "citizen" -> new CitizenDashboard(conn, id, name);
                    default -> JOptionPane.showMessageDialog(this, "Unknown role!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
