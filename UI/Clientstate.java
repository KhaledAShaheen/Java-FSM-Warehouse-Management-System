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
        Iterator<Client> allClients = warehouse.getClients();

        while (allClients.hasNext()) {
            Client client = (Client) (allClients.next());
            if (client.getClientID().equals(WarehouseContext.instance().getUser())) {
                System.out.println(client);
                return;
            }
        }
    }

    public void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        if (allProducts.hasNext() == false) {
            System.out.println("No products to print");
            return;
        }
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            System.out.println("Product ID: " + product.getId() + " Name: " + product.getName() + " Sale Price: "
                    + product.getSalePrice());
        }
    }

    public void showTransactions() {
        Iterator<Invoice> invoices = warehouse.getInvoiceList(WarehouseContext.instance().getUser());
        if (invoices.hasNext() == false) {
            System.out.println("No invoices to print");
            return;
        }
        while (invoices.hasNext()) {
            Invoice invoice = (Invoice) (invoices.next());
            System.out.println(invoice);
        }
    }

    public void showPayments() {
        Iterator<Double> payments = warehouse.getPayments(WarehouseContext.instance().getUser());
        int num = 1;
        if (payments.hasNext() == false) {
            System.out.println("No payments to print");
            return;
        }
        while (payments.hasNext()) {
            Double payment = (Double) (payments.next());
            System.out.println("amount " + num + ": " + payment);
            num++;
        }
    }

    public void cartState() {
        WarehouseContext.instance().changeState(4);
    }

    public void showProductsInWaitlist() {
        Iterator<Hold> waitlist = warehouse.getWaitListByClientId(WarehouseContext.instance().getUser());
        if (waitlist == null || waitlist.hasNext() == false) {
            System.out.println("No products in waitlist to print");
            return;
        }
        while (waitlist.hasNext()) {
            Hold hold = (Hold) (waitlist.next());
            System.out.println(hold);
        }

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
