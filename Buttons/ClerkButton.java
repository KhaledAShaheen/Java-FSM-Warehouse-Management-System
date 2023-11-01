import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ClerkButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ClerkButton() {
        super("Clerk");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        (WarehouseContext.instance()).setLogin(WarehouseContext.IsClerk);
        Loginstate.instance().clear();
        (WarehouseContext.instance()).changeState(1);
    }

}
