import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class VehicleRental extends JFrame implements ActionListener {

    JTextField txtId, txtName, txtDays, txtLateDays;
    JComboBox<String> vehicleBox;
    JTextArea resultArea;
    JButton calculateBtn, clearBtn;

    VehicleRental() {
        setTitle("Vehicle Rental Management System");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("Customer ID:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Vehicle Type:"));
        vehicleBox = new JComboBox<>(new String[]{"Car", "Bike"});
        add(vehicleBox);

        add(new JLabel("Rental Days:"));
        txtDays = new JTextField();
        add(txtDays);

        add(new JLabel("Late Return Days:"));
        txtLateDays = new JTextField();
        add(txtLateDays);

        calculateBtn = new JButton("Calculate Bill");
        clearBtn = new JButton("Clear");

        calculateBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        add(calculateBtn);
        add(clearBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(new JLabel("Rental Bill:"));
        add(new JScrollPane(resultArea));

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateBtn) {
            try {
                int customerId = Integer.parseInt(txtId.getText());
                String customerName = txtName.getText();
                String vehicleType = vehicleBox.getSelectedItem().toString();
                int rentalDays = Integer.parseInt(txtDays.getText());
                int lateDays = Integer.parseInt(txtLateDays.getText());

                double ratePerDay, penaltyPerDay;

                if (vehicleType.equals("Car")) {
                    ratePerDay = 2000;
                    penaltyPerDay = 500;
                } else {
                    ratePerDay = 500;
                    penaltyPerDay = 100;
                }

                double rentalCharge = rentalDays * ratePerDay;
                double penalty = lateDays * penaltyPerDay;
                double finalBill = rentalCharge + penalty;

                resultArea.setText(
                        "Customer ID: " + customerId + "\n" +
                        "Customer Name: " + customerName + "\n" +
                        "Vehicle Type: " + vehicleType + "\n" +
                        "Rental Days: " + rentalDays + "\n" +
                        "Rental Charge: ₹" + rentalCharge + "\n" +
                        "Late Penalty: ₹" + penalty + "\n" +
                        "Final Bill: ₹" + finalBill);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid inputs!");
            }
        }

        if (e.getSource() == clearBtn) {
            txtId.setText("");
            txtName.setText("");
            txtDays.setText("");
            txtLateDays.setText("");
            resultArea.setText("");
        }
    }

    public static void main(String[] args) {
        new VehicleRental();
    }
}
