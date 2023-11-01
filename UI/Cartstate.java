import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.reflect.Member;

public class Cartstate extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private static Cartstate instance;
    private static final int EXIT = 0;
    private static final int VIEW_CART = 1;
    private static final int ADD_PRODUCT = 2;
    private static final int REMOVE_PRODUCT = 3;
    private static final int CHANGE_QTY = 4;
    private static final int CHECKOUT = 5;
    private static final int HELP = 6;

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
        System.out.println("Enter a number between 0 and 6 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(VIEW_CART + " to view cart");
        System.out.println(ADD_PRODUCT + " to add a product to cart");
        System.out.println(REMOVE_PRODUCT + " to remove a product from cart");
        System.out.println(CHANGE_QTY + " to change a product's quantity in cart");
        System.out.println(CHECKOUT + " to checkout");
        System.out.println(HELP + " for help");
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

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case VIEW_CART:
                    showProductsInWishlist();
                    break;
                case ADD_PRODUCT:
                    addProduct();
                    break;
                case REMOVE_PRODUCT:
                    removeProduct();
                    break;
                case CHANGE_QTY:
                    changeProductQty();
                    break;
                case CHECKOUT:
                    checkOut();
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
}