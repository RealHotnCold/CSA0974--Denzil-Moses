import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// ── Student class ─────────────────────────────────────────────────────────────
class Student {
    private int    studentId;
    private String studentName;
    private String course;
    private double feeBalance;

    private static final double TOTAL_FEE = 50000.0;

    // Method 1: Read student details
    public void readDetails(int id, String name, String course) {
        this.studentId   = id;
        this.studentName = name;
        this.course      = course;
        this.feeBalance  = TOTAL_FEE;
    }

    // Method 2: Pay fee amount
    public String payFee(double amount) {
        if (amount <= 0)           return "ERROR: Amount must be positive.";
        if (amount > feeBalance)   return "ERROR: Amount exceeds balance of ₹" + String.format("%.2f", feeBalance);
        feeBalance -= amount;
        return "OK";
    }

    // Method 3: Check remaining fee balance
    public double checkBalance() { return feeBalance; }

    // Method 4: Display student details
    public String displayDetails() {
        double paid = TOTAL_FEE - feeBalance;
        return String.format(
            "Student ID  : %d\n" +
            "Name        : %s\n" +
            "Course      : %s\n" +
            "Total Fee   : ₹%.2f\n" +
            "Paid So Far : ₹%.2f\n" +
            "─────────────────────\n" +
            "Balance Due : ₹%.2f",
            studentId, studentName, course, TOTAL_FEE, paid, feeBalance
        );
    }

    // Getters
    public int    getStudentId()   { return studentId; }
    public String getStudentName() { return studentName; }
    public String getCourse()      { return course; }
    public double getFeeBalance()  { return feeBalance; }
    public double getPaid()        { return TOTAL_FEE - feeBalance; }
    public static double getTotalFee() { return TOTAL_FEE; }
}

// ── Main GUI ──────────────────────────────────────────────────────────────────
public class StudentFeeManagement extends JFrame implements ActionListener {

    // AWT TextFields
    private TextField tfId, tfName, tfCourse, tfPayAmt;

    // Output labels
    private JLabel lblPaid, lblBalance, lblStatus;
    private JProgressBar pbFee;

    private JButton btnLoad, btnPay, btnBalance, btnDisplay, btnReset;
    private JTextArea taLog;
    private Student student;
    private int logCount = 0;

    // Palette
    private static final Color C_BG     = new Color(245, 247, 252);
    private static final Color C_PANEL  = Color.WHITE;
    private static final Color C_HEAD   = new Color(25, 60, 130);
    private static final Color C_BLUE   = new Color(24, 95, 165);
    private static final Color C_GREEN  = new Color(45, 120, 55);
    private static final Color C_AMBER  = new Color(160, 100, 10);
    private static final Color C_RED    = new Color(163, 45, 45);
    private static final Color C_PURPLE = new Color(90, 60, 160);
    private static final Color C_GRAY   = new Color(100, 100, 100);
    private static final Color C_BORDER = new Color(220, 220, 228);

    public StudentFeeManagement() {
        setTitle("Student Fee Management System");
        setSize(640, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout());

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        p.setBackground(C_HEAD);

        JLabel ico = new JLabel("🎓");
        ico.setFont(new Font("Dialog", Font.PLAIN, 26));

        JLabel title = new JLabel("Student Fee Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  AWT + Swing  |  Total Fee ₹50,000");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(new Color(170, 190, 230));

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
        p.add(buildPayPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildButtonPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildFeeBreakdown());
        p.add(Box.createVerticalStrut(10));
        p.add(buildLogPanel());
        return p;
    }

    // ── Form (AWT TextField + GridLayout) ────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = card("Student Details");
        JPanel grid  = new JPanel(new GridLayout(3, 2, 10, 8));
        grid.setBackground(C_PANEL);

        tfId     = new TextField("STU-001");
        tfName   = new TextField("Priya Sharma");
        tfCourse = new TextField("B.Tech CSE");

        for (TextField tf : new TextField[]{tfId, tfName, tfCourse})
            tf.setFont(new Font("SansSerif", Font.PLAIN, 13));

        grid.add(lbl("Student ID"));   grid.add(tfId);
        grid.add(lbl("Student Name")); grid.add(tfName);
        grid.add(lbl("Course"));       grid.add(tfCourse);

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    // ── Pay Fee row ───────────────────────────────────────────────────────────
    private JPanel buildPayPanel() {
        JPanel outer = card("Pay Fee");
        JPanel row   = new JPanel(new BorderLayout(10, 0));
        row.setBackground(C_PANEL);

        tfPayAmt = new TextField("10000");
        tfPayAmt.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel prefix = new JLabel("₹  Amount:");
        prefix.setFont(new Font("SansSerif", Font.PLAIN, 13));
        prefix.setForeground(C_GRAY);

        row.add(prefix,  BorderLayout.WEST);
        row.add(tfPayAmt, BorderLayout.CENTER);
        outer.add(row, BorderLayout.CENTER);
        return outer;
    }

    // ── Buttons (FlowLayout) ──────────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setBackground(C_BG);

        btnLoad    = mkBtn("Load Student",  C_BLUE,   Color.WHITE);
        btnPay     = mkBtn("Pay Fee",       C_GREEN,  Color.WHITE);
        btnBalance = mkBtn("Check Balance", C_AMBER,  Color.WHITE);
        btnDisplay = mkBtn("Display",       C_PURPLE, Color.WHITE);
        btnReset   = mkBtn("Reset",         C_RED,    Color.WHITE);

        btnPay.setEnabled(false);
        btnBalance.setEnabled(false);
        btnDisplay.setEnabled(false);

        for (JButton b : new JButton[]{btnLoad, btnPay, btnBalance, btnDisplay, btnReset})
            b.addActionListener(this);

        p.add(btnLoad); p.add(btnPay); p.add(btnBalance); p.add(btnDisplay); p.add(btnReset);
        return p;
    }

    // ── Fee Breakdown (2 metric cards + progress bar) ─────────────────────────
    private JPanel buildFeeBreakdown() {
        JPanel outer = card("Fee Overview");
        JPanel col   = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(C_PANEL);

        // Metric cards row
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setBackground(C_PANEL);

        lblPaid    = metricLabel("—");
        lblBalance = metricLabel("—");
        lblBalance.setForeground(C_RED);

        row.add(metricCard("Paid So Far",  lblPaid,    new Color(230, 248, 232)));
        row.add(metricCard("Balance Due",  lblBalance, new Color(255, 235, 235)));

        // Progress bar
        pbFee = new JProgressBar(0, 100);
        pbFee.setValue(0);
        pbFee.setStringPainted(true);
        pbFee.setString("Load a student first");
        pbFee.setForeground(C_BLUE);
        pbFee.setBackground(new Color(230, 235, 248));
        pbFee.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbFee.setPreferredSize(new Dimension(0, 26));
        pbFee.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        JLabel pbLbl = new JLabel("Payment Progress");
        pbLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbLbl.setForeground(C_GRAY);
        pbLbl.setBorder(new EmptyBorder(10, 0, 4, 0));

        col.add(row);
        col.add(pbLbl);
        col.add(pbFee);
        outer.add(col, BorderLayout.CENTER);
        return outer;
    }

    // ── Activity Log ──────────────────────────────────────────────────────────
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
        p.setBackground(new Color(228, 232, 245));
        p.setBorder(new MatteBorder(1, 0, 0, 0, C_BORDER));
        lblStatus = new JLabel("Ready — load a student to begin.");
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
        else if (src == btnPay)     handlePay();
        else if (src == btnBalance) handleBalance();
        else if (src == btnDisplay) handleDisplay();
        else if (src == btnReset)   handleReset();
    }

    private void handleLoad() {
        String idStr = tfId.getText().trim();
        String name  = tfName.getText().trim();
        String course= tfCourse.getText().trim();

        if (idStr.isEmpty() || name.isEmpty() || course.isEmpty()) {
            showWarn("All fields are required."); return;
        }
        int id;
        try { id = Integer.parseInt(idStr.replaceAll("[^0-9]", "")); }
        catch (NumberFormatException ex) { showWarn("Student ID must contain a number."); return; }

        student = new Student();
        student.readDetails(id, name, course);   // Method 1

        updateBreakdown();
        btnPay.setEnabled(true);
        btnBalance.setEnabled(true);
        btnDisplay.setEnabled(true);

        log(String.format("LOADED  | %s  [%s]  Balance: ₹%.2f", name, course, Student.getTotalFee()));
        setStatus("Loaded: " + name + "  |  Course: " + course + "  |  Balance: ₹50,000.00");
    }

    private void handlePay() {
        if (student == null) return;
        double amount;
        try {
            amount = Double.parseDouble(tfPayAmt.getText().trim());
        } catch (NumberFormatException ex) {
            showWarn("Enter a valid numeric amount."); return;
        }
        String result = student.payFee(amount);   // Method 2
        if (result.startsWith("ERROR")) {
            showWarn(result.replace("ERROR: ", ""));
            log("FAILED  | " + result);
        } else {
            updateBreakdown();
            log(String.format("PAID    | ₹%.2f  →  Balance: ₹%.2f", amount, student.checkBalance()));
            setStatus("Payment of ₹" + String.format("%.2f", amount) + " successful. Balance: ₹"
                    + String.format("%.2f", student.checkBalance()));
            if (student.checkBalance() == 0)
                JOptionPane.showMessageDialog(this, "🎉 All fees cleared!", "Fee Cleared", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleBalance() {
        if (student == null) return;
        double bal = student.checkBalance();   // Method 3
        log(String.format("BALANCE | ₹%.2f remaining for %s", bal, student.getStudentName()));
        setStatus("Balance for " + student.getStudentName() + ": ₹" + String.format("%.2f", bal));
        JOptionPane.showMessageDialog(this,
            "Student : " + student.getStudentName() +
            "\nCourse  : " + student.getCourse() +
            "\nBalance : ₹" + String.format("%.2f", bal),
            "Fee Balance", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleDisplay() {
        if (student == null) return;
        String details = student.displayDetails();   // Method 4
        log("DETAILS |\n" + details);
        JOptionPane.showMessageDialog(this, details, "Student Details — " + student.getStudentName(),
                JOptionPane.INFORMATION_MESSAGE);
        setStatus("Details displayed for " + student.getStudentName());
    }

    private void handleReset() {
        student = null;
        tfId.setText("STU-001"); tfName.setText("Priya Sharma");
        tfCourse.setText("B.Tech CSE"); tfPayAmt.setText("10000");
        lblPaid.setText("—"); lblBalance.setText("—");
        lblBalance.setForeground(C_RED);
        pbFee.setValue(0); pbFee.setString("Load a student first");
        taLog.setText(""); logCount = 0;
        btnPay.setEnabled(false); btnBalance.setEnabled(false); btnDisplay.setEnabled(false);
        setStatus("Reset — load a student to begin.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void updateBreakdown() {
        if (student == null) return;
        double paid  = student.getPaid();
        double bal   = student.checkBalance();
        double total = Student.getTotalFee();
        int    pct   = (int) Math.round((paid / total) * 100);

        lblPaid.setText("₹" + String.format("%,.2f", paid));
        lblPaid.setForeground(C_GREEN);
        lblBalance.setText("₹" + String.format("%,.2f", bal));
        lblBalance.setForeground(bal == 0 ? C_GREEN : C_RED);

        pbFee.setValue(pct);
        pbFee.setString(pct + "% paid  (₹" + String.format("%,.2f", paid) + " of ₹50,000)");
        if      (pct == 100) pbFee.setForeground(C_GREEN);
        else if (pct >= 50)  pbFee.setForeground(C_BLUE);
        else                 pbFee.setForeground(C_AMBER);
    }

    private void log(String msg) {
        logCount++;
        taLog.append(String.format("[%02d] %s%n", logCount, msg));
        taLog.setCaretPosition(taLog.getDocument().getLength());
    }
    private void setStatus(String s) { lblStatus.setText(s); }
    private void showWarn(String s)  { JOptionPane.showMessageDialog(this, s, "Input Error", JOptionPane.WARNING_MESSAGE); }

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
        SwingUtilities.invokeLater(StudentFeeManagement::new);
    }
}
