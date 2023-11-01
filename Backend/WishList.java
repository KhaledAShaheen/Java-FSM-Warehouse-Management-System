import java.io.*;
import java.util.*;

public class WishList implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Record> records;

    public WishList() {
        records = new LinkedList<Record>();
    }

    public Record insertRecord(Record record) {
        Record recordToChange = search(record.getProduct().getId());
        if (recordToChange != null) {
            recordToChange.setQuantity(recordToChange.getQuantity() + record.getQuantity());
            return recordToChange;
        } else {
            records.add(record);
            return record;
        }

    }

    public boolean removeRecord(String productId) {
        Record record = search(productId);
        if (record != null) {
            records.remove(record);
            return true;
        }
        return false;
    }

    public Record search(String productId) {
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext();) {
            Record record = (Record) iterator.next();
            if (record.getProduct().getId().equals(productId)) {
                return record;
            }
        }
        return null;
    }

    public boolean editQunatity(String productId, Integer Qunatity) {
        Record record = search(productId);
        if (record != null) {
            record.setQuantity(Qunatity);
            return true;
        }
        return false;
    }

    public Iterator<Record> retrieveRecords() {
        return records.iterator();
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<Record> records() {
        return records;
    }

    public String toString() {
        return records.toString();
    }
}