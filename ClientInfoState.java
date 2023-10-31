import java.util.*;
import java.text.*;
import java.time.LocalDate;
import java.io.*;

public class ClientInfoState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext Warehousecontext;
    private static ClientInfoState instance;

    private static final int EXIT = 0;
    private static final int DISPLAY_CLIENTS = 1;
    private static final int DISPLAY_CLIENTS_OUTSTANDING_BALANCE = 2;
    private static final int DISPLAY_CLIENTS_NO_TRANSACTION = 3;
    private static final int HELP = 4;

    private ClientInfoState() {
        super();
        warehouse = Warehouse.instance();
        // context = LibContext.instance();
    }

    public static ClientInfoState instance() {
        if (instance == null) {
            instance = new ClientInfoState();
        }
        return instance;
    }

    public void help() {
        System.out.println("Enter a number between 0 and 4 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(DISPLAY_CLIENTS + " to display all clients");
        System.out.println(DISPLAY_CLIENTS_OUTSTANDING_BALANCE + " to display all clients with outstanding balance");
        System.out.println(
                DISPLAY_CLIENTS_NO_TRANSACTION + " to display all clients without transactions in the last 6 months");
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

    public void showClientsWithoutTransactions() {
        Iterator<Invoice> invoices = warehouse.getInvoiceList(WarehouseContext.instance().getUser());
        int hasInvoices = 0;
        if (invoices.hasNext() == false) {
            System.out.println("No invoices to print");
            return;
        }

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

        while (invoices.hasNext()) {
            Invoice invoice = (Invoice) (invoices.next());
            if (invoice.getDate().isBefore(sixMonthsAgo)) {
                System.out.println(invoice);
                hasInvoices++;
            }
        }
        if (hasInvoices == 0) {
            System.out.println("No invoices to print");
            return;
        }
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case DISPLAY_CLIENTS:
                    showClients();
                    break;
                case DISPLAY_CLIENTS_OUTSTANDING_BALANCE:
                    showClientsWithBalance();
                    break;
                case DISPLAY_CLIENTS_NO_TRANSACTION:
                    showClientsWithoutTransactions();
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
        (WarehouseContext.instance()).changeState(1);
    }
}
