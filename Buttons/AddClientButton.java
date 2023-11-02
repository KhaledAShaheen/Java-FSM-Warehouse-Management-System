import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class AddClientButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public AddClientButton() {
        super("Add Client");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        Clerkstate.instance().addClient();
        Clerkstate.instance().run();
    }

}
