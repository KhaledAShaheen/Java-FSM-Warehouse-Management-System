import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ShowCartButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ShowCartButton() {
        super("View Cart");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        (Cartstate.instance()).showProductsInWishlist();
        (Cartstate.instance()).run();
    }

}
