import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class LogoutButtonCartState extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public LogoutButtonCartState() {
        super("Exit");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Loginstate.instance().clear();
        (Cartstate.instance()).logout();
    }

}
