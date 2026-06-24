import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AirlineTicketReservationSystem extends JFrame implements ActionListener {

    JTextField txtPassengerId, txtPassengerName, txtFlightNo, txtTicketFare, txtNoTickets;
    JTextArea resultArea;
    JButton calculateBtn, clearBtn;

    public AirlineTicketReservationSystem() {

        setTitle("Airline Ticket Reservation System");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("Passenger ID:"));
        txtPassengerId = new JTextField();
        add(txtPassengerId);

        add(new JLabel("Passenger Name:"));
        txtPassengerName = new JTextField();
        add(txtPassengerName);

        add(new JLabel("Flight Number:"));
        txtFlightNo = new JTextField();
        add(txtFlightNo);

        add(new JLabel("Ticket Fare (₹):"));
        txtTicketFare = new JTextField();
        add(txtTicketFare);

        add(new JLabel("Number of Tickets:"));
        txtNoTickets = new JTextField();
        add(txtNoTickets);

        calculateBtn = new JButton("Book Tickets");
        clearBtn = new JButton("Clear");

        calculateBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        add(calculateBtn);
        add(clearBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(new JLabel("Booking Details:"));
        add(new JScrollPane(resultArea));

        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == calculateBtn) {

            try {
                int passengerId = Integer.parseInt(txtPassengerId.getText());
                String passengerName = txtPassengerName.getText();
                String flightNo = txtFlightNo.getText();
                double ticketFare = Double.parseDouble(txtTicketFare.getText());
                int numberOfTickets = Integer.parseInt(txtNoTickets.getText());

                double totalCost = ticketFare * numberOfTickets;

                double discount = 0;
                if (numberOfTickets > 5) {
                    discount = totalCost * 0.05;
                }

                double amountAfterDiscount = totalCost - discount;
                double gst = amountAfterDiscount * 0.12;
                double finalAmount = amountAfterDiscount + gst;

                resultArea.setText(
                        "Passenger ID: " + passengerId + "\n" +
                        "Passenger Name: " + passengerName + "\n" +
                        "Flight Number: " + flightNo + "\n" +
                        "Ticket Fare: ₹" + ticketFare + "\n" +
                        "Number of Tickets: " + numberOfTickets + "\n" +
                        "Total Cost: ₹" + totalCost + "\n" +
                        "Discount: ₹" + discount + "\n" +
                        "GST (12%): ₹" + gst + "\n" +
                        "Final Amount: ₹" + finalAmount
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid details!");
            }
        }

        if (e.getSource() == clearBtn) {
            txtPassengerId.setText("");
            txtPassengerName.setText("");
            txtFlightNo.setText("");
            txtTicketFare.setText("");
            txtNoTickets.setText("");
            resultArea.setText("");
        }
    }

    public static void main(String[] args) {
        new AirlineTicketReservationSystem();
    }
}
