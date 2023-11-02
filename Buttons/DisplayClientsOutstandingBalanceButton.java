import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class DisplayClientsOutstandingBalanceButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public DisplayClientsOutstandingBalanceButton() {
        super("Display All Clients With Outstanding Balance");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        ClientInfoState.instance().showClientsWithBalance();
        ClientInfoState.instance().run();
    }

}
