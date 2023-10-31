import java.io.Serializable;

public class Invoice implements Serializable {
  private static final long serialVersionUID = 1L;
  private Product product;
  private int quantity;
  private double totalPrice;
  private String clientId;

  public Invoice(Product product, int quantity, double totalPrice, String clientId) {
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.clientId = clientId;
  }

  public Product getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public String getClientId() {
    return clientId;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public void setClientID(String clientId) {
    this.clientId = clientId;
  }

  public String toString() {
    return "Client ID: " + clientId + " Product ID: " + product.getId() + " Name: " + product.getName() + " UnitPrice: "
        + product.getSalePrice()
        + " Qunatity: " + quantity + " Total Price: " + totalPrice + "\n";
  }
}
