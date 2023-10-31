import java.util.*;
import java.text.*;

public class Clientstate extends WarehouseState {
    private static Clientstate clientstate;
    private static Warehouse warehouse;

    private static final int EXIT = 0;
    private static final int SHOW_CLIENT_DETAILS = 1;
    private static final int SHOW_PRODUCT_LIST = 2;
    private static final int SHOW_CLIENT_TRANSACTIONS = 3;
    private static final int MODIFY_SHOPPING_CART = 4;
    private static final int DISPLAY_WISHLIST = 5;
    private static final int PROCESS_ORDER = 6;
    private static final int LOGOUT = 7;
    private static final int HELP = 8;

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
        System.out.println("Enter a number between 0 and 8 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(SHOW_CLIENT_DETAILS + " to show client details");
        System.out.println(SHOW_PRODUCT_LIST + " to show products with sale price");
        System.out.println(SHOW_CLIENT_TRANSACTIONS + " to show transactions");
        System.out.println(MODIFY_SHOPPING_CART + " to modify shopping cart");
        System.out.println(DISPLAY_WISHLIST + " to display wishlist");
        System.out.println(PROCESS_ORDER + " to process order");
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

    public void modifyCartMenu() {
        System.out.println(1 + " Add products to shopping cart");
        System.out.println(2 + " Modify existing shopping cart");
        System.out.println("X" + " Anything else to exit");
    }

    public void modifyShoppingCart() {
        int choice;
        modifyCartMenu();
        choice = WarehouseContext.getNumber("");

        if (choice == 1) {

            Client client = warehouse.searchClient(WarehouseContext.instance().getUser());

            while (true) // Keep adding products until client says no
            {
                String productId = WarehouseContext.getToken("Enter product's id");
                int quantity = WarehouseContext.getNumber("Enter product quantity");
                Record result = warehouse.addProductToWishlist(productId, quantity, client);
                if (result != null) {
                    System.out.println(result);
                } else {
                    System.out.println("Product wasn't found");
                }
                if (!WarehouseContext.yesOrNo("Add more products?")) {
                    break;
                }

            }
        }

        else if (choice == 2) {
            Client client = warehouse.searchClient(WarehouseContext.instance().getUser());
            Iterator<Record> wishListItems = warehouse.getWishList(WarehouseContext.instance().getUser());
            WishList wishlistCopy = warehouse.generateWishListCopy(client);

            if (wishListItems.hasNext() == false) {
                System.out.println("No records to print");
                return;
            }
            while (wishListItems.hasNext()) {
                Record wishListItem = (Record) (wishListItems.next());
                System.out.println(wishListItem);
                System.out.println("1. Keep Product");
                System.out.println("2. Remove Product");
                System.out.println("3. Change Product Qunatity");
                int value = Integer.parseInt(WarehouseContext.getToken("Enter Choice:"));
                switch (value) {
                    case 1:
                        break;
                    case 2:
                        Boolean result1 = wishlistCopy.removeRecord(wishListItem.getProduct().getId());
                        if (result1) {
                            System.out.println("Remove Successful!");
                        }
                        break;
                    case 3:
                        int newQuantity = WarehouseContext.getNumber("Enter the product's new Qunatity:");
                        Boolean result2 = wishlistCopy.editQunatity(wishListItem.getProduct().getId(), newQuantity);
                        if (result2) {
                            System.out.println("Edit Successful!");
                        }
                        break;
                }

            }
            warehouse.updateWishList(client, wishlistCopy);
        }

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

    public void processOrder() {

        Client client = warehouse.searchClient(WarehouseContext.instance().getUser());

        Iterator<Record> wishListItems = warehouse.getWishList(WarehouseContext.instance().getUser());
        WishList wishlistCopy = warehouse.generateWishListCopy(client);

        if (wishListItems.hasNext() == false) {
            System.out.println("No records to print");
            return;
        }
        while (wishListItems.hasNext()) {
            Record wishListItem = (Record) (wishListItems.next());
            System.out.println(wishListItem);
            System.out.println("1. Keep Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Change Product Qunatity");
            int value = Integer.parseInt(WarehouseContext.getToken("Enter Choice:"));
            switch (value) {
                case 1:
                    break;
                case 2:
                    Boolean result1 = wishlistCopy.removeRecord(wishListItem.getProduct().getId());
                    if (result1) {
                        System.out.println("Remove Successful!");
                    }
                    break;
                case 3:
                    int newQuantity = WarehouseContext.getNumber("Enter the product's new Qunatity:");
                    Boolean result2 = wishlistCopy.editQunatity(wishListItem.getProduct().getId(), newQuantity);
                    if (result2) {
                        System.out.println("Edit Successful!");
                    }
                    break;
            }

        }
        if (WarehouseContext.yesOrNo("Confirm Order?")) {
            warehouse.updateWishList(client, wishlistCopy);
            List<Invoice> invoices = warehouse.createInvoice(client, wishlistCopy);

            for (int i = 0; i < invoices.size(); i++) {
                warehouse.addInvoiceToInvoiceList(invoices.get(i), client);
            }

            System.out.println("Order confirmed!");
            if (!invoices.isEmpty()) {
                System.out.println("Invoice:");
                System.out.println(invoices.toString());
            } else {
                System.out.println("nothing invoiced");
            }
        } else {
            System.out.println("Order NOT confirmed!");
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
                case MODIFY_SHOPPING_CART:
                    modifyShoppingCart();
                    break;
                case DISPLAY_WISHLIST:
                    showProductsInWishlist();
                    break;
                case PROCESS_ORDER:
                    processOrder();
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
