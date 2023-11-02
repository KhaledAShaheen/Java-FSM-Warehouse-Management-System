import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class CartStateButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public CartStateButton() {
        super("Switch To Cart State");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        Clientstate.instance().cartState();
    }

}
