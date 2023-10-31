import java.util.*;
import java.io.*;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Client> clients = new LinkedList<Client>(); // Holds clients in Linked List
    private static ClientList clientlist;

    private ClientList() {
    }

    public static ClientList instance() {
        if (clientlist == null) {
            return (clientlist = new ClientList());
        } else {
            return clientlist;
        }
    }

    public boolean insertClient(Client client) {
        clients.add(client);
        return true;
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public Client search(String clientId) {
        for (Iterator<Client> iterator = clients.iterator(); iterator.hasNext();) {
            Client client = (Client) iterator.next();
            if (client.getClientID().equals(clientId)) {
                return client;
            }
        }
        return null;
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(clientlist);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void readObject(java.io.ObjectInputStream input) {
        try {
            if (clientlist != null) {
                return;
            } else {
                input.defaultReadObject();
                if (clientlist == null) {
                    clientlist = (ClientList) input.readObject();
                } else {
                    input.readObject();
                }
            }
        } catch (IOException ioe) {
            System.out.println("in ClientList readObject \n" + ioe);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public String toString() {
        return clients.toString();
    }
}
