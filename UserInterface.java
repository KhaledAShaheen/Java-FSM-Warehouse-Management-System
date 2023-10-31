import java.util.*;
import java.text.*;
import java.io.*;

public class UserInterface {
    private static UserInterface userInterface;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;

    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int ADD_PRODUCTS = 2;
    private static final int ADD_PRODUCTS_TO_WISHLIST = 3;
    private static final int PROCESS_ORDER = 4;
    private static final int PROCESS_SHIPMENT = 5;
    private static final int SHOW_CLIENTS = 6;
    private static final int SHOW_PRODUCTS = 7;
    private static final int SHOW_PRODUCTS_IN_WISHLIST = 8;
    private static final int SHOW_PRODUCTS_IN_WAITLIST = 9;
    private static final int SHOW_CLIENTS_WITH_BLANACE = 10;
    private static final int SAVE = 11;
    private static final int RETRIEVE = 12;
    private static final int HELP = 13;

    private UserInterface() {
        if (yesOrNo("Look for saved data and use it?")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
    }

    public static UserInterface instance() {
        if (userInterface == null) {
            return userInterface = new UserInterface();
        } else {
            return userInterface;
        }
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
    }

    public int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num.intValue();
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }

    public float getFloat(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Float num = Float.valueOf(item);
                return num.intValue();
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }

    public Calendar getDate(String prompt) {
        do {
            try {
                Calendar date = new GregorianCalendar();
                String item = getToken(prompt);
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
                int value = Integer.parseInt(getToken("\nEnter command: " + HELP + " for help"));
                if (value >= EXIT && value <= HELP) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public void help() {
        System.out.println("Enter a number between 0 and 13 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(ADD_CLIENT + " to add a client");
        System.out.println(ADD_PRODUCTS + " to add products");
        System.out.println(ADD_PRODUCTS_TO_WISHLIST + " to add products to a client's wishlist");
        System.out.println(PROCESS_ORDER + " to process a client's order");
        System.out.println(PROCESS_SHIPMENT + " to process a product's shipment");
        System.out.println(SHOW_CLIENTS + " to print clients");
        System.out.println(SHOW_PRODUCTS + " to print products");
        System.out.println(SHOW_PRODUCTS_IN_WISHLIST + " to print a client's wishlist items");
        System.out.println(SHOW_PRODUCTS_IN_WAITLIST + " to print a product's waitlist items");
        System.out.println(SHOW_CLIENTS_WITH_BLANACE + " to print a client's with an outstanding balance");
        System.out.println(SAVE + " to save data");
        System.out.println(RETRIEVE + " to retrieve");
        System.out.println(HELP + " for help");
        // yesOrNo(null);
    }

    public void addClient() {
        String name = getToken("Enter client name");
        String address = getToken("Enter address");
        Client result;
        result = warehouse.addClient(name, address);
        if (result == null) {
            System.out.println("Could not add client");
        }
        System.out.println(result);
    }

    public void addProduct() {
        Product result;
        do {
            String name = getToken("Enter name");
            int amount = getNumber("Enter amount");
            float saleprice = getFloat("Enter saleprice");
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

    public void addProductsToWishlist() {
        String clientid = getToken("Enter client's id");
        Client client = warehouse.searchClient(clientid);
        if (client == null) {
            System.out.println("Invalid Client Id");
            return;
        }
        while (true) // Keep adding products until client says no
        {
            String productId = getToken("Enter product's id");
            int quantity = getNumber("Enter product quantity");
            Record result = warehouse.addProductToWishlist(productId, quantity, client);
            if (result != null) {
                System.out.println(result);
            } else {
                System.out.println("Product wasn't found");
            }
            if (!yesOrNo("Add more products?")) {
                break;
            }

        }
    }

    public void processOrder() {
        String clientId = getToken("Enter client's id");
        Client client = warehouse.searchClient(clientId);
        if (client == null) {
            System.out.println("Invalid Client Id");
            return;
        }

        Iterator<Record> wishListItems = warehouse.getWishList(clientId);
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
            int value = Integer.parseInt(getToken("Enter Choice:"));
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
                    int newQuantity = getNumber("Enter the product's new Qunatity:");
                    Boolean result2 = wishlistCopy.editQunatity(wishListItem.getProduct().getId(), newQuantity);
                    if (result2) {
                        System.out.println("Edit Successful!");
                    }
                    break;
            }

        }
        if (yesOrNo("Confirm Order?")) {
            warehouse.updateWishList(client, wishlistCopy);
            List<Invoice> invoices = warehouse.createInvoice(client, wishlistCopy);
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

    public void processShippment() {
        String productId = getToken("Enter product's id");
        Product result = warehouse.searchProduct(productId);
        if (result == null) {
            System.out.println("Invalid Product Id");
            return;
        }
        int quantity = getNumber("Enter product quantity");
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
                int newQuantity = getNumber("Enter the product's new Qunatity:");
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

    public void showClients() {
        Iterator<Client> allClients = warehouse.getClients();
        if (allClients.hasNext() == false) {
            System.out.println("No clients to print");
            return;
        }
        while (allClients.hasNext()) {
            Client client = (Client) (allClients.next());
            System.out.println(client);
        }
    }

    public void showClientsWithBalance() {
        Iterator<Client> allClients = warehouse.getClients();
        if (allClients.hasNext() == false) {
            System.out.println("No clients to print");
            return;
        }
        while (allClients.hasNext()) {
            Client client = (Client) (allClients.next());
            if (client.getBalance() > 0) {
                System.out.println(client);
            }
        }
    }

    public void showProductsInWishlist() {
        String clientId = getToken("Enter client's id");
        Client client = warehouse.searchClient(clientId);
        if (client == null) {
            System.out.println("Client not found");
            return;
        }
        Iterator<Record> records = warehouse.getWishList(clientId);
        if (records.hasNext() == false) {
            System.out.println("No records to print");
            return;
        }
        while (records.hasNext()) {
            Record record = (Record) (records.next());
            System.out.println(record);
        }
    }

    public void showProductsInWaitlist() {
        String productId = getToken("Enter product's id");
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

    private void save() {
        if (Warehouse.save()) {
            System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n");
        } else {
            System.out.println(" There has been an error in saving \n");
        }
    }

    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println(" The warehouse has been successfully retrieved from the file LibraryData \n");
                warehouse = tempWarehouse;
            } else {
                System.out.println("File doesnt exist; creating new warehouse");
                warehouse = Warehouse.instance();
            }
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ADD_CLIENT:
                    addClient();
                    break;
                case ADD_PRODUCTS:
                    addProduct();
                    break;
                case ADD_PRODUCTS_TO_WISHLIST:
                    addProductsToWishlist();
                    break;
                case PROCESS_ORDER:
                    processOrder();
                    break;
                case PROCESS_SHIPMENT:
                    processShippment();
                    break;
                case SAVE:
                    save();
                    break;
                case RETRIEVE:
                    retrieve();
                    break;
                case SHOW_CLIENTS:
                    showClients();
                    break;
                case SHOW_PRODUCTS:
                    showProducts();
                    break;
                case SHOW_PRODUCTS_IN_WISHLIST:
                    showProductsInWishlist();
                    break;
                case HELP:
                    help();
                    break;
                case SHOW_PRODUCTS_IN_WAITLIST:
                    showProductsInWaitlist();
                    break;
                case SHOW_CLIENTS_WITH_BLANACE:
                    showClientsWithBalance();
                    break;
            }
        }
    }

    public static void main(String[] s) {
        UserInterface.instance().process();
    }

}
