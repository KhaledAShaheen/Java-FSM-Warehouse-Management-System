import java.util.*;
import java.text.*;
import java.io.*;

public class Loginstate extends WarehouseState {
    private static final int CLIENT_LOGIN = 0;
    private static final int CLERK_LOGIN = 1;
    private static final int MANAGER_LOGIN = 2;
    private static final int EXIT = 3;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WarehouseContext warehouseContext;
    private static Loginstate instance;

    private Loginstate() {
        super();
        // context = LibContext.instance();
    }

    public static Loginstate instance() {
        if (instance == null) {
            instance = new Loginstate();
        }
        return instance;
    }

    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(WarehouseContext.getToken("Enter command:"));
                if (value <= EXIT && value >= CLIENT_LOGIN) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    private void clerk() {
        (WarehouseContext.instance()).setLogin(WarehouseContext.IsClerk);
        (WarehouseContext.instance()).changeState(1);
    }

    private void client() {
        String clientID = WarehouseContext.getToken("Please input the client id: ");
        if (Warehouse.instance().searchClient(clientID) != null) {
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
            (WarehouseContext.instance()).setClient(clientID);
            (WarehouseContext.instance()).changeState(0);
        } else
            System.out.println("Invalid client id.");
    }

    private void manager() {
        (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
        (WarehouseContext.instance()).changeState(2);
    }

    public void process() {
        int command;
        System.out.println("input 0 to login as Client\n" +
                "input 1 to login as Clerk\n" +
                "input 2 to login as Manager\n" +
                "input 3 to exit the system\n");
        while ((command = getCommand()) != EXIT) {

            System.out.println("entered command:" + command);

            switch (command) {
                case CLIENT_LOGIN:
                    client();
                    break;
                case CLERK_LOGIN:
                    clerk();
                    break;
                case MANAGER_LOGIN:
                    manager();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println("input 0 to login as Client\n" +
                    "input 1 to login as Clerk\n" +
                    "input 2 to login as Manager\n" +
                    "input 3 to exit the system\n");
        }

        (WarehouseContext.instance()).changeState(3);
    }

    public void run() {
        process();
    }

}
