import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ShowClientDetailsButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ShowClientDetailsButton() {
        super("Display My Info");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        Clientstate.instance().showClientDetails();
        Clientstate.instance().run();
    }

}
