package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class WelcomeFrame extends JFrame {

    private final Connection conn;

    public WelcomeFrame(Connection conn) {
        this.conn = conn;

        setTitle("Ghost Infrastructure Tracker - Welcome");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(245, 248, 250));

        // ----------- HEADER SECTION -----------
        JLabel title = new JLabel("Ghost Infrastructure Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0, 70, 140));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ----------- OVERVIEW PANEL -----------
        JTextArea overview = new JTextArea();
        overview.setText("""
                A centralized system for transparent and efficient infrastructure project tracking.

                • Authorities can assign contractors, approve projects, and monitor progress.
                • Contractors can view assigned projects, request funds, and update milestones.
                • Citizens can view public projects and submit complaints or feedback.

                This platform promotes accountability and ensures better governance through 
                digital oversight of all ongoing infrastructure work.
                """);
        overview.setWrapStyleWord(true);
        overview.setLineWrap(true);
        overview.setEditable(false);
        overview.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        overview.setBackground(new Color(245, 248, 250));
        overview.setBorder(BorderFactory.createEmptyBorder(20, 60, 10, 60));

        add(overview, BorderLayout.CENTER);

        // ----------- BUTTON PANEL -----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setBackground(new Color(245, 248, 250));

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");
        JButton exitBtn = new JButton("Exit");

        styleButton(loginBtn, new Color(0, 120, 215));
        styleButton(signupBtn, new Color(0, 170, 80));
        styleButton(exitBtn, new Color(200, 50, 50));

        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);
        buttonPanel.add(exitBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ----------- ACTIONS -----------
        loginBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(conn); // existing login window
        });

        signupBtn.addActionListener(e -> {
            dispose();
            new SignupFrame(conn); // existing signup window
        });

        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
