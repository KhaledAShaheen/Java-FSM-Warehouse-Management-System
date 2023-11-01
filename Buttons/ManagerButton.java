import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ManagerButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ManagerButton() {
        super("Manager");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
        Loginstate.instance().clear();
        (WarehouseContext.instance()).changeState(2);
    }

}
