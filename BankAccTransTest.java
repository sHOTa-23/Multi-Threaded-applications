import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

//use outputstream and printstream to saw printed text on console
class BankAccTransTest  {
    @Test
    public void testBasic() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args1 = {};
        Bank.main(args1);
        assertEquals(os.toString(),"Args: transaction-file [num-workers [limit]]\r\n");
    }
    @Test
    public void testBasic1() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args1 = {"C:\\Users\\user\\IdeaProjects\\hw4\\small.txt"};
        Bank.main(args1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        String s = "acct: 0 balance: 1001 trans: 1\r\n" +
                "acct: 1 balance: 999 trans: 1\r\n" +
                "acct: 2 balance: 1001 trans: 1\r\n" +
                "acct: 3 balance: 999 trans: 1\r\n" +
                "acct: 4 balance: 1001 trans: 1\r\n" +
                "acct: 5 balance: 999 trans: 1\r\n" +
                "acct: 6 balance: 1001 trans: 1\r\n" +
                "acct: 7 balance: 999 trans: 1\r\n" +
                "acct: 8 balance: 1001 trans: 1\r\n" +
                "acct: 9 balance: 999 trans: 1\r\n" +
                "acct: 10 balance: 1001 trans: 1\r\n" +
                "acct: 11 balance: 999 trans: 1\r\n" +
                "acct: 12 balance: 1001 trans: 1\r\n" +
                "acct: 13 balance: 999 trans: 1\r\n" +
                "acct: 14 balance: 1001 trans: 1\r\n" +
                "acct: 15 balance: 999 trans: 1\r\n" +
                "acct: 16 balance: 1001 trans: 1\r\n" +
                "acct: 17 balance: 999 trans: 1\r\n" +
                "acct: 18 balance: 1001 trans: 1\r\n" +
                "acct: 19 balance: 999 trans: 1\r\n";
        assertEquals(s,os.toString());
    }

    @Test
    public void test0() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args1 = {"C:\\Users\\user\\IdeaProjects\\hw4\\small.txt","5"};
        Bank.main(args1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        String s = "acct: 0 balance: 1001 trans: 1\r\n" +
                "acct: 1 balance: 999 trans: 1\r\n" +
                "acct: 2 balance: 1001 trans: 1\r\n" +
                "acct: 3 balance: 999 trans: 1\r\n" +
                "acct: 4 balance: 1001 trans: 1\r\n" +
                "acct: 5 balance: 999 trans: 1\r\n" +
                "acct: 6 balance: 1001 trans: 1\r\n" +
                "acct: 7 balance: 999 trans: 1\r\n" +
                "acct: 8 balance: 1001 trans: 1\r\n" +
                "acct: 9 balance: 999 trans: 1\r\n" +
                "acct: 10 balance: 1001 trans: 1\r\n" +
                "acct: 11 balance: 999 trans: 1\r\n" +
                "acct: 12 balance: 1001 trans: 1\r\n" +
                "acct: 13 balance: 999 trans: 1\r\n" +
                "acct: 14 balance: 1001 trans: 1\r\n" +
                "acct: 15 balance: 999 trans: 1\r\n" +
                "acct: 16 balance: 1001 trans: 1\r\n" +
                "acct: 17 balance: 999 trans: 1\r\n" +
                "acct: 18 balance: 1001 trans: 1\r\n" +
                "acct: 19 balance: 999 trans: 1\r\n";
        assertEquals(s,os.toString());
    }
    @Test
    public void test1() {
        String[] args = {"test1.txt", "1"};
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        Bank.main(args);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        String s = "acct: 0 balance: 2000 trans: 5\r\n" +
                "acct: 1 balance: 0 trans: 5\r\n" +
                "acct: 2 balance: 1000 trans: 0\r\n" +
                "acct: 3 balance: 1000 trans: 0\r\n" +
                "acct: 4 balance: 1000 trans: 0\r\n" +
                "acct: 5 balance: 1000 trans: 0\r\n" +
                "acct: 6 balance: 1000 trans: 0\r\n" +
                "acct: 7 balance: 1000 trans: 0\r\n" +
                "acct: 8 balance: 1000 trans: 0\r\n" +
                "acct: 9 balance: 1000 trans: 0\r\n" +
                "acct: 10 balance: 1000 trans: 0\r\n" +
                "acct: 11 balance: 1000 trans: 0\r\n" +
                "acct: 12 balance: 1000 trans: 0\r\n" +
                "acct: 13 balance: 1000 trans: 0\r\n" +
                "acct: 14 balance: 1000 trans: 0\r\n" +
                "acct: 15 balance: 1000 trans: 0\r\n" +
                "acct: 16 balance: 1000 trans: 0\r\n" +
                "acct: 17 balance: 1000 trans: 0\r\n" +
                "acct: 18 balance: 1000 trans: 0\r\n" +
                "acct: 19 balance: 1000 trans: 0\r\n";
        assertEquals(s, os.toString());
    }

}