import java.io.*;
import java.util.*;

public class InvoiceList implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Invoice> invoices;

    public InvoiceList(){
        invoices = new LinkedList<Invoice>();
    }

    public Invoice insertInvoice(Invoice invoice) {
        Invoice invoiceToChange = search(invoice.getProduct().getId());
        if (invoiceToChange != null) {
            invoiceToChange.setQuantity(invoiceToChange.getQuantity() + invoice.getQuantity());
            return invoiceToChange;
        } else {
            invoices.add(invoice);
            return invoice;
        }
    }

    public boolean removeInvoice(String productId) {
        Invoice invoice = search(productId);
        if (invoice != null) {
            invoices.remove(invoice);
            return true;
        }
        return false;
    }
    
    public Invoice search(String productId) {
        for (Iterator<Invoice> iterator = invoices.iterator(); iterator.hasNext();) {
            Invoice invoice = (Invoice) iterator.next();
            if (invoice.getProduct().getId().equals(productId)) {
                return invoice;
            }
        }
        return null;
    }

    public Iterator<Invoice> retrieveInvoices() {
        return invoices.iterator();
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<Invoice> invoices() {
        return invoices;
    }

    public String toString() {
        return invoices.toString();
    }
}
