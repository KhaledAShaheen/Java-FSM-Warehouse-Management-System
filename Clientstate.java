import java.util.*;
import java.text.*;

public class Clientstate extends WarehouseState {
    private static Clientstate clientstate;
    private static Warehouse warehouse;

    private static final int EXIT = 0;
    private static final int SHOW_CLIENT_DETAILS = 1;
    private static final int SHOW_PRODUCT_LIST = 2;
    private static final int SHOW_CLIENT_TRANSACTIONS = 3;
    private static final int SHOW_CLIENT_PAYMENTS = 4;
    private static final int CART_STATE = 5;
    private static final int DISPLAY_WAITLIST = 6;
    private static final int PROCESS_ORDER = 7;
    private static final int LOGOUT = 8;
    private static final int HELP = 9;

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

    public Calendar getDate(String prompt) {
        do {
            try {
                Calendar date = new GregorianCalendar();
                String item = WarehouseContext.getToken(prompt);
                DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                date.setTime(df.parse(item));
                return date;
            } catch (Exception fe) {
                System.out.println("Please input a date as mm/dd/yy");
            }
        } while (true);
    }

    public int getCommand() {
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
        System.out.println("Enter a number between 0 and 9 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(SHOW_CLIENT_DETAILS + " to show client details");
        System.out.println(SHOW_PRODUCT_LIST + " to show products with sale price");
        System.out.println(SHOW_CLIENT_TRANSACTIONS + " to show transactions");
        System.out.println(SHOW_CLIENT_PAYMENTS + " to show payments");
        System.out.println(CART_STATE + " go to cart state");
        System.out.println(DISPLAY_WAITLIST + " to display client's waitlist");
        System.out.println(LOGOUT + " to logout");
        System.out.println(HELP + " for help");
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
            System.out.println(product);
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
        if (waitlist.hasNext() == false) {
            System.out.println("No products in waitlist to print");
            return;
        }
        while (waitlist.hasNext()) {
            Hold hold = (Hold) (waitlist.next());
            System.out.println(hold);
        }

    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {

                case SHOW_CLIENT_DETAILS:
                    showClientDetails();
                    break;
                case SHOW_PRODUCT_LIST:
                    showProducts();
                    break;
                case SHOW_CLIENT_TRANSACTIONS:
                    showTransactions();
                    break;
                case SHOW_CLIENT_PAYMENTS:
                    showPayments();
                    break;
                case CART_STATE:
                    cartState();
                    break;
                case DISPLAY_WAITLIST:
                    showProductsInWaitlist();
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

    public void logout() {
        if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk) { // stem.out.println(" going to clerk
                                                                                    // \n ");
            (WarehouseContext.instance()).changeState(1); // exit with a code 1
        } else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsClient) { // stem.out.println(" going to
                                                                                          // login \n");
            (WarehouseContext.instance()).changeState(3); // exit with a code 3
        } else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsManager) { // stem.out.println(" going
                                                                                           // to login \n");
            (WarehouseContext.instance()).changeState(2); // exit with a code 2
        } else
            (WarehouseContext.instance()).changeState(4); // exit code 4, indicates error
    }

}
