import java.util.*;
import java.io.*;

public class ProductList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Product> products = new LinkedList<Product>();
    private static ProductList productList;

    private ProductList() {
    }

    public static ProductList instance() {
        if (productList == null) {
            return (productList = new ProductList());
        } else {
            return productList;
        }
    }

    public boolean insertProduct(Product product) {
        products.add(product);
        return true;
    }

    public Iterator<Product> getProducts() {
        return products.iterator();
    }

    public Product search(String productId) {
        for (Iterator<Product> iterator = products.iterator(); iterator.hasNext();) {
            Product product = (Product) iterator.next();
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(productList);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void readObject(java.io.ObjectInputStream input) {
        try {
            if (productList != null) {
                return;
            } else {
                input.defaultReadObject();
                if (productList == null) {
                    productList = (ProductList) input.readObject();
                } else {
                    input.readObject();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public String toString() {
        return products.toString();
    }
}
