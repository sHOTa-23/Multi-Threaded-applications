import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebFrame {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JTable table;
    private static DefaultTableModel model;
    private static volatile AtomicInteger count = new AtomicInteger(0);
    private static volatile AtomicInteger comp = new AtomicInteger(0);
    private static volatile Lock lock = new ReentrantLock();
    private static List<String> urls = new ArrayList<>();
    private static JLabel running;
    private static JLabel elapsed;
    private static JLabel completed;
    private static JProgressBar bar;
    private static WebFrame wFrame;
    private static JButton single;
    private static JButton multi;
    private static JButton stop;
    private static Launcher launcher;

    public static void main(String[] args) {
        wFrame = new WebFrame();
        wFrame.setUp();
    }
    //function to change table's concrete row with string
    public void highlightTable(String str, int row) {
        SwingUtilities.invokeLater(() -> model.setValueAt(str, row, 1));
    }

    //three cases: increment + changing label, decrement + changing label, increment completed numb + change bar and label
    public void updateInfo(int indicator) {
        if (indicator == 0) {
            int a = count.incrementAndGet();
            SwingUtilities.invokeLater(() -> running.setText("Running: " + a));
            return;
        }
        if (indicator == 1) {
            int a = count.decrementAndGet();
            SwingUtilities.invokeLater(() -> running.setText("Running: " + a));
            return;
        }
        int a = comp.incrementAndGet();
        SwingUtilities.invokeLater(() -> {
            bar.setValue(a);
            completed.setText("completed: " + a);
        });


    }
    //setUp core
    private void setUp() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setUpSouth();
        setUpCenter();
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    //set up center with table
    private static void setUpCenter() {
        model = new DefaultTableModel(new String[]{"url", "status"}, 0);
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setUpTableInfo();
    }
    //read all the urls from file


    //!!!!!!!!! change the path of the file !!!!!!!!!
    private static void setUpTableInfo() {
        File file = new File("C:\\Users\\user\\IdeaProjects\\hw4\\links.txt");
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String url = scan.nextLine();
                urls.add(url);
                model.addRow(new String[]{url, ""});
            }
            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //sets up buttons and staff
    private static void setUpSouth() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel butt1 = new JPanel();
        butt1.setLayout(new BorderLayout());
        single = new JButton("Single Thread Fetch");
        panel.add(single);
        multi = new JButton("Concurrent Fetch");
        panel.add(multi);
        JTextField text = new JTextField();
        text.setMaximumSize(new Dimension(40, 40));
        panel.add(text);
        running = new JLabel("Running: ");
        completed = new JLabel("Completed: ");
        elapsed = new JLabel("Elapsed: ");
        panel.add(running);
        panel.add(completed);
        panel.add(elapsed);
        bar = new JProgressBar();
        panel.add(bar);
        stop = new JButton("Stop");
        panel.add(stop);
        mainPanel.add(panel, BorderLayout.SOUTH);   //let the swing thread set up bar and label info  and then start launcher
        single.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            setUpForLauncher();
            launcher = new Launcher(1);
            launcher.start();
        }));
        multi.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            try {  //same here except first we need to parse int from string
                launcher = new Launcher(Integer.parseInt(text.getText()));
                setUpForLauncher();
                launcher.start();
            } catch (NumberFormatException n) {
                n.printStackTrace();
            }

        }));   //synchronized block with locks
        stop.addActionListener(e -> {
            lock.lock();
            launcher.interrupt();
            lock.unlock();
        });
    }

    //initial form of frame
    private static void setUpForLauncher() {
        single.setEnabled(false);
        multi.setEnabled(false);
        stop.setEnabled(true);
        clearStatus();
        elapsed.setText("Elapsed: ");
        bar.setValue(0);
        bar.setMaximum(urls.size());
        count.set(0);
        comp.set(0);
    }
    //clear all the text from first collumn
    private static void clearStatus() {
        for (int i = 0; i < urls.size(); i++) {
            model.setValueAt("", i, 1);
        }
    }

    private static class Launcher extends Thread {
        private Semaphore sem;
        List<WebWorker> workers = new ArrayList<>();
            //init semaphore with limit
        public Launcher(int limit) {
            sem = new Semaphore(limit);
        }


        private void updateInfo(long diff) {
            count.decrementAndGet();    //decrement for launcher thread
            SwingUtilities.invokeLater(() -> {
                elapsed.setText("Elapsed: " + diff + " ms");
                running.setText("Running : " + count.get());
                single.setEnabled(true);
                multi.setEnabled(true);
                stop.setEnabled(false);
            });
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            count.incrementAndGet();    //increment atomic integer
            try {
                for (int i = 0; i < urls.size(); i++) {
                    sem.acquire();    //acquire semaphore
                    lock.lock();    // this is used to avoid (stop + starting thread) problem
                    WebWorker worker = new WebWorker(urls.get(i), wFrame, i, sem);
                    workers.add(worker);
                    worker.start();
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                stop.setEnabled(false);   //if interrupted interrupt all the workers which was started
                for (WebWorker ww : workers) {
                    ww.interrupt();
                }
            }
            //after that join them to this launcher thread, in order to update info after all the thread will finnish
            for (WebWorker worker : workers) {
                try {
                    stop.setEnabled(false);
                    worker.join();
                } catch (InterruptedException ex) {  //if this moment was interrupted then interrupt all the worker threads
                    stop.setEnabled(false);
                    for (WebWorker ww : workers) {
                        ww.interrupt();
                    }
                }
            }
            long finnish = System.currentTimeMillis();
            updateInfo(finnish - start);
        }
    }

}
