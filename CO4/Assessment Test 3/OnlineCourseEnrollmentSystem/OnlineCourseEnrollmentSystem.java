import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OnlineCourseEnrollmentSystem extends JFrame implements ActionListener {

    JTextField txtStudentId, txtStudentName, txtCourseName, txtCourseFee;
    JTextArea resultArea;
    JButton calculateBtn, clearBtn;

    public OnlineCourseEnrollmentSystem() {

        setTitle("Online Course Enrollment System");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Student ID:"));
        txtStudentId = new JTextField();
        add(txtStudentId);

        add(new JLabel("Student Name:"));
        txtStudentName = new JTextField();
        add(txtStudentName);

        add(new JLabel("Course Name:"));
        txtCourseName = new JTextField();
        add(txtCourseName);

        add(new JLabel("Course Fee (₹):"));
        txtCourseFee = new JTextField();
        add(txtCourseFee);

        calculateBtn = new JButton("Enroll");
        clearBtn = new JButton("Clear");

        calculateBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        add(calculateBtn);
        add(clearBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(new JLabel("Enrollment Details:"));
        add(new JScrollPane(resultArea));

        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == calculateBtn) {

            try {
                int studentId = Integer.parseInt(txtStudentId.getText());
                String studentName = txtStudentName.getText();
                String courseName = txtCourseName.getText();
                double courseFee = Double.parseDouble(txtCourseFee.getText());

                double registrationFee = 500;
                double scholarship = 0;

                if (courseFee > 20000) {
                    scholarship = courseFee * 0.15;
                }

                double payableAmount = courseFee - scholarship + registrationFee;

                resultArea.setText(
                        "Student ID: " + studentId + "\n" +
                        "Student Name: " + studentName + "\n" +
                        "Course Name: " + courseName + "\n" +
                        "Course Fee: ₹" + courseFee + "\n" +
                        "Registration Fee: ₹" + registrationFee + "\n" +
                        "Scholarship Discount: ₹" + scholarship + "\n" +
                        "Payable Amount: ₹" + payableAmount
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid details!");
            }
        }

        if (e.getSource() == clearBtn) {
            txtStudentId.setText("");
            txtStudentName.setText("");
            txtCourseName.setText("");
            txtCourseFee.setText("");
            resultArea.setText("");
        }
    }

    public static void main(String[] args) {
        new OnlineCourseEnrollmentSystem();
    }
}
