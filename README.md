Ghost Infrastructure Tracker
Overview
The Ghost Infrastructure Tracker is a Java + MySQL-based infrastructure management system designed to enhance transparency, accountability, and progress tracking of public infrastructure projects.
It provides a Swing GUI for easy interaction between Authorities, Contractors, and Citizens, ensuring all project stages — from approval to completion — are monitored and recorded digitally.
Objectives
* To provide a digital platform for monitoring and managing infrastructure projects.
* To enhance communication between authorities, contractors, and citizens.
* To ensure transparency and reduce cases of “ghost projects.”
* To maintain secure, persistent records in a MySQL database.
* To simplify milestone tracking, fund requests, and complaint handling.
Work Flow

Features
Authority
* Approve and register new infrastructure projects.
* Assign contractors to approved projects.
* Audit completed projects and mark them as finished.
* View all projects and their progress status.
Contractor
* Request project-related funds.
* Update or complete assigned milestones.
* View all assigned projects and their statuses.
Citizen
* View ongoing and completed projects.
* File complaints about delayed or poor-quality projects.




Database

Technologies Used
* Programming Language: Java
* Database: MySQL
* GUI Framework: Java Swing
* JDBC Driver: MySQL Connector/J
How to Run
* Create a database named infrastructure_tracker.
* Execute the SQL scripts to create the required tables.
* Add MySQL Connector/J JAR file to your project classpath.
* Place all Java files (Main.java, InfrastructureTrackerGUI.java, LoginFrame.java, AuthorityDashboard.java, ContractorDashboard.java, CitizenDashboard.java) in the same src folder or package.
* Update your MySQL username and password in the code.
* Run the project by executing InfrastructureTrackerGUI.java.

