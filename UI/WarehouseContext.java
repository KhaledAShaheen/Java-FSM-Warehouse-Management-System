import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class WarehouseContext {

    private int currentState;
    private static Warehouse warehouse;
    private static WarehouseContext context;
    private int currentUser;
    private String userID;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static final int IsClient = 0;
    public static final int IsClerk = 1;
    public static final int IsManager = 2;
    private WarehouseState[] states;
    private int[][] nextState;
    private static JFrame LibFrame;

    public static String getToken(String prompt) {
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

    public static int getNumber(String prompt) {
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

    public static float getFloat(String prompt) {
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

    public static boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
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

    public void setLogin(int code) {
        currentUser = code;
    }

    public void setClient(String uID) {
        userID = uID;
    }

    public int getLogin() {
        return currentUser;
    }

    public String getUser() {
        return userID;
    }

    public JFrame getFrame() {
        return LibFrame;
    }

    private WarehouseContext() { // constructor
        int option = JOptionPane.showConfirmDialog(
                null,
                "Look for saved data and use it?",
                "Load Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
        // set up the FSM and transition table;
        states = new WarehouseState[6];
        states[0] = Clientstate.instance();
        states[1] = Clerkstate.instance();
        states[2] = Managerstate.instance();
        states[3] = Loginstate.instance();
        states[4] = Cartstate.instance();
        states[5] = ClientInfoState.instance();
        nextState = new int[6][6];

        // CLIENTSTATE
        nextState[0][0] = 0;
        nextState[0][1] = 1;
        nextState[0][2] = -2;
        nextState[0][3] = 3;
        nextState[0][4] = 4;
        nextState[0][5] = -2;
        // CLERKSTATE
        nextState[1][0] = 0;
        nextState[1][1] = 1;
        nextState[1][2] = 2;
        nextState[1][3] = 3;
        nextState[1][4] = -2;
        nextState[1][5] = 5;
        // MANGERSTATE
        nextState[2][0] = -2;
        nextState[2][1] = 1;
        nextState[2][2] = 2;
        nextState[2][3] = 3;
        nextState[2][4] = -2;
        nextState[2][5] = -2;
        // LOGIN STATE
        nextState[3][0] = 0;
        nextState[3][1] = 1;
        nextState[3][2] = 2;
        nextState[3][3] = -1;
        nextState[3][4] = -2;
        nextState[3][5] = -2;
        // CART STATE
        nextState[4][0] = 0;
        nextState[4][1] = -2;
        nextState[4][2] = -2;
        nextState[4][3] = -2;
        nextState[4][4] = -2;
        nextState[4][5] = -2;
        // CLIENT INFORMATION STATE
        nextState[5][0] = -2;
        nextState[5][1] = 1;
        nextState[5][2] = -2;
        nextState[5][3] = -2;
        nextState[5][4] = -2;
        nextState[5][5] = -2;
        currentState = 3; // current is login

        LibFrame = new JFrame("Warehouse GUI");
        LibFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // better use the default close operation
        LibFrame.setSize(1600, 900);
        LibFrame.setLocationRelativeTo(null); // Center on screen
        LibFrame.setVisible(true); // Make frame visible
    }

    public void changeState(int transition) {
        // System.out.println("current state " + currentState + " \n \n ");
        currentState = nextState[currentState][transition];
        if (currentState == -2) {
            System.out.println("Error has occurred");
            terminate();
        }
        if (currentState == -1)
            terminate();
        // System.out.println("current state " + currentState + " \n \n ");
        states[currentState].run();
    }

    private void terminate() {
        // Use JOptionPane.showConfirmDialog to ask the user if they want to save data
        int option = JOptionPane.showConfirmDialog(
                null,
                "Save data?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        // If the user chooses 'YES', attempt to save the Warehouse data
        if (option == JOptionPane.YES_OPTION) {
            boolean saveSuccess = Warehouse.save(); // Assuming Warehouse.save() is a static method returning a boolean
            if (saveSuccess) {
                JOptionPane.showMessageDialog(null,
                        "The Warehouse has been successfully saved in the file WarehouseData.");
            } else {
                JOptionPane.showMessageDialog(null, "There has been an error in saving.", "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // Say goodbye to the user using JOptionPane instead of System.out.println
        JOptionPane.showMessageDialog(null, "Goodbye!", "Exit", JOptionPane.INFORMATION_MESSAGE);

        // Exit the application
        System.exit(0);
    }

    public static WarehouseContext instance() {
        if (context == null) {
            System.out.println("calling constructor");
            context = new WarehouseContext();
        }
        return context;
    }

    public void process() {
        states[currentState].run();
    }

    public static void main(String[] args) {
        WarehouseContext.instance().process();
    }

}
