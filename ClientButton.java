import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ClientButton extends JButton implements ActionListener {
    // private static ClerkButton instance;
    // private JButton userButton;
    public ClientButton() {
        super("Client");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        String clientID = JOptionPane.showInputDialog(
                Loginstate.instance().getFrame(), "Please input the client id: ");
        if (Warehouse.instance().searchClient(clientID) != null) {
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
            Loginstate.instance().clear();
            (WarehouseContext.instance()).setClient(clientID);
            (WarehouseContext.instance()).changeState(0);
        } else
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "Invalid client id!");
    }

}
