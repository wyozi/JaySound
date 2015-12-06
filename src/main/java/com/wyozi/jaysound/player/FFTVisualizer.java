package com.wyozi.jaysound.player;

import ddf.minim.analysis.FFT;

import javax.swing.*;
import java.awt.*;

/**
 * Created by wyozi on 6.12.2015.
 */
public class FFTVisualizer {
    private final JFrame frame;

    private volatile FFT fft;
    public void updateFFT(FFT fft) {
        this.fft = fft;
        this.frame.repaint();
    }

    final float[] bandCache = new float[512];

    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel() {
            long time;
            int draws;

            @Override
            protected void paintComponent(Graphics g) {
                if (fft == null)
                    return;

                int height = frame.getHeight() - 50;

                int x = 0;
                for (int i = 0;i < fft.specSize(); i++) {
                    g.setColor(Color.BLUE);
                    float band = fft.getBand(i);

                    float newBand = Math.max(bandCache[i] - 0.4f, band);
                    int ha = ((int)newBand*4);
                    g.fillRect(i*2, height - ha, 2, ha);

                    bandCache[i] = newBand;
                }

                if (time == 0 || time <= System.currentTimeMillis() - 1000) {
                    time = System.currentTimeMillis();
                    int fps = draws;
                    draws = 0;
                    frame.setTitle("FPS " + fps);
                }
                draws += 1;
            }
        };

        frame.setContentPane(p);

        Dimension d = new Dimension(520, 600);
        frame.setSize(d);
        frame.setMinimumSize(d);
        frame.setVisible(true);
    }
}
