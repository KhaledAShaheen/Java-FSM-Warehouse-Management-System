import java.util.*;
import java.text.*;
import java.io.*;

public class Managerstate extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext Warehousecontext;
  private static Managerstate instance;

  private static final int EXIT = 0;
  private static final int ADD_PRODUCTS = 1;
  private static final int PROCESS_SHIPMENT = 2;
  private static final int CLERK_MENU = 3;
  private static final int LOGOUT = 4;
  private static final int HELP = 5;

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

  public void help() {
    System.out.println("Enter a number between 0 and 5 as explained below:");

    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_PRODUCTS + " to add product");
    System.out.println(PROCESS_SHIPMENT + " to receive a shipment");
    System.out.println(CLERK_MENU + " to become a clerk");
    System.out.println(LOGOUT + " to logout");
    System.out.println(HELP + " for help");
  }

  private boolean yesOrNo(String prompt) {
    String more = WarehouseContext.getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(WarehouseContext.getToken("Enter command: " + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {

        case ADD_PRODUCTS:
          addProduct();
          break;
        case PROCESS_SHIPMENT:
          processShippment();
          break;
        case CLERK_MENU:
          clerkMenu();
          break;
        case LOGOUT:
          logout();
          break;
        case HELP:
          help();
          break;
      }
    }
    logout();
  }

  public void run() {
    process();
  }

  public void addProduct() {
    Product result;
    do {
      String name = WarehouseContext.getToken("Enter name");
      int amount = WarehouseContext.getNumber("Enter amount");
      float saleprice = WarehouseContext.getFloat("Enter saleprice");
      result = warehouse.addProduct(name, amount, saleprice);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added");
      }
      if (!yesOrNo("Add more products?")) {
        break;
      }
    } while (true);
  }

  public void processShippment() {
    String productId = WarehouseContext.getToken("Enter product's id");
    Product result = warehouse.searchProduct(productId);
    if (result == null) {
      System.out.println("Invalid Product Id");
      return;
    }
    int quantity = WarehouseContext.getNumber("Enter product quantity");
    warehouse.addQtyToProduct(productId, quantity);
    Iterator<Hold> waitListItems = warehouse.getWaitListCopy(productId);
    if (waitListItems.hasNext() == false) {
      System.out.println("No holds to print");
      return;
    }
    int qtyLeft = 0;
    while (waitListItems.hasNext()) {
      Hold waitListItem = (Hold) (waitListItems.next());
      qtyLeft = result.getAmount();
      System.out.println("Quantity left in stock: " + qtyLeft);
      if (qtyLeft == 0) {
        System.out.println("Product amount in stock not sufficient.");
        break;
      }
      System.out.println(waitListItem);
      if (yesOrNo("Leave on WaitList?")) {
        System.out.println("Success!");
        continue;
      }
      if (yesOrNo("Keep with existing quantity?")) {
        Invoice invoice = warehouse.processShippment(productId, waitListItem.getClient().getClientID(),
            waitListItem.getAmount());
        if (invoice != null) {
          System.out.println("Invoice:");
          System.out.println(invoice.toString());
        }
        continue;
      }
      if (yesOrNo("Order with different qunatity?")) {
        int newQuantity = WarehouseContext.getNumber("Enter the product's new Qunatity:");
        Invoice invoice = warehouse.processShippment(productId, waitListItem.getClient().getClientID(),
            newQuantity);
        if (invoice != null) {
          System.out.println("Invoice:");
          System.out.println(invoice.toString());
        }
        continue;
      }

    }
  }

  public void clerkMenu() {
    (WarehouseContext.instance()).changeState(1);
  }

  public void logout() {
    (WarehouseContext.instance()).changeState(3);
  }
}
