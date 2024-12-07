package tickets.utils;

import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.swing.JLabel;
import javax.swing.*;
import java.awt.event.ActionListener;

public class Countdown {
    private JLabel labelTimeDetail;
    private Timer timer;

    public Countdown(JLabel labelTimeDetail, LocalDateTime startTime) {
        this.labelTimeDetail = labelTimeDetail;

        long secondsRemaining = Duration.between(LocalDateTime.now(), startTime.plusMinutes(5)).getSeconds();
        if (secondsRemaining <= 0) {
            return;
        }

        startTimer(secondsRemaining);
    }

    private void startTimer(long initialSeconds) {
        final long[] secondsRemaining = { initialSeconds };
        long restOfInitial = initialSeconds - 4;

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secondsRemaining[0] > restOfInitial) {
                    secondsRemaining[0]--;
                    labelTimeDetail.setText(formatTime(secondsRemaining[0]));
                } else {
                    timer.stop();
                    labelTimeDetail.setText("00:00:00");
                }
            }
        });
        timer.start();
    }

    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
