import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Loginstate extends WarehouseState {
    private static final int CLIENT_LOGIN = 0;
    private static final int EXIT = 3;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WarehouseContext warehouseContext;
    private JFrame frame;
    private static Loginstate instance;
    private AbstractButton clerkButton, logoutButton, clientButton, managerButton;

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

    public void clear() { // clean up stuff
        frame.getContentPane().removeAll();
        frame.paint(frame.getGraphics());
    }

    public JFrame getFrame() { // clean up stuff
        return frame;
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());
        clerkButton = new ClerkButton();
        logoutButton = new LogoutButton();
        clientButton = new ClientButton();
        managerButton = new ManagerButton();

        frame.getContentPane().add(this.clientButton);
        frame.getContentPane().add(this.clerkButton);
        frame.getContentPane().add(this.managerButton);
        frame.getContentPane().add(this.logoutButton);
        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        // frame.repaint();
        frame.toFront();
        frame.requestFocus();
    }

}
