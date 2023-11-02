import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class AddProductCartButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public AddProductCartButton() {
        super("Add Product To Cart");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        (Cartstate.instance()).addProduct();
        (Cartstate.instance()).run();
    }

}
