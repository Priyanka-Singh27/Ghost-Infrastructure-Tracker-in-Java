//import java.text.SimpleDateFormat;
//import java.util.*;
//
//class ProjectDetails {
//    String projectName;
//    String location;
//    double budget;
//    Date startDate;
//    Date endDate;
//    String type;
//
//    public ProjectDetails(String projectName, String location, double budget, Date startDate, Date endDate, String type) {
//        this.projectName = projectName;
//        this.location = location;
//        this.budget = budget;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.type = type;
//    }
//}
//
//class Milestone {
//    String description;
//    double weightage;
//    boolean isCompleted;
//
//    public Milestone(String description, double weightage) {
//        this.description = description;
//        this.weightage = weightage;
//        this.isCompleted = false;
//    }
//}
//
//class Funding {
//    double sanctionedAmount;
//    double releasedAmount;
//    double utilizedAmount;
//
//    public Funding(double sanctionedAmount) {
//        this.sanctionedAmount = sanctionedAmount;
//        this.releasedAmount = 0;
//        this.utilizedAmount = 0;
//    }
//
//    public void releaseFunds(double amount) {
//        if (amount <= (sanctionedAmount - releasedAmount)) {
//            releasedAmount += amount;
//            System.out.println("Released " + amount + " funds. Total released: " + releasedAmount);
//        } else {
//            System.out.println("Not enough sanctioned funds left!");
//        }
//    }
//
//    public void useFunds(double amount) {
//        if (amount <= releasedAmount - utilizedAmount) {
//            utilizedAmount += amount;
//            System.out.println("Utilized " + amount + " funds. Total utilized: " + utilizedAmount);
//        } else {
//            System.out.println("Not enough released funds available!");
//        }
//    }
//
//    public double getRemainingFunds() {
//        return sanctionedAmount - releasedAmount;
//    }
//}
//
//class CitizenReport {
//    String citizenName;
//    String issue;
//    Date dateReported;
//
//    public CitizenReport(String citizenName, String issue, Date dateReported) {
//        this.citizenName = citizenName;
//        this.issue = issue;
//        this.dateReported = dateReported;
//    }
//}
//
//class AuditReport {
//    Date auditDate;
//    String findings;
//    String recommendations;
//
//    public AuditReport(Date auditDate, String findings, String recommendations) {
//        this.auditDate = auditDate;
//        this.findings = findings;
//        this.recommendations = recommendations;
//    }
//}
//
//class Project {
//    String name;
//    String location;
//    double budget;
//    Date startDate;
//    Date endDate;
//    String type;
//
//    List<Milestone> milestones;
//    List<Contractor> assignedContractors;
//    Funding fundingDetails;
//    List<CitizenReport> citizenReports;
//    List<AuditReport> auditHistory;
//
//    boolean completed;
//
//    public Project(ProjectDetails details) {
//        this.name = details.projectName;
//        this.location = details.location;
//        this.budget = details.budget;
//        this.startDate = details.startDate;
//        this.endDate = details.endDate;
//        this.type = details.type;
//
//        this.milestones = new ArrayList<>();
//        this.assignedContractors= new ArrayList<>();
//        this.citizenReports = new ArrayList<>();
//        this.auditHistory = new ArrayList<>();
//        this.fundingDetails = new Funding(details.budget);
//        this.completed = false;
//
//        generateMilestones();
//    }
//
//    private void generateMilestones() {
//        // Example auto-generated milestones based on project type
//        switch(type.toLowerCase()) {
//            case "road":
//                milestones.add(new Milestone("Land cleared", 20));
//                milestones.add(new Milestone("Foundation laid", 30));
//                milestones.add(new Milestone("Paving completed", 30));
//                milestones.add(new Milestone("Marking & signage", 20));
//                break;
//            case "bridge":
//                milestones.add(new Milestone("Foundation completed", 30));
//                milestones.add(new Milestone("Pillars erected", 30));
//                milestones.add(new Milestone("Deck laid", 40));
//                break;
//            default:
//                System.out.print("enter the number of milestones you want to add: ");
//                Scanner sc= new Scanner(System.in);
//                int n=sc.nextInt();
//                sc.nextLine(); // consume newline
//                for (int i=0;i<n;i++){
//                    System.out.print("enter the description of milestone no. "+(i+1)+": ");
//                    String mile= sc.nextLine();
//                    System.out.print("enter it's weightage: " );
//                    double weight= sc.nextDouble();
//                    milestones.add(new Milestone(mile,weight));
//                    sc.nextLine();
//                }
//        }
//    }
//
//    public double getProgress() {
//        double totalWeight = 0;
//        double completedWeight = 0;
//        for (Milestone m : milestones) {
//            totalWeight += m.weightage;
//            if (m.isCompleted) completedWeight += m.weightage;
//        }
//        return totalWeight == 0 ? 0 : (completedWeight / totalWeight) * 100;
//    }
//
//    public void checkCompletion() {
//        completed = getProgress() >= 100;
//    }
//}
//
//class Authority {
//    String name;
//
//    public Authority(String name) {
//        this.name = name;
//    }
//
//    public Project approveProject(ProjectDetails details) {
//        Project p = new Project(details);
//        System.out.println("Project Approved: " + p.name);
//        return p;
//    }
//
//    public void assignContractor(Project project, Contractor contractor) {
//        project.assignedContractors.add(contractor);
//        System.out.println("Contractor " + contractor.name + " assigned to project " + project.name);
//    }
//
//    public void auditProject(Project project) {
//        String findings = "Audit conducted. Progress: " + project.getProgress() + "%";
//        String recommendations = "Ensure milestones are completed as per schedule.";
//        AuditReport report = new AuditReport(new Date(), findings, recommendations);
//        project.auditHistory.add(report);
//        System.out.println("Audit completed for project " + project.name);
//    }
//}
//

//class Contractor extends Authority {
//    public Contractor(String name) {
//        super(name);
//    }
//
//    public void updateProgress(Project project, int milestoneIndex) {
//        if (!project.assignedContractors.contains(this)){
//            System.out.println("you are not the assigned contractor to this project!!");
//            return;
//        }
//        if (milestoneIndex >= 0 && milestoneIndex < project.milestones.size()) {
//            project.milestones.get(milestoneIndex).isCompleted = true;
//            System.out.println("milestone " + project.milestones.get(milestoneIndex).description + " marked as completed.");
//            project.checkCompletion();
//        } else {
//            System.out.println("Invalid milestone index!");
//        }
//    }
//
//    public void requestFunds(Project project, double amount) {
//        project.fundingDetails.releaseFunds(amount);
//    }
//}
//

//public class Main {
//    static Scanner sc = new Scanner(System.in);
//    static List<Project> projects = new ArrayList<>();
//    static List<Contractor> contractors= new ArrayList<>();
//
//    public static void main(String[] args) throws Exception {
//        Authority authority = new Authority("Govt Authority");
//
//        while(true) {
//            System.out.println("\nGhost Infrastructure Tracker Menu");
//            System.out.println("1. Authority: Approve Project");
//            System.out.println("2. Authority: Assign Contractor");
//            System.out.println("3. Contractor: Update Milestone");
//            System.out.println("4. Contractor: Request Funds");
//            System.out.println("5. Citizen: Report Issue");
//            System.out.println("6. Authority: Audit Project");
//            System.out.println("7. View Project Status");
//            System.out.println("0. Exit");
//            System.out.print("Enter choice: ");
//            int choice = sc.nextInt();
//            sc.nextLine();
//
//            switch(choice) {
//                case 1:
//                    approveProject(authority);
//                    break;
//                case 2:
//                    assignContractor(authority);
//                    break;
//                case 3:
//                    updateMilestone();
//                    break;
//                case 4:
//                    requestFunds();
//                    break;
//                case 5:
//                    reportIssue();
//                    break;
//                case 6:
//                    auditProject(authority);
//                    break;
//                case 7:
//                    viewStatus();
//                    break;
//                case 0:
//                    System.out.println("Exiting...");
//                    return;
//                default:
//                    System.out.println("Invalid choice!");
//            }
//        }
//    }
//
//    static void approveProject(Authority authority) throws Exception {
//        System.out.print("Project Name: ");
//        String name = sc.nextLine();
//        System.out.print("Location: ");
//        String loc = sc.nextLine();
//        System.out.print("Budget: ");
//        double budget = sc.nextDouble();
//        sc.nextLine();
//        System.out.print("Start Date (yyyy-MM-dd): ");
//        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine());
//        System.out.print("End Date (yyyy-MM-dd): ");
//        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine());
//        if (!end.after(start)) {
//            System.out.println("End date must be after start date. Project approval cancelled.");
//            return;
//        }
//        System.out.print("Type (road/bridge/hospital/etc.): ");
//        String type = sc.nextLine();
//
//        ProjectDetails details = new ProjectDetails(name, loc, budget, start, end, type);
//        Project p = authority.approveProject(details);
//        projects.add(p);
//    }
//
//    static void assignContractor(Authority authority) {
//        Project project = selectProject();
//        Contractor contractor1 = selectContractor();
//        if(project != null) authority.assignContractor(project, contractor1);
//    }
//
//    static Contractor selectContractor() {
//        if(contractors.isEmpty()) {
//            System.out.println("No contractors available!");
//            System.out.print("Enter new contractor name: ");
//            String contractorName = sc.nextLine();
//            Contractor newContractor = new Contractor(contractorName);
//            contractors.add(newContractor);
//            return newContractor;
//        }
//        System.out.println("Select Contractor:");
//        for(int i=0;i<contractors.size();i++) {
//            System.out.println(i + ". " + contractors.get(i).name);
//        }
//        int idx = sc.nextInt();
//        sc.nextLine();
//        if(idx < 0 || idx >= contractors.size()) return null;
//        return contractors.get(idx);
//    }
//
//    static void updateMilestone() {
//        Project project = selectProject();
//        if(project == null || project.assignedContractors == null) {
//            System.out.println("No contractor assigned to this project.");
//            return;
//        }
//        System.out.println("Milestones:");
//        for(int i=0;i<project.milestones.size();i++) {
//            Milestone m = project.milestones.get(i);
//            System.out.println(i + ". " + m.description + " [" + (m.isCompleted ? "Completed" : "Pending") + "]");
//        }
//        //DONE: need to find the assigned contractor then that contractor will update
//        System.out.println("Select contractor working on this milestone:");
//        for (int i = 0; i < project.assignedContractors.size(); i++) {
//            System.out.println(i + ". " + project.assignedContractors.get(i).name);
//        }
//        int cIdx = sc.nextInt();
//        sc.nextLine();
//        if (cIdx < 0 || cIdx >= project.assignedContractors.size()) {
//            System.out.println("Invalid contractor index!");
//            return;
//        }
//        Contractor contractor = project.assignedContractors.get(cIdx);
//
//        System.out.print("Enter milestone index to mark completed: ");
//        int midx = sc.nextInt();
//        sc.nextLine();
////        Contractor contractor= project.assignedContractors;
//        contractor.updateProgress(project, midx);
//    }
//
//    static void requestFunds() {
//        Project project = selectProject();
//        if (project == null || project.assignedContractors.isEmpty()) {
//            System.out.println("No contractors assigned to this project.");
//            return;
//        }
//
//        //same thing here find the assigned contractor
//        System.out.println("Select contractor requesting funds:");
//        for (int i = 0; i < project.assignedContractors.size(); i++) {
//            System.out.println(i + ". " + project.assignedContractors.get(i).name);
//        }
//        int cIdx = sc.nextInt();
//        sc.nextLine();
//        if (cIdx < 0 || cIdx >= project.assignedContractors.size()) {
//            System.out.println("Invalid contractor index!");
//            return;
//        }
//        Contractor contractor = project.assignedContractors.get(cIdx);
//
//        System.out.print("Enter amount to request: ");
//        double amount = sc.nextDouble();
//        sc.nextLine();
//
//        contractor.requestFunds(project, amount);
//    }
//
//    static void reportIssue() throws Exception {
//        Project project = selectProject();
//        if(project == null) return;
//        System.out.print("Your Name: ");
//        String name = sc.nextLine();
//        System.out.print("Describe Issue: ");
//        String issue = sc.nextLine();
//        project.citizenReports.add(new CitizenReport(name, issue, new Date()));
//        System.out.println("Report submitted.");
//    }
//
//    static void auditProject(Authority authority) {
//        Project project = selectProject();
//        if(project == null) return;
//        authority.auditProject(project);
//    }
//
//    static void viewStatus() {
//        Project project = selectProject();
//        if(project == null) return;
//        System.out.println("Project: " + project.name);
//        System.out.println("Location: " + project.location);
//        System.out.println("Budget: " + project.budget);
//        System.out.println("Type: " + project.type);
//        System.out.println("Progress: " + project.getProgress() + "%");
//        System.out.println("Assigned Contractors:");
//        if (project.assignedContractors.isEmpty()) {
//            System.out.println("- None assigned yet");
//        } else {
//            for (Contractor c : project.assignedContractors) {
//                System.out.println("- " + c.name);
//            }
//        }
//        System.out.println("Milestones:");
//        for(Milestone m : project.milestones) {
//            System.out.println("- " + m.description + " [" + (m.isCompleted ? "Completed" : "Pending") + "]");
//        }
//        System.out.println("Citizen Reports: " + project.citizenReports.size());
//        System.out.println("Audit Reports: " + project.auditHistory.size());
//    }
//
//    static Project selectProject() {
//        if(projects.isEmpty()) {
//            System.out.println("No projects available!");
//            return null;
//        }
//        System.out.println("Select Project:");
//        for(int i=0;i<projects.size();i++) {
//            System.out.println(i + ". " + projects.get(i).name + " (" + projects.get(i).type + ")");
//        }
//        int idx = sc.nextInt();
//        sc.nextLine();
//        if(idx < 0 || idx >= projects.size()) return null;
//        return projects.get(idx);
//    }
//}

//--------------------------><---------------------------------------------------------



//import java.sql.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.Date;
//import java.util.List;
//import java.text.SimpleDateFormat;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//
//public class Main {
//    static Scanner sc = new Scanner(System.in);
//    static Connection conn;
//
//    public static void main(String[] args) {
//        try {
//            connectDatabase();
//            System.out.println("Connected to database successfully.");
//
//            while (true) {
//                System.out.println("\n==== Welcome to Ghost Infrastructure Tracker ====");
//                System.out.println("1. Login");
//                System.out.println("2. Sign Up");
//                System.out.println("0. Exit");
//                System.out.print("Enter your choice: ");
//                int ch = sc.nextInt(); sc.nextLine();
//
//                switch (ch) {
//                    case 1: login(); break;
//                    case 2: signUp(); break;
//                    case 0: System.out.println("Goodbye!"); return;
//                    default: System.out.println("Invalid option!");
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    static void connectDatabase() throws Exception {
//        String url = "jdbc:mysql://localhost:3306/infrastructure_tracker";
//        String user = "root";
//        String pass = "Priyanka@7083";
//        conn = DriverManager.getConnection(url, user, pass);
//    }
//
//    static void signUp() throws Exception {
//        System.out.print("Enter name: ");
//        String name = sc.nextLine();
//        System.out.print("Enter contact: ");
//        long contact = sc.nextLong(); sc.nextLine();
//        System.out.print("Enter role (Authority/Contractor/Citizen): ");
//        String role = sc.nextLine();
//        System.out.print("Enter password: ");
//        String password = sc.nextLine();
//
//        PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO stakeholders (name, contact, role, password) VALUES (?, ?, ?, ?)"
//        );
//        ps.setString(1, name);
//        ps.setLong(2, contact);
//        ps.setString(3, role);
//        ps.setString(4, password);
//        ps.executeUpdate();
//
//        System.out.println("Signup successful! You can now log in.");
//    }
//
//    static void login() throws Exception {
//        System.out.print("Enter name: ");
//        String name = sc.nextLine();
//        System.out.print("Enter password: ");
//        String password = sc.nextLine();
//
//        PreparedStatement ps = conn.prepareStatement(
//                "SELECT * FROM stakeholders WHERE name=? AND password=?"
//        );
//        ps.setString(1, name);
//        ps.setString(2, password);
//        ResultSet rs = ps.executeQuery();
//
//        if (rs.next()) {
//            String role = rs.getString("role");
//            int id = rs.getInt("id");
//            System.out.println("Login successful as " + role);
//
//            if (role.equalsIgnoreCase("Authority")) {
//                authorityMenu(id, name);
//            } else if (role.equalsIgnoreCase("Contractor")) {
//                contractorMenu(id, name);
//            } else {
//                citizenMenu(id, name);
//            }
//        } else {
//            System.out.println("Invalid credentials!");
//        }
//    }
//
//    // ===================== MENUS ======================
//    static void authorityMenu(int id, String name) throws Exception {
//        while (true) {
//            System.out.println("\n--- Authority Menu ---");
//            System.out.println("1. Approve Project");
//            System.out.println("2. Assign Contractor");
//            System.out.println("3. Audit Project");
//            System.out.println("4. View Projects");
//            System.out.println("0. Logout");
//            System.out.print("Choice: ");
//            int ch = sc.nextInt(); sc.nextLine();
//
//            switch (ch) {
//                case 1: approveProject(); break;
//                case 2: assignContractor(); break;
//                case 3: auditProject(); break;
//                case 4: viewProjects(); break;
//                case 0: return;
//            }
//        }
//    }
//
//    static void contractorMenu(int id, String name) throws Exception {
//        while (true) {
//            System.out.println("\n--- Contractor Menu ---");
//            System.out.println("1. Request Funds");
//            System.out.println("2. Update Milestone");
//            System.out.println("3. View Assigned Projects");
//            System.out.println("0. Logout");
//            System.out.print("Choice: ");
//            int ch = sc.nextInt(); sc.nextLine();
//
//            switch (ch) {
//                case 1: requestFunds(id); break;
//                case 2: updateMilestone(id); break;
//                case 3: viewProjectsByContractor(id); break;
//                case 0: return;
//            }
//        }
//    }
//
//    static void citizenMenu(int id, String name) throws Exception {
//        while (true) {
//            System.out.println("\n--- Citizen Menu ---");
//            System.out.println("1. Report Complaint");
//            System.out.println("2. View Projects");
//            System.out.println("0. Logout");
//            System.out.print("Choice: ");
//            int ch = sc.nextInt(); sc.nextLine();
//
//            switch (ch) {
//                case 1: fileComplaint(id); break;
//                case 2: viewProjects(); break;
//                case 0: return;
//            }
//        }
//    }
//
//    // ===================== FUNCTIONALITIES ======================
//    static void approveProject() throws Exception {
//        System.out.print("Project Name: ");
//        String name = sc.nextLine();
//        System.out.print("Location: ");
//        String loc = sc.nextLine();
//        System.out.print("Budget: ");
//        double budget = sc.nextDouble(); sc.nextLine();
//        System.out.print("Start Date (yyyy-MM-dd): ");
//        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine());
//        System.out.print("End Date (yyyy-MM-dd): ");
//        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine());
//
//        if (!end.after(start)) {
//            System.out.println("End date must be after start date.");
//            return;
//        }
//
//        System.out.print("Type: ");
//        String type = sc.nextLine();
//
//        PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO projects (project_name, location, budget, start_date, end_date, type, status) VALUES (?, ?, ?, ?, ?, ?, 'Approved')",
//                Statement.RETURN_GENERATED_KEYS
//        );
//        ps.setString(1, name);
//        ps.setString(2, loc);
//        ps.setDouble(3, budget);
//        ps.setDate(4, new java.sql.Date(start.getTime()));
//        ps.setDate(5, new java.sql.Date(end.getTime()));
//        ps.setString(6, type);
//        ps.executeUpdate();
//
//        ResultSet keys = ps.getGeneratedKeys();
//        int projectId = -1;
//        if (keys.next()) projectId = keys.getInt(1);
//
//        System.out.println("Project approved successfully with ID: " + projectId);
//
//        // If not road or bridge, allow adding milestones
//        if (!type.equalsIgnoreCase("road") && !type.equalsIgnoreCase("bridge")) {
//            System.out.print("How many milestones do you want to add? ");
//            int count = sc.nextInt(); sc.nextLine();
//
//            for (int i = 1; i <= count; i++) {
//                System.out.println("\nEnter details for milestone " + i + ":");
//                System.out.print("Description: ");
//                String desc = sc.nextLine();
//                System.out.print("Weightage: ");
//                double weight = sc.nextDouble(); sc.nextLine();
//
//                PreparedStatement mps = conn.prepareStatement(
//                        "INSERT INTO milestones (project_id, description, weightage, is_completed) VALUES (?, ?, ?, false)"
//                );
//                mps.setInt(1, projectId);
//                mps.setString(2, desc);
//                mps.setDouble(3, weight);
//                mps.executeUpdate();
//            }
//
//            System.out.println("All milestones added successfully!");
//        }
//    }
//
//    static void assignContractor() throws Exception {
//        System.out.println("\nAvailable Contractors:");
//        PreparedStatement ps = conn.prepareStatement(
//                "SELECT id, name, contact FROM stakeholders WHERE role='Contractor'"
//        );
//        ResultSet rs = ps.executeQuery();
//
//        boolean hasContractors = false;
//        while (rs.next()) {
//            hasContractors = true;
//            System.out.println("ID: " + rs.getInt("id") + " | Name: " + rs.getString("name") + " | Contact: " + rs.getLong("contact"));
//        }
//
//        if (!hasContractors) {
//            System.out.println("No contractors found in the system.");
//        }
//
//        System.out.println("\nDo you want to:");
//        System.out.println("1. Choose an existing contractor");
//        System.out.println("2. Add a new contractor");
//        System.out.print("Enter choice: ");
//        int choice = sc.nextInt(); sc.nextLine();
//        int contractorId = -1;
//
//        if (choice == 1) {
//            System.out.print("Enter Contractor ID to assign: ");
//            contractorId = sc.nextInt(); sc.nextLine();
//        } else if (choice == 2) {
//            System.out.print("Enter Contractor Name: ");
//            String cname = sc.nextLine();
//            System.out.print("Enter Contractor Contact: ");
//            long contact = sc.nextLong(); sc.nextLine();
//            System.out.print("Enter Contractor Password: ");
//            String pass = sc.nextLine();
//
//            PreparedStatement addC = conn.prepareStatement(
//                    "INSERT INTO stakeholders (name, contact, role, password) VALUES (?, ?, 'Contractor', ?)",
//                    Statement.RETURN_GENERATED_KEYS
//            );
//            addC.setString(1, cname);
//            addC.setLong(2, contact);
//            addC.setString(3, pass);
//            addC.executeUpdate();
//            ResultSet keys = addC.getGeneratedKeys();
//            if (keys.next()) contractorId = keys.getInt(1);
//
//            System.out.println("New contractor added successfully with ID: " + contractorId);
//        } else {
//            System.out.println("Invalid choice.");
//            return;
//        }
//
//        System.out.print("Enter Project ID to assign: ");
//        int projectId = sc.nextInt(); sc.nextLine();
//
//        // Update the project with assigned contractor
//        PreparedStatement ps2 = conn.prepareStatement(
//                "UPDATE projects SET contractor_id=?, status='In Progress' WHERE project_id=?"
//        );
//        ps2.setInt(1, contractorId);
//        ps2.setInt(2, projectId);
//        ps2.executeUpdate();
//
//        // Add to contractors table (store contractor name)
//        PreparedStatement nameQuery = conn.prepareStatement("SELECT name FROM stakeholders WHERE id=?");
//        nameQuery.setInt(1, contractorId);
//        ResultSet cnameRs = nameQuery.executeQuery();
//        String cname = "";
//        if (cnameRs.next()) cname = cnameRs.getString("name");
//
//        PreparedStatement ps3 = conn.prepareStatement(
//                "INSERT INTO contractors (contractor_id, contractor_name, project_id, status) VALUES (?, ?, ?, 'Assigned')"
//        );
//        ps3.setInt(1, contractorId);
//        ps3.setString(2, cname);
//        ps3.setInt(3, projectId);
//        ps3.executeUpdate();
//
//        System.out.println("Contractor '" + cname + "' assigned successfully to Project ID " + projectId + ".");
//
//    }
//
//    static void requestFunds(int contractorId) throws Exception {
//        System.out.print("Enter Project ID: ");
//        int pid = sc.nextInt();
//        System.out.print("Enter Amount: ");
//        double amt = sc.nextDouble(); sc.nextLine();
//
//        PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO funding_requests (project_id, contractor_id, amount, status) VALUES (?, ?, ?, 'Pending')"
//        );
//        ps.setInt(1, pid);
//        ps.setInt(2, contractorId);
//        ps.setDouble(3, amt);
//        ps.executeUpdate();
//
//        System.out.println("Funding request submitted.");
//    }
//
//    static void fileComplaint(int citizenId) throws Exception {
//        System.out.print("Enter Project ID: ");
//        int pid = sc.nextInt(); sc.nextLine();
//        System.out.print("Enter Complaint: ");
//        String complaint = sc.nextLine();
//
//        PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO complaints (project_id, citizen_id, complaint) VALUES (?, ?, ?)"
//        );
//        ps.setInt(1, pid);
//        ps.setInt(2, citizenId);
//        ps.setString(3, complaint);
//        ps.executeUpdate();
//
//        System.out.println("Complaint filed successfully!");
//    }
//
//    static void viewProjects() throws Exception {
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery("SELECT * FROM projects");
//        while (rs.next()) {
//            System.out.println(rs.getInt("project_id") + ". " + rs.getString("project_name") +
//                    " | " + rs.getString("status") + " | Contractor ID: " + rs.getInt("contractor_id"));
//        }
//    }
//
//    static void viewProjectsByContractor(int contractorId) throws Exception {
//        PreparedStatement ps = conn.prepareStatement(
//                "SELECT * FROM projects WHERE contractor_id=?"
//        );
//        ps.setInt(1, contractorId);
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            System.out.println(rs.getInt("project_id") + ". " + rs.getString("project_name") +
//                    " | " + rs.getString("status"));
//        }
//    }
//
//    static void updateMilestone(int contractorId) throws Exception {
//        System.out.print("Enter Project ID: ");
//        int pid = sc.nextInt(); sc.nextLine();
//        System.out.print("Milestone Description: ");
//        String desc = sc.nextLine();
//        System.out.print("Weightage: ");
//        double weight = sc.nextDouble();
//
//        PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO milestones (project_id, description, weightage, is_completed) VALUES (?, ?, ?, true)"
//        );
//        ps.setInt(1, pid);
//        ps.setString(2, desc);
//        ps.setDouble(3, weight);
//        ps.executeUpdate();
//
//        System.out.println(" Milestone updated as completed!");
//    }
//
//    static void auditProject() throws Exception {
//        System.out.print("Enter Project ID: ");
//        int pid = sc.nextInt(); sc.nextLine();
//
//        PreparedStatement ps = conn.prepareStatement(
//                "UPDATE projects SET status='Completed' WHERE project_id=?"
//        );
//        ps.setInt(1, pid);
//        ps.executeUpdate();
//
//        System.out.println(" Project marked as completed.");
//    }
//}


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    static final Scanner sc = new Scanner(System.in);
    static Connection conn;

    public static void main(String[] args) {
        try {
            connectDatabase();
            System.out.println("Connected to database successfully.");

            while (true) {
                System.out.println("\n Welcome to Ghost Infrastructure Tracke");
                System.out.println("1. Login");
                System.out.println("2. Sign Up");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int ch = safeNextInt(); sc.nextLine();

                switch (ch) {
                    case 1 -> login();
                    case 2 -> signUp();
                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option!");
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal error: ");
            e.printStackTrace();
        } finally {
            closeConn();
        }
    }

    // mysql connection
    static void connectDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/infrastructure_tracker?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String pass = "Priyanka@7083";
        conn = DriverManager.getConnection(url, user, pass);
    }

    static void closeConn() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException ignored) {}
    }

    static int safeNextInt() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    static boolean projectExists(int projectId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM projects WHERE project_id = ?")) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    static Integer getProjectContractorId(int projectId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT contractor_id FROM projects WHERE project_id = ?")) {
            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int c = rs.getInt("contractor_id");
                    if (rs.wasNull()) return null;
                    return c;
                }
                return null;
            }
        }
    }

    //sign up
    static void signUp() throws Exception {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter contact: ");
        long contact = Long.parseLong(sc.nextLine());
        System.out.print("Enter role (Authority/Contractor/Citizen): ");
        String role = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO stakeholders (name, contact, role, password) VALUES (?, ?, ?, ?)"
        )) {
            ps.setString(1, name);
            ps.setLong(2, contact);
            ps.setString(3, role);
            ps.setString(4, password);
            ps.executeUpdate();
            System.out.println("Signup successful! You can now log in.");
        }
    }

    static void login() throws Exception {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try (PreparedStatement ps = conn.prepareStatement("SELECT id, role FROM stakeholders WHERE name=? AND password=?")) {
            ps.setString(1, name);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    int id = rs.getInt("id");
                    System.out.println("✅ Login successful as " + role);
                    switch (role.toLowerCase()) {
                        case "authority" -> authorityMenu(id, name);
                        case "contractor" -> contractorMenu(id, name);
                        default -> citizenMenu(id, name);
                    }
                } else {
                    System.out.println("Invalid credentials!");
                }
            }
        }
    }

    // menu
    static void authorityMenu(int id, String name) throws Exception {
        while (true) {
            System.out.println("\nAuthority Menu");
            System.out.println("1. Approve Project");
            System.out.println("2. Assign Contractor");
            System.out.println("3. Audit Project");
            System.out.println("4. View Projects");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            int ch = safeNextInt(); sc.nextLine();
            switch (ch) {
                case 1 -> approveProject();
                case 2 -> assignContractor();
                case 3 -> auditProject();
                case 4 -> viewProjects();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void contractorMenu(int id, String name) throws Exception {
        while (true) {
            System.out.println("\nContractor Menu");
            System.out.println("1. Request Funds");
            System.out.println("2. Mark Pending Milestone Completed");
            System.out.println("3. View Assigned Projects");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            int ch = safeNextInt(); sc.nextLine();
            switch (ch) {
                case 1 -> requestFunds(id);
                case 2 -> completeMilestoneFlow(id);
                case 3 -> viewProjectsByContractor(id);
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void citizenMenu(int id, String name) throws Exception {
        while (true) {
            System.out.println("\nCitizen Menu");
            System.out.println("1. Report Complaint");
            System.out.println("2. View Projects");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            int ch = safeNextInt(); sc.nextLine();
            switch (ch) {
                case 1 -> fileComplaint(id);
                case 2 -> viewProjects();
                case 0 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void approveProject() throws Exception {
        System.out.print("Project Name: "); String name = sc.nextLine();
        System.out.print("Location: "); String loc = sc.nextLine();
        System.out.print("Budget: "); double budget = Double.parseDouble(sc.nextLine());
        System.out.print("Start Date (yyyy-MM-dd): ");
        java.util.Date start = new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine());
        System.out.print("End Date (yyyy-MM-dd): ");
        java.util.Date end = new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine());
        if (!end.after(start)) {
            System.out.println("End date must be after start date.");
            return;
        }
        System.out.print("Type: "); String type = sc.nextLine();

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO projects (project_name, location, budget, start_date, end_date, type, status) VALUES (?, ?, ?, ?, ?, ?, 'Approved')",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, loc);
            ps.setDouble(3, budget);
            ps.setDate(4, new java.sql.Date(start.getTime()));
            ps.setDate(5, new java.sql.Date(end.getTime()));
            ps.setString(6, type);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int projectId = keys.getInt(1);
                    System.out.println("Project approved successfully with ID: " + projectId);
                    // auto-generate or prompt for milestones
                    if (type.equalsIgnoreCase("road")) {
                        insertMilestone(projectId, "Land cleared", 20);
                        insertMilestone(projectId, "Foundation laid", 30);
                        insertMilestone(projectId, "Paving completed", 30);
                        insertMilestone(projectId, "Marking & signage", 20);
                        System.out.println("Auto-generated milestones for road.");
                    } else if (type.equalsIgnoreCase("bridge")) {
                        insertMilestone(projectId, "Foundation completed", 30);
                        insertMilestone(projectId, "Pillars erected", 30);
                        insertMilestone(projectId, "Deck laid", 40);
                        System.out.println("Auto-generated milestones for bridge.");
                    } else {
                        System.out.print("How many milestones do you want to add? ");
                        int count = safeNextInt(); sc.nextLine();
                        for (int i = 1; i <= count; i++) {
                            System.out.println("Milestone " + i + " description: ");
                            String desc = sc.nextLine();
                            System.out.print("Weightage: ");
                            double w = Double.parseDouble(sc.nextLine());
                            insertMilestone(projectId, desc, w);
                        }
                        System.out.println("All milestones added.");
                    }
                } else {
                    System.out.println("Insert succeeded but couldn't obtain project id.");
                }
            }
        }
    }

    static void insertMilestone(int projectId, String desc, double weight) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO milestones (project_id, description, weightage, is_completed) VALUES (?, ?, ?, false)")) {
            ps.setInt(1, projectId);
            ps.setString(2, desc);
            ps.setDouble(3, weight);
            ps.executeUpdate();
        }
    }

    static void assignContractor() throws Exception {
        // list contractors from stakeholders table
        System.out.println("\nAvailable Contractors (from stakeholders):");
        try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, contact FROM stakeholders WHERE role='Contractor'")) {
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("name") + " | contact: " + rs.getLong("contact"));
                }
                if (!any) System.out.println("No contractors found.");
            }
        }

        System.out.println("\n1) Choose existing contractor");
        System.out.println("2) Add new contractor");
        System.out.print("choice: ");
        int choice = safeNextInt(); sc.nextLine();

        int stakeholderContractorId;
        if (choice == 1) {
            System.out.print("Enter Contractor (stakeholder) ID to assign: ");
            stakeholderContractorId = safeNextInt(); sc.nextLine();
            // verify contractor exists in stakeholders and role
            try (PreparedStatement chk = conn.prepareStatement("SELECT role FROM stakeholders WHERE id = ?")) {
                chk.setInt(1, stakeholderContractorId);
                try (ResultSet rs = chk.executeQuery()) {
                    if (!rs.next() || !"Contractor".equalsIgnoreCase(rs.getString("role"))) {
                        System.out.println("No contractor stakeholder found with that ID.");
                        return;
                    }
                }
            }
        } else if (choice == 2) {
            System.out.print("Contractor name: "); String cname = sc.nextLine();
            System.out.print("Contact: "); long contact = Long.parseLong(sc.nextLine());
            System.out.print("Password: "); String pw = sc.nextLine();
            // insert into stakeholders
            try (PreparedStatement ins = conn.prepareStatement("INSERT INTO stakeholders (name, contact, role, password) VALUES(?, ?, 'Contractor', ?)", Statement.RETURN_GENERATED_KEYS)) {
                ins.setString(1, cname);
                ins.setLong(2, contact);
                ins.setString(3, pw);
                ins.executeUpdate();
                try (ResultSet keys = ins.getGeneratedKeys()) {
                    if (keys.next()) stakeholderContractorId = keys.getInt(1);
                    else {
                        System.out.println("Could not create contractor in stakeholders.");
                        return;
                    }
                }
            }
            // create contractors row below
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("Enter Project ID to assign: ");
        int projectId = safeNextInt(); sc.nextLine();
        if (!projectExists(projectId)) {
            System.out.println("Project not found.");
            return;
        }

        // Update project contractor reference (always done)
        try (PreparedStatement upd = conn.prepareStatement("UPDATE projects SET contractor_id = ?, status='In Progress' WHERE project_id = ?")) {
            upd.setInt(1, stakeholderContractorId);
            upd.setInt(2, projectId);
            upd.executeUpdate();
        }

        // Try to insert into contractors table. If contractor_id primary key already exists,
        // update its project_id to reflect new assignment.
        try {
            try (PreparedStatement ins = conn.prepareStatement("INSERT INTO contractors (contractor_id, contractor_name, project_id, status) VALUES (?, (SELECT name FROM stakeholders WHERE id=?), ?, 'Assigned')")) {
                ins.setInt(1, stakeholderContractorId);
                ins.setInt(2, stakeholderContractorId);
                ins.setInt(3, projectId);
                ins.executeUpdate();
                System.out.println("Contractor record created and assigned.");
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            // existing contractor_id in contractors table - update project assignment
            try (PreparedStatement upd = conn.prepareStatement("UPDATE contractors SET project_id = ?, status='Assigned', contractor_name = (SELECT name FROM stakeholders WHERE id=?) WHERE contractor_id = ?")) {
                upd.setInt(1, projectId);
                upd.setInt(2, stakeholderContractorId);
                upd.setInt(3, stakeholderContractorId);
                upd.executeUpdate();
                System.out.println("Existing contractor entry updated with new project assignment.");
            }
        }

        System.out.println("Contractor (stakeholder id " + stakeholderContractorId + ") assigned to project " + projectId);
    }

    static void requestFunds(int contractorId) throws Exception {
        System.out.print("Enter Project ID: ");
        int pid = safeNextInt(); sc.nextLine();
        Integer assigned = getProjectContractorId(pid);
        if (assigned == null || assigned != contractorId) {
            System.out.println("You are not the assigned contractor for this project (or project missing).");
            return;
        }
        System.out.print("Enter Amount: ");
        double amt = Double.parseDouble(sc.nextLine());

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO funding_requests (project_id, contractor_id, amount, status) VALUES (?, ?, ?, 'Pending')")) {
            ps.setInt(1, pid);
            ps.setInt(2, contractorId);
            ps.setDouble(3, amt);
            ps.executeUpdate();
            System.out.println("Funding request submitted.");
        }
    }

    static void completeMilestoneFlow(int contractorId) throws Exception {
        System.out.print("Enter Project ID: ");
        int pid = safeNextInt(); sc.nextLine();
        Integer assigned = getProjectContractorId(pid);
        if (assigned == null || assigned != contractorId) {
            System.out.println("You are not assigned to this project or project doesn't exist.");
            return;
        }
        // list pending milestones
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT milestone_id, description FROM milestones WHERE project_id = ? AND is_completed = FALSE")) {
            ps.setInt(1, pid);
            try (ResultSet rs = ps.executeQuery()) {
                int i = 1;
                while (rs.next()) {
                    ids.add(rs.getInt("milestone_id"));
                    System.out.println(i + ". " + rs.getString("description") + " (id=" + rs.getInt("milestone_id") + ")");
                    i++;
                }
            }
        }
        if (ids.isEmpty()) {
            System.out.println("No pending milestones.");
            return;
        }
        System.out.print("Pick milestone number to mark completed: ");
        int pick = safeNextInt(); sc.nextLine();
        if (pick < 1 || pick > ids.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        int milestoneId = ids.get(pick - 1);
        try (PreparedStatement up = conn.prepareStatement("UPDATE milestones SET is_completed = TRUE WHERE milestone_id = ?")) {
            up.setInt(1, milestoneId);
            up.executeUpdate();
            System.out.println("✅ Milestone marked completed.");
        }
    }

    static void fileComplaint(int citizenId) throws Exception {
        System.out.print("Enter Project ID: ");
        int pid = safeNextInt(); sc.nextLine();
        if (!projectExists(pid)) {
            System.out.println("Project not found.");
            return;
        }
        System.out.print("Enter Complaint: ");
        String complaint = sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO complaints (project_id, citizen_id, complaint) VALUES (?, ?, ?)")) {
            ps.setInt(1, pid);
            ps.setInt(2, citizenId);
            ps.setString(3, complaint);
            ps.executeUpdate();
            System.out.println("✅ Complaint filed successfully!");
        }
    }

    static void viewProjects() throws Exception {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT project_id, project_name, status, contractor_id FROM projects")) {
            while (rs.next()) {
                System.out.println(rs.getInt("project_id") + ". " + rs.getString("project_name")
                        + " | " + rs.getString("status") + " | Contractor ID: " + rs.getInt("contractor_id"));
            }
        }
    }

    static void viewProjectsByContractor(int contractorId) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement("SELECT project_id, project_name, status FROM projects WHERE contractor_id = ?")) {
            ps.setInt(1, contractorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt("project_id") + ". " + rs.getString("project_name")
                            + " | " + rs.getString("status"));
                }
            }
        }
    }

    static void auditProject() throws Exception {
        System.out.print("Enter Project ID: ");
        int pid = safeNextInt(); sc.nextLine();
        if (!projectExists(pid)) {
            System.out.println("Project not found.");
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement("UPDATE projects SET status='Completed' WHERE project_id = ?")) {
            ps.setInt(1, pid);
            ps.executeUpdate();
            System.out.println("Project marked as completed.");
        }
    }
}

