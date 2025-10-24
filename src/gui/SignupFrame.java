package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignupFrame extends JFrame {
    private final Connection conn;
    private final JTextField nameField;
    private final JTextField contactField;
    private final JComboBox<String> roleBox;
    private final JPasswordField passField;

    public SignupFrame(Connection conn) {
        this.conn = conn;

        setTitle("Ghost Infrastructure Tracker - Sign Up");
        setSize(420, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // ===== HEADER =====
        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0, 70, 140));
        add(title, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(15);
        contactField = new JTextField(15);
        roleBox = new JComboBox<>(new String[]{"Authority", "Contractor", "Citizen"});
        passField = new JPasswordField(15);

        JLabel nameLabel = new JLabel("Name:");
        JLabel contactLabel = new JLabel("Contact:");
        JLabel roleLabel = new JLabel("Role:");
        JLabel passLabel = new JLabel("Password:");

        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Row 1 – Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Row 2 – Contact
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(contactLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        // Row 3 – Role
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(roleBox, gbc);

        // Row 4 – Password
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JButton signupBtn = new JButton("Register");
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signupBtn.setBackground(new Color(0, 120, 215));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFocusPainted(false);
        signupBtn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(signupBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTION =====
        signupBtn.addActionListener(e -> signUp());

        setVisible(true);
    }

    private void signUp() {
        try {
            String name = nameField.getText().trim();
            String contactText = contactField.getText().trim();
            String role = (String) roleBox.getSelectedItem();
            String password = new String(passField.getPassword()).trim();

            if (name.isEmpty() || contactText.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields.");
                return;
            }

            if (!contactText.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "⚠️ Enter a valid 10-digit contact number.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO stakeholders (name, contact, role, password) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, name);
                ps.setLong(2, Long.parseLong(contactText));
                ps.setString(3, role);
                ps.setString(4, password);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Signup successful! Please login.");
                dispose();
                new LoginFrame(conn);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

//    public SignupFrame(Connection conn) {
//        this.conn = conn;
//        setTitle("Sign Up");
//        setSize(400, 300);
//        setLayout(new GridLayout(6, 2, 5, 5));
//        setLocationRelativeTo(null);
//
//        nameField = new JTextField();
//        contactField = new JTextField();
//        roleField = new JTextField();
//        passField = new JTextField();
//
//        add(new JLabel("Name:"));
//        add(nameField);
//        add(new JLabel("Contact:"));
//        add(contactField);
//        add(new JLabel("Role (Authority/Contractor/Citizen):"));
//        add(roleField);
//        add(new JLabel("Password:"));
//        add(passField);
//
//        JButton signupBtn = new JButton("Register");
//        add(new JLabel(""));
//        add(signupBtn);
//
//        signupBtn.addActionListener(e -> signUp());
//        setVisible(true);
//    }
//
//    private void signUp() {
//        try (PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO stakeholders (name, contact, role, password) VALUES (?, ?, ?, ?)")) {
//            ps.setString(1, nameField.getText());
//            ps.setLong(2, Long.parseLong(contactField.getText()));
//            ps.setString(3, roleField.getText());
//            ps.setString(4, passField.getText());
//            ps.executeUpdate();
//
//            JOptionPane.showMessageDialog(this, "Signup successful! Please login.");
//            dispose();
//            new LoginFrame(conn);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//        }
//    }
}
