import java.util.concurrent.locks.Lock;

// Transaction.java
/*
 (provided code)
 Transaction is just a dumb struct to hold
 one transaction. Supports toString.
*/
public class Transaction {
    private int from;
    private int to;
    private int amount;
//added getter methods
    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getAmount() {
        return amount;
    }

    public Transaction(int from, int to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
