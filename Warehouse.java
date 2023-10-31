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

    public Record addProductToWishlist(String productId, int quantity, String clientId) {
        Client result = searchClient(clientId);
        if (result != null) {
            Product product = searchProduct(productId);
            if (product == null) {
                return null;
            }
            Record record = new Record(product, quantity);
            return result.addRecord(record);
        }
        return null;
    }

    public Boolean removeProductFromWishList(String productId, String clientId) {
        Client result = searchClient(clientId);
        if (result != null) {
            Product product = searchProduct(productId);
            if (product == null) {
                return false;
            }
            return result.removeRecord(productId);
        }
        return false;
    }

    public Invoice addInvoiceToInvoiceList(Invoice invoice, Client client) {
        return client.addInvoice(invoice);
    }

    public void addQtyToProduct(String productId, int quantity) {
        Product product = searchProduct(productId);
        if (product != null) {
            product.setAmount(product.getAmount() + quantity);
        }
    }

    public boolean editProductQunatity(String productId, String clientId, Integer qty) {
        Product result = searchProduct(productId);
        Client client = searchClient(clientId);
        if (result != null) {
            return client.editQunatity(result.getId(), qty);
        }
        return false;
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

    public List<Invoice> createInvoice(String clientId) {
        Client client = searchClient(clientId);
        Iterator<Record> wishListItems = generateWishListCopy(clientId).retrieveRecords();
        List<Invoice> invoices = new LinkedList<>();
        double price = 0.0;
        double totalPriceInvoice = 0.0;
        while (wishListItems.hasNext()) {

            Record wishListItem = (Record) (wishListItems.next());
            Product inventoryProduct = searchProduct(wishListItem.getProduct().getId());

            if (wishListItem.getQuantity() <= inventoryProduct.getAmount()) {
                price = wishListItem.getQuantity() * wishListItem.getProduct().getSalePrice();

                addInvoiceToInvoiceList(new Invoice(wishListItem.getProduct(), wishListItem.getQuantity(), price,
                        client.getClientID()), client);

                inventoryProduct.setAmount(inventoryProduct.getAmount() - wishListItem.getQuantity());

            } else if (wishListItem.getQuantity() > inventoryProduct.getAmount()) {
                price = inventoryProduct.getAmount() * inventoryProduct.getSalePrice();
                if (inventoryProduct.getAmount() != 0) {
                    addInvoiceToInvoiceList(
                            new Invoice(inventoryProduct, inventoryProduct.getAmount(), price, client.getClientID()),
                            client);
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
            result.addPayment(payment);
        }
    }

    public Iterator<Hold> getWaitListByClientId(String clientId) {
        Client result = searchClient(clientId);
        List<Hold> clientHolds = new LinkedList<Hold>();
        if (result != null) {
            Iterator<Product> allProducts = warehouse.getProducts();
            if (allProducts.hasNext() == false) {
                return;
            }
            while (allProducts.hasNext()) {
                Product product = (Product) (allProducts.next());
                Hold result1 = product.searchHoldByClientId(clientId);
                if (result1 != null) {
                    clientHolds.add(result1);
                }
            }
            return clientHolds.iterator();
        }
        return null;
    }

    public Iterator<Double> getPayments(String clientId) {
        Client result = searchClient(clientId);
        if (result != null) {
            return result.retrievePayments();
        }
        return null;
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

    public WishList generateWishListCopy(String clientId) {
        Client client = searchClient(clientId);
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
