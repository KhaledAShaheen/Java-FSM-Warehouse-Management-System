import java.io.Serializable;
import java.time.LocalDate;

public class Invoice implements Serializable {
  private static final long serialVersionUID = 1L;
  private Product product;
  private int quantity;
  private double totalPrice;
  private String clientId;
  private LocalDate date;

  public Invoice(Product product, int quantity, double totalPrice, String clientId) {
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.clientId = clientId;
    this.date = LocalDate.now();
  }

  public Product getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public LocalDate getDate() {
    return date;
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
