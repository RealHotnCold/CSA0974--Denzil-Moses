import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// Book class representing a library book
class Book {
    private int bookId;
    private String bookTitle;
    private String authorName;
    private int copiesAvailable;
    private static final int INITIAL_COPIES = 20;

    // Method 1: Read book details
    public void readDetails(int id, String title, String author) {
        this.bookId = id;
        this.bookTitle = title;
        this.authorName = author;
        this.copiesAvailable = INITIAL_COPIES;
    }

    // Method 2: Issue a book
    public boolean issueBook() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
            return true;
        }
        return false;
    }

    // Method 3: Return a book
    public boolean returnBook() {
        if (copiesAvailable < INITIAL_COPIES) {
            copiesAvailable++;
            return true;
        }
        return false;
    }

    // Method 4: Display updated book details
    public String displayDetails() {
        return String.format(
            "Book ID     : %d\nTitle       : %s\nAuthor      : %s\nAvailable   : %d / %d",
            bookId, bookTitle, authorName, copiesAvailable, INITIAL_COPIES
        );
    }

    // Getters
    public int getCopiesAvailable() { return copiesAvailable; }
    public String getTitle()        { return bookTitle; }
    public int getBookId()          { return bookId; }
}

// Main GUI class using Swing + AWT
public class LibraryManagementSystem extends JFrame implements ActionListener {

    // AWT/Swing components
    private TextField tfId, tfTitle, tfAuthor;   // AWT TextField
    private JButton btnLoad, btnIssue, btnReturn, btnDisplay, btnClear;
    private JTextArea taLog;
    private JLabel lblStatus, lblAvail;
    private JProgressBar pbAvail;
    private Book book;
    private int logCount = 0;

    // Colors
    private static final Color C_BG     = new Color(245, 247, 250);
    private static final Color C_PANEL  = Color.WHITE;
    private static final Color C_BLUE   = new Color(24, 95, 165);
    private static final Color C_GREEN  = new Color(59, 109, 17);
    private static final Color C_RED    = new Color(163, 45, 45);
    private static final Color C_GRAY   = new Color(100, 100, 100);
    private static final Color C_BORDER = new Color(220, 220, 225);

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setSize(620, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);

        // Use BorderLayout as main layout manager
        setLayout(new BorderLayout(10, 10));

        add(buildHeader(),     BorderLayout.NORTH);
        add(buildCenter(),     BorderLayout.CENTER);
        add(buildStatusBar(),  BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Header Panel ──────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        p.setBackground(C_BLUE);

        JLabel icon = new JLabel("📚");
        icon.setFont(new Font("Dialog", Font.PLAIN, 26));

        JLabel title = new JLabel("Library Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  AWT + Swing Demo");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(new Color(180, 210, 240));

        p.add(icon); p.add(title); p.add(sub);
        return p;
    }

    // ── Center: form + stats + log ────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        p.add(buildFormPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildStatsPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildButtonPanel());
        p.add(Box.createVerticalStrut(10));
        p.add(buildLogPanel());
        return p;
    }

    // ── Form Panel (AWT TextField inside Swing) ───────────────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = card("Book Details");
        // GridLayout: 3 rows × 2 cols
        JPanel grid = new JPanel(new GridLayout(3, 2, 10, 8));
        grid.setBackground(C_PANEL);

        tfId     = new TextField("LIB-001");
        tfTitle  = new TextField("Design Patterns");
        tfAuthor = new TextField("Gang of Four");

        styleAWTField(tfId);
        styleAWTField(tfTitle);
        styleAWTField(tfAuthor);

        grid.add(label("Book ID"));     grid.add(tfId);
        grid.add(label("Book Title"));  grid.add(tfTitle);
        grid.add(label("Author Name")); grid.add(tfAuthor);

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    // ── Stats Panel ───────────────────────────────────────────────────────────
    private JPanel buildStatsPanel() {
        JPanel outer = card("Availability");
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(C_PANEL);

        lblAvail = new JLabel("Load a book first");
        lblAvail.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblAvail.setForeground(C_GRAY);

        pbAvail = new JProgressBar(0, 20);
        pbAvail.setValue(0);
        pbAvail.setStringPainted(true);
        pbAvail.setString("—");
        pbAvail.setForeground(C_GREEN);
        pbAvail.setBackground(new Color(235, 240, 235));
        pbAvail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pbAvail.setPreferredSize(new Dimension(0, 24));

        row.add(lblAvail, BorderLayout.WEST);
        row.add(pbAvail,  BorderLayout.CENTER);
        outer.add(row, BorderLayout.CENTER);
        return outer;
    }

    // ── Button Panel (FlowLayout) ─────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        p.setBackground(C_BG);

        btnLoad    = styledBtn("Load Book",   C_BLUE,  Color.WHITE);
        btnIssue   = styledBtn("Issue Book",  C_RED,   Color.WHITE);
        btnReturn  = styledBtn("Return Book", C_GREEN, Color.WHITE);
        btnDisplay = styledBtn("Display",     new Color(80, 60, 150), Color.WHITE);
        btnClear   = styledBtn("Clear Log",   C_GRAY,  Color.WHITE);

        btnIssue.setEnabled(false);
        btnReturn.setEnabled(false);
        btnDisplay.setEnabled(false);

        for (JButton b : new JButton[]{btnLoad, btnIssue, btnReturn, btnDisplay, btnClear})
            b.addActionListener(this);

        p.add(btnLoad); p.add(btnIssue); p.add(btnReturn); p.add(btnDisplay); p.add(btnClear);
        return p;
    }

    // ── Log Panel ─────────────────────────────────────────────────────────────
    private JPanel buildLogPanel() {
        JPanel outer = card("Activity Log");
        taLog = new JTextArea(9, 0);
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
        p.setBackground(new Color(230, 234, 240));
        p.setBorder(new MatteBorder(1, 0, 0, 0, C_BORDER));

        lblStatus = new JLabel("Ready — load a book to begin.");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblStatus.setForeground(C_GRAY);
        p.add(lblStatus);
        return p;
    }

    // ── ActionListener (Event Handling) ──────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnLoad) {
            handleLoad();
        } else if (src == btnIssue) {
            handleIssue();
        } else if (src == btnReturn) {
            handleReturn();
        } else if (src == btnDisplay) {
            handleDisplay();
        } else if (src == btnClear) {
            taLog.setText("");
            logCount = 0;
            setStatus("Log cleared.");
        }
    }

    private void handleLoad() {
        String idStr  = tfId.getText().trim();
        String title  = tfTitle.getText().trim();
        String author = tfAuthor.getText().trim();

        if (idStr.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr.replaceAll("[^0-9]", ""));
            if (id <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Book ID must contain a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        book = new Book();
        book.readDetails(id, title, author);   // Method 1: Read Details

        updateStats();
        btnIssue.setEnabled(true);
        btnReturn.setEnabled(true);
        btnDisplay.setEnabled(true);

        log("LOADED  | " + title + "  [Author: " + author + "]  Copies: 20/20");
        setStatus("Book loaded: \"" + title + "\"  —  20 copies available.");
    }

    private void handleIssue() {
        if (book == null) return;
        boolean ok = book.issueBook();   // Method 2: Issue
        if (ok) {
            updateStats();
            log("ISSUED  | \"" + book.getTitle() + "\"  —  " + book.getCopiesAvailable() + " remaining");
            setStatus("Book issued. " + book.getCopiesAvailable() + " copies remaining.");
        } else {
            log("FAILED  | Cannot issue — no copies available");
            setStatus("⚠ No copies available to issue.");
            JOptionPane.showMessageDialog(this, "No copies available!", "Issue Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleReturn() {
        if (book == null) return;
        boolean ok = book.returnBook();  // Method 3: Return
        if (ok) {
            updateStats();
            log("RETURNED| \"" + book.getTitle() + "\"  —  " + book.getCopiesAvailable() + " available");
            setStatus("Book returned. " + book.getCopiesAvailable() + " copies available.");
        } else {
            log("FAILED  | All copies already in library");
            setStatus("All copies already returned.");
        }
    }

    private void handleDisplay() {
        if (book == null) return;
        String details = book.displayDetails();  // Method 4: Display
        log("DETAILS |\n" + details);
        JOptionPane.showMessageDialog(this, details, "Book Details", JOptionPane.INFORMATION_MESSAGE);
        setStatus("Details displayed for Book ID: " + book.getBookId());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void updateStats() {
        if (book == null) return;
        int avail = book.getCopiesAvailable();
        pbAvail.setMaximum(20);
        pbAvail.setValue(avail);
        pbAvail.setString(avail + " / 20 available");

        if (avail == 0) {
            pbAvail.setForeground(C_RED);
            lblAvail.setText("Out of Stock");
            lblAvail.setForeground(C_RED);
        } else if (avail <= 5) {
            pbAvail.setForeground(new Color(180, 100, 0));
            lblAvail.setText("Low Stock");
            lblAvail.setForeground(new Color(180, 100, 0));
        } else {
            pbAvail.setForeground(C_GREEN);
            lblAvail.setText("In Stock");
            lblAvail.setForeground(C_GREEN);
        }
    }

    private void log(String msg) {
        logCount++;
        String entry = String.format("[%02d] %s%n", logCount, msg);
        taLog.append(entry);
        taLog.setCaretPosition(taLog.getDocument().getLength());
    }

    private void setStatus(String msg) {
        lblStatus.setText(msg);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(C_GRAY);
        return l;
    }

    private void styleAWTField(TextField tf) {
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBackground(new Color(250, 251, 253));
    }

    private JButton styledBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // card() helper: titled white panel with BorderLayout
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
        // Use system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(LibraryManagementSystem::new);
    }
}
