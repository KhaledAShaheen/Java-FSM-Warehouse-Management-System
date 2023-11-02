import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class DisplayClientsButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public DisplayClientsButton() {
        super("Display All Clients");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        ClientInfoState.instance().showClients();
        ClientInfoState.instance().run();
    }

}
