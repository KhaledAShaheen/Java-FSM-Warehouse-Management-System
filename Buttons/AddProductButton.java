import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class AddProductButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public AddProductButton() {
        super("Add Product");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        Managerstate.instance().addProduct();
        Managerstate.instance().run();

    }

}
