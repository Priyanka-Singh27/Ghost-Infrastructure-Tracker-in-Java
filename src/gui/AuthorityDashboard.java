package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthorityDashboard extends JFrame {
    private final Connection conn;
    private final int authorityId;
    private final String authorityName;

    public AuthorityDashboard(Connection conn, int id, String name) {
        this.conn = conn;
        this.authorityId = id;
        this.authorityName = name;

        setTitle("Authority Dashboard - " + name);
        setSize(950, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
//===========================================
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        JLabel header = new JLabel("Welcome, " + authorityName, JLabel.CENTER);
//        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
//        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//
//        JButton logoutBtn = new JButton("Logout");
//        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        logoutBtn.setBackground(new Color(220, 53, 69));
//        logoutBtn.setForeground(Color.WHITE);
//        logoutBtn.setFocusPainted(false);
//        logoutBtn.addActionListener(e -> {
//            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?",
//                    "Logout Confirmation", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                dispose();
//                new WelcomeFrame(conn); // Go back to Welcome Page
//            }
//        });
//
//        headerPanel.add(headerPanel, BorderLayout.CENTER);
//        headerPanel.add(logoutBtn, BorderLayout.EAST);
//================================================

        JLabel header = new JLabel("Welcome, " + authorityName, JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Approve Project", approveProjectPanel());
        tabs.addTab("Assign Contractor", assignContractorPanel());
        tabs.addTab("Audit Project", auditPanel());
        tabs.addTab("View Projects", viewProjectsPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    //  APPROVE PROJECT
    private JPanel approveProjectPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField budgetField = new JTextField();
        JTextField startDateField = new JTextField("yyyy-MM-dd");
        JTextField endDateField = new JTextField("yyyy-MM-dd");
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"road", "bridge", "other"});
        JButton approveBtn = new JButton("Approve Project");

        // Form Layout
        addRow(panel, gbc, 0, "Project Name:", nameField);
        addRow(panel, gbc, 1, "Location:", locationField);
        addRow(panel, gbc, 2, "Budget (₹):", budgetField);
        addRow(panel, gbc, 3, "Start Date:", startDateField);
        addRow(panel, gbc, 4, "End Date:", endDateField);
        addRow(panel, gbc, 5, "Project Type:", typeBox);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(approveBtn, gbc);

        approveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String loc = locationField.getText().trim();
                double budget = Double.parseDouble(budgetField.getText().trim());
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDateField.getText().trim());
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDateField.getText().trim());
                String type = (String) typeBox.getSelectedItem();

                if (name.isEmpty() || loc.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled!");
                    return;
                }

                if (!end.after(start)) {
                    JOptionPane.showMessageDialog(this, "End date must be after start date.");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO projects (project_name, location, budget, start_date, end_date, type, status) VALUES (?, ?, ?, ?, ?, ?, 'Approved')",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, name);
                ps.setString(2, loc);
                ps.setDouble(3, budget);
                ps.setDate(4, new java.sql.Date(start.getTime()));
                ps.setDate(5, new java.sql.Date(end.getTime()));
                ps.setString(6, type);
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                int projectId = keys.next() ? keys.getInt(1) : -1;

                JOptionPane.showMessageDialog(this, "Project Approved! (ID: " + projectId + ")");

                autoAddMilestones(projectId, type);
                resetFields(nameField, locationField, budgetField, startDateField, endDateField, typeBox);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid number for budget!");
            } catch (Exception ex) {
                showError("Error approving project", ex);
            }
        });

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void autoAddMilestones(int projectId, String type) throws SQLException {
        if (type.equalsIgnoreCase("road")) {
            insertMilestone(projectId, "Land cleared", 20);
            insertMilestone(projectId, "Foundation laid", 30);
            insertMilestone(projectId, "Paving completed", 30);
            insertMilestone(projectId, "Marking & signage", 20);
        } else if (type.equalsIgnoreCase("bridge")) {
            insertMilestone(projectId, "Foundation completed", 30);
            insertMilestone(projectId, "Pillars erected", 30);
            insertMilestone(projectId, "Deck laid", 40);
        } else {
            int count = Integer.parseInt(JOptionPane.showInputDialog("Enter number of milestones:"));
            for (int i = 1; i <= count; i++) {
                String desc = JOptionPane.showInputDialog("Milestone " + i + " description:");
                double weight = Double.parseDouble(JOptionPane.showInputDialog("Weightage:"));
                insertMilestone(projectId, desc, weight);
            }
        }
        JOptionPane.showMessageDialog(this, "Milestones added successfully!");
    }

    private void insertMilestone(int projectId, String desc, double weight) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO milestones (project_id, description, weightage, is_completed) VALUES (?, ?, ?, false)"
        );
        ps.setInt(1, projectId);
        ps.setString(2, desc);
        ps.setDouble(3, weight);
        ps.executeUpdate();
    }

    private void resetFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }
    private void resetFields(JTextField f1, JTextField f2, JTextField f3, JTextField f4, JTextField f5, JComboBox<?> box) {
        f1.setText(""); f2.setText(""); f3.setText(""); f4.setText("yyyy-MM-dd"); f5.setText("yyyy-MM-dd"); box.setSelectedIndex(0);
    }

    //  ASSIGN CONTRACTOR
    private JPanel assignContractorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Contact"}, 0);
        JTable contractorTable = new JTable(tableModel);
        JButton refreshBtn = new JButton("Load Contractors");
        JButton assignBtn = new JButton("Assign Contractor");

        JTextField projectIdField = new JTextField();
        JTextField contractorIdField = new JTextField();

        JPanel bottomPanel = new JPanel(new GridLayout(3, 2, 10, 8));
        bottomPanel.add(new JLabel("Project ID:"));
        bottomPanel.add(projectIdField);
        bottomPanel.add(new JLabel("Contractor ID:"));
        bottomPanel.add(contractorIdField);
        bottomPanel.add(assignBtn);

        panel.add(refreshBtn, BorderLayout.NORTH);
        panel.add(new JScrollPane(contractorTable), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadContractors(tableModel));

        assignBtn.addActionListener(e -> {
            try {
                int projectId = Integer.parseInt(projectIdField.getText().trim());
                int contractorId = Integer.parseInt(contractorIdField.getText().trim());

                if (!doesProjectExist(projectId)) {
                    JOptionPane.showMessageDialog(this, "Project ID does not exist!");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE projects SET contractor_id=?, status='In Progress' WHERE project_id=?"
                );
                ps.setInt(1, contractorId);
                ps.setInt(2, projectId);
                ps.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO contractors (contractor_id, contractor_name, project_id, status) VALUES (?, (SELECT name FROM stakeholders WHERE id=?), ?, 'Assigned')"
                );
                ps2.setInt(1, contractorId);
                ps2.setInt(2, contractorId);
                ps2.setInt(3, projectId);
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Contractor assigned successfully!");
                projectIdField.setText("");
                contractorIdField.setText("");
            } catch (Exception ex) {
                showError("Error assigning contractor", ex);
            }
        });

        return panel;
    }

    private void loadContractors(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            ResultSet rs = conn.createStatement().executeQuery("SELECT id, name, contact FROM stakeholders WHERE role='Contractor'");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getLong("contact")});
            }
        } catch (Exception ex) {
            showError("Error loading contractors", ex);
        }
    }

    private boolean doesProjectExist(int projectId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT project_id FROM projects WHERE project_id=?");
        ps.setInt(1, projectId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    //  AUDIT PROJECT
    private JPanel auditPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel heading = new JLabel("Enter Project ID to Audit:");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JTextField pidField = new JTextField(10);
        JButton auditBtn = new JButton("Mark Completed");

        JPanel centerPanel = new JPanel();
        centerPanel.add(heading);
        centerPanel.add(pidField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(auditBtn);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        auditBtn.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(pidField.getText().trim());
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE projects SET status='Completed' WHERE project_id=?"
                );
                ps.setInt(1, pid);
                int updated = ps.executeUpdate();

                if (updated > 0)
                    JOptionPane.showMessageDialog(this, "Project " + pid + " marked as completed!");
                else
                    JOptionPane.showMessageDialog(this, "Project ID not found.");
                pidField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid Project ID!");
            } catch (Exception ex) {
                showError("Error auditing project", ex);
            }
        });

        return panel;
    }

    // VIEW PROJECTS
    private JPanel viewProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new String[]{
                "Project ID", "Name", "Location", "Budget", "Start Date", "End Date", "Type", "Status", "Contractor ID"
        }, 0);
        JTable table = new JTable(model);
        JButton refreshBtn = new JButton("Refresh Projects");

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadProjects(model));

        // Auto-load on open
        loadProjects(model);

        return panel;
    }

    private void loadProjects(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM projects");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("project_id"),
                        rs.getString("project_name"),
                        rs.getString("location"),
                        rs.getDouble("budget"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("contractor_id")
                });
            }
        } catch (Exception ex) {
            showError("Error loading projects", ex);
        }
    }

    // - UTILITIES
    private void showError(String msg, Exception ex) {
        JOptionPane.showMessageDialog(this, msg + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

}


//public class AuthorityDashboard extends JFrame {
//    private final Connection conn;
//    private final int authorityId;
//    private final String authorityName;
//
//    public AuthorityDashboard(Connection conn, int id, String name) {
//        this.conn = conn;
//        this.authorityId = id;
//        this.authorityName = name;
//
//        setTitle("Authority Dashboard - " + name);
//        setSize(800, 600);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        JTabbedPane tabs = new JTabbedPane();
//        tabs.addTab("Approve Project", approveProjectPanel());
//        tabs.addTab("Assign Contractor", assignContractorPanel());
//        tabs.addTab("Audit Project", auditPanel());
//        tabs.addTab("View Projects", viewProjectsPanel());
//
//        add(tabs);
//        setVisible(true);
//    }
//
//    //  APPROVE PROJECT
//    private JPanel approveProjectPanel() {
//        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 5));
//
//        JTextField nameField = new JTextField();
//        JTextField locationField = new JTextField();
//        JTextField budgetField = new JTextField();
//        JTextField startDateField = new JTextField("yyyy-MM-dd");
//        JTextField endDateField = new JTextField("yyyy-MM-dd");
//        JTextField typeField = new JTextField();
//
//        JButton approveBtn = new JButton("Approve Project");
//
//        panel.add(new JLabel("Project Name:"));
//        panel.add(nameField);
//        panel.add(new JLabel("Location:"));
//        panel.add(locationField);
//        panel.add(new JLabel("Budget:"));
//        panel.add(budgetField);
//        panel.add(new JLabel("Start Date (yyyy-MM-dd):"));
//        panel.add(startDateField);
//        panel.add(new JLabel("End Date (yyyy-MM-dd):"));
//        panel.add(endDateField);
//        panel.add(new JLabel("Type (road/bridge/other):"));
//        panel.add(typeField);
//        panel.add(new JLabel(""));
//        panel.add(approveBtn);
//
//        approveBtn.addActionListener(e -> {
//            try {
//                String name = nameField.getText();
//                String loc = locationField.getText();
//                double budget = Double.parseDouble(budgetField.getText());
//                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDateField.getText());
//                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDateField.getText());
//                String type = typeField.getText();
//
//                if (!end.after(start)) {
//                    JOptionPane.showMessageDialog(this, "End date must be after start date.");
//                    return;
//                }
//
//                PreparedStatement ps = conn.prepareStatement(
//                        "INSERT INTO projects (project_name, location, budget, start_date, end_date, type, status) VALUES (?, ?, ?, ?, ?, ?, 'Approved')",
//                        Statement.RETURN_GENERATED_KEYS
//                );
//                ps.setString(1, name);
//                ps.setString(2, loc);
//                ps.setDouble(3, budget);
//                ps.setDate(4, new java.sql.Date(start.getTime()));
//                ps.setDate(5, new java.sql.Date(end.getTime()));
//                ps.setString(6, type);
//                ps.executeUpdate();
//
//                ResultSet keys = ps.getGeneratedKeys();
//                int projectId = -1;
//                if (keys.next()) projectId = keys.getInt(1);
//
//                JOptionPane.showMessageDialog(this, "✅ Project Approved (ID: " + projectId + ")");
//
//                // auto/add milestones
//                if (type.equalsIgnoreCase("road")) {
//                    insertMilestone(projectId, "Land cleared", 20);
//                    insertMilestone(projectId, "Foundation laid", 30);
//                    insertMilestone(projectId, "Paving completed", 30);
//                    insertMilestone(projectId, "Marking & signage", 20);
//                } else if (type.equalsIgnoreCase("bridge")) {
//                    insertMilestone(projectId, "Foundation completed", 30);
//                    insertMilestone(projectId, "Pillars erected", 30);
//                    insertMilestone(projectId, "Deck laid", 40);
//                } else {
//                    String countStr = JOptionPane.showInputDialog("Enter number of milestones:");
//                    int count = Integer.parseInt(countStr);
//                    for (int i = 1; i <= count; i++) {
//                        String desc = JOptionPane.showInputDialog("Milestone " + i + " description:");
//                        String weightStr = JOptionPane.showInputDialog("Weightage:");
//                        double weight = Double.parseDouble(weightStr);
//                        insertMilestone(projectId, desc, weight);
//                    }
//                }
//
//                JOptionPane.showMessageDialog(this, "Milestones added successfully!");
//                nameField.setText("");
//                locationField.setText("");
//                budgetField.setText("");
//                startDateField.setText("yyyy-MM-dd");
//                endDateField.setText("yyyy-MM-dd");
//                typeField.setText("");
//
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        });
//
//        return panel;
//    }
//
//    private void insertMilestone(int projectId, String desc, double weight) throws SQLException {
//        PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO milestones (project_id, description, weightage, is_completed) VALUES (?, ?, ?, false)"
//        );
//        ps.setInt(1, projectId);
//        ps.setString(2, desc);
//        ps.setDouble(3, weight);
//        ps.executeUpdate();
//    }
//
//    //  ASSIGN CONTRACTOR
//    private JPanel assignContractorPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        JButton refreshBtn = new JButton("Load Contractors");
//        JButton assignBtn = new JButton("Assign Contractor");
//
//        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Contact"}, 0);
//        JTable contractorTable = new JTable(tableModel);
//        JScrollPane scroll = new JScrollPane(contractorTable);
//
//        JPanel bottom = new JPanel(new GridLayout(3, 2, 5, 5));
//        JTextField projectIdField = new JTextField();
//        JTextField contractorIdField = new JTextField();
//        bottom.add(new JLabel("Project ID:"));
//        bottom.add(projectIdField);
//        bottom.add(new JLabel("Contractor ID:"));
//        bottom.add(contractorIdField);
//        bottom.add(assignBtn);
//
//        panel.add(refreshBtn, BorderLayout.NORTH);
//        panel.add(scroll, BorderLayout.CENTER);
//        panel.add(bottom, BorderLayout.SOUTH);
//
//        refreshBtn.addActionListener(e -> {
//            try {
//                tableModel.setRowCount(0);
//                Statement st = conn.createStatement();
//                ResultSet rs = st.executeQuery("SELECT id, name, contact FROM stakeholders WHERE role='Contractor'");
//                while (rs.next()) {
//                    tableModel.addRow(new Object[]{
//                            rs.getInt("id"), rs.getString("name"), rs.getLong("contact")
//                    });
//                }
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error loading contractors: " + ex.getMessage());
//            }
//        });
//
//        assignBtn.addActionListener(e -> {
//            try {
//                int projectId = Integer.parseInt(projectIdField.getText());
//                int contractorId = Integer.parseInt(contractorIdField.getText());
//
//                PreparedStatement ps = conn.prepareStatement(
//                        "UPDATE projects SET contractor_id=?, status='In Progress' WHERE project_id=?"
//                );
//                ps.setInt(1, contractorId);
//                ps.setInt(2, projectId);
//                ps.executeUpdate();
//
//                PreparedStatement ps2 = conn.prepareStatement(
//                        "INSERT INTO contractors (contractor_id, contractor_name, project_id, status) VALUES (?, (SELECT name FROM stakeholders WHERE id=?), ?, 'Assigned')"
//                );
//                ps2.setInt(1, contractorId);
//                ps2.setInt(2, contractorId);
//                ps2.setInt(3, projectId);
//                ps2.executeUpdate();
//
//                JOptionPane.showMessageDialog(this, "✅ Contractor assigned successfully!");
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        });
//
//        return panel;
//    }
//
//    //AUDIT PROJECT
//    private JPanel auditPanel() {
//        JPanel panel = new JPanel(new BorderLayout(10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
//
//        // ===== TOP SECTION: Instruction Label =====
//        JLabel heading = new JLabel("Enter Project ID to Audit:");
//        heading.setFont(new Font("Segoe UI", Font.BOLD, 14));
//
//        // ===== CENTER SECTION: Input Field =====
//        JTextField pidField = new JTextField(15);
//        JPanel inputPanel = new JPanel();
//        inputPanel.add(heading);
//        inputPanel.add(pidField);
//
//        // ===== BOTTOM SECTION: Button =====
//        JButton auditBtn = new JButton("Mark Completed");
//        auditBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        auditBtn.setFocusPainted(false);
//        auditBtn.setBackground(new Color(0, 120, 215));
//        auditBtn.setForeground(Color.WHITE);
//        auditBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(auditBtn);
//
//        // ===== Add to Main Panel =====
//        panel.add(inputPanel, BorderLayout.NORTH);
//        panel.add(Box.createVerticalStrut(40), BorderLayout.CENTER);
//        panel.add(buttonPanel, BorderLayout.SOUTH);
//
//        // ===== Button Action =====
//        auditBtn.addActionListener(e -> {
//            try {
//                int pid = Integer.parseInt(pidField.getText().trim());
//                PreparedStatement ps = conn.prepareStatement(
//                        "UPDATE projects SET status='Completed' WHERE project_id=?"
//                );
//                ps.setInt(1, pid);
//                int updated = ps.executeUpdate();
//
//                if (updated > 0) {
//                    JOptionPane.showMessageDialog(this,
//                            "Project " + pid + " marked as completed!");
//                    pidField.setText("");
//                } else {
//                    JOptionPane.showMessageDialog(this,
//                            "Project ID not found.");
//                }
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(this,
//                        "Please enter a valid Project ID.");
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this,
//                        "Error: " + ex.getMessage());
//            }
//        });
//
//        return panel;
//    }
//
////    private JPanel auditPanel() {
////        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
////        JTextField pidField = new JTextField();
////        JButton auditBtn = new JButton("Mark Completed");
////
////        panel.add(new JLabel("Enter Project ID to Audit:"));
////        panel.add(pidField);
////        panel.add(auditBtn);
////
////        auditBtn.addActionListener(e -> {
////            try {
////                int pid = Integer.parseInt(pidField.getText());
////                PreparedStatement ps = conn.prepareStatement(
////                        "UPDATE projects SET status='Completed' WHERE project_id=?"
////                );
////                ps.setInt(1, pid);
////                ps.executeUpdate();
////                JOptionPane.showMessageDialog(this, "Project marked as completed.");
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
////            }
////        });
////
////        return panel;
////    }
//
//    // VIEW PROJECTS
//    private JPanel viewProjectsPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Status", "Contractor ID"}, 0);
//        JTable table = new JTable(model);
//        JButton refreshBtn = new JButton("Refresh");
//
//        panel.add(new JScrollPane(table), BorderLayout.CENTER);
//        panel.add(refreshBtn, BorderLayout.SOUTH);
//
//        refreshBtn.addActionListener(e -> {
//            try {
//                model.setRowCount(0);
//                Statement st = conn.createStatement();
//                ResultSet rs = st.executeQuery("SELECT * FROM projects");
//                while (rs.next()) {
//                    model.addRow(new Object[]{
//                            rs.getInt("project_id"),
//                            rs.getString("project_name"),
//                            rs.getString("status"),
//                            rs.getInt("contractor_id")
//                    });
//                }
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        });
//
//        return panel;
//    }
//}
