import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ExitClientInfoStateButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ExitClientInfoStateButton() {
        super("Exit");
        this.setListener();
    }

    public void setListener() {
        // System.out.println("In clerkButton setListener\n");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        ClientInfoState.instance().logout();
    }

}
