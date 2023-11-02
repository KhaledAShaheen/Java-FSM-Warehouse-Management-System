import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ShowProductsInWaitlistButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ShowProductsInWaitlistButton() {
        super("Show A Product Waitlist");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        Clerkstate.instance().showProductsInWaitlist();
        Clerkstate.instance().run();

    }

}
