// Account.java

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
    private int id;
    private int balance;
    private int transactions;
    private Lock lock;
    // It may work out to be handy for the account to
    // have a pointer to its Bank.
    // (a suggestion, not a requirement)
    private Bank bank;

    public Account(Bank bank, int id, int balance) {
        this.bank = bank;
        this.id = id;
        this.balance = balance;
        transactions = 0;
        lock = new ReentrantLock();
    }
    // using basic locking tool to avoid shared data racing
    public void deposit(int amount) {
        lock.lock();
        balance += amount;
        transactions++;
        lock.unlock();
    }
    //same here
    public void withdraw(int amount) {
        lock.lock();
        balance -= amount;
        transactions++;
        lock.unlock();
    }
    //overridden toString method, used to print accounts final state
    @Override
    public String toString() {
        lock.lock();
        String s = "acct: " + id + " balance: " + balance + " trans: " + transactions;
        lock.unlock();
        return s;
    }
}
