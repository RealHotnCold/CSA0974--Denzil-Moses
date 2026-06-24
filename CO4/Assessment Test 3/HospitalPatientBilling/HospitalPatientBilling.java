import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// ── Patient class ─────────────────────────────────────────────────────────────
class Patient {
    private int    patientId;
    private String patientName;
    private String roomType;          // "General" or "Special"
    private int    daysAdmitted;
    private double treatmentCharges;

    private static final double GENERAL_RATE = 1500.0;
    private static final double SPECIAL_RATE = 3000.0;

    // Method 1: Read patient details
    public void readDetails(int id, String name, String room, int days, double treatment) {
        this.patientId        = id;
        this.patientName      = name;
        this.roomType         = room;
        this.daysAdmitted     = days;
        this.treatmentCharges = treatment;
    }

    // Method 2: Calculate room charges
    public double calculateRoomCharges() {
        double rate = roomType.equalsIgnoreCase("Special") ? SPECIAL_RATE : GENERAL_RATE;
        return rate * daysAdmitted;
    }

    // Method 3: Calculate total hospital bill
    public double calculateTotalBill() {
        return calculateRoomCharges() + treatmentCharges;
    }

    // Method 4: Display patient billing details (formatted string)
    public String displayBillingDetails() {
        double roomCharge = calculateRoomCharges();
        double total      = calculateTotalBill();
        double rate       = roomType.equalsIgnoreCase("Special") ? SPECIAL_RATE : GENERAL_RATE;
        return String.format(
            "Patient ID       : %d\n"         +
            "Patient Name     : %s\n"         +
            "Room Type        : %s\n"         +
            "Days Admitted    : %d\n"         +
            "Room Rate        : ₹%.2f/day\n"  +
            "Room Charges     : ₹%.2f\n"      +
            "Treatment Charges: ₹%.2f\n"      +
            "─────────────────────────────\n" +
            "Total Bill       : ₹%.2f",
            patientId, patientName, roomType,
            daysAdmitted, rate,
            roomCharge, treatmentCharges, total);
    }

    // Getters
    public int    getPatientId()        { return patientId; }
    public String getPatientName()      { return patientName; }
    public String getRoomType()         { return roomType; }
    public int    getDaysAdmitted()     { return daysAdmitted; }
    public double getTreatmentCharges() { return treatmentCharges; }
    public static double getGeneralRate() { return GENERAL_RATE; }
    public static double getSpecialRate() { return SPECIAL_RATE; }
}

// ── Main GUI ──────────────────────────────────────────────────────────────────
public class HospitalPatientBilling extends JFrame implements ActionListener {

    // AWT TextFields
    private TextField tfId, tfName, tfDays, tfTreatment;

    // Swing radio buttons for room type
    private JRadioButton rbGeneral, rbSpecial;
    private ButtonGroup  bgRoom;

    // Output labels
    private JLabel lblRoomCharge, lblTreatment, lblTotal, lblStatus;
    private JProgressBar pbDays;

    private JButton btnLoad, btnRoomCalc, btnBillCalc, btnDisplay, btnReset;
    private JTextArea taLog;

    private Patient patient;
    private int logCount = 0;

    // Palette
    private static final Color C_BG     = new Color(244, 247, 252);
    private static final Color C_PANEL  = Color.WHITE;
    private static final Color C_HEAD   = new Color(20, 55, 100);
    private static final Color C_BLUE   = new Color(24, 95, 165);
    private static final Color C_GREEN  = new Color(45, 120, 55);
    private static final Color C_RED    = new Color(163, 45, 45);
    private static final Color C_AMBER  = new Color(160, 100, 10);
    private static final Color C_PURPLE = new Color(90, 60, 160);
    private static final Color C_TEAL   = new Color(15, 110, 90);
    private static final Color C_GRAY   = new Color(100, 100, 100);
    private static final Color C_BORDER = new Color(218, 222, 232);

    public HospitalPatientBilling() {
        setTitle("Hospital Patient Billing System");
        setSize(660, 790);
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

        JLabel ico = new JLabel("🏥");
        ico.setFont(new Font("Dialog", Font.PLAIN, 26));

        JLabel title = new JLabel("Hospital Patient Billing System");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  AWT + Swing  |  General ₹1,500/day · Special ₹3,000/day");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(new Color(160, 195, 230));

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
        p.add(buildBillBreakdown());
        p.add(Box.createVerticalStrut(10));
        p.add(buildLogPanel());
        return p;
    }

    // ── Form (AWT TextField + GridLayout + RadioButton) ───────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = card("Patient Details");
        JPanel grid  = new JPanel(new GridLayout(5, 2, 10, 8));
        grid.setBackground(C_PANEL);

        tfId        = new TextField("P-1001");
        tfName      = new TextField("Meena Sundar");
        tfDays      = new TextField("5");
        tfTreatment = new TextField("8000");

        for (TextField tf : new TextField[]{tfId, tfName, tfDays, tfTreatment})
            tf.setFont(new Font("SansSerif", Font.PLAIN, 13));

        rbGeneral = new JRadioButton("General  (₹1,500/day)", true);
        rbSpecial = new JRadioButton("Special  (₹3,000/day)", false);
        rbGeneral.setBackground(C_PANEL);
        rbSpecial.setBackground(C_PANEL);
        rbGeneral.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rbSpecial.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bgRoom = new ButtonGroup();
        bgRoom.add(rbGeneral); bgRoom.add(rbSpecial);

        JPanel radioRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioRow.setBackground(C_PANEL);
        radioRow.add(rbGeneral);
        radioRow.add(Box.createHorizontalStrut(12));
        radioRow.add(rbSpecial);

        grid.add(lbl("Patient ID"));           grid.add(tfId);
        grid.add(lbl("Patient Name"));         grid.add(tfName);
        grid.add(lbl("Room Type"));            grid.add(radioRow);
        grid.add(lbl("Days Admitted"));        grid.add(tfDays);
        grid.add(lbl("Treatment Charges (₹)")); grid.add(tfTreatment);

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    // ── Buttons (FlowLayout) ──────────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setBackground(C_BG);

        btnLoad    = mkBtn("Load Patient",   C_BLUE,   Color.WHITE);
        btnRoomCalc= mkBtn("Room Charges",   C_TEAL,   Color.WHITE);
        btnBillCalc= mkBtn("Total Bill",     C_AMBER,  Color.WHITE);
        btnDisplay = mkBtn("Display Bill",   C_PURPLE, Color.WHITE);
        btnReset   = mkBtn("Reset",          C_RED,    Color.WHITE);

        btnRoomCalc.setEnabled(false);
        btnBillCalc.setEnabled(false);
        btnDisplay.setEnabled(false);

        for (JButton b : new JButton[]{btnLoad, btnRoomCalc, btnBillCalc, btnDisplay, btnReset})
            b.addActionListener(this);

        p.add(btnLoad); p.add(btnRoomCalc); p.add(btnBillCalc); p.add(btnDisplay); p.add(btnReset);
        return p;
    }

    // ── Bill Breakdown (3 metric cards + progress bar) ────────────────────────
    private JPanel buildBillBreakdown() {
        JPanel outer = card("Bill Breakdown");
        JPanel col   = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(C_PANEL);

        // 3 metric cards
        JPanel row = new JPanel(new GridLayout(1, 3, 12, 0));
        row.setBackground(C_PANEL);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        lblRoomCharge = metricLabel("—");
        lblTreatment  = metricLabel("—");
        lblTotal      = metricLabel("—");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 20));

        row.add(metricCard("Room Charges",    lblRoomCharge, new Color(230, 240, 255)));
        row.add(metricCard("Treatment",       lblTreatment,  new Color(255, 248, 225)));
        row.add(metricCard("Total Bill",      lblTotal,      new Color(230, 248, 232)));

        // Days admitted progress bar (0–30 scale)
        pbDays = new JProgressBar(0, 30);
        pbDays.setValue(0);
        pbDays.setStringPainted(true);
        pbDays.setString("Load a patient first");
        pbDays.setForeground(C_BLUE);
        pbDays.setBackground(new Color(228, 235, 248));
        pbDays.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbDays.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        JLabel pbLbl = new JLabel("Days Admitted (scale: 0–30)");
        pbLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbLbl.setForeground(C_GRAY);
        pbLbl.setBorder(new EmptyBorder(10, 0, 4, 0));
        pbLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        col.add(row);
        col.add(pbLbl);
        col.add(pbDays);
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
        p.setBackground(new Color(228, 233, 245));
        p.setBorder(new MatteBorder(1, 0, 0, 0, C_BORDER));
        lblStatus = new JLabel("Ready — load a patient to begin.");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblStatus.setForeground(C_GRAY);
        p.add(lblStatus);
        return p;
    }

    // ── Event Handling ────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if      (src == btnLoad)     handleLoad();
        else if (src == btnRoomCalc) handleRoomCalc();
        else if (src == btnBillCalc) handleBillCalc();
        else if (src == btnDisplay)  handleDisplay();
        else if (src == btnReset)    handleReset();
    }

    private void handleLoad() {
        String idStr = tfId.getText().trim();
        String name  = tfName.getText().trim();
        String dStr  = tfDays.getText().trim();
        String tStr  = tfTreatment.getText().trim();
        String room  = rbSpecial.isSelected() ? "Special" : "General";

        if (idStr.isEmpty() || name.isEmpty() || dStr.isEmpty() || tStr.isEmpty()) {
            showWarn("All fields are required."); return;
        }
        int id, days; double treatment;
        try { id = Integer.parseInt(idStr.replaceAll("[^0-9]","")); }
        catch (NumberFormatException ex) { showWarn("Patient ID must contain a number."); return; }
        try { days = Integer.parseInt(dStr); if (days <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { showWarn("Days must be a positive integer."); return; }
        try { treatment = Double.parseDouble(tStr); if (treatment < 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { showWarn("Treatment charges must be non-negative."); return; }

        patient = new Patient();
        patient.readDetails(id, name, room, days, treatment);   // Method 1

        // Reset metric cards
        lblRoomCharge.setText("—"); lblRoomCharge.setForeground(C_GRAY);
        lblTreatment.setText("—");  lblTreatment.setForeground(C_GRAY);
        lblTotal.setText("—");      lblTotal.setForeground(C_GRAY);

        int barDays = Math.min(days, 30);
        pbDays.setValue(barDays);
        pbDays.setString(days + " day" + (days > 1 ? "s" : "") + "  [" + room + "]");
        pbDays.setForeground(days > 20 ? C_RED : days > 10 ? C_AMBER : C_BLUE);

        btnRoomCalc.setEnabled(true);
        btnBillCalc.setEnabled(true);
        btnDisplay.setEnabled(true);

        log(String.format("LOADED  | %s  [%s]  Days: %d  Treatment: ₹%.2f", name, room, days, treatment));
        setStatus("Loaded: " + name + "  |  " + room + " Room  |  " + days + " days");
    }

    private void handleRoomCalc() {
        if (patient == null) return;
        double rc = patient.calculateRoomCharges();     // Method 2
        lblRoomCharge.setText("₹" + String.format("%,.2f", rc));
        lblRoomCharge.setForeground(C_BLUE);
        double rate = patient.getRoomType().equalsIgnoreCase("Special")
                      ? Patient.getSpecialRate() : Patient.getGeneralRate();
        log(String.format("ROOM    | %s  %d days × ₹%.2f = ₹%.2f",
                patient.getRoomType(), patient.getDaysAdmitted(), rate, rc));
        setStatus("Room charges: ₹" + String.format("%.2f", rc)
                + "  (" + patient.getRoomType() + " @ ₹" + String.format("%.0f", rate) + "/day)");
    }

    private void handleBillCalc() {
        if (patient == null) return;
        // Ensure room charges are shown
        double rc = patient.calculateRoomCharges();
        lblRoomCharge.setText("₹" + String.format("%,.2f", rc));
        lblRoomCharge.setForeground(C_BLUE);

        lblTreatment.setText("₹" + String.format("%,.2f", patient.getTreatmentCharges()));
        lblTreatment.setForeground(C_AMBER);

        double total = patient.calculateTotalBill();    // Method 3
        lblTotal.setText("₹" + String.format("%,.2f", total));
        lblTotal.setForeground(C_GREEN);

        log(String.format("TOTAL   | Room ₹%.2f + Treatment ₹%.2f = ₹%.2f",
                rc, patient.getTreatmentCharges(), total));
        setStatus("Total bill: ₹" + String.format("%.2f", total));
    }

    private void handleDisplay() {
        if (patient == null) return;
        String details = patient.displayBillingDetails();   // Method 4
        log("DISPLAY |\n" + details);
        JOptionPane.showMessageDialog(this, details,
                "Bill — " + patient.getPatientName(), JOptionPane.INFORMATION_MESSAGE);
        setStatus("Bill displayed for Patient ID: " + patient.getPatientId());
    }

    private void handleReset() {
        patient = null; logCount = 0;
        tfId.setText("P-1001"); tfName.setText("Meena Sundar");
        tfDays.setText("5"); tfTreatment.setText("8000");
        rbGeneral.setSelected(true);
        lblRoomCharge.setText("—"); lblRoomCharge.setForeground(C_GRAY);
        lblTreatment.setText("—");  lblTreatment.setForeground(C_GRAY);
        lblTotal.setText("—");      lblTotal.setForeground(C_GRAY);
        pbDays.setValue(0); pbDays.setString("Load a patient first");
        taLog.setText("");
        btnRoomCalc.setEnabled(false); btnBillCalc.setEnabled(false); btnDisplay.setEnabled(false);
        setStatus("Reset — load a patient to begin.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
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
            new LineBorder(C_BORDER, 1, true), new EmptyBorder(10, 10, 10, 10)));
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
            new LineBorder(C_BORDER, 1, true), new EmptyBorder(10, 12, 10, 12)));
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
        SwingUtilities.invokeLater(HospitalPatientBilling::new);
    }
}
