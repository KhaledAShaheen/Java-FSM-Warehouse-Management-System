import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ClerkMenuButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ClerkMenuButton() {
        super("Switch To Clerk Menu");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        (WarehouseContext.instance()).changeState(1);
    }

}
