// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
    private volatile boolean isActive;
    private static int LIMIT = 100000000;
    private static volatile int reachPoint = 10000;
    private SwingThread thread;
    private JLabel label;
    private JTextField textField;

    public JCount() {
        isActive = false;
        textField = new JTextField();
        textField.setText("0");
        label = new JLabel();
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        Thread curr;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(textField);
        add(label);
        add(start);
        add(stop);
        thread = null;
        start.addActionListener(e -> {
            //if thread is not null, interrupt it and start it again
            if (thread != null) thread.interrupt();
            int limit = 0;
            if(Integer.parseInt(textField.getText()) >= 1 &&Integer.parseInt(textField.getText()) <= LIMIT) limit = Integer.parseInt(textField.getText());
            thread = new SwingThread(label,limit);
            thread.start();
        });
        stop.addActionListener(e -> {
            //if thread is not null interrupt it
            if (thread != null) thread.interrupt();
        });
        add(Box.createRigidArea(new Dimension(0, 40)));
    }

    private static class SwingThread extends Thread {
        private volatile JLabel label;
        private volatile  int limit;
        //we need each separate JLabel for each thread and limit of course
        public SwingThread(JLabel label,int limit) {
            this.label = label;
            this.limit = limit;
        }

        @Override
        public void run() {
            boolean isInterrupted = false;
            for (int i = 1; i <= limit; i++) {
                if (i % reachPoint == 0) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        isInterrupted = true; //if interrupted break the cycle and flag my boolean true
                        break;
                    }
                    String text = Integer.toString(i);
                    SwingUtilities.invokeLater(() -> label.setText(text)); //use invokeLater to set text to label
                }
                if (isInterrupted()) {
                    isInterrupted = true;
                    break;
                }
            }
            if (isInterrupted) SwingUtilities.invokeLater(() -> label.setText("0"));
            //this particular else clause is to show non 10000 divisible nums too, for example 100003 and etc
            else SwingUtilities.invokeLater(() -> label.setText(Integer.toString(limit)));
        }
    }
    //method which crates 4 JCount object and one frame for it
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("The Count");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(300, 600);

        frame.add(new JCount());
        frame.add(new JCount());
        frame.add(new JCount());
        frame.add(new JCount());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static public void main(String[] args) {
        //invoke gui staff
        SwingUtilities.invokeLater(JCount::createAndShowGUI);

    }
}

