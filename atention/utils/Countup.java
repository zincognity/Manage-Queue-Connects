package atention.utils;

import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Countup {
    private JLabel labelTimeDetail;
    private Timer timer;
    private int secondsRemaining;

    public Timer getTimer() {
        return this.timer;
    }

    public Countup(JLabel labelTimeDetail) {
        secondsRemaining = 0;
        this.labelTimeDetail = labelTimeDetail;
        startTimer();
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining++;
                labelTimeDetail.setText(formatTime(secondsRemaining));
            }
        });
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
        labelTimeDetail.setText("00:00:00");
    }

    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
