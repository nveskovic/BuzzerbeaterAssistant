package com.buzzerbeater.ui.tabs;

import com.buzzerbeater.ui.BuzzerBeaterAssistant;
import com.buzzerbeater.utils.Messages;
import com.buzzerbeater.utils.SwingUIHelper;
import com.buzzerbeater.workers.SendMessagesToOwners;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendMessagesPanel  extends JPanel {

    private final JComboBox<String> comboBoxSelectMessage;
    private final JCheckBox chckbxExcludeBotAndRetired;
    private final JButton btnLoadFromFailedMessages;
    private final JButton btnLoadPlayersFrom;
    private List<String> playersWithUnsentMsgs = new ArrayList<String>();
    private List<String> playersBotAndRetired = new ArrayList<String>();
    private final JTextArea txtMessageEN;
    private final JScrollPane scrollPanePlayers;
    private final JTextArea txtPlayersList;
    private final JScrollPane scrollPaneOutput;
    private final JTextArea txtLogOutput;
    private final JButton btnSendMessages;
    private final JProgressBar sendMessagesProgresBar;
    private final JCheckBox chckbxSkipTLPlayers;
    private HashMap<String,String> messagesMap = Messages.getSkillsUpdateMessageMap();
    private final JTextArea txtMessageDomestic;
    private final JTextField textFieldSubjectLocalized;
    private final JTextField textFieldSubjectEnglish;

    public SendMessagesPanel() {
        setName("Send messages");
        setLayout(null);

        JLabel lblDomesticMessage = new JLabel("Localized message");
        lblDomesticMessage.setBounds(376, 7, 124, 16);
        add(lblDomesticMessage);

        JLabel lblEnglishMessage = new JLabel("English message");
        lblEnglishMessage.setHorizontalAlignment(SwingConstants.RIGHT);
        lblEnglishMessage.setBounds(1154, 7, 124, 16);
        add(lblEnglishMessage);

        txtMessageDomestic = new JTextArea();
        lblDomesticMessage.setLabelFor(txtMessageDomestic);
        txtMessageDomestic.setLineWrap(true);
        txtMessageDomestic.setText(Messages.getCustomMap().get(Messages.MESSAGE_DOMESTIC_KEY));
        txtMessageDomestic.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtMessageDomestic.setTabSize(4);
        txtMessageDomestic.setWrapStyleWord(true);
        String tooltip = "Type " + Messages.playerNamePlaceholder + " to have player name set in the message.\r\n" +
                "Type  " + Messages.playerIDPlaceholder + " to have player ID set in the message.";
        txtMessageDomestic.setToolTipText(tooltip);
        txtMessageDomestic.setBounds(376, 75, 449, 191);
        add(txtMessageDomestic);

        textFieldSubjectLocalized = new JTextField();
        textFieldSubjectLocalized.setBounds(376, 40, 407, 22);
        textFieldSubjectLocalized.setText(Messages.getCustomMap().get(Messages.SUBJECT_DOMESTIC_KEY));
        textFieldSubjectLocalized.setColumns(10);
        add(textFieldSubjectLocalized);

        textFieldSubjectEnglish = new JTextField();
        textFieldSubjectEnglish.setColumns(10);
        textFieldSubjectEnglish.setBounds(871, 39, 407, 22);
        textFieldSubjectEnglish.setText(Messages.getCustomMap().get(Messages.SUBJECT_ENGLISH_KEY));
        add(textFieldSubjectEnglish);

        txtMessageEN = new JTextArea();
        lblEnglishMessage.setLabelFor(txtMessageEN);
        txtMessageEN.setWrapStyleWord(true);
        txtMessageEN.setToolTipText(tooltip);
        txtMessageEN.setText(Messages.getCustomMap().get(Messages.MESSAGE_ENGLISH_KEY));
        txtMessageEN.setTabSize(4);
        txtMessageEN.setLineWrap(true);
        txtMessageEN.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtMessageEN.setBounds(837, 75, 441, 191);
        add(txtMessageEN);

        comboBoxSelectMessage = new JComboBox<String>();
        comboBoxSelectMessage.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {

                    if(comboBoxSelectMessage.getSelectedIndex()!=0) {

                        // disable editing
                        txtMessageDomestic.setEnabled(false);
                        txtMessageEN.setEnabled(false);

                        // populate message map
                        if(comboBoxSelectMessage.getSelectedIndex()==1) {
                            messagesMap = Messages.getGreetingMessageMap();
                        } else if (comboBoxSelectMessage.getSelectedIndex()==2) {
                            messagesMap = Messages.getSkillsUpdateMessageMap();
                        }

                    } else {
                        messagesMap = Messages.getCustomMap();

                        // enable editing
                        txtMessageDomestic.setEnabled(true);
                        txtMessageEN.setEnabled(true);
                    }

                    // set text
                    txtMessageDomestic.setText(messagesMap.get(Messages.MESSAGE_DOMESTIC_KEY));
                    txtMessageEN.setText(messagesMap.get(Messages.MESSAGE_ENGLISH_KEY));
                    textFieldSubjectEnglish.setText(messagesMap.get(Messages.SUBJECT_ENGLISH_KEY));
                    textFieldSubjectLocalized.setText(messagesMap.get(Messages.SUBJECT_DOMESTIC_KEY));
                }
            }
        });
        comboBoxSelectMessage.setModel(new DefaultComboBoxModel<String>(
                new String[] {"Custom message (type your own)",
                        "Default NT Scout greeting message",
                        "Default NT Scout ask for skills update"}));
        comboBoxSelectMessage.setEditable(true);
        comboBoxSelectMessage.setBounds(685, 4, 287, 22);
        comboBoxSelectMessage.setSelectedIndex(2);
        add(comboBoxSelectMessage);

        scrollPanePlayers = new JScrollPane();
        scrollPanePlayers.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanePlayers.setBounds(12, 37, 352, 599);
        add(scrollPanePlayers);

        txtPlayersList = new JTextArea();
        scrollPanePlayers.setViewportView(txtPlayersList);
        txtPlayersList.setMargin(new Insets(2, 5, 2, 2));

        scrollPaneOutput = new JScrollPane();
        scrollPaneOutput.setBounds(376, 317, 902, 351);
        add(scrollPaneOutput);

        txtLogOutput = new JTextArea();
        scrollPaneOutput.setViewportView(txtLogOutput);
        txtLogOutput.setWrapStyleWord(true);
        txtLogOutput.setToolTipText("Log output");
        txtLogOutput.setTabSize(4);

        btnSendMessages = new JButton("Send messages");
        btnSendMessages.setBounds(376, 279, 134, 25);
        add(btnSendMessages);

        sendMessagesProgresBar = new JProgressBar();
        sendMessagesProgresBar.setStringPainted(true);
        sendMessagesProgresBar.setBounds(646, 279, 632, 25);
        add(sendMessagesProgresBar);

        chckbxSkipTLPlayers = new JCheckBox("Skip TL players?");
        chckbxSkipTLPlayers.setBounds(512, 275, 126, 33);
        add(chckbxSkipTLPlayers);

        btnSendMessages.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(btnSendMessages.isEnabled()) {

                    txtLogOutput.setForeground(Color.BLACK);
                    java.util.List<String> playerIDs = SwingUIHelper.getListOfLinesFromJTextArea(txtPlayersList);

                    // get messages from the UI form
                    messagesMap = Messages.getBaseMap();
                    messagesMap.put(
                            Messages.SUBJECT_DOMESTIC_KEY, textFieldSubjectLocalized.getText());
                    messagesMap.put(
                            Messages.SUBJECT_ENGLISH_KEY, textFieldSubjectEnglish.getText());
                    messagesMap.put(
                            Messages.MESSAGE_DOMESTIC_KEY, txtMessageDomestic.getText()
                            //+"\n\nSent from my BuzzerBeater Assistant"
                            //+ " [link=https://www.facebook.com/buzzerbeaterassistant]"
                    );
                    messagesMap.put(
                            Messages.MESSAGE_ENGLISH_KEY, txtMessageEN.getText()
                            //+"\n\nSent from my BuzzerBeater Assistant"
                            //+ " [link=https://www.facebook.com/buzzerbeaterassistant]"
                    );

                    SendMessagesToOwners sendMessagesWorker = new SendMessagesToOwners(
                            BuzzerBeaterAssistant.USERNAME,
                            BuzzerBeaterAssistant.PASSWORD,
                            BuzzerBeaterAssistant.TEAMID,
                            playerIDs,
                            messagesMap,
                            txtLogOutput,
                            chckbxSkipTLPlayers.isSelected(), //skipTLPlayers
                            BuzzerBeaterAssistant.USEVISIBLEBROWSER, //visible browser
                            BuzzerBeaterAssistant.BROWSERTYPE // browser type
                    );

                    sendMessagesWorker.addPropertyChangeListener(
                            new PropertyChangeListener() {
                                public  void propertyChange(PropertyChangeEvent evt) {
                                    if ("progress".equals(evt.getPropertyName())) {
                                        sendMessagesProgresBar.setValue((Integer)evt.getNewValue());
                                    } else if ("state".equals(evt.getPropertyName())) {
                                        if(sendMessagesWorker.getState().equals(SwingWorker.StateValue.DONE)) {
                                            btnSendMessages.setEnabled(true);
                                            // update players lists
                                            playersWithUnsentMsgs = sendMessagesWorker.getPlayersWithMessagesNotSent();
                                            playersBotAndRetired = sendMessagesWorker.getPlayersFromRetiredAndBot();
                                        } else {
                                            btnSendMessages.setEnabled(false);
                                        }
                                    }
                                }
                            });

                    sendMessagesProgresBar.setValue(0);
                    sendMessagesWorker.execute();
                }
            }
        });	 // end of mouse-clicked listener


        chckbxExcludeBotAndRetired = new JCheckBox("Exclude BOT&retired");
        chckbxExcludeBotAndRetired.setSelected(true);
        chckbxExcludeBotAndRetired.setBounds(217, 643, 147, 25);
        add(chckbxExcludeBotAndRetired);

        btnLoadFromFailedMessages = new JButton("Load from failed messages");
        btnLoadFromFailedMessages.setHorizontalAlignment(SwingConstants.LEFT);
        btnLoadFromFailedMessages.setBounds(12, 643, 199, 25);
        add(btnLoadFromFailedMessages);

        btnLoadFromFailedMessages.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                // test data - uncomment if needed
                //				playersWithUnsentMsgs.clear();
                //				playersWithUnsentMsgs.add("asdasdasdads1");
                //				playersWithUnsentMsgs.add("asdasdasdads2");
                //				playersWithUnsentMsgs.add("asdasdasdads3");
                //
                //				playersBotAndRetired.clear();
                //				playersBotAndRetired.add("bot1");
                //				playersBotAndRetired.add("bot3");
                //				playersBotAndRetired.add("bot2");

                List<String> playersToResend = new ArrayList<String>();
                playersToResend.addAll(playersWithUnsentMsgs);
                if(!chckbxExcludeBotAndRetired.isSelected()) {
                    playersToResend.addAll(playersBotAndRetired);
                }

                if(playersToResend.isEmpty()) {
                    txtLogOutput.append("No players to resend messages to." + System.getProperty("line.separator"));
                    return;
                }

                txtPlayersList.setText(null);
                for(String player : playersToResend) {
                    txtPlayersList.append(player + System.getProperty("line.separator"));
                }
            }
        });


        btnLoadPlayersFrom = new JButton("Load players from file");
        btnLoadPlayersFrom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                JFileChooser fc = new JFileChooser(new File("."));
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Text files", "txt");
                fc.setFileFilter(filter);


                // Show open dialog; this method does not return until the dialog is closed
                fc.showOpenDialog(btnLoadPlayersFrom.getParent());
                File selFile = fc.getSelectedFile();

                SwingUIHelper.loadLinesFromFileToTextArea(selFile, txtPlayersList, txtLogOutput);
            }
        });
        btnLoadPlayersFrom.setBounds(12, 3, 175, 25);
        add(btnLoadPlayersFrom);

        JLabel lblOrEnterPlayers = new JLabel("or enter players manually");
        lblOrEnterPlayers.setLabelFor(btnLoadPlayersFrom);
        lblOrEnterPlayers.setBounds(195, 7, 169, 16);
        add(lblOrEnterPlayers);

        JLabel lblSubject = new JLabel("Subject");
        lblSubject.setBounds(811, 46, 53, 16);
        add(lblSubject);
    }
}
