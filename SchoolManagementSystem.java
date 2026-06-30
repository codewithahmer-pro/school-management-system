import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class SchoolManagementSystem extends JFrame {
    
    // Theme Colors
    private boolean isDarkMode = true;
    private Color bgPrimary = new Color(30, 30, 30);
    private Color bgSecondary = new Color(45, 45, 45);
    private Color accentColor = new Color(0, 150, 136); // Teal Accent
    private Color textPrimary = Color.WHITE;
    private Color textSecondary = new Color(200, 200, 200);

    // Cards for switching screens
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Components that need theme updates
    private JPanel sidebar, dashboardPanel, studentPanel, coursePanel, feePanel, resultPanel;
    private JLabel lblTotalStudents, lblTotalCourses, lblTotalFees;
    private DefaultTableModel studentTableModel;
    private JTable studentTable;
    private JTextField txtSearch;

    public SchoolManagementSystem() {
        setTitle("Professional School Management System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add Screens
        mainPanel.add(createLoginScreen(), "Login");
        mainPanel.add(createMainAppScreen(), "App");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    // ==========================================
    // 1. LOGIN SYSTEM
    // ==========================================
    private JPanel createLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgPrimary);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(accentColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(textPrimary);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblUser, gbc);

        JTextField txtUser = new JTextField(15);
        txtUser.setBackground(bgSecondary);
        txtUser.setForeground(textPrimary);
        txtUser.setCaretColor(textPrimary);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtUser, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(textPrimary);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(15);
        txtPass.setBackground(bgSecondary);
        txtPass.setForeground(textPrimary);
        txtPass.setCaretColor(textPrimary);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtPass, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(accentColor);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());

            // Simple Validation
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else if (username.equals("admin") && password.equals("admin123")) {
                cardLayout.show(mainPanel, "App");
            } else {
                JOptionPane.showMessageDialog(panel, "Invalid Credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // ==========================================
    // MAIN APP LAYOUT (Sidebar + Content)
    // ==========================================
    private JPanel createMainAppScreen() {
        JPanel appPanel = new JPanel(new BorderLayout());

        // Sidebar
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(bgSecondary);
        sidebar.setPreferredSize(new Dimension(220, 700));

        JLabel lblBrand = new JLabel("  EduManager");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(accentColor);
        lblBrand.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));
        sidebar.add(lblBrand);

        String[] navItems = {"Dashboard", "Students", "Courses", "Fees", "Results"};
        JPanel contentCards = new JPanel(new CardLayout());

        // Initialize Module Panels
        dashboardPanel = createDashboardPanel();
        studentPanel = createStudentModule();
        coursePanel = createCourseModule();
        feePanel = createFeeModule();
        resultPanel = createResultModule();

        contentCards.add(dashboardPanel, "Dashboard");
        contentCards.add(studentPanel, "Students");
        contentCards.add(coursePanel, "Courses");
        contentCards.add(feePanel, "Fees");
        contentCards.add(resultPanel, "Results");

        for (String item : navItems) {
            JButton btnNav = new JButton("  " + item);
            btnNav.setMaximumSize(new Dimension(220, 45));
            btnNav.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            btnNav.setBackground(bgSecondary);
            btnNav.setForeground(textSecondary);
            btnNav.setBorderPainted(false);
            btnNav.setFocusPainted(false);
            btnNav.setHorizontalAlignment(SwingConstants.LEFT);

            btnNav.addActionListener(e -> {
                CardLayout cl = (CardLayout) contentCards.getLayout();
                cl.show(contentCards, item);
            });
            sidebar.add(btnNav);
        }

        sidebar.add(Box.createVerticalGlue());

        // Extra Feature: Dark Mode Toggle Button
        JButton btnToggleTheme = new JButton(" ◐ Toggle Theme");
        btnToggleTheme.setMaximumSize(new Dimension(220, 45));
        btnToggleTheme.setBackground(bgSecondary);
        btnToggleTheme.setForeground(accentColor);
        btnToggleTheme.setBorderPainted(false);
        btnToggleTheme.addActionListener(e -> toggleTheme());
        sidebar.add(btnToggleTheme);

        appPanel.add(sidebar, BorderLayout.WEST);
        appPanel.add(contentCards, BorderLayout.CENTER);

        return appPanel;
    }

    // ==========================================
    // 2. DASHBOARD MODULE
    // ==========================================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgPrimary);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Header
        JLabel lblHeader = new JLabel("Dashboard Overview");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeader.setForeground(textPrimary);
        panel.add(lblHeader, BorderLayout.NORTH);

        // Stats Cards Grid
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setBackground(bgPrimary);
        statsGrid.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        lblTotalStudents = createStatCard("Total Students", "1,240", new Color(33, 150, 243), statsGrid);
        lblTotalCourses = createStatCard("Total Courses", "18", new Color(76, 175, 80), statsGrid);
        lblTotalFees = createStatCard("Total Fees Collected", "$45,200", new Color(255, 152, 0), statsGrid);

        panel.add(statsGrid, BorderLayout.CENTER);

        // Recent Students Section
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(bgSecondary);
        recentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor), "Recent Activity", 0, 0, null, textPrimary));
        
        String[] columns = {"ID", "Name", "Action", "Time"};
        String[][] data = {
            {"101", "Ali Khan", "Enrolled", "Just Now"},
            {"102", "Zainab Fatima", "Fee Paid", "5 mins ago"},
            {"103", "Hamza Ahmed", "Course Assigned", "1 hour ago"}
        };
        JTable recentTable = new JTable(data, columns);
        recentTable.setBackground(bgSecondary);
        recentTable.setForeground(textPrimary);
        recentPanel.add(new JScrollPane(recentTable), BorderLayout.CENTER);

        panel.add(recentPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JLabel createStatCard(String title, String value, Color accent, JPanel parent) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(bgSecondary);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, accent));

        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setForeground(textSecondary);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblVal = new JLabel("  " + value);
        lblVal.setForeground(textPrimary);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 22));

        card.add(lblTitle);
        card.add(lblVal);
        parent.add(card);
        return lblVal;
    }

    // ==========================================
    // 3. STUDENT MODULE (CRUD + Search + Sort)
    // ==========================================
    private JPanel createStudentModule() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgPrimary);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Action Bar
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBackground(bgPrimary);

        JLabel lblTitle = new JLabel("Student Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(textPrimary);
        actionBar.add(lblTitle, BorderLayout.WEST);

        // Search Bar Implementation
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBox.setBackground(bgPrimary);
        JLabel lblSearch = new JLabel("🔍 Search: ");
        lblSearch.setForeground(textPrimary);
        txtSearch = new JTextField(15);
        txtSearch.setBackground(bgSecondary);
        txtSearch.setForeground(textPrimary);
        searchBox.add(lblSearch);
        searchBox.add(txtSearch);
        actionBar.add(searchBox, BorderLayout.EAST);

        panel.add(actionBar, BorderLayout.NORTH);

        // Student Data Table
        String[] columns = {"Roll No", "Name", "Age", "Class", "Contact"};
        studentTableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(studentTableModel);
        studentTable.setBackground(bgSecondary);
        studentTable.setForeground(textPrimary);
        
        // Table Sorting Feature
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(studentTableModel);
        studentTable.setRowSorter(sorter);
        
        txtSearch.addByKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txtSearch.getText()));
            }
        });

        // Pre-populate data
        studentTableModel.addRow(new Object[]{"01", "Ahmed Ali", "20", "BSCS", "03001234567"});
        studentTableModel.addRow(new Object[]{"02", "Ayesha Sana", "21", "BBA", "03217654321"});

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // CRUD Form & Buttons
        JPanel crudPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        crudPanel.setBackground(bgPrimary);
        crudPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JTextField txtRoll = new JTextField(); txtRoll.setBorder(BorderFactory.createTitledBorder("Roll No"));
        JTextField txtName = new JTextField(); txtName.setBorder(BorderFactory.createTitledBorder("Name"));
        JTextField txtAge = new JTextField(); txtAge.setBorder(BorderFactory.createTitledBorder("Age"));
        JTextField txtClass = new JTextField(); txtClass.setBorder(BorderFactory.createTitledBorder("Class"));

        crudPanel.add(txtRoll); crudPanel.add(txtName); crudPanel.add(txtAge); crudPanel.add(txtClass);

        JButton btnAdd = new JButton("➕ Add");
        JButton btnUpdate = new JButton("🔄 Update");
        JButton btnDelete = new JButton("❌ Delete");
        
        // Export Feature Button
        JButton btnExport = new JButton("📄 Export Report");

        crudPanel.add(btnAdd); crudPanel.add(btnUpdate); crudPanel.add(btnDelete); crudPanel.add(btnExport);
        panel.add(crudPanel, BorderLayout.SOUTH);

        // CRUD Functionalities & Validation
        btnAdd.addActionListener(e -> {
            if(txtRoll.getText().isEmpty() || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Roll No and Name are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            } else {
                studentTableModel.addRow(new Object[]{txtRoll.getText(), txtName.getText(), txtAge.getText(), txtClass.getText(), "-"});
                lblTotalStudents.setText(String.valueOf(studentTableModel.getRowCount() + 1238)); // Update Counter
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if(selectedRow >= 0) {
                studentTable.setValueAt(txtRoll.getText(), selectedRow, 0);
                studentTable.setValueAt(txtName.getText(), selectedRow, 1);
                studentTable.setValueAt(txtAge.getText(), selectedRow, 2);
                studentTable.setValueAt(txtClass.getText(), selectedRow, 3);
            } else {
                JOptionPane.showMessageDialog(panel, "Select a row to update.");
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if(selectedRow >= 0) {
                studentTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(panel, "Select a row to delete.");
            }
        });

        // Extra Feature: Export Data to Text/CSV format simulating PDF Export
        btnExport.addActionListener(e -> {
            try (FileWriter writer = new FileWriter("Student_Report.txt")) {
                writer.write("=== STUDENT REPORT ===\n\n");
                for (int i = 0; i < studentTableModel.getRowCount(); i++) {
                    writer.write(studentTableModel.getValueAt(i, 0) + " - " + studentTableModel.getValueAt(i, 1) + "\n");
                }
                JOptionPane.showMessageDialog(panel, "Report successfully exported to 'Student_Report.txt'!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return panel;
    }

    // ==========================================
    // 4. COURSE MODULE
    // ==========================================
    private JPanel createCourseModule() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgPrimary);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Course Management Panel", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(textPrimary);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        JTextField txtCourseName = new JTextField(15);
        txtCourseName.setBorder(BorderFactory.createTitledBorder("Course Name"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(txtCourseName, gbc);

        JTextField txtCourseCode = new JTextField(15);
        txtCourseCode.setBorder(BorderFactory.createTitledBorder("Course Code"));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtCourseCode, gbc);

        JButton btnAddCourse = new JButton("📖 Add Course");
        btnAddCourse.setBackground(accentColor);
        btnAddCourse.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnAddCourse, gbc);

        // Assign Course Sub-section
        JComboBox<String> cmbStudents = new JComboBox<>(new String[]{"Ahmed Ali (01)", "Ayesha Sana (02)"});
        cmbStudents.setBorder(BorderFactory.createTitledBorder("Select Student"));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(cmbStudents, gbc);

        JComboBox<String> cmbCourses = new JComboBox<>(new String[]{"Object Oriented Programming", "Discrete Mathematics", "Data Structures"});
        cmbCourses.setBorder(BorderFactory.createTitledBorder("Select Course"));
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(cmbCourses, gbc);

        JButton btnAssign = new JButton("🔗 Assign Course to Student");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(btnAssign, gbc);

        btnAddCourse.addActionListener(e -> JOptionPane.showMessageDialog(panel, "New Course Added Successfully!"));
        btnAssign.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Course Successfully Assigned!"));

        return panel;
    }

    // ==========================================
    // 5. FEE MODULE & RECEIPT PRINT
    // ==========================================
    private JPanel createFeeModule() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(bgPrimary);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Fee Status & Billing");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(textPrimary);
        panel.add(lblTitle, BorderLayout.NORTH);

        // Fee Table
        String[] columns = {"Roll No", "Student Name", "Total Fee", "Status"};
        Object[][] data = {
            {"01", "Ahmed Ali", "$1500", "Paid"},
            {"02", "Ayesha Sana", "$1500", "Unpaid"}
        };
        JTable feeTable = new JTable(new DefaultTableModel(data, columns));
        feeTable.setBackground(bgSecondary);
        feeTable.setForeground(textPrimary);
        panel.add(new JScrollPane(feeTable), BorderLayout.CENTER);

        // Sidebar Actions for Fee Receipt Printing
        JPanel sideActions = new JPanel(new GridLayout(3, 1, 10, 10));
        sideActions.setBackground(bgPrimary);
        
        JButton btnMarkPaid = new JButton("✓ Mark as Paid");
        JButton btnPrintReceipt = new JButton("🖨️ Print Receipt");
        
        sideActions.add(btnMarkPaid);
        sideActions.add(btnPrintReceipt);
        panel.add(sideActions, BorderLayout.EAST);

        // Print Receipt Feature Action
        btnPrintReceipt.addActionListener(e -> {
            String receipt = "===============================\n" +
                             "       CASH RECEIPT           \n" +
                             "===============================\n" +
                             "Student: Ahmed Ali\n" +
                             "Roll No: 01\n" +
                             "Amount Paid: $1500\n" +
                             "Status: SUCCESS / PAID\n" +
                             "===============================";
            JOptionPane.showMessageDialog(panel, receipt, "Print Job Emulation", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    // ==========================================
    // 6. RESULT MODULE (Marks, Grade, GPA)
    // ==========================================
    private JPanel createResultModule() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgPrimary);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Exam Performance & Results");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(textPrimary);
        panel.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Roll No", "Subject", "Marks", "Grade", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        model.addRow(new Object[]{"01", "Java OOP", "88", "A", "3.8"});
        model.addRow(new Object[]{"01", "Discrete Math", "92", "A+", "4.0"});
        model.addRow(new Object[]{"02", "Data Structures", "74", "B", "3.0"});

        JTable resTable = new JTable(model);
        resTable.setBackground(bgSecondary);
        resTable.setForeground(textPrimary);
        panel.add(new JScrollPane(resTable), BorderLayout.CENTER);

        // Quick Grading Calculator Input Form
        JPanel calcPanel = new JPanel(new FlowLayout());
        calcPanel.setBackground(bgSecondary);
        calcPanel.setBorder(BorderFactory.createTitledBorder(null, "Quick Marks Entry", 0, 0, null, textPrimary));

        JTextField txtMks = new JTextField(5);
        txtMks.setBorder(BorderFactory.createTitledBorder("Marks"));
        JButton btnCalc = new JButton("Calculate Grade");

        calcPanel.add(txtMks);
        calcPanel.add(btnCalc);
        panel.add(calcPanel, BorderLayout.SOUTH);

        btnCalc.addActionListener(e -> {
            try {
                int marks = Integer.parseInt(txtMks.getText());
                String grade; double gpa;
                if(marks >= 90) { grade = "A+"; gpa = 4.0; }
                else if(marks >= 80) { grade = "A"; gpa = 3.7; }
                else if(marks >= 70) { grade = "B"; gpa = 3.0; }
                else { grade = "F"; gpa = 0.0; }

                JOptionPane.showMessageDialog(panel, "Calculated Outcome:\nGrade: " + grade + "\nGPA: " + gpa);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please input clean numeric marks values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // ==========================================
    // THEME SWITCHING (DARK MODE / LIGHT MODE)
    // ==========================================
    private void toggleTheme() {
        if (isDarkMode) {
            // Switch to Professional Light Slate Mode
            bgPrimary = new Color(240, 242, 245);
            bgSecondary = Color.WHITE;
            textPrimary = Color.BLACK;
            textSecondary = new Color(70, 70, 70);
        } else {
            // Switch back to Dark Slate Mode
            bgPrimary = new Color(30, 30, 30);
            bgSecondary = new Color(45, 45, 45);
            textPrimary = Color.WHITE;
            textSecondary = new Color(200, 200, 200);
        }
        isDarkMode = !isDarkMode;

        // Apply Theme Changes dynamically to parent view containers
        sidebar.setBackground(bgSecondary);
        dashboardPanel.setBackground(bgPrimary);
        studentPanel.setBackground(bgPrimary);
        coursePanel.setBackground(bgPrimary);
        feePanel.setBackground(bgPrimary);
        resultPanel.setBackground(bgPrimary);
        
        studentTable.setBackground(bgSecondary);
        studentTable.setForeground(textPrimary);
        txtSearch.setBackground(bgSecondary);
        txtSearch.setForeground(textPrimary);

        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void main(String[] args) {
        // Run application on standard Swing Thread window stack
        SwingUtilities.invokeLater(() -> {
            new SchoolManagementSystem().setVisible(true);
        });
    }
}
