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
        frame.setTitle("Login");

        // Set the BoxLayout for the content pane directly
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create and add the title label
        JLabel titleLabel = new JLabel("Welcome, Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        frame.getContentPane().add(titleLabel);

        // Add some vertical space after the title
        frame.getContentPane().add(Box.createVerticalStrut(20));

        clerkButton = new ClerkButton();
        clerkButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        logoutButton = new LogoutButton();
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        clientButton = new ClientButton();
        clientButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        managerButton = new ManagerButton();
        managerButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button

        // Add buttons to the frame's content pane
        frame.getContentPane().add(clientButton);
        frame.getContentPane().add(Box.createVerticalStrut(10)); // Add space between buttons
        frame.getContentPane().add(clerkButton);
        frame.getContentPane().add(Box.createVerticalStrut(10)); // Add space between buttons
        frame.getContentPane().add(managerButton);
        frame.getContentPane().add(Box.createVerticalStrut(10)); // Add space between buttons
        frame.getContentPane().add(logoutButton);

        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();
    }

}
