package com.buzzerbeater.ui.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class FooterPanel extends JPanel {
    public FooterPanel() {
        setLayout(null);

        JLabel lblPowerdBySpaciulis = new JLabel("Powered by spaciulis");
        lblPowerdBySpaciulis.setHorizontalAlignment(SwingConstants.CENTER);
        lblPowerdBySpaciulis.setFont(new Font("Courier New", Font.BOLD, 16));
        lblPowerdBySpaciulis.setForeground(Color.BLUE);
        lblPowerdBySpaciulis.setBounds(0, 0, 290, 37);
        lblPowerdBySpaciulis.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblPowerdBySpaciulis.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            URI uri = new URI("http://www.buzzerbeater.com/team/100438/overview.aspx");
                            desktop.browse(uri);
                        } catch (Exception ex) {}
                    }
                }
            }
        });
        add(lblPowerdBySpaciulis);

        JLabel lblEmail = new JLabel("spaciulis@gmail.com");
        lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
        lblEmail.setForeground(Color.BLUE);
        lblEmail.setFont(new Font("Courier New", Font.BOLD, 16));
        lblEmail.setBounds(311, 0, 290, 37);
        lblEmail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblEmail.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            URI uri = new URI("mailto:"+lblEmail.getText());
                            desktop.browse(uri);
                        } catch (Exception ex) {}
                    }
                }
            }
        });
        add(lblEmail);

        JLabel lblYoutube = new JLabel("youtube");
        lblYoutube.setHorizontalAlignment(SwingConstants.CENTER);
        lblYoutube.setForeground(Color.BLUE);
        lblYoutube.setFont(new Font("Courier New", Font.BOLD, 16));
        lblYoutube.setBounds(613, 0, 290, 37);
        lblYoutube.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblYoutube.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            URI uri = new URI("https://www.youtube.com/channel/UCdE0dAjswIhzKQ2xNHE38Ng");
                            desktop.browse(uri);
                        } catch (Exception ex) {}
                    }
                }
            }
        });
        add(lblYoutube);
    }
}
