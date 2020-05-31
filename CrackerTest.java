import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CrackerTest {
    @Test
    public void testBasic() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args1 = {};
        Cracker.main(args1);
        assertEquals(os.toString(),"Args: target length [workers]\r\n");
    }
    @Test
    public void testBasic1() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args1 = {"d1854cae891ec7b29161ccaf79a24b00c274bdaa","1"};
        Cracker.main(args1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        assertEquals("n\r\n", os.toString());
    }
    @Test
    public void testGenerate() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args = {"a!"};
        Cracker.main(args);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        assertEquals("34800e15707fae815d7c90d49de44aca97e2d759\r\n", os.toString());
    }

    @Test
    public void testGenerate1() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args = {"xyz"};
        Cracker.main(args);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        assertEquals("66b27417d37e024c46526c2f6d358a754fc552f3\r\n", os.toString());
    }

    @Test
    public void test1() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args = {"66b27417d37e024c46526c2f6d358a754fc552f3", "3", "1"};
        Cracker.main(args);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}
        assertEquals("xyz\r\n", os.toString());
    }

    @Test
    public void test11() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args = {"66b27417d37e024c46526c2f6d358a754fc552f3", "3", "2"};
        Cracker.main(args);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        assertEquals("xyz\r\n", os.toString());
    }

    @Test
    public void test12() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args = {"66b27417d37e024c46526c2f6d358a754fc552f3", "2", "-1"};
        Cracker.main(args);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        assertEquals(0, os.toString().length());
    }

    @Test
    public void test13() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args = {"66b27417d37e024c46526c2f6d358a754fc552f3", "2", "33"};
        Cracker.main(args);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        assertEquals(0, os.toString().length());
    }

    @Test
    public void test14() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        String[] args = {"fd1286353570c5703799ba76999323b7c7447b06", "3", "44"};
        Cracker.main(args);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        assertEquals("no\r\n", os.toString());
    }


}