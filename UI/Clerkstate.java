import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Clerkstate extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private static Clerkstate instance;
    private JFrame frame;
    private AbstractButton addClientButton, showProductsButton, switchToClientInfoStateButton, acceptPaymentButton,
            clientMenuButton, showProductsInWaitlistButton, logoutButton;

    private Clerkstate() {
        super();
        warehouse = Warehouse.instance();
        // context = LibContext.instance();
    }

    public static Clerkstate instance() {
        if (instance == null) {
            instance = new Clerkstate();
        }
        return instance;
    }

    public void addClient() {
        String name = JOptionPane.showInputDialog(
                Loginstate.instance().getFrame(), "Enter client name:");

        String address = JOptionPane.showInputDialog(
                Loginstate.instance().getFrame(), "Enter address:");
        // Make sure name and address are not empty before proceeding to add a client
        if (name != null && !name.trim().isEmpty() && address != null && !address.trim().isEmpty()) {
            Client result = warehouse.addClient(name, address);

            if (result != null) {
                // Assuming Client class overrides toString to provide meaningful information
                JOptionPane.showMessageDialog(Loginstate.instance().getFrame(),
                        "Client added successfully:\n" + result);
            } else {
                JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Client could not be added");
            }
        } else {
            // Inform the user that the name and address are required
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(),
                    "Name and address are required to add a client.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        if (!allProducts.hasNext()) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "No products to show");
            return;
        }

        // Use a StringBuilder to accumulate product strings
        StringBuilder productListBuilder = new StringBuilder();
        while (allProducts.hasNext()) {
            Product product = allProducts.next();
            productListBuilder.append(product.toString()).append("\n");
        }

        // Create a JTextArea to display your products
        JTextArea textArea = new JTextArea(20, 25);
        textArea.setText(productListBuilder.toString());
        textArea.setEditable(false); // Make it read-only

        // Wrap JTextArea inside a JScrollPane in case of overflow
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Display the JScrollPane in a JOptionPane
        JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), scrollPane);
    }

    public void clientInfoState() {
        WarehouseContext.instance().changeState(5);
    }

    public void showProductsInWaitlist() {
        String productId = JOptionPane.showInputDialog(
                Loginstate.instance().getFrame(), "Enter product's id:");

        if (productId == null || productId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Product ID is required.");
            return;
        }

        Product product = warehouse.searchProduct(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Product not found");
            return;
        }

        Iterator<Hold> holds = warehouse.getWaitList(productId);
        if (!holds.hasNext()) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "No holds to show for this product");
            return;
        }

        StringBuilder holdsListBuilder = new StringBuilder();
        while (holds.hasNext()) {
            Hold hold = holds.next();
            holdsListBuilder.append(hold.toString()).append("\n");
        }

        JTextArea textArea = new JTextArea(20, 25);
        textArea.setText(holdsListBuilder.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), scrollPane, "Products in Waitlist",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void acceptPayment() {
        String clientid = JOptionPane.showInputDialog(
                Loginstate.instance().getFrame(), "Enter client's id:");

        if (clientid == null || clientid.trim().isEmpty()) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Client ID is required.");
            return;
        }
        Client client = warehouse.searchClient(clientid);
        if (client == null) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Invalid Client Id");
            return;
        }
        if (client.getBalance() == 0) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Client balance is 0");
            return;
        }
        JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Client Balance: " + client.getBalance());
        while (true) {
            String paymentStr = JOptionPane.showInputDialog(
                    Loginstate.instance().getFrame(), "Enter payment amount:");
            // Validate if payment is a valid float and within the range
            try {
                float payment = Float.parseFloat(paymentStr);
                if (payment > client.getBalance()) {
                    JOptionPane.showMessageDialog(Loginstate.instance().getFrame(),
                            "Enter payment less than or equal to " + client.getBalance());
                } else if (payment <= 0) {
                    JOptionPane.showMessageDialog(Loginstate.instance().getFrame(),
                            "Enter payment more than 0");
                } else {
                    warehouse.acceptPayment(payment, clientid);
                    JOptionPane.showMessageDialog(Loginstate.instance().getFrame(),
                            "Payment accepted. New Balance: " + client.getBalance());
                    break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(Loginstate.instance().getFrame(),
                        "Please enter a valid payment amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void clientMenu() {
        String userID = JOptionPane.showInputDialog(
                WarehouseContext.instance().getFrame(), "Please input the client id: ");
        if (userID == null || userID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    WarehouseContext.instance().getFrame(), "Client ID is required.");
            run();
            return;
        }
        if (Warehouse.instance().searchClient(userID) != null) {
            WarehouseContext.instance().setClient(userID);
            WarehouseContext.instance().changeState(0);
            // Assuming changeState(0) refreshes the UI to the appropriate client context
        } else {
            JOptionPane.showMessageDialog(
                    WarehouseContext.instance().getFrame(), "Invalid client id.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.setTitle("Clerk");

        // Set the BoxLayout for the content pane directly
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create and add the title label
        JLabel titleLabel = new JLabel("Clerk Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        frame.getContentPane().add(titleLabel);

        // Add some vertical space after the title
        frame.getContentPane().add(Box.createVerticalStrut(20));

        // Create buttons for the required actions
        addClientButton = new AddClientButton();
        addClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        showProductsButton = new ShowProductsButton();
        showProductsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        switchToClientInfoStateButton = new SwitchToClientInfoStateButton();
        switchToClientInfoStateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        acceptPaymentButton = new AcceptPaymentButton();
        acceptPaymentButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        clientMenuButton = new ClientMenuButton();
        clientMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        showProductsInWaitlistButton = new ShowProductsInWaitlistButton();
        showProductsInWaitlistButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutButton = new LogoutButtonClerk();
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add buttons to the frame's content pane
        frame.getContentPane().add(addClientButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(showProductsButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(switchToClientInfoStateButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(acceptPaymentButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(clientMenuButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(showProductsInWaitlistButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(logoutButton);

        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();

    }
}