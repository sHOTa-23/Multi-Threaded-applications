import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import javax.swing.*;

public class WebWorker extends Thread {

    private volatile String urlInString;
    private volatile WebFrame frame;
    private volatile int row;
    private volatile Semaphore sem;

    public WebWorker(String ulrInString, WebFrame frame, int row, Semaphore sem) {
        this.urlInString = ulrInString;
        this.frame = frame;
        this.row = row;
        this.sem = sem;
    }

    @Override
    public void run() {
        frame.updateInfo(0);  //WebFrame method. case 0: increment running thread num on label
        download();  //slightly changed download method, added interruption handling cases
        frame.updateInfo(1);     //WebFrame method. case 1: decrement running thread num on label
        frame.updateInfo(-1); //WebFrame method. case -1: update bar and and decrement  completed thread num
        sem.release();  //release the semaphore

    }

    public void download() {
        InputStream input = null;
        StringBuilder contents = null;
        try {
            URL url = new URL(urlInString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);
            long start = System.currentTimeMillis();
            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }
            long finnish = System.currentTimeMillis();

            // Successful download if we get here
            frame.highlightTable(new SimpleDateFormat("HH:mm:ss").format(new Date(finnish)) +
                    "  " + (finnish - start) + "ms  " + contents.length() + "bytes", row);

        }
        // Otherwise control jumps to a catch...
        catch (MalformedURLException ignored) {
            frame.highlightTable("err", row);

        } catch (InterruptedException exception) {
            // YOUR CODE HERE
            // deal with interruption
            frame.highlightTable("Interrupted", row);
        } catch (IOException ignored) {
            frame.highlightTable("err", row);
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }

    }
}

