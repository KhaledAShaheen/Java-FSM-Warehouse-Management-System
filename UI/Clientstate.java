import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Clientstate extends WarehouseState {
    private static Clientstate clientstate;
    private static Warehouse warehouse;
    private JFrame frame;
    private AbstractButton showClientDetailsButton, showProductListButton, showClientTransactionsButton,
            showClientPaymentsButton,
            cartStateButton, displayWaitlistButton, logoutButtonClient;

    private Clientstate() {
        warehouse = Warehouse.instance();
    }

    public static Clientstate instance() {
        if (clientstate == null) {
            return clientstate = new Clientstate();
        } else {
            return clientstate;
        }
    }

    public void showClientDetails() {
        // Assuming WarehouseContext.getUser() returns a String ID
        String clientId = WarehouseContext.instance().getUser();
        Iterator<Client> allClients = warehouse.getClients();

        while (allClients.hasNext()) {
            Client client = allClients.next();
            if (client.getClientID().equals(clientId)) {
                // Assuming Client class overrides toString to provide meaningful information
                JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                        "Client Details:\n" + client,
                        "Client Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        // If no client matches, inform the user
        JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                "No details found for the client with ID: " + clientId,
                "Client Not Found",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        // Check if there are no products to display
        if (!allProducts.hasNext()) {
            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                    "No products to print", "Product List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Use StringBuilder to accumulate product details
        StringBuilder productsInfo = new StringBuilder();

        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            productsInfo.append("Product ID: ")
                    .append(product.getId())
                    .append(", Name: ")
                    .append(product.getName())
                    .append(", Sale Price: ")
                    .append(product.getSalePrice())
                    .append("\n"); // Add a new line for each product
        }

        // Create a JTextArea to display the products information
        JTextArea textArea = new JTextArea(productsInfo.toString());
        textArea.setEditable(false); // Make the text area non-editable
        JScrollPane scrollPane = new JScrollPane(textArea); // Add text area to scroll pane

        // Show the scroll pane inside a JOptionPane
        JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                scrollPane, "Product List", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showTransactions() {
        Iterator<Invoice> invoices = warehouse.getInvoiceList(WarehouseContext.instance().getUser());

        // Check if there are no invoices to display
        if (!invoices.hasNext()) {
            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                    "No invoices to print", "Transaction List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Use StringBuilder to accumulate invoice details
        StringBuilder invoiceInfo = new StringBuilder();

        while (invoices.hasNext()) {
            Invoice invoice = invoices.next();
            // Assuming Invoice class has a meaningful toString implementation
            invoiceInfo.append(invoice.toString()).append("\n");
        }

        // Create a JTextArea to display the invoices information
        JTextArea textArea = new JTextArea(invoiceInfo.toString());
        textArea.setEditable(false); // Make the text area non-editable
        JScrollPane scrollPane = new JScrollPane(textArea); // Add text area to a scroll pane

        // Show the scroll pane inside a JOptionPane
        JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                scrollPane, "Transaction List", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showPayments() {
        Iterator<Double> payments = warehouse.getPayments(WarehouseContext.instance().getUser());
        int num = 1; // Counter for numbering the payments
        StringBuilder paymentsInfo = new StringBuilder();

        if (!payments.hasNext()) {
            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                    "No payments to print", "Payment List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        while (payments.hasNext()) {
            Double payment = payments.next();
            paymentsInfo.append("Payment ").append(num).append(": ").append(payment).append("\n");
            num++;
        }

        JTextArea textArea = new JTextArea(paymentsInfo.toString());
        textArea.setEditable(false); // The text area should not be editable by the user
        JScrollPane scrollPane = new JScrollPane(textArea); // Make the text area scrollable

        JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                scrollPane, "Payment List", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cartState() {
        WarehouseContext.instance().changeState(4);
    }

    public void showProductsInWaitlist() {
        Iterator<Hold> waitlist = warehouse.getWaitListByClientId(WarehouseContext.instance().getUser());

        // Check if the waitlist is empty or null
        if (waitlist == null || !waitlist.hasNext()) {
            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                    "No products in waitlist to print", "Waitlist", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Use a StringBuilder to accumulate the hold details
        StringBuilder waitlistInfo = new StringBuilder();

        while (waitlist.hasNext()) {
            Hold hold = waitlist.next();
            // Assuming Hold class has a meaningful toString implementation
            waitlistInfo.append(hold.toString()).append("\n");
        }

        // Create a JTextArea to display the waitlist information
        JTextArea textArea = new JTextArea(waitlistInfo.toString());
        textArea.setEditable(false); // Make the text area non-editable
        JScrollPane scrollPane = new JScrollPane(textArea); // Add text area to a scroll pane

        // Show the scroll pane inside a JOptionPane
        JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                scrollPane, "Waitlist", JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.setTitle("Client Menu");

        // Set the BoxLayout for the content pane directly
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create and add the title label
        JLabel titleLabel = new JLabel("Client Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        frame.getContentPane().add(titleLabel);

        // Add some vertical space after the title
        frame.getContentPane().add(Box.createVerticalStrut(20));

        // Create instances of custom button classes
        showClientDetailsButton = new ShowClientDetailsButton();
        showClientDetailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        showProductListButton = new ShowProductListButton();
        showProductListButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        showClientTransactionsButton = new ShowClientTransactionsButton();
        showClientTransactionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        showClientPaymentsButton = new ShowClientPaymentsButton();
        showClientPaymentsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        cartStateButton = new CartStateButton();
        cartStateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        displayWaitlistButton = new DisplayWaitlistButton();
        displayWaitlistButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutButtonClient = new LogoutButtonClient();
        logoutButtonClient.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the custom buttons to the frame's content pane
        frame.getContentPane().add(showClientDetailsButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(showProductListButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(showClientTransactionsButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(showClientPaymentsButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(cartStateButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(displayWaitlistButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(logoutButtonClient);

        frame.setVisible(true);
        frame.repaint(); // Use repaint instead of paint for GUI refresh
        frame.toFront();
        frame.requestFocus();

    }

    public void logout() {
        if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk) { // stem.out.println(" going to clerk
                                                                                    // \n ");
            (WarehouseContext.instance()).changeState(1); // exit with a code 1
        } else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsClient) { // stem.out.println(" going to
                                                                                          // login \n");
            (WarehouseContext.instance()).changeState(3); // exit with a code 3
        } else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsManager) { // stem.out.println(" going
                                                                                           // to login \n");
            (WarehouseContext.instance()).changeState(1); // exit with a code 1
        } else
            (WarehouseContext.instance()).changeState(4); // exit code 4, indicates error
    }

}
