import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.reflect.Member;

public class Clerkstate extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private static Clerkstate instance;
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int SHOW_PRODUCTS = 2;
    private static final int CLIENT_INFO_STATE = 3;
    private static final int ACCEPT_PAYMENT = 4;
    private static final int CLIENT_MENU = 5;
    private static final int SHOW_PRODUCTS_IN_WAITLIST = 6;
    private static final int LOGOUT = 7;
    private static final int HELP = 8;

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

    public static int getCommand() {
        do {
            try {
                int value = Integer.parseInt(WarehouseContext.getToken("Enter command:" + HELP + " for help"));
                if (value >= EXIT && value <= HELP) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public void help() {
        System.out.println("Enter a number between 0 and 8 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(ADD_CLIENT + " to add a client");
        System.out.println(SHOW_PRODUCTS + " to print products");
        System.out.println(CLIENT_INFO_STATE + " go to client info state");
        System.out.println(ACCEPT_PAYMENT + " to accept a client's payment");
        System.out.println(CLIENT_MENU + " to switch to the client menu");
        System.out.println(SHOW_PRODUCTS_IN_WAITLIST + " to show waitlist for a product");
        System.out.println(LOGOUT + " logout of clerk state");
        System.out.println(HELP + " for help");
    }

    public void addClient() {
        String name = WarehouseContext.getToken("Enter client name");
        String address = WarehouseContext.getToken("Enter address");
        Client result;
        result = warehouse.addClient(name, address);
        if (result == null) {
            System.out.println("Could not add client");
        }
        System.out.println(result);
    }

    public void showProducts() {
        Iterator<Product> allProducts = warehouse.getProducts();
        if (allProducts.hasNext() == false) {
            System.out.println("No products to print");
            return;
        }
        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            System.out.println(product);
        }
    }

    public void clientInfoState() {
        WarehouseContext.instance().changeState(5);
    }

    public void showProductsInWaitlist() {
        String productId = WarehouseContext.getToken("Enter product's id");
        Product product = warehouse.searchProduct(productId);
        if (product == null) {
            System.out.println("Product not found");
            return;
        }
        Iterator<Hold> holds = warehouse.getWaitList(productId);
        if (holds.hasNext() == false) {
            System.out.println("No holds to print");
            return;
        }
        while (holds.hasNext()) {
            Hold hold = (Hold) (holds.next());
            System.out.println(hold);
        }
    }

    public void acceptPayment() {
        String clientid = WarehouseContext.getToken("Enter client's id");
        Client client = warehouse.searchClient(clientid);
        if (client == null) {
            System.out.println("Invalid Client Id");
            return;
        }
        if (client.getBalance() == 0) {
            System.out.println("client balance is 0");
            return;
        }
        System.out.println("Client Balance: " + client.getBalance());
        while (true) {
            float payment = WarehouseContext.getFloat("Enter payment amount:");
            if (payment > client.getBalance()) {
                System.out.println("Enter payment less than or equal to " + client.getBalance());
            } else if (payment <= 0) {
                System.out.println("Enter payment more than 0 ");
            } else {
                warehouse.acceptPayment(payment, clientid);
                break;
            }
        }
    }

    public void clientMenu() {
        String userID = WarehouseContext.getToken("Please input the client id: ");
        if (Warehouse.instance().searchClient(userID) != null) {
            (WarehouseContext.instance()).setClient(userID);
            (WarehouseContext.instance()).changeState(0);
        } else
            System.out.println("Invalid client id.");
    }

    public void logout() {
        if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk) {

            (WarehouseContext.instance()).changeState(3);
        } else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsManager) {
            // to login \n");
            (WarehouseContext.instance()).changeState(2);
        } else
            (WarehouseContext.instance()).changeState(3);
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ADD_CLIENT:
                    addClient();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case CLIENT_INFO_STATE:
                    clientInfoState();
                    break;
                case ACCEPT_PAYMENT:
                    acceptPayment();
                    break;
                case CLIENT_MENU:
                    clientMenu();
                    break;
                case SHOW_PRODUCTS_IN_WAITLIST:
                    showProductsInWaitlist();
                    break;
                case HELP:
                    help();
                    break;
                case LOGOUT:
                    logout();
                    break;
            }
        }
        logout();
    }

    public void run() {
        process();
    }
}