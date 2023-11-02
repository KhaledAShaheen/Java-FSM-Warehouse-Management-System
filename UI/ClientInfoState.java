import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.time.*;

public class ClientInfoState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext Warehousecontext;
    private static ClientInfoState instance;
    private JFrame frame;
    private AbstractButton displayClientsButton, displayClientsOutstandingBalanceButton, displayClientsNoTransButton,
            exitClientInfoStateButton;

    private ClientInfoState() {
        super();
        warehouse = Warehouse.instance();
        // context = LibContext.instance();
    }

    public static ClientInfoState instance() {
        if (instance == null) {
            instance = new ClientInfoState();
        }
        return instance;
    }

    public void showClients() {
        Iterator<Client> allClients = warehouse.getClients();
        StringBuilder clientsListBuilder = new StringBuilder();

        if (!allClients.hasNext()) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "No clients to display", "Clients List",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        while (allClients.hasNext()) {
            Client client = allClients.next();
            clientsListBuilder.append(client.toString()).append("\n");
        }

        JTextArea textArea = new JTextArea(clientsListBuilder.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 250));

        JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), scrollPane, "Clients List",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showClientsWithBalance() {
        Iterator<Client> allClients = warehouse.getClients();
        StringBuilder clientsWithBalanceBuilder = new StringBuilder();
        int hasClients = 0;

        if (!allClients.hasNext()) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "No clients to display",
                    "Clients with Balance", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        while (allClients.hasNext()) {
            Client client = allClients.next();
            if (client.getBalance() > 0) {
                clientsWithBalanceBuilder.append(client.toString()).append("\n");
                hasClients++;
            }
        }

        if (hasClients == 0) {
            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), "No clients with outstanding balance",
                    "Clients with Balance", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JTextArea textArea = new JTextArea(clientsWithBalanceBuilder.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 250));

            JOptionPane.showMessageDialog(Loginstate.instance().getFrame(), scrollPane, "Clients with Balance",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void showClientsWithoutTransactions() {
        Iterator<Invoice> invoices = warehouse.getInvoiceList(WarehouseContext.instance().getUser());
        StringBuilder clientsWithoutTransactionsBuilder = new StringBuilder();
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        int hasInvoices = 0;

        if (invoices == null) {
            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(), "No invoices to display",
                    "Clients without Transactions", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        while (invoices.hasNext()) {
            Invoice invoice = invoices.next();
            if (invoice.getDate().isBefore(sixMonthsAgo)) {
                clientsWithoutTransactionsBuilder.append(invoice.toString()).append("\n");
                hasInvoices++;
            }
        }

        if (hasInvoices == 0) {
            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(),
                    "No clients without transactions in the past six months", "Clients without Transactions",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JTextArea textArea = new JTextArea(clientsWithoutTransactionsBuilder.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 250));

            JOptionPane.showMessageDialog(WarehouseContext.instance().getFrame(), scrollPane,
                    "Clients without Transactions", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.setTitle("Client Information State");

        // Set the BoxLayout for the content pane directly
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create and add the title label
        JLabel titleLabel = new JLabel("Client Information State", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        frame.getContentPane().add(titleLabel);

        // Add some vertical space after the title
        frame.getContentPane().add(Box.createVerticalStrut(20));

        // Instantiate custom buttons
        displayClientsButton = new DisplayClientsButton();
        displayClientsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        displayClientsOutstandingBalanceButton = new DisplayClientsOutstandingBalanceButton();
        displayClientsOutstandingBalanceButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        displayClientsNoTransButton = new DisplayClientsNoTransButton();
        displayClientsNoTransButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        exitClientInfoStateButton = new ExitClientInfoStateButton();
        exitClientInfoStateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add custom buttons to the frame's content pane
        frame.getContentPane().add(displayClientsButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(displayClientsOutstandingBalanceButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(displayClientsNoTransButton);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(exitClientInfoStateButton);

        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();

    }

    public void logout() {
        (WarehouseContext.instance()).changeState(1);
    }
}
