package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CitizenDashboard extends JFrame {
    private final Connection conn;
    private final int citizenId;
    private final String citizenName;

    public CitizenDashboard(Connection conn, int id, String name) {
        this.conn = conn;
        this.citizenId = id;
        this.citizenName = name;

        setTitle("Citizen Dashboard - " + name);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
//===================================================
        // Header with logout
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        JLabel header = new JLabel("Welcome, " + citizenName, JLabel.CENTER);
//        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
//        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//
//        JButton logoutBtn = new JButton("Logout");
//        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        logoutBtn.setBackground(new Color(200, 50, 50));
//        logoutBtn.setForeground(Color.WHITE);
//        logoutBtn.setFocusPainted(false);
//        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
//
//        logoutBtn.addActionListener(e -> {
//            dispose();
//            new WelcomeFrame(conn);
//        });
//===============================
//        headerPanel.add(header, BorderLayout.CENTER);
//        headerPanel.add(logoutBtn, BorderLayout.EAST);
//        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("View Projects", viewProjectsPanel());
        tabs.addTab("Report Complaint", complaintPanel());

        add(tabs);
        setVisible(true);
    }

    //  VIEW PROJECTS
    private JPanel viewProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Project ID", "Project Name", "Status", "Contractor ID"}, 0
        );
        JTable table = new JTable(model);
        JButton refreshBtn = new JButton("Refresh");

        refreshBtn.addActionListener(e -> {
            try {
                model.setRowCount(0);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(
                        "SELECT project_id, project_name, status, contractor_id FROM projects"
                );
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("project_id"),
                            rs.getString("project_name"),
                            rs.getString("status"),
                            rs.getInt("contractor_id")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading projects: " + ex.getMessage());
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        return panel;
    }

    // - REPORT COMPLAINT
    private JPanel complaintPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // ===== HEADER =====
        JLabel heading = new JLabel("Submit a Complaint", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(new Color(0, 70, 140));
        panel.add(heading, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel projectIdLabel = new JLabel("Enter Project ID:");
        JTextField projectIdField = new JTextField(15);
        JLabel complaintLabel = new JLabel("Enter Complaint:");
        JTextArea complaintArea = new JTextArea(6, 25);
        complaintArea.setLineWrap(true);
        complaintArea.setWrapStyleWord(true);
        JScrollPane complaintScroll = new JScrollPane(complaintArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Row 1 – Project ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(projectIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(projectIdField, gbc);

        // Row 2 – Complaint
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(complaintLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(complaintScroll, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // ===== BUTTON SECTION =====
        JButton submitBtn = new JButton("Submit Complaint");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submitBtn.setFocusPainted(false);
        submitBtn.setBackground(new Color(0, 120, 215));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== ACTION HANDLER =====
        submitBtn.addActionListener(e -> {
            try {
                int projectId = Integer.parseInt(projectIdField.getText().trim());
                String complaintText = complaintArea.getText().trim();

                if (complaintText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complaint cannot be empty.");
                    return;
                }

                // Check if project exists
                PreparedStatement chk = conn.prepareStatement("SELECT 1 FROM projects WHERE project_id = ?");
                chk.setInt(1, projectId);
                ResultSet rs = chk.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "❌ Project not found.");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO complaints (project_id, citizen_id, complaint) VALUES (?, ?, ?)"
                );
                ps.setInt(1, projectId);
                ps.setInt(2, citizenId);
                ps.setString(3, complaintText);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Complaint submitted successfully!");
                projectIdField.setText("");
                complaintArea.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Project ID.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error submitting complaint: " + ex.getMessage());
            }
        });

        return panel;
    }

//    private JPanel complaintPanel() {
//        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
//        JTextField projectIdField = new JTextField();
//        JTextArea complaintArea = new JTextArea(5, 20);
//        JButton submitBtn = new JButton("Submit Complaint");
//
//        panel.add(new JLabel("Enter Project ID:"));
//        panel.add(projectIdField);
//        panel.add(new JLabel("Enter Complaint:"));
//        panel.add(new JScrollPane(complaintArea));
//        panel.add(submitBtn);
//
//        submitBtn.addActionListener(e -> {
//            try {
//                int projectId = Integer.parseInt(projectIdField.getText());
//                String complaintText = complaintArea.getText().trim();
//
//                if (complaintText.isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Complaint cannot be empty.");
//                    return;
//                }
//
//                // Check if project exists
//                PreparedStatement chk = conn.prepareStatement("SELECT 1 FROM projects WHERE project_id = ?");
//                chk.setInt(1, projectId);
//                ResultSet rs = chk.executeQuery();
//                if (!rs.next()) {
//                    JOptionPane.showMessageDialog(this, "❌ Project not found.");
//                    return;
//                }
//
//                PreparedStatement ps = conn.prepareStatement(
//                        "INSERT INTO complaints (project_id, citizen_id, complaint) VALUES (?, ?, ?)"
//                );
//                ps.setInt(1, projectId);
//                ps.setInt(2, citizenId);
//                ps.setString(3, complaintText);
//                ps.executeUpdate();
//                JOptionPane.showMessageDialog(this, "✅ Complaint submitted successfully!");
//
//                // Reset fields
//                projectIdField.setText("");
//                complaintArea.setText("");
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error submitting complaint: " + ex.getMessage());
//            }
//        });
//
//        return panel;
//    }
}
