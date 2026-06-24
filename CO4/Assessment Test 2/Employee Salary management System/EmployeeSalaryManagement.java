import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// ── Employee class ────────────────────────────────────────────────────────────
class Employee {
    private int    empId;
    private String empName;
    private String department;
    private double basicSalary;

    private static final double DEFAULT_BASIC = 25000.0;

    // Method 1: Read employee details
    public void readDetails(int id, String name, String dept, double basic) {
        this.empId      = id;
        this.empName    = name;
        this.department = dept;
        this.basicSalary = basic;
    }

    // Method 2: Calculate HRA (20% of basic)
    public double calculateHRA() { return basicSalary * 0.20; }

    // Method 3: Calculate DA (10% of basic)
    public double calculateDA()  { return basicSalary * 0.10; }

    // Method 4: Display gross salary (basic + HRA + DA)
    public double grossSalary()  { return basicSalary + calculateHRA() + calculateDA(); }

    public String displayDetails() {
        return String.format(
            "Emp ID      : %d\n" +
            "Name        : %s\n" +
            "Department  : %s\n" +
            "Basic Salary: ₹%.2f\n" +
            "HRA (20%%)  : ₹%.2f\n" +
            "DA  (10%%)  : ₹%.2f\n" +
            "─────────────────────\n" +
            "Gross Salary: ₹%.2f",
            empId, empName, department,
            basicSalary, calculateHRA(), calculateDA(), grossSalary()
        );
    }

    // Getters
    public int    getEmpId()       { return empId; }
    public String getEmpName()     { return empName; }
    public String getDepartment()  { return department; }
    public double getBasicSalary() { return basicSalary; }
    public static double getDefaultBasic() { return DEFAULT_BASIC; }
}

// ── Main GUI ──────────────────────────────────────────────────────────────────
public class EmployeeSalaryManagement extends JFrame implements ActionListener {

    // AWT TextField for inputs
    private TextField tfId, tfName, tfDept, tfBasic;

    // Swing output labels
    private JLabel lblHRA, lblDA, lblGross, lblStatus;

    // Breakdown cards
    private JPanel cardHRA, cardDA, cardGross;

    private JButton btnLoad, btnCalcHRA, btnCalcDA, btnDisplay, btnReset;
    private JTextArea taLog;
    private Employee emp;
    private int logCount = 0;

    // Palette
    private static final Color C_BG     = new Color(245, 247, 250);
    private static final Color C_PANEL  = Color.WHITE;
    private static final Color C_HEAD   = new Color(30, 80, 40);
    private static final Color C_GREEN  = new Color(45, 120, 55);
    private static final Color C_BLUE   = new Color(24, 95, 165);
    private static final Color C_AMBER  = new Color(160, 100, 10);
    private static final Color C_PURPLE = new Color(90, 60, 160);
    private static final Color C_RED    = new Color(163, 45, 45);
    private static final Color C_GRAY   = new Color(100, 100, 100);
    private static final Color C_BORDER = new Color(220, 220, 225);

    public EmployeeSalaryManagement() {
        setTitle("Employee Salary Management");
        setSize(640, 730);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        p.setBackground(C_HEAD);

        JLabel ico = new JLabel("👤");
        ico.setFont(new Font("Dialog", Font.PLAIN, 26));

        JLabel title = new JLabel("Employee Salary Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  AWT + Swing  |  HRA · DA · Gross");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(new Color(160, 210, 170));

        p.add(ico); p.add(title); p.add(sub);
        return p;
    }

    // ── Center ────────────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(12, 14, 10, 14));

        p.add(buildFormPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildButtonPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildSalaryBreakdown());
        p.add(Box.createVerticalStrut(10));
        p.add(buildLogPanel());
        return p;
    }

    // ── Form (AWT TextField + GridLayout) ────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = card("Employee Details");
        JPanel grid  = new JPanel(new GridLayout(4, 2, 10, 8));
        grid.setBackground(C_PANEL);

        tfId    = new TextField("1001");
        tfName  = new TextField("Rajesh Kumar");
        tfDept  = new TextField("Engineering");
        tfBasic = new TextField("25000");

        for (TextField tf : new TextField[]{tfId, tfName, tfDept, tfBasic})
            tf.setFont(new Font("SansSerif", Font.PLAIN, 13));

        grid.add(lbl("Employee ID"));   grid.add(tfId);
        grid.add(lbl("Employee Name")); grid.add(tfName);
        grid.add(lbl("Department"));    grid.add(tfDept);
        grid.add(lbl("Basic Salary ₹")); grid.add(tfBasic);

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    // ── Buttons (FlowLayout) ──────────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setBackground(C_BG);

        btnLoad    = mkBtn("Load Employee", C_GREEN,  Color.WHITE);
        btnCalcHRA = mkBtn("Calc HRA",      C_BLUE,   Color.WHITE);
        btnCalcDA  = mkBtn("Calc DA",        C_AMBER,  Color.WHITE);
        btnDisplay = mkBtn("Gross Salary",  C_PURPLE, Color.WHITE);
        btnReset   = mkBtn("Reset",          C_RED,    Color.WHITE);

        btnCalcHRA.setEnabled(false);
        btnCalcDA.setEnabled(false);
        btnDisplay.setEnabled(false);

        for (JButton b : new JButton[]{btnLoad, btnCalcHRA, btnCalcDA, btnDisplay, btnReset})
            b.addActionListener(this);

        p.add(btnLoad); p.add(btnCalcHRA); p.add(btnCalcDA); p.add(btnDisplay); p.add(btnReset);
        return p;
    }

    // ── Salary Breakdown Cards (GridLayout 1×3) ───────────────────────────────
    private JPanel buildSalaryBreakdown() {
        JPanel outer = card("Salary Breakdown");
        JPanel row   = new JPanel(new GridLayout(1, 3, 12, 0));
        row.setBackground(C_PANEL);

        lblHRA   = metricLabel("—");
        lblDA    = metricLabel("—");
        lblGross = metricLabel("—");
        lblGross.setForeground(C_GREEN);

        cardHRA   = metricCard("HRA  (20%)", lblHRA,  new Color(230, 240, 255));
        cardDA    = metricCard("DA   (10%)", lblDA,   new Color(255, 248, 225));
        cardGross = metricCard("Gross Salary", lblGross, new Color(230, 248, 232));

        row.add(cardHRA); row.add(cardDA); row.add(cardGross);
        outer.add(row, BorderLayout.CENTER);
        return outer;
    }

    // ── Log ───────────────────────────────────────────────────────────────────
    private JPanel buildLogPanel() {
        JPanel outer = card("Activity Log");
        taLog = new JTextArea(7, 0);
        taLog.setEditable(false);
        taLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        taLog.setBackground(new Color(248, 248, 252));
        taLog.setForeground(new Color(40, 40, 60));
        taLog.setMargin(new Insets(6, 8, 6, 8));
        JScrollPane sp = new JScrollPane(taLog);
        sp.setBorder(new LineBorder(C_BORDER));
        outer.add(sp, BorderLayout.CENTER);
        return outer;
    }

    // ── Status Bar ────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        p.setBackground(new Color(230, 235, 230));
        p.setBorder(new MatteBorder(1, 0, 0, 0, C_BORDER));
        lblStatus = new JLabel("Ready — load an employee to begin.");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblStatus.setForeground(C_GRAY);
        p.add(lblStatus);
        return p;
    }

    // ── Event Handling ────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if      (src == btnLoad)    handleLoad();
        else if (src == btnCalcHRA) handleHRA();
        else if (src == btnCalcDA)  handleDA();
        else if (src == btnDisplay) handleGross();
        else if (src == btnReset)   handleReset();
    }

    private void handleLoad() {
        String idStr  = tfId.getText().trim();
        String name   = tfName.getText().trim();
        String dept   = tfDept.getText().trim();
        String bStr   = tfBasic.getText().trim();

        if (idStr.isEmpty() || name.isEmpty() || dept.isEmpty() || bStr.isEmpty()) {
            showWarn("All fields are required."); return;
        }
        int id; double basic;
        try { id    = Integer.parseInt(idStr.replaceAll("[^0-9]", "")); }
        catch (NumberFormatException ex) { showWarn("Employee ID must be numeric."); return; }
        try { basic = Double.parseDouble(bStr); if (basic <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { showWarn("Basic salary must be a positive number."); return; }

        emp = new Employee();
        emp.readDetails(id, name, dept, basic);   // Method 1

        // Reset breakdown display
        lblHRA.setText("—"); lblDA.setText("—"); lblGross.setText("—");
        lblHRA.setForeground(C_GRAY); lblDA.setForeground(C_GRAY);
        lblGross.setForeground(C_GREEN);

        btnCalcHRA.setEnabled(true);
        btnCalcDA.setEnabled(true);
        btnDisplay.setEnabled(true);

        log(String.format("LOADED  | %s  [%s]  Basic: ₹%.2f", name, dept, basic));
        setStatus("Loaded: " + name + " | Dept: " + dept + " | Basic: ₹" + String.format("%.2f", basic));
    }

    private void handleHRA() {
        if (emp == null) return;
        double hra = emp.calculateHRA();   // Method 2
        lblHRA.setText("₹" + String.format("%,.2f", hra));
        lblHRA.setForeground(C_BLUE);
        log(String.format("HRA     | 20%% of ₹%.2f = ₹%.2f", emp.getBasicSalary(), hra));
        setStatus("HRA calculated: ₹" + String.format("%.2f", hra));
    }

    private void handleDA() {
        if (emp == null) return;
        double da = emp.calculateDA();   // Method 3
        lblDA.setText("₹" + String.format("%,.2f", da));
        lblDA.setForeground(C_AMBER);
        log(String.format("DA      | 10%% of ₹%.2f = ₹%.2f", emp.getBasicSalary(), da));
        setStatus("DA calculated: ₹" + String.format("%.2f", da));
    }

    private void handleGross() {
        if (emp == null) return;
        // Ensure HRA & DA are shown
        lblHRA.setText("₹" + String.format("%,.2f", emp.calculateHRA()));
        lblHRA.setForeground(C_BLUE);
        lblDA.setText("₹"  + String.format("%,.2f", emp.calculateDA()));
        lblDA.setForeground(C_AMBER);

        double gross = emp.grossSalary();   // Method 4
        lblGross.setText("₹" + String.format("%,.2f", gross));
        lblGross.setForeground(C_GREEN);

        log("DETAILS |\n" + emp.displayDetails());
        JOptionPane.showMessageDialog(this, emp.displayDetails(), "Salary Details — " + emp.getEmpName(),
                JOptionPane.INFORMATION_MESSAGE);
        setStatus("Gross salary for " + emp.getEmpName() + ": ₹" + String.format("%.2f", gross));
    }

    private void handleReset() {
        emp = null;
        tfId.setText("1001");
        tfName.setText("Rajesh Kumar");
        tfDept.setText("Engineering");
        tfBasic.setText("25000");
        lblHRA.setText("—"); lblDA.setText("—"); lblGross.setText("—");
        lblHRA.setForeground(C_GRAY); lblDA.setForeground(C_GRAY);
        taLog.setText(""); logCount = 0;
        btnCalcHRA.setEnabled(false);
        btnCalcDA.setEnabled(false);
        btnDisplay.setEnabled(false);
        setStatus("Reset — load an employee to begin.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void log(String msg) {
        logCount++;
        taLog.append(String.format("[%02d] %s%n", logCount, msg));
        taLog.setCaretPosition(taLog.getDocument().getLength());
    }
    private void setStatus(String s)      { lblStatus.setText(s); }
    private void showWarn(String s)       { JOptionPane.showMessageDialog(this, s, "Input Error", JOptionPane.WARNING_MESSAGE); }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(C_GRAY);
        return l;
    }

    private JLabel metricLabel(String t) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 18));
        l.setForeground(C_GRAY);
        return l;
    }

    private JPanel metricCard(String title, JLabel val, Color bg) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(C_BORDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(C_GRAY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private JButton mkBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(C_BORDER, 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(C_GRAY);
        lbl.setBorder(new MatteBorder(0, 0, 1, 0, C_BORDER));
        lbl.setPreferredSize(new Dimension(0, 26));
        p.add(lbl, BorderLayout.NORTH);
        return p;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(EmployeeSalaryManagement::new);
    }
}
