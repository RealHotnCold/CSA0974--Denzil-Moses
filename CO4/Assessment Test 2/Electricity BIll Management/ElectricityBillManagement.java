import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// ── Consumer class ────────────────────────────────────────────────────────────
class Consumer {
    private int    consumerNo;
    private String consumerName;
    private String connectionType;   // "Domestic" or "Commercial"
    private double unitsConsumed;

    private static final double DOMESTIC_RATE   = 5.0;
    private static final double COMMERCIAL_RATE = 8.0;

    // Method 1: Read consumer details
    public void readDetails(int no, String name, String type, double units) {
        this.consumerNo     = no;
        this.consumerName   = name;
        this.connectionType = type;
        this.unitsConsumed  = units;
    }

    // Method 4: Validate connection type (called before bill calc)
    public boolean validateConnectionType() {
        return connectionType != null &&
               (connectionType.equalsIgnoreCase("Domestic") ||
                connectionType.equalsIgnoreCase("Commercial"));
    }

    // Method 2: Calculate electricity bill
    public double calculateBill() {
        if (!validateConnectionType()) return -1;
        double rate = connectionType.equalsIgnoreCase("Domestic") ? DOMESTIC_RATE : COMMERCIAL_RATE;
        return unitsConsumed * rate;
    }

    // Method 3: Display bill amount (returns formatted string)
    public String displayBill() {
        if (!validateConnectionType()) return "Invalid connection type.";
        double rate  = connectionType.equalsIgnoreCase("Domestic") ? DOMESTIC_RATE : COMMERCIAL_RATE;
        double bill  = calculateBill();
        return String.format(
            "Consumer No : %d\n"   +
            "Name        : %s\n"   +
            "Connection  : %s\n"   +
            "Units       : %.1f\n" +
            "Rate        : ₹%.2f/unit\n" +
            "─────────────────────\n" +
            "Bill Amount : ₹%.2f",
            consumerNo, consumerName, connectionType, unitsConsumed, rate, bill);
    }

    // Getters
    public int    getConsumerNo()     { return consumerNo; }
    public String getConsumerName()   { return consumerName; }
    public String getConnectionType() { return connectionType; }
    public double getUnitsConsumed()  { return unitsConsumed; }
    public static double getDomesticRate()   { return DOMESTIC_RATE; }
    public static double getCommercialRate() { return COMMERCIAL_RATE; }
}

// ── Main GUI ──────────────────────────────────────────────────────────────────
public class ElectricityBillManagement extends JFrame implements ActionListener {

    // AWT TextFields
    private TextField tfNo, tfName, tfUnits;

    // Swing radio buttons for connection type
    private JRadioButton rbDomestic, rbCommercial;
    private ButtonGroup  bgType;

    // Output labels
    private JLabel lblRate, lblBill, lblStatus, lblValidation;

    private JButton btnLoad, btnCalc, btnDisplay, btnValidate, btnReset;
    private JTextArea taLog;
    private JProgressBar pbUnits;

    private Consumer consumer;
    private int logCount = 0;

    // Palette
    private static final Color C_BG      = new Color(245, 248, 252);
    private static final Color C_PANEL   = Color.WHITE;
    private static final Color C_HEAD    = new Color(20, 60, 100);
    private static final Color C_BLUE    = new Color(24, 95, 165);
    private static final Color C_GREEN   = new Color(45, 120, 55);
    private static final Color C_AMBER   = new Color(160, 100, 10);
    private static final Color C_RED     = new Color(163, 45, 45);
    private static final Color C_PURPLE  = new Color(90, 60, 160);
    private static final Color C_TEAL    = new Color(15, 110, 85);
    private static final Color C_GRAY    = new Color(100, 100, 100);
    private static final Color C_BORDER  = new Color(218, 222, 230);

    public ElectricityBillManagement() {
        setTitle("Electricity Bill Management");
        setSize(650, 780);
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

        JLabel ico = new JLabel("⚡");
        ico.setFont(new Font("Dialog", Font.PLAIN, 26));

        JLabel title = new JLabel("Electricity Bill Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  AWT + Swing  |  Domestic ₹5/unit · Commercial ₹8/unit");
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
        p.add(buildBillPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildLogPanel());
        return p;
    }

    // ── Form (AWT TextField + GridLayout + Radio Buttons) ────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = card("Consumer Details");

        // GridLayout 4 rows × 2 cols
        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 8));
        grid.setBackground(C_PANEL);

        tfNo    = new TextField("C-1001");
        tfName  = new TextField("Anand Krishnan");
        tfUnits = new TextField("350");

        for (TextField tf : new TextField[]{tfNo, tfName, tfUnits})
            tf.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Radio buttons for connection type (Swing inside AWT form)
        rbDomestic   = new JRadioButton("Domestic",   true);
        rbCommercial = new JRadioButton("Commercial", false);
        rbDomestic.setBackground(C_PANEL);
        rbCommercial.setBackground(C_PANEL);
        rbDomestic.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rbCommercial.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bgType = new ButtonGroup();
        bgType.add(rbDomestic);
        bgType.add(rbCommercial);

        JPanel radioRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioRow.setBackground(C_PANEL);
        radioRow.add(rbDomestic);
        radioRow.add(Box.createHorizontalStrut(16));
        radioRow.add(rbCommercial);

        grid.add(lbl("Consumer No"));       grid.add(tfNo);
        grid.add(lbl("Consumer Name"));     grid.add(tfName);
        grid.add(lbl("Connection Type"));   grid.add(radioRow);
        grid.add(lbl("Units Consumed"));    grid.add(tfUnits);

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    // ── Buttons ───────────────────────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setBackground(C_BG);

        btnLoad     = mkBtn("Load Consumer", C_BLUE,   Color.WHITE);
        btnValidate = mkBtn("Validate Type", C_TEAL,   Color.WHITE);
        btnCalc     = mkBtn("Calc Bill",     C_AMBER,  Color.WHITE);
        btnDisplay  = mkBtn("Display Bill",  C_PURPLE, Color.WHITE);
        btnReset    = mkBtn("Reset",         C_RED,    Color.WHITE);

        btnValidate.setEnabled(false);
        btnCalc.setEnabled(false);
        btnDisplay.setEnabled(false);

        for (JButton b : new JButton[]{btnLoad, btnValidate, btnCalc, btnDisplay, btnReset})
            b.addActionListener(this);

        p.add(btnLoad); p.add(btnValidate); p.add(btnCalc); p.add(btnDisplay); p.add(btnReset);
        return p;
    }

    // ── Bill breakdown panel ──────────────────────────────────────────────────
    private JPanel buildBillPanel() {
        JPanel outer = card("Bill Overview");
        JPanel col   = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(C_PANEL);

        // Validation badge
        lblValidation = new JLabel("Connection type not validated yet.", SwingConstants.CENTER);
        lblValidation.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblValidation.setForeground(C_GRAY);
        lblValidation.setOpaque(true);
        lblValidation.setBackground(new Color(240, 240, 245));
        lblValidation.setBorder(new EmptyBorder(5, 10, 5, 10));
        lblValidation.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblValidation.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        // Metric row: Rate card + Bill card
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setBackground(C_PANEL);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        lblRate = metricLabel("—");
        lblBill = metricLabel("—");
        lblBill.setFont(new Font("SansSerif", Font.BOLD, 22));

        row.add(metricCard("Rate / Unit",   lblRate, new Color(255, 248, 225)));
        row.add(metricCard("Bill Amount",   lblBill, new Color(230, 248, 232)));

        // Progress bar for units (0–1000 scale)
        pbUnits = new JProgressBar(0, 1000);
        pbUnits.setValue(0);
        pbUnits.setStringPainted(true);
        pbUnits.setString("Load a consumer first");
        pbUnits.setForeground(C_BLUE);
        pbUnits.setBackground(new Color(228, 235, 248));
        pbUnits.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbUnits.setPreferredSize(new Dimension(0, 26));
        pbUnits.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        JLabel pbLbl = new JLabel("Units Consumed (scale: 0–1000)");
        pbLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbLbl.setForeground(C_GRAY);
        pbLbl.setBorder(new EmptyBorder(10, 0, 4, 0));
        pbLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        col.add(lblValidation);
        col.add(Box.createVerticalStrut(10));
        col.add(row);
        col.add(pbLbl);
        col.add(pbUnits);
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
        lblStatus = new JLabel("Ready — load a consumer to begin.");
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
        else if (src == btnValidate) handleValidate();
        else if (src == btnCalc)     handleCalc();
        else if (src == btnDisplay)  handleDisplay();
        else if (src == btnReset)    handleReset();
    }

    private void handleLoad() {
        String noStr = tfNo.getText().trim();
        String name  = tfName.getText().trim();
        String units = tfUnits.getText().trim();
        String type  = rbDomestic.isSelected() ? "Domestic" : "Commercial";

        if (noStr.isEmpty() || name.isEmpty() || units.isEmpty()) {
            showWarn("All fields are required."); return;
        }
        int no; double u;
        try { no = Integer.parseInt(noStr.replaceAll("[^0-9]", "")); }
        catch (NumberFormatException ex) { showWarn("Consumer No must contain a number."); return; }
        try { u = Double.parseDouble(units); if (u < 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { showWarn("Units must be a non-negative number."); return; }

        consumer = new Consumer();
        consumer.readDetails(no, name, type, u);    // Method 1

        // Reset bill display
        lblRate.setText("—"); lblBill.setText("—");
        lblRate.setForeground(C_GRAY); lblBill.setForeground(C_GRAY);
        lblValidation.setText("Connection type not validated yet.");
        lblValidation.setBackground(new Color(240, 240, 245));
        lblValidation.setForeground(C_GRAY);

        int units100 = Math.min((int) u, 1000);
        pbUnits.setValue(units100);
        pbUnits.setString(String.format("%.1f units", u));
        pbUnits.setForeground(u > 700 ? C_RED : u > 400 ? C_AMBER : C_BLUE);

        btnValidate.setEnabled(true);
        btnCalc.setEnabled(true);
        btnDisplay.setEnabled(true);

        log(String.format("LOADED  | %s  [%s]  Units: %.1f", name, type, u));
        setStatus("Loaded: " + name + "  |  Type: " + type + "  |  Units: " + String.format("%.1f", u));
    }

    private void handleValidate() {
        if (consumer == null) return;
        boolean valid = consumer.validateConnectionType();    // Method 4
        if (valid) {
            lblValidation.setText("✔  Connection type \"" + consumer.getConnectionType() + "\" is VALID.");
            lblValidation.setBackground(new Color(225, 248, 230));
            lblValidation.setForeground(C_GREEN);
            log("VALID   | Connection type \"" + consumer.getConnectionType() + "\" validated successfully.");
            setStatus("Validation passed: " + consumer.getConnectionType());
        } else {
            lblValidation.setText("✘  Invalid connection type: \"" + consumer.getConnectionType() + "\"");
            lblValidation.setBackground(new Color(255, 232, 232));
            lblValidation.setForeground(C_RED);
            log("INVALID | Unknown type: \"" + consumer.getConnectionType() + "\"");
            setStatus("Validation failed for: " + consumer.getConnectionType());
        }
    }

    private void handleCalc() {
        if (consumer == null) return;
        double bill = consumer.calculateBill();     // Method 2
        if (bill < 0) {
            showWarn("Invalid connection type. Cannot calculate bill."); return;
        }
        double rate = consumer.getConnectionType().equalsIgnoreCase("Domestic")
                      ? Consumer.getDomesticRate() : Consumer.getCommercialRate();
        lblRate.setText("₹" + String.format("%.2f", rate));
        lblRate.setForeground(C_AMBER);
        lblBill.setText("₹" + String.format("%,.2f", bill));
        lblBill.setForeground(C_GREEN);

        log(String.format("CALC    | %.1f units × ₹%.2f = ₹%.2f",
                consumer.getUnitsConsumed(), rate, bill));
        setStatus("Bill calculated: ₹" + String.format("%.2f", bill)
                + "  (" + consumer.getConnectionType() + " @ ₹" + rate + "/unit)");
    }

    private void handleDisplay() {
        if (consumer == null) return;
        String details = consumer.displayBill();    // Method 3
        log("DISPLAY |\n" + details);
        JOptionPane.showMessageDialog(this, details,
                "Bill — " + consumer.getConsumerName(), JOptionPane.INFORMATION_MESSAGE);
        setStatus("Bill displayed for Consumer No: " + consumer.getConsumerNo());
    }

    private void handleReset() {
        consumer = null;
        tfNo.setText("C-1001"); tfName.setText("Anand Krishnan"); tfUnits.setText("350");
        rbDomestic.setSelected(true);
        lblRate.setText("—"); lblBill.setText("—");
        lblRate.setForeground(C_GRAY); lblBill.setForeground(C_GRAY);
        lblValidation.setText("Connection type not validated yet.");
        lblValidation.setBackground(new Color(240, 240, 245));
        lblValidation.setForeground(C_GRAY);
        pbUnits.setValue(0); pbUnits.setString("Load a consumer first");
        taLog.setText(""); logCount = 0;
        btnValidate.setEnabled(false); btnCalc.setEnabled(false); btnDisplay.setEnabled(false);
        setStatus("Reset — load a consumer to begin.");
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
        SwingUtilities.invokeLater(ElectricityBillManagement::new);
    }
}
