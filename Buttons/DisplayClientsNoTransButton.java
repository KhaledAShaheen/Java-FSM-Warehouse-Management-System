import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class DisplayClientsNoTransButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public DisplayClientsNoTransButton() {
        super("Display All Clients With NO Transactions in the Last 6 Months");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        ClientInfoState.instance().showClientsWithoutTransactions();
        ClientInfoState.instance().run();
    }

}
