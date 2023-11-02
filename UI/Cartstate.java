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
        if (records.hasNext() == false) {
            System.out.println("No records to print");
            return;
        }
        while (records.hasNext()) {
            Record record = (Record) (records.next());
            System.out.println(record);
        }
    }

    public void addProduct() {
        String productId = WarehouseContext.getToken("Enter product's id");
        int quantity = WarehouseContext.getNumber("Enter product quantity");
        Record result = warehouse.addProductToWishlist(productId, quantity, WarehouseContext.instance().getUser());
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("Product wasn't found");
        }
    }

    public void removeProduct() {
        String productId = WarehouseContext.getToken("Enter product's id");
        boolean result = warehouse.removeProductFromWishList(productId, WarehouseContext.instance().getUser());
        if (result) {
            System.out.println("Product removed successfully");
        } else {
            System.out.println("Product wasn't found");
        }
    }

    public void changeProductQty() {
        String productId = WarehouseContext.getToken("Enter product's id");
        int newQuantity = WarehouseContext.getNumber("Enter the product's new Qunatity:");
        boolean result = warehouse.editProductQunatity(productId, WarehouseContext.instance().getUser(), newQuantity);
        if (result) {
            System.out.println("Edit Successful!");
        } else {
            System.out.println("Product wasn't found");
        }
    }

    public void checkOut() {
        Iterator<Record> records = warehouse.getWishList(WarehouseContext.instance().getUser());
        if (records.hasNext() == false) {
            System.out.println("Nothing to checkout, wishlist empty!");
            return;
        }
        if (WarehouseContext.yesOrNo("Confirm Order?")) {
            Iterator<Invoice> invoices = warehouse.createInvoice(WarehouseContext.instance().getUser());
            System.out.println("Order confirmed!");
            if (invoices == null) {
                System.out.println("nothing invoiced");
            } else {
                System.out.println("invoice created");
            }
        } else {
            System.out.println("Order NOT confirmed!");
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