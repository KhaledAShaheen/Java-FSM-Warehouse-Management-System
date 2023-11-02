import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ShowClientTransactionsButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ShowClientTransactionsButton() {
        super("Display My Transcations");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        Clientstate.instance().showTransactions();
        Clientstate.instance().run();
    }

}
