import java.io.*;

public class Hold implements Serializable {
    private static final long serialVersionUID = 1L;
    private Client client;
    private int amount;
    private String productId;

    public Hold(Client client, int amount) {
        this.client = client;
        this.amount = amount;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public int getAmount() {
        return amount;
    }

    public String getProductId() {
        return productId;
    }

    public String toString() {
        String string = "ProductID: " + productId + " ClientID: " + client.getClientID() + "  Client Name: "
                + client.getName() + "  Amount: "
                + amount;
        return string;
    }

}