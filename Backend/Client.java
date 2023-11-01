import java.io.Serializable;
import java.util.*;

public class Client implements Serializable {
  private static final long serialVersionUID = 1L;
  private String ClientID;
  private String Name;
  private String Address;
  private static final String CLIENT_STRING = "C";
  private WishList wishList;
  private double Balance;
  private InvoiceList invoiceList;
  private LinkedList<Double> paymentList;

  public Client(String Name, String Address) {
    this.Name = Name;
    this.Address = Address;
    this.Balance = 0;
    ClientID = CLIENT_STRING + (ClientIdServer.instance()).getId(); // do ClientIdServer
    this.wishList = new WishList();
    this.invoiceList = new InvoiceList();
    this.paymentList = new LinkedList<Double>();

  }

  public void addPayment(double payment) {
    paymentList.add(payment);
  }

  public Iterator<Double> retrievePayments() {
    return paymentList.iterator();
  }

  public Record addRecord(Record record) {
    return wishList.insertRecord(record);
  }

  public Boolean removeRecord(String productId) {
    return wishList.removeRecord(productId);
  }

  public Boolean editQunatity(String productId, Integer Qunatity) {
    return wishList.editQunatity(productId, Qunatity);
  }

  public Iterator<Record> getRecords() {
    return wishList.retrieveRecords();
  }

  public List<Record> getRecordsList() {
    return wishList.records();
  }

  public Invoice addInvoice(Invoice invoice) {
    return invoiceList.insertInvoice(invoice);
  }

  public Iterator<Invoice> getinvoices() {
    return invoiceList.retrieveInvoices();
  }

  public List<Invoice> getInvoiceList() {
    return invoiceList.invoices();
  }

  public String getClientID() {
    return ClientID;
  }

  public String getName() {
    return Name;
  }

  public String getAddress() {
    return Address;
  }

  public double getBalance() {
    return Balance;
  }

  public void setClientID(String newClientID) {
    ClientID = newClientID;
  }

  public void setName(String newName) {
    Name = newName;
  }

  public void setAddress(String newAddress) {
    Address = newAddress;
  }

  public void setWishList(WishList wishList) {
    this.wishList = wishList;
  }

  public void setInvoiceList(InvoiceList invoiceList) {
    this.invoiceList = invoiceList;
  }

  public void setBalanace(double newBalance) {
    this.Balance = newBalance;
  }

  public boolean equals(String ClientID) {
    return this.ClientID.equals(ClientID);
  }

  public String toString() {
    return "ClientID: " + ClientID + " Name: " + Name + " Address: " + Address + " Balance: " + Balance;
  }
}
