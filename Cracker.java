// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/
import java.security.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;


public class Cracker {
    // Array of chars used to produce strings
    public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
    private static volatile CountDownLatch latch;
    private static volatile HashSet<String> words;

    /*
     Given a byte[] array, produces a hex String,
     such as "234a6f". with 2 chars for each byte in the array.
     (provided code)
    */
    public static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int val = bytes[i];
            val = val & 0xff;  // remove higher bits, sign
            if (val < 16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    private static String generateHash(String targ) {
        String s = "";
        try {      //using MessageDigest  class to generate hashed words
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(targ.getBytes());
            s = hexToString(md.digest());
        } catch (NoSuchAlgorithmException e) {}
        return s;
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Args: target length [workers]");
            return;
        }
        // args: targ len [num]
        String targ = args[0];
        if (args.length == 1) System.out.println(generateHash(targ));
        else {
            int len = Integer.parseInt(args[1]);
            int num = 1;
            if (args.length > 2) {
                num = Integer.parseInt(args[2]);
                if(num < 0) num = 1;
                if (num > CHARS.length) num = CHARS.length;
            }
            //init latch with threads num
            latch = new CountDownLatch(num);
            words = new HashSet<>();  //use hashSet to avoid duplicates
            int division = CHARS.length / num;
            int mod = CHARS.length % num;
            int counter = 0;
            if (mod == 0) {     //if character num is divisible by worker num divide equally
                for (int i = 0; i < num; i++) {
                    char[] chars = Arrays.copyOfRange(CHARS, counter, counter + division); //create local chars
                    new Worker(len, targ, chars).start();
                    counter += division;
                }
            } else {
                for (int i = 0; i < num - 1; i++) {
                    char[] chars = Arrays.copyOfRange(CHARS, counter, counter + division);
                    new Worker(len, targ, chars).start();
                    counter += division;
                }
                char[] chars = Arrays.copyOfRange(CHARS, counter, CHARS.length);
                new Worker(len, targ, chars).start(); //start last worker
            }
            try {
                latch.await();    //wait for workers
                for (String s : words) System.out.println(s); //print every possible version
            } catch (InterruptedException e){}
        }

        // a! 34800e15707fae815d7c90d49de44aca97e2d759
        // xyz 66b27417d37e024c46526c2f6d358a754fc552f3
    }

    private static class Worker extends Thread {
        private char[] myChars;
        private static int maxInt;
        private static String hashWord;

        //need max length, hashed word and divided chars
        public Worker(int maxInt, String hashWord, char[] myChars) {
            this.myChars = myChars;
            Worker.maxInt = maxInt;
            Worker.hashWord = hashWord;
        }
            //simple recursion  ro get all combination
        private static void getAllCombination(String word) {
            if (word.length() > maxInt) return;  //base case
            if (hashWord.equals(generateHash(word))) {
                words.add(word);    //if matched put it in the set
            }
            for (char ch : CHARS) {    // generate other combinations
                getAllCombination(word + ch);
            }
        }


        @Override
        public void run() {
            for (int i = 0; i < myChars.length; i++) {   //for every divided char get all combination
                getAllCombination(String.valueOf(myChars[i]));
            }
            latch.countDown();  //count down
        }
    }
}
