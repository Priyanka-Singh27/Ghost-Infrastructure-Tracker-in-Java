package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractorDashboard extends JFrame {
    private final Connection conn;
    private final int contractorId;
    private final String contractorName;

    public ContractorDashboard(Connection conn, int id, String name) {
        this.conn = conn;
        this.contractorId = id;
        this.contractorName = name;

        setTitle("Contractor Dashboard - " + name);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
//========================================
        // Header with logout button
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        JLabel header = new JLabel("Welcome, " + contractorName, JLabel.CENTER);
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
//
//        headerPanel.add(header, BorderLayout.CENTER);
//        headerPanel.add(logoutBtn, BorderLayout.EAST);
//
//        add(headerPanel, BorderLayout.NORTH);
//======================================================
        JLabel header = new JLabel("Welcome, " + contractorName , JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab(" View Projects", viewProjectsPanel());
        tabs.addTab(" Request Funds", requestFundsPanel());
        tabs.addTab(" Complete Milestone", completeMilestonePanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    //  VIEW PROJECTS
    private JPanel viewProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new String[]{"Project ID", "Project Name", "Budget", "Status"}, 0);
        JTable table = new JTable(model);
        JButton refreshBtn = new JButton("Refresh");

        refreshBtn.addActionListener(e -> loadProjects(model));

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        // Load automatically on tab open
        loadProjects(model);

        return panel;
    }

    private void loadProjects(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT project_id, project_name, budget, status FROM projects WHERE contractor_id = ?"
            );
            ps.setInt(1, contractorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("project_id"),
                        rs.getString("project_name"),
                        rs.getDouble("budget"),
                        rs.getString("status")
                });
            }
        } catch (SQLException ex) {
            showError("Error loading projects", ex);
        }
    }

    //  REQUEST FUNDS
    private JPanel requestFundsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField projectIdField = new JTextField();
        JTextField amountField = new JTextField();
        JTextArea reasonArea = new JTextArea(3, 20);
        reasonArea.setLineWrap(true);
        JButton submitBtn = new JButton("Submit Request");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Project ID:"), gbc);
        gbc.gridx = 1;
        panel.add(projectIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Amount (₹):"), gbc);
        gbc.gridx = 1;
        panel.add(amountField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(submitBtn, gbc);

        submitBtn.addActionListener(e -> {
            try {
                int projectId = Integer.parseInt(projectIdField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());


                if (!isProjectAssigned(projectId)) {
                    JOptionPane.showMessageDialog(this, "❌ You are not assigned to this project.");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO funding_requests (project_id, contractor_id, amount, status) VALUES (?, ?, ?, 'Pending')"
                );
                ps.setInt(1, projectId);
                ps.setInt(2, contractorId);
                ps.setDouble(3, amount);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Funding request submitted successfully!");
                projectIdField.setText("");
                amountField.setText("");
                reasonArea.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
            } catch (SQLException ex) {
                showError("Error submitting funding request", ex);
            }
        });

        return panel;
    }

    //  COMPLETE MILESTONE
    private JPanel completeMilestonePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new String[]{"Milestone ID", "Description"}, 0);
        JTable table = new JTable(model);
        JTextField projectIdField = new JTextField();
        JButton loadBtn = new JButton(" Load Pending Milestones");
        JButton completeBtn = new JButton(" Mark as Completed");

        JPanel topPanel = new JPanel(new GridLayout(1, 3, 8, 8));
        topPanel.add(new JLabel("Project ID:"));
        topPanel.add(projectIdField);
        topPanel.add(loadBtn);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(completeBtn, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(projectIdField.getText().trim());

                if (!isProjectAssigned(pid)) {
                    JOptionPane.showMessageDialog(this, " You are not assigned to this project.");
                    return;
                }

                model.setRowCount(0);
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT milestone_id, description FROM milestones WHERE project_id = ? AND is_completed = FALSE"
                );
                ps.setInt(1, pid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("milestone_id"),
                            rs.getString("description"),
                    });
                }
            } catch (Exception ex) {
                showError("Error loading milestones", ex);
            }
        });

        completeBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a milestone to mark complete.");
                return;
            }

            int milestoneId = (int) table.getValueAt(selectedRow, 0);
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE milestones SET is_completed = TRUE WHERE milestone_id = ?"
                );
                ps.setInt(1, milestoneId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, " Milestone marked as completed!");
                model.removeRow(selectedRow);
            } catch (SQLException ex) {
                showError("Error updating milestone", ex);
            }
        });

        return panel;
    }

    private boolean isProjectAssigned(int projectId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT contractor_id FROM projects WHERE project_id = ?"
        );
        ps.setInt(1, projectId);
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getInt("contractor_id") == contractorId;
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(this, message + ": " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

//public class ContractorDashboard extends JFrame {
//    private final Connection conn;
//    private final int contractorId;
//    private final String contractorName;
//
//    public ContractorDashboard(Connection conn, int id, String name) {
//        this.conn = conn;
//        this.contractorId = id;
//        this.contractorName = name;
//
//        setTitle("Contractor Dashboard - " + name);
//        setSize(800, 600);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        JTabbedPane tabs = new JTabbedPane();
//        tabs.addTab("View Projects", viewProjectsPanel());
//        tabs.addTab("Request Funds", requestFundsPanel());
//        tabs.addTab("Complete Milestone", completeMilestonePanel());
//
//        add(tabs);
//        setVisible(true);
//    }
//
//    //  VIEW PROJECTS ----------------
//    private JPanel viewProjectsPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        DefaultTableModel model = new DefaultTableModel(new String[]{"Project ID", "Project Name", "Status"}, 0);
//        JTable table = new JTable(model);
//        JButton refreshBtn = new JButton("Refresh");
//
//        refreshBtn.addActionListener(e -> {
//            try {
//                model.setRowCount(0);
//                PreparedStatement ps = conn.prepareStatement(
//                        "SELECT project_id, project_name, status FROM projects WHERE contractor_id = ?"
//                );
//                ps.setInt(1, contractorId);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    model.addRow(new Object[]{
//                            rs.getInt("project_id"),
//                            rs.getString("project_name"),
//                            rs.getString("status")
//                    });
//                }
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error loading projects: " + ex.getMessage());
//            }
//        });
//
//        panel.add(new JScrollPane(table), BorderLayout.CENTER);
//        panel.add(refreshBtn, BorderLayout.SOUTH);
//        return panel;
//    }
//
//    // ---------------- 2️⃣ REQUEST FUNDS ----------------
//    private JPanel requestFundsPanel() {
//        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
//        JTextField projectIdField = new JTextField();
//        JTextField amountField = new JTextField();
//        JButton submitBtn = new JButton("Submit Request");
//
//        panel.add(new JLabel("Project ID:"));
//        panel.add(projectIdField);
//        panel.add(new JLabel("Amount:"));
//        panel.add(amountField);
//        panel.add(new JLabel(""));
//        panel.add(submitBtn);
//
//        submitBtn.addActionListener(e -> {
//            try {
//                int projectId = Integer.parseInt(projectIdField.getText());
//                double amount = Double.parseDouble(amountField.getText());
//
//                // Verify assignment
//                PreparedStatement check = conn.prepareStatement(
//                        "SELECT contractor_id FROM projects WHERE project_id = ?"
//                );
//                check.setInt(1, projectId);
//                ResultSet rs = check.executeQuery();
//                if (rs.next() && rs.getInt("contractor_id") == contractorId) {
//                    PreparedStatement ps = conn.prepareStatement(
//                            "INSERT INTO funding_requests (project_id, contractor_id, amount, status) VALUES (?, ?, ?, 'Pending')"
//                    );
//                    ps.setInt(1, projectId);
//                    ps.setInt(2, contractorId);
//                    ps.setDouble(3, amount);
//                    ps.executeUpdate();
//                    JOptionPane.showMessageDialog(this, "✅ Funding request submitted!");
//                } else {
//                    JOptionPane.showMessageDialog(this, "❌ You are not assigned to this project.");
//                }
//
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error submitting request: " + ex.getMessage());
//            }
//        });
//
//        return panel;
//    }
//
//    // ---------------- 3️⃣ COMPLETE MILESTONE ----------------
//    private JPanel completeMilestonePanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        DefaultTableModel model = new DefaultTableModel(new String[]{"Milestone ID", "Description"}, 0);
//        JTable table = new JTable(model);
//        JTextField projectIdField = new JTextField();
//        JButton loadBtn = new JButton("Load Pending Milestones");
//        JButton completeBtn = new JButton("Mark as Completed");
//
//        JPanel topPanel = new JPanel(new GridLayout(1, 3, 5, 5));
//        topPanel.add(new JLabel("Project ID:"));
//        topPanel.add(projectIdField);
//        topPanel.add(loadBtn);
//
//        panel.add(topPanel, BorderLayout.NORTH);
//        panel.add(new JScrollPane(table), BorderLayout.CENTER);
//        panel.add(completeBtn, BorderLayout.SOUTH);
//
//        loadBtn.addActionListener(e -> {
//            try {
//                int pid = Integer.parseInt(projectIdField.getText());
//
//                // check contractor assignment
//                PreparedStatement chk = conn.prepareStatement(
//                        "SELECT contractor_id FROM projects WHERE project_id=?"
//                );
//                chk.setInt(1, pid);
//                ResultSet rs = chk.executeQuery();
//                if (!rs.next() || rs.getInt("contractor_id") != contractorId) {
//                    JOptionPane.showMessageDialog(this, "❌ You are not assigned to this project.");
//                    return;
//                }
//
//                model.setRowCount(0);
//                PreparedStatement ps = conn.prepareStatement(
//                        "SELECT milestone_id, description FROM milestones WHERE project_id = ? AND is_completed = FALSE"
//                );
//                ps.setInt(1, pid);
//                ResultSet rs2 = ps.executeQuery();
//                while (rs2.next()) {
//                    model.addRow(new Object[]{
//                            rs2.getInt("milestone_id"),
//                            rs2.getString("description")
//                    });
//                }
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        });
//
//        completeBtn.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(this, "Select a milestone to mark complete.");
//                return;
//            }
//            int milestoneId = (int) table.getValueAt(selectedRow, 0);
//            try {
//                PreparedStatement ps = conn.prepareStatement(
//                        "UPDATE milestones SET is_completed = TRUE WHERE milestone_id = ?"
//                );
//                ps.setInt(1, milestoneId);
//                ps.executeUpdate();
//                JOptionPane.showMessageDialog(this, "✅ Milestone marked as completed!");
//                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        });
//
//        return panel;
//    }
//}
