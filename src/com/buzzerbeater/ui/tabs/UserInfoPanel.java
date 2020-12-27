package com.buzzerbeater.ui.tabs;

import com.buzzerbeater.ui.BuzzerBeaterAssistant;
import com.buzzerbeater.utils.BrowserType;
import com.buzzerbeater.utils.Files;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserInfoPanel extends JPanel {
    private final JTextField teamIDField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel lblActionresult;
    private final JButton btnSave;
    private final JButton btnLoad;
    private final JCheckBox chckbxUseVisibleBrowser;
    private final JComboBox<BrowserType> comboBoxBrowser;

    public UserInfoPanel() {
        setName("User info");
        setLayout(null);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(5, 8, 102, 16);
        lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblUsername);

        usernameField = new JTextField();
        lblUsername.setLabelFor(usernameField);
        usernameField.setBounds(118, 8, 405, 29);
        usernameField.setHorizontalAlignment(SwingConstants.LEFT);
        add(usernameField);
        usernameField.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
        lblPassword.setBounds(5, 42, 102, 16);
        add(lblPassword);

        passwordField = new JPasswordField();
        lblPassword.setLabelFor(passwordField);
        passwordField.setBounds(118, 43, 405, 28);
        add(passwordField);

        teamIDField = new JTextField();
        teamIDField.setBounds(118, 77, 405, 28);
        add(teamIDField);

        JLabel lblTeamid = new JLabel("TeamID");
        lblTeamid.setLabelFor(teamIDField);
        lblTeamid.setHorizontalAlignment(SwingConstants.LEFT);
        lblTeamid.setBounds(5, 80, 102, 16);
        add(lblTeamid);

        lblActionresult = new JLabel("");
        lblActionresult.setBounds(216, 168, 230, 25);
        add(lblActionresult);


        btnSave = new JButton("Save");
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                File selFile = new File("data/userInfo.properties");

                // create the list from usersInfo fields
                ArrayList<String> userInfo = new ArrayList<String>();
                userInfo.add(usernameField.getText());
                userInfo.add(new String(passwordField.getPassword()));
                userInfo.add(teamIDField.getText());
                userInfo.add(comboBoxBrowser.getSelectedItem().toString());
                userInfo.add(chckbxUseVisibleBrowser.isSelected() ? "true" : "false");


                try {
                    Files.saveLinesToFile(userInfo, selFile);
                    lblActionresult.setForeground(Color.BLUE);
                    lblActionresult.setText("Info saved!" + System.getProperty("line.separator"));
                    return;
                } catch (Exception exc) {
                    lblActionresult.setForeground(Color.RED);
                    lblActionresult.setText("ERROR: Info not saved!" + System.getProperty("line.separator"));
                    return;
                }
            }
        });
        btnSave.setBounds(5, 178, 97, 25);
        add(btnSave);

        btnLoad = new JButton("Load");
        btnLoad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                loadUserInfo();
            }
        });
        btnLoad.setBounds(107, 178, 97, 25);
        add(btnLoad);

        JLabel useVisibleBrowserLbl = new JLabel("Visible browser");
        useVisibleBrowserLbl.setHorizontalAlignment(SwingConstants.LEFT);
        useVisibleBrowserLbl.setBounds(5, 150, 102, 16);
        add(useVisibleBrowserLbl);

        chckbxUseVisibleBrowser = new JCheckBox("(if checked browser will be visible but app will run 3-4 times slower)");
        chckbxUseVisibleBrowser.setBounds(118, 148, 405, 18);
        add(chckbxUseVisibleBrowser);

        JLabel lblBrowser = new JLabel("Browser");
        lblBrowser.setHorizontalAlignment(SwingConstants.LEFT);
        lblBrowser.setBounds(5, 118, 102, 16);
        add(lblBrowser);

        comboBoxBrowser = new JComboBox<BrowserType>();
        comboBoxBrowser.setEditable(true);
        for(BrowserType bt : BrowserType.values()) {
            comboBoxBrowser.addItem(bt);
        }
        comboBoxBrowser.setBounds(118, 113, 142, 26);
        add(comboBoxBrowser);

        // initial actions
        loadUserInfo();
    }

    private void loadUserInfo() {
        File selFile = new File("data/userInfo.properties");

        // create the list from usersInfo fields
        List<String> userInfo = new ArrayList<String>();

        try {
            userInfo = Files.readLinesFromFile(selFile);
            if(userInfo.size() < 4 || userInfo.size() > 5) {
                lblActionresult.setForeground(Color.RED);
                lblActionresult.setText("ERROR: Corrupted properties file" + System.getProperty("line.separator"));
                return;
            }

            // update the configuration and the UI
            BuzzerBeaterAssistant.USERNAME = userInfo.get(0);
            BuzzerBeaterAssistant.PASSWORD = userInfo.get(1);
            BuzzerBeaterAssistant.TEAMID = userInfo.get(2);
            BuzzerBeaterAssistant.BROWSERTYPE = BrowserType.valueOf(userInfo.get(3));

            usernameField.setText(BuzzerBeaterAssistant.USERNAME);
            passwordField.setText(BuzzerBeaterAssistant.PASSWORD);
            teamIDField.setText(BuzzerBeaterAssistant.TEAMID);
            comboBoxBrowser.setSelectedItem(BuzzerBeaterAssistant.BROWSERTYPE);

            if(userInfo.size() == 5 && userInfo.get(4).toLowerCase().trim().equals("true")) {
                BuzzerBeaterAssistant.USEVISIBLEBROWSER = true;
                chckbxUseVisibleBrowser.setSelected(true);
            } else {
                chckbxUseVisibleBrowser.setSelected(false);
                BuzzerBeaterAssistant.USEVISIBLEBROWSER = true;
            }

            lblActionresult.setForeground(Color.BLUE);
            lblActionresult.setText("Info loaded!" + System.getProperty("line.separator"));

            return;
        } catch (Exception exc) {
            lblActionresult.setForeground(Color.RED);
            lblActionresult.setText("ERROR: Info not loaded!" + System.getProperty("line.separator"));
            return;
        }
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getTeamID() {
        return teamIDField.getText();
    }
}
