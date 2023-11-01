import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Managerstate extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext Warehousecontext;
  private static Managerstate instance;
  private JFrame frame;
  private AbstractButton addProductsButton, processShipmentButton, clerkMenuButton, logoutButton;

  private Managerstate() {
    super();
    warehouse = Warehouse.instance();
    // context = LibContext.instance();
  }

  public static Managerstate instance() {
    if (instance == null) {
      instance = new Managerstate();
    }
    return instance;
  }

  public void addProduct() {
    Product result;
    String name = JOptionPane.showInputDialog(
        Loginstate.instance().getFrame(), "Enter name: ");

    int amount = Integer.parseInt(JOptionPane.showInputDialog(
        Loginstate.instance().getFrame(), "Enter amount: "));

    float saleprice = Float.parseFloat(JOptionPane.showInputDialog(
        Loginstate.instance().getFrame(), "Enter saleprice: "));

    result = warehouse.addProduct(name, amount, saleprice);
    if (result != null) {
      JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), result);

    } else {
      JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Product could not be added");
    }
    run();

  }

  public void processShippment() {
    JFrame parentFrame = Loginstate.instance().getFrame();

    String productId = JOptionPane.showInputDialog(parentFrame, "Enter product's id");
    Product result = warehouse.searchProduct(productId);

    if (result == null) {
      JOptionPane.showMessageDialog(parentFrame, "Invalid Product Id");
      run();
      return;
    }

    String qtyStr = JOptionPane.showInputDialog(parentFrame, "Enter product quantity");
    int quantity;
    try {
      quantity = Integer.parseInt(qtyStr);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(parentFrame, "Invalid quantity entered.");
      run();
      return;
    }
    warehouse.addQtyToProduct(productId, quantity);

    Iterator<Hold> waitListItems = warehouse.getWaitListCopy(productId);

    if (!waitListItems.hasNext()) {
      JOptionPane.showMessageDialog(parentFrame, "No waitlist items to print");
      run();
      return;
    }

    while (waitListItems.hasNext()) {
      Hold waitListItem = (Hold) waitListItems.next();
      int qtyLeft = result.getAmount();

      JOptionPane.showMessageDialog(parentFrame, "Quantity left in stock: " + qtyLeft);

      if (qtyLeft == 0) {
        JOptionPane.showMessageDialog(parentFrame, "Product amount in stock not sufficient.");
        break;
      }

      int option = JOptionPane.showOptionDialog(parentFrame,
          "Waitlist item details:\n" + waitListItem.toString(),
          "Waitlist Item",
          JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          new String[] { "Leave on WaitList", "Keep with existing quantity", "Order with different quantity" },
          "Leave on WaitList");

      switch (option) {
        case 0: // Leave on WaitList
          JOptionPane.showMessageDialog(parentFrame, "Success!");
          break;
        case 1: // Keep with existing quantity
          Invoice invoice1 = warehouse.processShippment(productId, waitListItem.getClient().getClientID(),
              waitListItem.getAmount());
          if (invoice1 != null) {
            JOptionPane.showMessageDialog(parentFrame, "Invoice:\n" + invoice1.toString());
          }
          break;
        case 2: // Order with different quantity
          String newQtyStr = JOptionPane.showInputDialog(parentFrame, "Enter the product's new Quantity:");
          int newQuantity;
          try {
            newQuantity = Integer.parseInt(newQtyStr);
          } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parentFrame, "Invalid quantity entered.");
            continue;
          }
          Invoice invoice2 = warehouse.processShippment(productId, waitListItem.getClient().getClientID(), newQuantity);
          if (invoice2 != null) {
            JOptionPane.showMessageDialog(parentFrame, "Invoice:\n" + invoice2.toString());
          }
          break;
        default:
          break;
      }
    }
    run();

  }

  public void clerkMenu() {
    (WarehouseContext.instance()).changeState(1);
  }

  public void logout() {
    (WarehouseContext.instance()).changeState(3);
  }

  public void run() {

    frame = WarehouseContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.setTitle("Manager");

    // Set the BoxLayout for the content pane directly
    frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

    // Create and add the title label
    JLabel titleLabel = new JLabel("Hi Boss! Manager Menu", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
    frame.getContentPane().add(titleLabel);

    // Add some vertical space after the title
    frame.getContentPane().add(Box.createVerticalStrut(20));

    addProductsButton = new AddProductButton();
    addProductsButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
    processShipmentButton = new ProcessShipmentButton();
    processShipmentButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
    clerkMenuButton = new ClerkMenuButton();
    clerkMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
    logoutButton = new logoutButtonManager();
    logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button

    // Add buttons to the frame's content pane
    frame.getContentPane().add(addProductsButton);
    frame.getContentPane().add(Box.createVerticalStrut(10)); // Add space between buttons
    frame.getContentPane().add(processShipmentButton);
    frame.getContentPane().add(Box.createVerticalStrut(10)); // Add space between buttons
    frame.getContentPane().add(clerkMenuButton);
    frame.getContentPane().add(Box.createVerticalStrut(10)); // Add space between buttons
    frame.getContentPane().add(logoutButton);

    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
  }

}
