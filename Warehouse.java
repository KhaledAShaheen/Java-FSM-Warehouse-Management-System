import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    private ClientList clientlist;
    private ProductList productlist;
    private static Warehouse warehouse;

    private Warehouse() {
        clientlist = ClientList.instance();
        productlist = ProductList.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null) {
            ProductIdServer.instance();
            ClientIdServer.instance();
            return (warehouse = new Warehouse());
        } else {
            return warehouse;
        }
    }

    public Product addProduct(String name, int amount, float salePrice) {
        Product product = new Product(name, amount, salePrice);
        if (productlist.insertProduct(product)) {
            return (product);
        }
        return null;
    }

    public Client addClient(String name, String address) {
        Client client = new Client(name, address);
        if (clientlist.insertClient(client)) {
            return (client);
        }
        return null;
    }

    public Record addProductToWishlist(String productId, int quantity, Client client) {
        Product product = searchProduct(productId);
        if (product == null) {
            return null;
        } else {
            Record record = new Record(product, quantity);
            return client.addRecord(record);
        }
    }

    public Invoice addInvoiceToInvoiceList(Invoice invoice, Client client)
    {
        return client.addInvoice(invoice);
    }

    public void addQtyToProduct(String productId, int quantity) {
        Product product = searchProduct(productId);
        if (product != null) {
            product.setAmount(product.getAmount() + quantity);
        }
    }

    public Invoice processShippment(String productId, String clientId, int quantity) {
        Product product = searchProduct(productId);
        Client client = searchClient(clientId);
        double price = 0.0;
        if (product != null && client != null) {
            if (quantity <= product.getAmount()) {
                price = quantity * product.getSalePrice();
                product.removeHold(clientId);
                client.setBalanace(client.getBalance() + price);
                Invoice invoice = product.createInvoice(clientId, price, quantity);
                product.setAmount(product.getAmount() - quantity);
                return invoice;
            } else if (quantity >= product.getAmount() && product.getAmount() != 0) {
                price = product.getAmount() * product.getSalePrice();
                product.editHoldQty(clientId, quantity - product.getAmount());
                client.setBalanace(client.getBalance() + price);
                Invoice invoice = product.createInvoice(clientId, price, product.getAmount());
                product.setAmount(0);
                return invoice;
            }
        }
        return null;
    }

    public void updateWishList(Client client, WishList wishlistCopy) {
        client.setWishList(wishlistCopy);
    }

    public List<Invoice> createInvoice(Client client, WishList wishlistCopy) {
        Iterator<Record> wishListItems = wishlistCopy.retrieveRecords();
        List<Invoice> invoices = new LinkedList<>();
        double price = 0.0;
        double totalPriceInvoice = 0.0;
        while (wishListItems.hasNext()) {

            Record wishListItem = (Record) (wishListItems.next());
            Product inventoryProduct = searchProduct(wishListItem.getProduct().getId());

            if (wishListItem.getQuantity() <= inventoryProduct.getAmount()) {
                price = wishListItem.getQuantity() * wishListItem.getProduct().getSalePrice();
                invoices.add(new Invoice(wishListItem.getProduct(), wishListItem.getQuantity(), price,
                        client.getClientID()));
                inventoryProduct.setAmount(inventoryProduct.getAmount() - wishListItem.getQuantity());

            } else if (wishListItem.getQuantity() > inventoryProduct.getAmount()) {
                price = inventoryProduct.getAmount() * inventoryProduct.getSalePrice();
                if (inventoryProduct.getAmount() != 0) {
                    invoices.add(
                            new Invoice(inventoryProduct, inventoryProduct.getAmount(), price, client.getClientID()));
                }
                Hold hold = new Hold(client, wishListItem.getQuantity() - inventoryProduct.getAmount());
                inventoryProduct.addHold(hold);
                inventoryProduct.setAmount(0);
            }
            totalPriceInvoice += price;
        }
        client.setBalanace(client.getBalance() + totalPriceInvoice);
        return invoices;
    }

    public void acceptPayment(float payment, String clientId) {
        Client result = searchClient(clientId);
        if (result != null) {
            result.setBalanace(result.getBalance() - payment);
        }
    }

    public Iterator<Product> getProducts() {
        return productlist.getProducts();
    }

    public Iterator<Client> getClients() {
        return clientlist.getClients();
    }

    public Iterator<Record> getWishList(String clientId) {
        Client result = searchClient(clientId);
        if (result != null) {
            return result.getRecords();
        }
        return null;
    }

    public Iterator<Invoice> getInvoiceList(String clientId) {
        Client result = searchClient(clientId);
        if (result != null) {
            return result.getinvoices();
        }
        return null;
    }

    public Iterator<Hold> getWaitList(String productId) {
        Product result = searchProduct(productId);
        if (result != null) {
            return result.getWaitListCopy();
        }
        return null;
    }

    public Iterator<Hold> getWaitListCopy(String productId) {
        Product result = searchProduct(productId);
        if (result != null) {
            return result.getWaitListCopy();
        }
        return null;
    }

    public WishList generateWishListCopy(Client client) {
        List<Record> records = new LinkedList<Record>();
        for (Record record : client.getRecordsList()) {
            Record newRecord = new Record(record.getProduct(), record.getQuantity());
            records.add(newRecord);
        }
        WishList tempWishList = new WishList();
        tempWishList.setRecords(records);
        return tempWishList;
    }

    public Client searchClient(String clientId) {
        Client client = clientlist.search(clientId);
        if (client != null) {
            return client;
        }
        return null;
    }

    public Product searchProduct(String productId) {
        Product product = productlist.search(productId);
        if (product != null) {
            return product;
        }
        return null;
    }

    public static Warehouse retrieve() {
        try {
            FileInputStream file = new FileInputStream("WarehouseData");
            ObjectInputStream input = new ObjectInputStream(file);
            input.readObject();
            ProductIdServer.retrieve(input);
            ClientIdServer.retrieve(input);
            return warehouse;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }

    public static boolean save() {
        try {
            FileOutputStream file = new FileOutputStream("WarehouseData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(warehouse);
            output.writeObject(ProductIdServer.instance());
            output.writeObject(ClientIdServer.instance());
            output.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void readObject(java.io.ObjectInputStream input) {
        try {
            input.defaultReadObject();
            if (warehouse == null) {
                warehouse = (Warehouse) input.readObject();
            } else {
                input.readObject();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return productlist + "\n" + clientlist;
    }

}
