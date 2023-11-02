import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Cartstate extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private JFrame frame;
    private AbstractButton showCartButton, addProductButton, removeProductButton,
            changeProdQtyButton,
            checkoutButton, logoutButtonCartState;

    private static Cartstate instance;
    private static final int EXIT = 0;

    private Cartstate() {
        super();
        warehouse = Warehouse.instance();
        // context = LibContext.instance();
    }

    public static Cartstate instance() {
        if (instance == null) {
            instance = new Cartstate();
        }
        return instance;
    }

    public void showProductsInWishlist() {
        Iterator<Record> records = warehouse.getWishList(WarehouseContext.instance().getUser());
        if (!records.hasNext()) {
            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(), "No products to show");
            return;
        }

        // Use a StringBuilder to accumulate product strings
        StringBuilder productListBuilder = new StringBuilder();
        while (records.hasNext()) {
            Record record = records.next();
            productListBuilder.append("Product ID: ").append(record.getProduct().getId())
                    .append(" Name: ").append(record.getProduct().getName())
                    .append(" Sale Price: ").append(record.getProduct().getSalePrice())
                    .append(" Amount: ").append(record.getQuantity())
                    .append("\n");
        }

        // Create a JTextArea to display your products
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setText(productListBuilder.toString());
        textArea.setEditable(false); // Make it read-only

        // Wrap JTextArea inside a JScrollPane in case of overflow
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Display the JScrollPane in a JOptionPane
        JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(), scrollPane, "Product List",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void addProduct() {
        JFrame frame = WarehouseContext.instance().getFrame();

        // Prompt for product ID
        String productId = JOptionPane.showInputDialog(frame, "Enter product's id:");
        if (productId == null || productId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Product ID is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop the operation if no ID is provided
        }

        // Prompt for quantity
        Integer quantity = null;
        while (quantity == null) {
            String quantityString = JOptionPane.showInputDialog(frame, "Enter product quantity:");
            try {
                quantity = Integer.parseInt(quantityString);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for quantity!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // Attempt to add the product to the wishlist
        Record result = warehouse.addProductToWishlist(productId, quantity, WarehouseContext.instance().getUser());
        if (result != null) {
            JOptionPane.showMessageDialog(frame, "Product added to wishlist:\n" + result, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Product wasn't found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeProduct() {
        JFrame frame = WarehouseContext.instance().getFrame();

        // Prompt for product ID
        String productId = JOptionPane.showInputDialog(frame, "Enter product's id:");
        if (productId == null || productId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Product ID is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop the operation if no ID is provided
        }

        // Attempt to remove the product from the wishlist
        boolean result = warehouse.removeProductFromWishList(productId, WarehouseContext.instance().getUser());
        if (result) {
            JOptionPane.showMessageDialog(frame, "Product removed successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Product wasn't found or could not be removed", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void changeProductQty() {
        JFrame frame = WarehouseContext.instance().getFrame();

        // Prompt for the product ID
        String productId = JOptionPane.showInputDialog(frame, "Enter product's id:");
        if (productId == null || productId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Product ID is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if no ID is entered
        }

        // Prompt for the new quantity
        String quantityString = JOptionPane.showInputDialog(frame, "Enter the product's new quantity:");
        // Handle cancel option and empty input
        if (quantityString == null || quantityString.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Quantity is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if no quantity is entered
        }

        // Parse the quantity string to an integer
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid number format for quantity.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return; // Exit if input is not a valid integer
        }

        // Attempt to change the product quantity
        boolean result = warehouse.editProductQunatity(productId, WarehouseContext.instance().getUser(), newQuantity);
        if (result) {
            JOptionPane.showMessageDialog(frame, "Edit Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Product wasn't found or could not be updated", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void checkOut() {
        JFrame frame = WarehouseContext.instance().getFrame();

        // Check if the wishlist is empty
        Iterator<Record> records = warehouse.getWishList(WarehouseContext.instance().getUser());
        if (!records.hasNext()) {
            JOptionPane.showMessageDialog(frame, "Nothing to checkout, wishlist is empty!", "Checkout",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Ask for order confirmation
        int confirm = JOptionPane.showConfirmDialog(frame, "Confirm Order?", "Checkout Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Process the checkout if confirmed
            Iterator<Invoice> invoices = warehouse.createInvoice(WarehouseContext.instance().getUser());
            if (invoices == null) {
                JOptionPane.showMessageDialog(frame, "An error occurred. No invoice was created.", "Checkout Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Order confirmed and invoice created!", "Order Confirmed",
                        JOptionPane.INFORMATION_MESSAGE);
                // You might want to display the invoices here or handle them as needed
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Order NOT confirmed!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void logout() {
        WarehouseContext.instance().changeState(0);
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.setTitle("Cart Menu"); // Changed title to match context

        // Set the BoxLayout for the content pane directly
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create and add the title label
        JLabel titleLabel = new JLabel("Cart Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.getContentPane().add(titleLabel);

        // Add some vertical space after the title
        frame.getContentPane().add(Box.createVerticalStrut(20));

        // Create instances of custom button classes for the specified buttons
        showCartButton = new ShowCartButton();
        showCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addProductButton = new AddProductCartButton();
        addProductButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeProductButton = new RemoveProductButton();
        removeProductButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        changeProdQtyButton = new ChangeProdQtyButton();
        changeProdQtyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        checkoutButton = new CheckoutButton();
        checkoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutButtonCartState = new LogoutButtonCartState();
        logoutButtonCartState.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the custom buttons to the frame's content pane
        frame.getContentPane().add(showCartButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(addProductButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(removeProductButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(changeProdQtyButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(checkoutButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(logoutButtonCartState);

        frame.setVisible(true);
        frame.validate(); // This is to validate the container after adding/removing components
        frame.repaint(); // Use repaint to refresh the GUI after making changes
        frame.toFront();
        frame.requestFocus();

    }
}