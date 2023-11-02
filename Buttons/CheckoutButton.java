import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class CheckoutButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public CheckoutButton() {
        super("Checkout");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        (Cartstate.instance()).checkOut();
        (Cartstate.instance()).run();
    }

}
