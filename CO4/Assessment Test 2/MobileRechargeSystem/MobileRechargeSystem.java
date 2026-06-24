import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// ── Subscriber class ──────────────────────────────────────────────────────────
class Subscriber {
    private String mobileNo;
    private String subscriberName;
    private String serviceProvider;
    private double accountBalance;

    private static final double INITIAL_BALANCE = 500.0;

    // Method 1: Read subscriber details
    public void readDetails(String mobile, String name, String provider) {
        this.mobileNo        = mobile;
        this.subscriberName  = name;
        this.serviceProvider = provider;
        this.accountBalance  = INITIAL_BALANCE;
    }

    // Method 2: Recharge account balance
    public String recharge(double amount) {
        if (amount <= 0)    return "ERROR: Amount must be positive.";
        if (amount > 10000) return "ERROR: Single recharge limit is ₹10,000.";
        accountBalance += amount;
        return "OK";
    }

    // Method 3: Deduct balance for call/data usage
    public String deduct(double amount) {
        if (amount <= 0)          return "ERROR: Amount must be positive.";
        if (amount > accountBalance) return "ERROR: Insufficient balance (₹" + String.format("%.2f", accountBalance) + ").";
        accountBalance -= amount;
        return "OK";
    }

    // Method 4: Display updated balance
    public String displayBalance() {
        return String.format(
            "Mobile No   : %s\n" +
            "Name        : %s\n" +
            "Provider    : %s\n" +
            "─────────────────────\n" +
            "Balance     : ₹%.2f",
            mobileNo, subscriberName, serviceProvider, accountBalance);
    }

    public String getMobileNo()        { return mobileNo; }
    public String getSubscriberName()  { return subscriberName; }
    public String getServiceProvider() { return serviceProvider; }
    public double getAccountBalance()  { return accountBalance; }
    public static double getInitialBalance() { return INITIAL_BALANCE; }
}

// ── Main GUI ──────────────────────────────────────────────────────────────────
public class MobileRechargeSystem extends JFrame implements ActionListener {

    private TextField tfMobile, tfName, tfProvider, tfAmount;

    private JLabel lblBalance, lblStatus, lblProvider;
    private JProgressBar pbBalance;
    private JComboBox<String> cbUsageType;

    private JButton btnLoad, btnRecharge, btnDeduct, btnDisplay, btnReset;
    private JTextArea taLog;

    private Subscriber sub;
    private int logCount = 0;
    private double maxSeen = 500.0;   // tracks highest balance for progress bar

    private static final Color C_BG     = new Color(244, 246, 252);
    private static final Color C_PANEL  = Color.WHITE;
    private static final Color C_HEAD   = new Color(30, 30, 80);
    private static final Color C_BLUE   = new Color(24, 95, 165);
    private static final Color C_GREEN  = new Color(45, 120, 55);
    private static final Color C_RED    = new Color(163, 45, 45);
    private static final Color C_AMBER  = new Color(160, 100, 10);
    private static final Color C_PURPLE = new Color(90, 60, 160);
    private static final Color C_GRAY   = new Color(100, 100, 100);
    private static final Color C_BORDER = new Color(218, 222, 232);

    public MobileRechargeSystem() {
        setTitle("Mobile Recharge System");
        setSize(650, 760);
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

        JLabel ico = new JLabel("📱");
        ico.setFont(new Font("Dialog", Font.PLAIN, 26));

        JLabel title = new JLabel("Mobile Recharge System");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  AWT + Swing  |  Initial Balance ₹500");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(new Color(170, 175, 220));

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
        p.add(buildActionPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildButtonPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildBalancePanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildLogPanel());
        return p;
    }

    // ── Subscriber form (AWT TextField + GridLayout) ──────────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = card("Subscriber Details");
        JPanel grid  = new JPanel(new GridLayout(3, 2, 10, 8));
        grid.setBackground(C_PANEL);

        tfMobile   = new TextField("9876543210");
        tfName     = new TextField("Kavya Reddy");
        tfProvider = new TextField("Jio");

        for (TextField tf : new TextField[]{tfMobile, tfName, tfProvider})
            tf.setFont(new Font("SansSerif", Font.PLAIN, 13));

        grid.add(lbl("Mobile Number"));    grid.add(tfMobile);
        grid.add(lbl("Subscriber Name"));  grid.add(tfName);
        grid.add(lbl("Service Provider")); grid.add(tfProvider);

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    // ── Recharge / Deduct amount row ──────────────────────────────────────────
    private JPanel buildActionPanel() {
        JPanel outer = card("Transaction");
        JPanel grid  = new JPanel(new GridLayout(2, 2, 10, 8));
        grid.setBackground(C_PANEL);

        tfAmount = new TextField("100");
        tfAmount.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // JComboBox for usage type (Swing component)
        cbUsageType = new JComboBox<>(new String[]{"Call Charges", "Data Usage", "SMS Pack", "Roaming"});
        cbUsageType.setFont(new Font("SansSerif", Font.PLAIN, 13));

        grid.add(lbl("Amount (₹)"));  grid.add(tfAmount);
        grid.add(lbl("Usage Type"));  grid.add(cbUsageType);

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    // ── Buttons (FlowLayout) ──────────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setBackground(C_BG);

        btnLoad     = mkBtn("Load Subscriber", C_BLUE,   Color.WHITE);
        btnRecharge = mkBtn("Recharge",        C_GREEN,  Color.WHITE);
        btnDeduct   = mkBtn("Deduct Usage",    C_AMBER,  Color.WHITE);
        btnDisplay  = mkBtn("Display Balance", C_PURPLE, Color.WHITE);
        btnReset    = mkBtn("Reset",           C_RED,    Color.WHITE);

        btnRecharge.setEnabled(false);
        btnDeduct.setEnabled(false);
        btnDisplay.setEnabled(false);

        for (JButton b : new JButton[]{btnLoad, btnRecharge, btnDeduct, btnDisplay, btnReset})
            b.addActionListener(this);

        p.add(btnLoad); p.add(btnRecharge); p.add(btnDeduct); p.add(btnDisplay); p.add(btnReset);
        return p;
    }

    // ── Balance panel ─────────────────────────────────────────────────────────
    private JPanel buildBalancePanel() {
        JPanel outer = card("Account Balance");
        JPanel col   = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(C_PANEL);

        // Provider badge + big balance
        lblProvider = new JLabel("—", SwingConstants.CENTER);
        lblProvider.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblProvider.setForeground(C_GRAY);
        lblProvider.setOpaque(true);
        lblProvider.setBackground(new Color(235, 238, 250));
        lblProvider.setBorder(new EmptyBorder(4, 10, 4, 10));
        lblProvider.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblBalance = new JLabel("₹—", SwingConstants.CENTER);
        lblBalance.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblBalance.setForeground(C_GRAY);
        lblBalance.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Progress bar
        pbBalance = new JProgressBar(0, 100);
        pbBalance.setValue(0);
        pbBalance.setStringPainted(true);
        pbBalance.setString("Load a subscriber first");
        pbBalance.setForeground(C_GREEN);
        pbBalance.setBackground(new Color(230, 240, 230));
        pbBalance.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbBalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        JLabel pbLbl = new JLabel("Balance relative to peak");
        pbLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbLbl.setForeground(C_GRAY);
        pbLbl.setBorder(new EmptyBorder(8, 0, 4, 0));
        pbLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        col.add(lblProvider);
        col.add(Box.createVerticalStrut(8));
        col.add(lblBalance);
        col.add(pbLbl);
        col.add(pbBalance);
        outer.add(col, BorderLayout.CENTER);
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
        p.setBackground(new Color(228, 230, 248));
        p.setBorder(new MatteBorder(1, 0, 0, 0, C_BORDER));
        lblStatus = new JLabel("Ready — load a subscriber to begin.");
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
        else if (src == btnRecharge) handleRecharge();
        else if (src == btnDeduct)   handleDeduct();
        else if (src == btnDisplay)  handleDisplay();
        else if (src == btnReset)    handleReset();
    }

    private void handleLoad() {
        String mobile   = tfMobile.getText().trim();
        String name     = tfName.getText().trim();
        String provider = tfProvider.getText().trim();

        if (mobile.isEmpty() || name.isEmpty() || provider.isEmpty()) {
            showWarn("All fields are required."); return;
        }
        if (!mobile.matches("[0-9]{10}")) {
            showWarn("Mobile number must be exactly 10 digits."); return;
        }

        sub = new Subscriber();
        sub.readDetails(mobile, name, provider);    // Method 1
        maxSeen = Subscriber.getInitialBalance();

        updateBalanceUI();
        btnRecharge.setEnabled(true);
        btnDeduct.setEnabled(true);
        btnDisplay.setEnabled(true);

        log(String.format("LOADED  | %s  [%s]  Provider: %s  Balance: ₹500.00", name, mobile, provider));
        setStatus("Loaded: " + name + "  |  " + provider + "  |  Balance: ₹500.00");
    }

    private void handleRecharge() {
        if (sub == null) return;
        double amt = parseAmount(); if (amt < 0) return;
        String res = sub.recharge(amt);     // Method 2
        if (res.startsWith("ERROR")) { showWarn(res.replace("ERROR: ","")); log("FAILED  | "+res); return; }
        if (sub.getAccountBalance() > maxSeen) maxSeen = sub.getAccountBalance();
        updateBalanceUI();
        log(String.format("RECHARGE| +₹%.2f  →  Balance: ₹%.2f", amt, sub.getAccountBalance()));
        setStatus("Recharged ₹" + String.format("%.2f", amt) + "  |  New balance: ₹" + String.format("%.2f", sub.getAccountBalance()));
    }

    private void handleDeduct() {
        if (sub == null) return;
        double amt = parseAmount(); if (amt < 0) return;
        String usage = (String) cbUsageType.getSelectedItem();
        String res = sub.deduct(amt);   // Method 3
        if (res.startsWith("ERROR")) { showWarn(res.replace("ERROR: ","")); log("FAILED  | "+res); return; }
        updateBalanceUI();
        log(String.format("DEDUCT  | %s  -₹%.2f  →  Balance: ₹%.2f", usage, amt, sub.getAccountBalance()));
        setStatus("Deducted ₹" + String.format("%.2f", amt) + " for " + usage + "  |  Balance: ₹" + String.format("%.2f", sub.getAccountBalance()));
        if (sub.getAccountBalance() < 50)
            JOptionPane.showMessageDialog(this, "⚠ Low balance! ₹" + String.format("%.2f", sub.getAccountBalance()) + " remaining.", "Low Balance", JOptionPane.WARNING_MESSAGE);
    }

    private void handleDisplay() {
        if (sub == null) return;
        String details = sub.displayBalance();  // Method 4
        log("DISPLAY |\n" + details);
        JOptionPane.showMessageDialog(this, details, "Balance — " + sub.getSubscriberName(), JOptionPane.INFORMATION_MESSAGE);
        setStatus("Balance displayed for: " + sub.getMobileNo());
    }

    private void handleReset() {
        sub = null; maxSeen = 500.0; logCount = 0;
        tfMobile.setText("9876543210"); tfName.setText("Kavya Reddy");
        tfProvider.setText("Jio"); tfAmount.setText("100");
        lblBalance.setText("₹—"); lblBalance.setForeground(C_GRAY);
        lblProvider.setText("—"); lblProvider.setBackground(new Color(235, 238, 250));
        pbBalance.setValue(0); pbBalance.setString("Load a subscriber first");
        taLog.setText("");
        btnRecharge.setEnabled(false); btnDeduct.setEnabled(false); btnDisplay.setEnabled(false);
        setStatus("Reset — load a subscriber to begin.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private double parseAmount() {
        try {
            double v = Double.parseDouble(tfAmount.getText().trim());
            if (v <= 0) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException ex) {
            showWarn("Enter a valid positive amount."); return -1;
        }
    }

    private void updateBalanceUI() {
        if (sub == null) return;
        double bal = sub.getAccountBalance();
        lblBalance.setText("₹" + String.format("%,.2f", bal));
        lblBalance.setForeground(bal < 50 ? C_RED : bal < 150 ? C_AMBER : C_GREEN);

        lblProvider.setText("  " + sub.getServiceProvider() + "  ·  " + sub.getMobileNo() + "  ");
        lblProvider.setBackground(new Color(235, 238, 250));

        int pct = (maxSeen > 0) ? (int) Math.round((bal / maxSeen) * 100) : 0;
        pct = Math.min(pct, 100);
        pbBalance.setValue(pct);
        pbBalance.setString(String.format("₹%.2f  (%d%% of peak ₹%.2f)", bal, pct, maxSeen));
        pbBalance.setForeground(pct < 20 ? C_RED : pct < 50 ? C_AMBER : C_GREEN);
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
        SwingUtilities.invokeLater(MobileRechargeSystem::new);
    }
}
