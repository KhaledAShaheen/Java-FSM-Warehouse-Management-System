import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class LogoutButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public LogoutButton() {
        super("Logout");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        (WarehouseContext.instance()).changeState(3);
    }

}
