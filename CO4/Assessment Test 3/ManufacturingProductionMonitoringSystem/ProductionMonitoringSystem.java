import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProductionMonitoringSystem extends JFrame implements ActionListener {

    JTextField txtProductId, txtProductName, txtTargetQty, txtProducedQty;
    JTextArea resultArea;
    JButton calculateBtn, clearBtn;

    public ProductionMonitoringSystem() {
        setTitle("Manufacturing Production Monitoring System");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Product ID:"));
        txtProductId = new JTextField();
        add(txtProductId);

        add(new JLabel("Product Name:"));
        txtProductName = new JTextField();
        add(txtProductName);

        add(new JLabel("Target Quantity:"));
        txtTargetQty = new JTextField();
        add(txtTargetQty);

        add(new JLabel("Produced Quantity:"));
        txtProducedQty = new JTextField();
        add(txtProducedQty);

        calculateBtn = new JButton("Calculate");
        clearBtn = new JButton("Clear");

        calculateBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        add(calculateBtn);
        add(clearBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(new JLabel("Result:"));
        add(new JScrollPane(resultArea));

        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == calculateBtn) {
            try {
                int productId = Integer.parseInt(txtProductId.getText());
                String productName = txtProductName.getText();
                double targetQty = Double.parseDouble(txtTargetQty.getText());
                double producedQty = Double.parseDouble(txtProducedQty.getText());

                double efficiency = (producedQty / targetQty) * 100;

                String status;
                if (producedQty >= targetQty) {
                    status = "Achieved";
                } else {
                    status = "Not Achieved";
                }

                resultArea.setText(
                        "Product ID: " + productId + "\n" +
                        "Product Name: " + productName + "\n" +
                        "Target Quantity: " + targetQty + "\n" +
                        "Produced Quantity: " + producedQty + "\n" +
                        "Efficiency: " + String.format("%.2f", efficiency) + "%\n" +
                        "Production Status: " + status
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid values!");
            }
        }

        if (e.getSource() == clearBtn) {
            txtProductId.setText("");
            txtProductName.setText("");
            txtTargetQty.setText("");
            txtProducedQty.setText("");
            resultArea.setText("");
        }
    }

    public static void main(String[] args) {
        new ProductionMonitoringSystem();
    }
}
