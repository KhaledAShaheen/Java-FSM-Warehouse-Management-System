import java.io.*;
import java.util.*;

public class WaitList implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Hold> holds = new LinkedList<Hold>();
    private String productId;

    public WaitList() {
    }

    public boolean insertHold(Hold hold, String productId) {
        hold.setProductId(productId);
        Hold holdToChange = search(hold.getClient().getClientID());
        if (holdToChange != null) {
            holdToChange.setAmount(holdToChange.getAmount() + hold.getAmount());
            return true;
        } else {
            holds.add(hold);
            return true;
        }
    }

    public boolean removeRecord(String clientId) {
        Hold hold = search(clientId);
        if (hold != null) {
            holds.remove(hold);
            return true;
        }
        return false;
    }

    public Hold search(String clientId) {
        for (Iterator<Hold> iterator = holds.iterator(); iterator.hasNext();) {
            Hold hold = (Hold) iterator.next();
            if (hold.getClient().getClientID().equals(clientId)) {
                return hold;
            }
        }
        return null;
    }

    public boolean editAmount(String clientId, Integer amount) {
        Hold hold = search(clientId);
        if (hold != null) {
            hold.setAmount(amount);
            return true;
        }
        return false;
    }

    public Iterator<Hold> retrieveHolds() {
        return holds.iterator();
    }

    public List<Hold> holds() {
        return holds;
    }

    public void setHolds(List<Hold> holds) {
        this.holds = holds;
    }

    public String toString() {
        return holds.toString();
    }
}