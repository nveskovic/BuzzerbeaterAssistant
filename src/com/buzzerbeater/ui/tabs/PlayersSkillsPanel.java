package com.buzzerbeater.ui.tabs;

import com.buzzerbeater.ui.BuzzerBeaterAssistant;
import com.buzzerbeater.utils.SwingUIHelper;
import com.buzzerbeater.workers.GetSkillsOfPlayersByInvitingThemToNTWorker;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

public class PlayersSkillsPanel extends JPanel {
    private final JTextField textFieldOutputFile;
    private final JScrollPane scrollPanePlayersToScout;
    private final JTextArea textAreaPlayersToScout;
    private final JScrollPane scrollPaneLogArea;
    private final JCheckBox chckbxSkipBotsretired;
    private final JCheckBox chckbxInviteToNt;
    private final JTextArea textAreaLog;
    private final JProgressBar draftAndPlayersProgressBar;
    private final JButton btnStartScoutingPlayers;
    private final JTextField textFieldMiniumPot;
    private final JButton btnLoadFromFile;
    private final JTextField textFieldOutputFileDraftSummary;
    private final JTextField textFieldseason;
    private final JScrollPane scrollPane;
    private final JTextArea textAreaLeaguIDsNotexpanded;
    private final JScrollPane scrollPane_1;
    private final JTextArea textAreaLeaguIDsExpanded;
    private final JButton btnGetPlayerURLFromDraft;

    public PlayersSkillsPanel() {
        setLayout(null);
        setName("Scout Players");

        JLabel lblOutputFile = new JLabel("Output File");
        lblOutputFile.setBounds(405, 58, 76, 16);
        add(lblOutputFile);

        textFieldOutputFile = new JTextField();
        textFieldOutputFile.setText("playersSkills.csv");
        textFieldOutputFile.setBounds(481, 55, 116, 22);
        add(textFieldOutputFile);
        textFieldOutputFile.setColumns(10);

        JLabel lblPlayersToScout = new JLabel("Players to scout");
        lblPlayersToScout.setBounds(12, 17, 110, 16);
        add(lblPlayersToScout);

        scrollPanePlayersToScout = new JScrollPane();
        scrollPanePlayersToScout.setBounds(12, 90, 585, 364);
        add(scrollPanePlayersToScout);

        textAreaPlayersToScout = new JTextArea();
        textAreaPlayersToScout.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPanePlayersToScout.setViewportView(textAreaPlayersToScout);

        scrollPaneLogArea = new JScrollPane();
        scrollPaneLogArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneLogArea.setBounds(12, 520, 1257, 170);
        add(scrollPaneLogArea);

        chckbxSkipBotsretired = new JCheckBox("Skip bots&retired");
        chckbxSkipBotsretired.setSelected(true);
        chckbxSkipBotsretired.setBounds(12, 56, 134, 25);
        add(chckbxSkipBotsretired);

        chckbxInviteToNt = new JCheckBox("Invite to NT");
        chckbxInviteToNt.setSelected(true);
        chckbxInviteToNt.setBounds(161, 56, 113, 25);
        add(chckbxInviteToNt);

        textAreaLog = new JTextArea();
        textAreaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPaneLogArea.setViewportView(textAreaLog);

        draftAndPlayersProgressBar = new JProgressBar();
        draftAndPlayersProgressBar.setFont(new Font("Courier New", Font.BOLD, 20));
        draftAndPlayersProgressBar.setStringPainted(true);
        draftAndPlayersProgressBar.setBounds(12, 467, 1257, 40);
        add(draftAndPlayersProgressBar);

        btnStartScoutingPlayers = new JButton("Start scouting players");
        btnStartScoutingPlayers.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                //xxx
                List<String> playerUrls = SwingUIHelper.getListOfLinesFromJTextArea(textAreaPlayersToScout);

                GetSkillsOfPlayersByInvitingThemToNTWorker scoutPlayersWorker
                        = new GetSkillsOfPlayersByInvitingThemToNTWorker(
                        BuzzerBeaterAssistant.USERNAME,
                        BuzzerBeaterAssistant.PASSWORD,
                        BuzzerBeaterAssistant.TEAMID,
                        playerUrls,
                        Integer.parseInt(textFieldMiniumPot.getText()),
                        chckbxSkipBotsretired.isSelected(),
                        chckbxInviteToNt.isSelected(),
                        new File(textFieldOutputFile.getText()),
                        textAreaLog,
                        BuzzerBeaterAssistant.USEVISIBLEBROWSER,
                        BuzzerBeaterAssistant.BROWSERTYPE // browser type
                );

                scoutPlayersWorker.addPropertyChangeListener(
                        new PropertyChangeListener() {
                            public  void propertyChange(PropertyChangeEvent evt) {
                                if ("progress".equals(evt.getPropertyName())) {
                                    draftAndPlayersProgressBar.setValue((Integer)evt.getNewValue());
                                } else if ("state".equals(evt.getPropertyName())) {
                                    if(scoutPlayersWorker.getState().equals(SwingWorker.StateValue.DONE)) {
                                        btnStartScoutingPlayers.setEnabled(true);
                                    } else {
                                        btnStartScoutingPlayers.setEnabled(false);
                                    }
                                }
                            }
                        });

                draftAndPlayersProgressBar.setValue(0);
                scoutPlayersWorker.execute();
            }
        });
        btnStartScoutingPlayers.setBounds(429, 13, 168, 25);
        add(btnStartScoutingPlayers);

        JLabel lblMinimumPotential = new JLabel("Min potential");
        lblMinimumPotential.setBounds(273, 58, 84, 16);
        add(lblMinimumPotential);

        textFieldMiniumPot = new JTextField();
        textFieldMiniumPot.setText("5");
        textFieldMiniumPot.setBounds(357, 55, 26, 22);
        add(textFieldMiniumPot);
        textFieldMiniumPot.setColumns(2);

        btnLoadFromFile = new JButton("Load from file ...");
        btnLoadFromFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                JFileChooser fc = new JFileChooser(new File("."));
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Text files", "txt");
                fc.setFileFilter(filter);


                // Show open dialog; this method does not return until the dialog is closed
                fc.showOpenDialog(btnLoadFromFile.getParent());
                File selFile = fc.getSelectedFile();

                SwingUIHelper.loadLinesFromFileToTextArea(selFile, textAreaPlayersToScout ,textAreaLog);
            }
        });
        btnLoadFromFile.setBounds(108, 13, 134, 20);
        add(btnLoadFromFile);

        JLabel outputFileDraftSummaryLbl = new JLabel("Output File");
        outputFileDraftSummaryLbl.setBounds(639, 29, 76, 16);
        add(outputFileDraftSummaryLbl);

        textFieldOutputFileDraftSummary = new JTextField();
        textFieldOutputFileDraftSummary.setText("playersURLs.txt");
        textFieldOutputFileDraftSummary.setColumns(10);
        textFieldOutputFileDraftSummary.setBounds(715, 26, 116, 22);
        textFieldOutputFileDraftSummary.setEnabled(false);
        add(textFieldOutputFileDraftSummary);

        JLabel seasonLbl = new JLabel("Season");
        seasonLbl.setBounds(843, 29, 116, 16);
        add(seasonLbl);

        textFieldseason = new JTextField();
        textFieldseason.setText("0");
        textFieldseason.setColumns(10);
        textFieldseason.setBounds(912, 26, 116, 22);
        textFieldseason.setEnabled(false);
        add(textFieldseason);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(639, 90, 134, 364);
        add(scrollPane);

        textAreaLeaguIDsNotexpanded = new JTextArea();
        textAreaLeaguIDsNotexpanded.setFont(new Font("Monospaced", Font.PLAIN, 18));
        textAreaLeaguIDsNotexpanded.setText("1277\r\n1278-1281\r\n1282-1297\r\n7369-7432");
        scrollPane.setViewportView(textAreaLeaguIDsNotexpanded);
        textAreaLeaguIDsNotexpanded.setEnabled(false);

        JLabel lblLeagueIDs = new JLabel("League IDs");
        lblLeagueIDs.setBounds(639, 58, 110, 16);
        add(lblLeagueIDs);

        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(776, 90, 491, 364);
        add(scrollPane_1);

        textAreaLeaguIDsExpanded = new JTextArea();
        textAreaLeaguIDsExpanded.setLineWrap(true);
        textAreaLeaguIDsExpanded.setEditable(false);
        textAreaLeaguIDsExpanded.setEnabled(false);
        textAreaLeaguIDsExpanded.setWrapStyleWord(true);
        textAreaLeaguIDsExpanded.setText("Scouting is now done through BB API using python scripts");
        scrollPane_1.setViewportView(textAreaLeaguIDsExpanded);
        textAreaLeaguIDsExpanded.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JLabel lblLeagueIdsExpanded = new JLabel("League IDs Expanded");
        lblLeagueIdsExpanded.setBounds(1133, 58, 134, 16);
        add(lblLeagueIdsExpanded);

        btnGetPlayerURLFromDraft = new JButton("Get player URLs");
//		btnGetPlayerURLFromDraft.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				// expand the list of league ids
//				List<String> leagueIDsNotExpanded = SwingUIHelper.getListOfLinesFromJTextArea(textAreaLeaguIDsNotexpanded);
//				List<String> leagueIDsExpanded = new ArrayList<String>();
//				for(String nonexpanded : leagueIDsNotExpanded) {
//					if(nonexpanded.contains("-")) {
//						String[] bounds = nonexpanded.split("-");
//						try{
//							int min = Integer.parseInt(bounds[0].trim());
//							int max = Integer.parseInt(bounds[1].trim());
//							for(int i=min; i<=max; i++){
//								leagueIDsExpanded.add(""+i);
//							}
//						} catch(Exception e) {
//							textAreaLog.setText("ERROR: Can't expand league IDs from non-expanded list");
//							return;
//						}
//					} else {
//						leagueIDsExpanded.add(nonexpanded.trim());
//					}
//				}
//
//				// display expanded league ids
//				SwingUIHelper.setListOfLinesToJTextArea(leagueIDsExpanded, textAreaLeaguIDsExpanded, ",");
//
//				// get player URLs from draft page
//				GetPlayersFromDraftWorker getPlayersFromDraftWorker = new GetPlayersFromDraftWorker(
//						usernameField.getText(),
//						new String(passwordField.getPassword()),
//						teamIDField.getText(),
//						leagueIDsExpanded,
//						Integer.parseInt(textFieldseason.getText()),
//						new File(textFieldOutputFileDraftSummary.getText()),
//						textAreaLog,
//						chckbxUseVisibleBrowser.isSelected(),
//						BrowserType.valueOf(comboBoxBrowser.getSelectedItem().toString()) // browser type
//						);
//
//				getPlayersFromDraftWorker.addPropertyChangeListener(
//						new PropertyChangeListener() {
//							public  void propertyChange(PropertyChangeEvent evt) {
//								if ("progress".equals(evt.getPropertyName())) {
//									draftAndPlayersProgressBar.setValue((Integer)evt.getNewValue());
//								} else if ("state".equals(evt.getPropertyName())) {
//									if(getPlayersFromDraftWorker.getState().equals(SwingWorker.StateValue.DONE)) {
//										btnGetPlayerURLFromDraft.setEnabled(true);
//									} else {
//										btnGetPlayerURLFromDraft.setEnabled(false);
//									}
//								}
//							}
//						});
//
//				draftAndPlayersProgressBar.setValue(0);
//				getPlayersFromDraftWorker.execute();
//			}
//		});
        btnGetPlayerURLFromDraft.setBounds(1099, 25, 168, 25);
        btnGetPlayerURLFromDraft.setEnabled(false); // Scouting is now done through BB API
        btnGetPlayerURLFromDraft.setToolTipText("Scouting is now done through BB API");
        add(btnGetPlayerURLFromDraft);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBounds(619, 14, 2, 440);
        add(separator);
    }
}
