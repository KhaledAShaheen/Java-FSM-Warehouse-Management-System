import java.io.*;
import java.util.*;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int amount;
    private double salePrice;
    private String id;
    private static final String PRODUCT_STRING = "P";
    private WaitList waitList;

    public Product(String name, int amount, double salePrice) {
        this.name = name;
        this.amount = amount;
        this.salePrice = salePrice;
        id = PRODUCT_STRING + (ProductIdServer.instance()).getId();
        this.waitList = new WaitList();
    }

    public Hold searchHoldByClientId(String clientId) {
        return waitList.search(clientId);
    }

    public boolean addHold(Hold hold, String id) {
        return waitList.insertHold(hold, id);
    }

    public boolean removeHold(String clientId) {
        return waitList.removeRecord(clientId);
    }

    public boolean editHoldQty(String clientId, Integer amount) {
        return waitList.editAmount(clientId, amount);
    }

    public Iterator<Hold> getHolds() {
        return waitList.retrieveHolds();
    }

    public List<Hold> getHoldsList() {
        return waitList.holds();
    }

    public WaitList getWaitList() {
        return waitList;
    }

    public Iterator<Hold> getWaitListCopy() {
        List<Hold> holds = new LinkedList<Hold>();
        for (Hold hold : getHoldsList()) {
            Hold newHold = new Hold(hold.getClient(), hold.getAmount());
            hold.setProductId(hold.getProductId());
            holds.add(newHold);
        }
        return holds.iterator();
    }

    public Invoice createInvoice(String clientId, double price, int quantity) {
        return new Invoice(this, quantity, price, clientId);
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getId() {
        return id;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setAmount(int newAmount) {
        amount = newAmount;
    }

    public void setSalePrice(double newSalePrice) {
        salePrice = newSalePrice;
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    public String toString() {
        String string = "Product ID: " + id + "  Name: " + name + "  Amount: " + amount + "  Sale Price: " + salePrice;
        return string;
    }
}