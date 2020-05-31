// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class Bank {
    public static final int ACCOUNTS = 20;     // number of accounts
    public static final int BALANCE = 1000;    //initial balance
    private static volatile BlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();
    private static volatile Account[] accounts = new Account[ACCOUNTS];
    private static volatile CountDownLatch latch;

    /*
     Reads transaction data (from/to/amt) from a file for processing.
     (provided code)
     */
    public static void readFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // Use stream tokenizer to get successive words from file
            StreamTokenizer tokenizer = new StreamTokenizer(reader);

            while (true) {
                int read = tokenizer.nextToken();
                if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
                int from = (int) tokenizer.nval;

                tokenizer.nextToken();
                int to = (int) tokenizer.nval;

                tokenizer.nextToken();
                int amount = (int) tokenizer.nval;

                Transaction transaction = new Transaction(to, from, amount);
                queue.put(transaction);
            }
            //poison pill
            queue.put(new Transaction(-1, -1, -1));

        } catch (Exception e) {System.exit(1);} //avoiding line coverage problems
    }

    /*
     Processes one file of transaction data
     -fork off workers
     -read file into the buffer
     -wait for the workers to finish
    */
    public static void processFile(String file, int numWorkers) {
        //first of all init CountDownLatch then start workers
        latch = new CountDownLatch(numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            new Worker().start();
        }
        //read file and wait for other threads to
        readFile(file);
        try {
            latch.await();
            printUpdatedAccounts();

        } catch (InterruptedException e) {}
    }

    private static void printUpdatedAccounts() {
        for (int i = 0; i < ACCOUNTS; i++) {
            System.out.println(accounts[i].toString());
        }
    }
    //filling accounts array
    private static void fillAccounts() {
        for (int i = 0; i < ACCOUNTS; i++) {
            accounts[i] = new Account(new Bank(), i, BALANCE);
        }
    }

    /*
     Looks at commandline args and calls Bank processing.
    */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Args: transaction-file [num-workers [limit]]");
           return;
        }
        fillAccounts();
        String file = args[0];
        int numWorkers = 1;
        if (args.length >= 2) {
            numWorkers = Integer.parseInt(args[1]);
        }
        processFile(file, numWorkers);
    }

    public static class Worker extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Transaction transaction = queue.take();  //take from queue
                    if (transaction.getFrom() == -1) { //if it is poison pill
                        if (latch.getCount() != 1) {    //if it is not the last one put it back
                           queue.put(transaction); //this will help us to poison pill every working thread
                        }
                        break;
                    }
                    accounts[(transaction.getTo())].deposit(transaction.getAmount());
                    accounts[(transaction.getFrom())].withdraw(transaction.getAmount());
                } catch (InterruptedException e) {}
            }
            latch.countDown(); //eventually count down latch
        }
    }
}

