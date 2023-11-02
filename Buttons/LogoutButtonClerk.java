import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class LogoutButtonClerk extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public LogoutButtonClerk() {
        super("Logout");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk) {

            (WarehouseContext.instance()).changeState(3);
        } else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsManager) {
            // to login \n");
            (WarehouseContext.instance()).changeState(2);
        } else
            (WarehouseContext.instance()).changeState(3);
    }

}
