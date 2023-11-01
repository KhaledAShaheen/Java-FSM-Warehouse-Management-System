import java.io.*;

public class Record implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private int quantity;

    public Record(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String toString() {
        String string = "Product Name: " + product.getName() +
                " Product Sale Price: " + product.getSalePrice() + " Quantity: " + quantity;
        return string;
    }

}