package com.buzzerbeater.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.buzzerbeater.Version;
import com.buzzerbeater.utils.BrowserType;
import com.buzzerbeater.utils.Files;
import com.buzzerbeater.utils.Messages;
import com.buzzerbeater.utils.SwingUIHelper;
import com.buzzerbeater.workers.AutobidPlayerOnSaleWorker;
import com.buzzerbeater.workers.AutobidStaffOnSaleWorker;
import com.buzzerbeater.workers.GetPlayersFromDraftWorker;
import com.buzzerbeater.workers.GetSkillsOfPlayersByInvitingThemToNTWorker;
import com.buzzerbeater.workers.SendMessagesToOwners;

import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import java.awt.Toolkit;
import java.awt.Insets;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;

import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JSeparator;

public final class BuzzerBeaterAssistant {

	private boolean visibleBrowser = false;
	private JFrame frmBbassistant;
	private SendMessagesToOwners sendMessagesWorker;


	// variables
	HashMap<String,String> messagesMap = Messages.getSkillsUpdateMessageMap();
	List<String> playersWithUnsentMsgs = new ArrayList<String>();
	List<String> playersBotAndRetired = new ArrayList<String>();
	private JTextField txtPlayerID1;
	private JTextField txtMaxPrice1;
	private JTextField txtPlayerID2;
	private JTextField txtMaxPrice2;
	private JLabel lblAuctionEndsIn1;

	AutobidPlayerOnSaleWorker[] autobidWorkers = new AutobidPlayerOnSaleWorker[2];
	AutobidStaffOnSaleWorker[] autobidStaffWorkers = new AutobidStaffOnSaleWorker[2];
	private JTextField txtStaffID1;
	private JTextField txtStaffID2;
	private JTextField txtStaffMaxPrice2;
	private JTextField txtStaffMaxPrice1;
	private JTextField textFieldOutputFile;
	private JTextField textFieldMiniumPot;
	private JTextField textFieldOutputFileDraftSummary;
	private JTextField textFieldseason;
	private JTextField textFieldSubjectLocalized;
	private JTextField textFieldSubjectEnglish;
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JTextField teamIDField;
	private JLabel lblActionresult;
	private JCheckBox chckbxUseVisibleBrowser;
	private JLabel lblBrowser;
	private JComboBox<BrowserType> comboBoxBrowser;

	public boolean isVisibleBrowser() {
		return visibleBrowser;
	}


	public void setVisibleBrowser(boolean visibleBrowser) {
		this.visibleBrowser = visibleBrowser;
	}


	/**
	 * Create the application.
	 */
	BuzzerBeaterAssistant() {
		initialize();
	}


	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frmBbassistant = new JFrame();
		frmBbassistant.setIconImage(Toolkit.getDefaultToolkit().getImage(BuzzerBeaterAssistant.class.getResource("/images/icon.jpg")));
		frmBbassistant.setResizable(false);
		frmBbassistant.setName("mainFrame");
		frmBbassistant.getContentPane().setFont(new Font("Courier New", Font.PLAIN, 16));
		frmBbassistant.setTitle("BuzzerBeater Assistant - "  + Version.getVersion());
		frmBbassistant.setBounds(100, 100, 1294, 811);
		frmBbassistant.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBbassistant.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(0.95);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frmBbassistant.getContentPane().add(splitPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabbedPane);

		JPanel userInfoTab = new JPanel();
		tabbedPane.addTab("User info", null, userInfoTab, null);
		userInfoTab.setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(5, 8, 102, 16);
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		userInfoTab.add(lblUsername);

		usernameField = new JTextField();
		lblUsername.setLabelFor(usernameField);
		usernameField.setBounds(118, 8, 405, 29);
		usernameField.setHorizontalAlignment(SwingConstants.LEFT);
		userInfoTab.add(usernameField);
		usernameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setBounds(5, 42, 102, 16);
		userInfoTab.add(lblPassword);

		passwordField = new JPasswordField();
		lblPassword.setLabelFor(passwordField);
		passwordField.setBounds(118, 43, 405, 28);
		userInfoTab.add(passwordField);

		teamIDField = new JTextField();
		teamIDField.setBounds(118, 77, 405, 28);
		userInfoTab.add(teamIDField);

		JLabel lblTeamid = new JLabel("TeamID");
		lblTeamid.setLabelFor(teamIDField);
		lblTeamid.setHorizontalAlignment(SwingConstants.LEFT);
		lblTeamid.setBounds(5, 80, 102, 16);
		userInfoTab.add(lblTeamid);

		lblActionresult = new JLabel("");
		lblActionresult.setBounds(216, 168, 230, 25);
		userInfoTab.add(lblActionresult);




		// ################################################################
		// # 
		// # Scout tab
		// #
		// ################################################################


		// UI objects
		JPanel ntScoutTab = new JPanel();
		tabbedPane.addTab("NT Scout", null, ntScoutTab, null);
		ntScoutTab.setLayout(null);

		JLabel lblDomesticMessage = new JLabel("Localized message");
		lblDomesticMessage.setBounds(376, 7, 124, 16);
		ntScoutTab.add(lblDomesticMessage);

		JLabel lblEnglishMessage = new JLabel("English message");
		lblEnglishMessage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEnglishMessage.setBounds(1154, 7, 124, 16);
		ntScoutTab.add(lblEnglishMessage);

		JTextArea txtMessageDomestic = new JTextArea();
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
		ntScoutTab.add(txtMessageDomestic);		

		textFieldSubjectLocalized = new JTextField();
		textFieldSubjectLocalized.setBounds(376, 40, 407, 22);
		textFieldSubjectLocalized.setText(Messages.getCustomMap().get(Messages.SUBJECT_DOMESTIC_KEY));
		textFieldSubjectLocalized.setColumns(10);
		ntScoutTab.add(textFieldSubjectLocalized);

		textFieldSubjectEnglish = new JTextField();
		textFieldSubjectEnglish.setColumns(10);
		textFieldSubjectEnglish.setBounds(871, 39, 407, 22);
		textFieldSubjectEnglish.setText(Messages.getCustomMap().get(Messages.SUBJECT_ENGLISH_KEY));
		ntScoutTab.add(textFieldSubjectEnglish);

		JTextArea txtMessageEN = new JTextArea();
		lblEnglishMessage.setLabelFor(txtMessageEN);
		txtMessageEN.setWrapStyleWord(true);
		txtMessageEN.setToolTipText(tooltip);
		txtMessageEN.setText(Messages.getCustomMap().get(Messages.MESSAGE_ENGLISH_KEY));
		txtMessageEN.setTabSize(4);
		txtMessageEN.setLineWrap(true);
		txtMessageEN.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtMessageEN.setBounds(837, 75, 441, 191);
		ntScoutTab.add(txtMessageEN);

		JComboBox<String> comboBoxSelectMessage = new JComboBox<String>();
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
		ntScoutTab.add(comboBoxSelectMessage);

		JScrollPane scrollPanePlayers = new JScrollPane();
		scrollPanePlayers.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanePlayers.setBounds(12, 37, 352, 599);
		ntScoutTab.add(scrollPanePlayers);

		JTextArea txtPlayersList = new JTextArea();
		scrollPanePlayers.setViewportView(txtPlayersList);
		txtPlayersList.setMargin(new Insets(2, 5, 2, 2));

		JScrollPane scrollPaneOutput = new JScrollPane();
		scrollPaneOutput.setBounds(376, 317, 902, 351);
		ntScoutTab.add(scrollPaneOutput);

		JTextArea txtLogOutput = new JTextArea();
		scrollPaneOutput.setViewportView(txtLogOutput);
		txtLogOutput.setWrapStyleWord(true);
		txtLogOutput.setToolTipText("Log output");
		txtLogOutput.setTabSize(4);

		JButton btnSendMessages = new JButton("Send messages");
		btnSendMessages.setBounds(376, 279, 134, 25);
		ntScoutTab.add(btnSendMessages);
		JProgressBar sendMessagesProgresBar = new JProgressBar();
		sendMessagesProgresBar.setStringPainted(true);
		sendMessagesProgresBar.setBounds(646, 279, 632, 25);
		ntScoutTab.add(sendMessagesProgresBar);

		JCheckBox chckbxSkipTLPlayers = new JCheckBox("Skip TL players?");
		chckbxSkipTLPlayers.setBounds(512, 275, 126, 33);
		ntScoutTab.add(chckbxSkipTLPlayers);

		btnSendMessages.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnSendMessages.isEnabled()) {

					txtLogOutput.setForeground(Color.BLACK);
					List<String> players = SwingUIHelper.getListOfLinesFromJTextArea(txtPlayersList);

					// get messages from the UI form
					messagesMap = Messages.getBaseMap();
					messagesMap.put(
							Messages.SUBJECT_DOMESTIC_KEY, textFieldSubjectLocalized.getText());
					messagesMap.put(
							Messages.SUBJECT_ENGLISH_KEY, textFieldSubjectEnglish.getText());
					messagesMap.put(
							Messages.MESSAGE_DOMESTIC_KEY, txtMessageDomestic.getText()
							+"\n\nSent from my BuzzerBeater Assistant"
							//+ " [link=https://www.facebook.com/buzzerbeaterassistant]"
							);
					messagesMap.put(
							Messages.MESSAGE_ENGLISH_KEY, txtMessageEN.getText()
							+"\n\nSent from my BuzzerBeater Assistant"
							//+ " [link=https://www.facebook.com/buzzerbeaterassistant]"
							);

					sendMessagesWorker = new SendMessagesToOwners(
							usernameField.getText(), 
							new String(passwordField.getPassword()),
							teamIDField.getText(),
							players, 
							messagesMap, 
							txtLogOutput,
							chckbxSkipTLPlayers.isSelected(), //skipTLPlayers
							chckbxUseVisibleBrowser.isSelected(), //visible browser
							BrowserType.valueOf(comboBoxBrowser.getSelectedItem().toString()) // browser type
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


		JCheckBox chckbxExcludeBotAndRetired = new JCheckBox("Exclude BOT&retired");
		chckbxExcludeBotAndRetired.setSelected(true);
		chckbxExcludeBotAndRetired.setBounds(217, 643, 147, 25);
		ntScoutTab.add(chckbxExcludeBotAndRetired);

		JButton btnLoadFromFailedMessages = new JButton("Load from failed messages");
		btnLoadFromFailedMessages.setHorizontalAlignment(SwingConstants.LEFT);
		btnLoadFromFailedMessages.setBounds(12, 643, 199, 25);
		ntScoutTab.add(btnLoadFromFailedMessages);
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


		JButton btnLoadPlayersFrom = new JButton("Load players from file");
		btnLoadPlayersFrom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Text files", "txt");
				fc.setFileFilter(filter);


				// Show open dialog; this method does not return until the dialog is closed
				fc.showOpenDialog(frmBbassistant);
				File selFile = fc.getSelectedFile();

				SwingUIHelper.loadLinesFromFileToTextArea(selFile, txtPlayersList, txtLogOutput);
			}
		});
		btnLoadPlayersFrom.setBounds(12, 3, 175, 25);
		ntScoutTab.add(btnLoadPlayersFrom);

		JLabel lblOrEnterPlayers = new JLabel("or enter players manually");
		lblOrEnterPlayers.setLabelFor(btnLoadPlayersFrom);
		lblOrEnterPlayers.setBounds(195, 7, 169, 16);
		ntScoutTab.add(lblOrEnterPlayers);

		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(811, 46, 53, 16);
		ntScoutTab.add(lblSubject);

		JButton btnSave = new JButton("Save");
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
		userInfoTab.add(btnSave);

		JButton btnLoad = new JButton("Load");
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				loadUserInfo();
			}
		});
		btnLoad.setBounds(107, 178, 97, 25);
		userInfoTab.add(btnLoad);

		tabbedPane.setSelectedComponent(userInfoTab);
		
		JLabel useVisibleBrowserLbl = new JLabel("Visible browser");
		useVisibleBrowserLbl.setHorizontalAlignment(SwingConstants.LEFT);
		useVisibleBrowserLbl.setBounds(5, 150, 102, 16);
		userInfoTab.add(useVisibleBrowserLbl);
		
		chckbxUseVisibleBrowser = new JCheckBox("(if checked browser will be visible but app will run 3-4 times slower)");
		chckbxUseVisibleBrowser.setBounds(118, 148, 405, 18);
		userInfoTab.add(chckbxUseVisibleBrowser);
		
		lblBrowser = new JLabel("Browser");
		lblBrowser.setHorizontalAlignment(SwingConstants.LEFT);
		lblBrowser.setBounds(5, 118, 102, 16);
		userInfoTab.add(lblBrowser);
		
		comboBoxBrowser = new JComboBox<BrowserType>();
		comboBoxBrowser.setEditable(true);
		for(BrowserType bt : BrowserType.values()) {
			comboBoxBrowser.addItem(bt);
		}
		comboBoxBrowser.setBounds(118, 113, 142, 26);
		userInfoTab.add(comboBoxBrowser);

		JPanel tradingAgentTab = new JPanel();
		tradingAgentTab.setEnabled(false);
		tabbedPane.addTab("TradingAgent", null, tradingAgentTab, null);
		tradingAgentTab.setLayout(null);

		JLabel label = new JLabel("PlayerID");
		label.setFont(new Font("Courier New", Font.PLAIN, 20));
		label.setBounds(123, 92, 103, 31);
		tradingAgentTab.add(label);

		JLabel label_1 = new JLabel("Max Price");
		label_1.setFont(new Font("Courier New", Font.PLAIN, 20));
		label_1.setBounds(278, 92, 116, 31);
		tradingAgentTab.add(label_1);

		JLabel label_2 = new JLabel("Trading Agent Status");
		label_2.setFont(new Font("Courier New", Font.PLAIN, 20));
		label_2.setBounds(570, 92, 680, 31);
		tradingAgentTab.add(label_2);

		JLabel lblEndsIn = new JLabel("Ends in");
		lblEndsIn.setFont(new Font("Courier New", Font.PLAIN, 20));
		lblEndsIn.setBounds(406, 92, 152, 31);
		tradingAgentTab.add(lblEndsIn);

		txtPlayerID1 = new JTextField();
		txtPlayerID1.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtPlayerID1.setColumns(10);
		txtPlayerID1.setBounds(123, 136, 148, 48);
		txtPlayerID1.setEnabled(false);
		tradingAgentTab.add(txtPlayerID1);

		txtMaxPrice1 = new JTextField();
		txtMaxPrice1.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtMaxPrice1.setColumns(10);
		txtMaxPrice1.setBounds(278, 136, 116, 48);
		txtMaxPrice1.setEnabled(false);
		tradingAgentTab.add(txtMaxPrice1);

		JLabel lblAgentStatus1;
		lblAgentStatus1 = new JLabel("");
		lblAgentStatus1.setForeground(new Color(0, 128, 0));
		lblAgentStatus1.setFont(new Font("Courier New", Font.PLAIN, 16));
		lblAgentStatus1.setBounds(570, 137, 680, 45);
		tradingAgentTab.add(lblAgentStatus1);

		JButton btnRunStopWorker1 = new JButton("Run");
//		btnRunStopWorker1.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				if(btnRunStopWorker1.isEnabled()) {
//					if(btnRunStopWorker1.getText().equals("Run")) { // run worker
//						autobidWorkers[0] = new AutobidPlayerOnSaleWorker(
//								usernameField.getText(),
//								new String(passwordField.getPassword()),
//								teamIDField.getText(),
//								txtPlayerID1.getText(),
//								Integer.parseInt(txtMaxPrice1.getText()),
//								lblAuctionEndsIn1,
//								lblAgentStatus1,
//								chckbxUseVisibleBrowser.isSelected(),
//								BrowserType.valueOf(comboBoxBrowser.getSelectedItem().toString()) // browser type
//								);
//
//						autobidWorkers[0].addPropertyChangeListener(
//								new PropertyChangeListener() {
//									public  void propertyChange(PropertyChangeEvent evt) {
//										if ("state".equals(evt.getPropertyName())) {
//											if(autobidWorkers[0].getState().equals(SwingWorker.StateValue.DONE)) {
//												btnRunStopWorker1.setText("Run");
//												txtPlayerID1.setEnabled(true);
//												txtMaxPrice1.setEnabled(true);
//												btnRunStopWorker1.setEnabled(true);
//											}
//										}
//									}
//								});
//
//						autobidWorkers[0].execute();
//						btnRunStopWorker1.setText("Stop");
//						txtPlayerID1.setEnabled(false);
//						txtMaxPrice1.setEnabled(false);
//					} else { // stop worker
//						btnRunStopWorker1.setEnabled(false);
//						autobidWorkers[0].cancel(true);
//					}
//				}
//			}
//		});
		btnRunStopWorker1.setFont(new Font("Courier New", Font.PLAIN, 22));
		btnRunStopWorker1.setBounds(12, 135, 97, 48);
		btnRunStopWorker1.setEnabled(false);
		tradingAgentTab.add(btnRunStopWorker1);

		txtPlayerID2 = new JTextField();
		txtPlayerID2.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtPlayerID2.setColumns(10);
		txtPlayerID2.setBounds(123, 219, 148, 48);
		txtPlayerID2.setEnabled(false);
		tradingAgentTab.add(txtPlayerID2);

		txtMaxPrice2 = new JTextField();
		txtMaxPrice2.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtMaxPrice2.setColumns(10);
		txtMaxPrice2.setBounds(278, 219, 116, 48);
		txtMaxPrice2.setEnabled(false);
		tradingAgentTab.add(txtMaxPrice2);

		JLabel lblAgentStatus2 = new JLabel("");
		lblAgentStatus2.setFont(new Font("Courier New", Font.PLAIN, 16));
		lblAgentStatus2.setForeground(Color.BLUE);
		lblAgentStatus2.setBounds(570, 219, 680, 48);
		tradingAgentTab.add(lblAgentStatus2);

		JLabel lblAuctionEndsIn2 = new JLabel("");
		lblAuctionEndsIn2.setFont(new Font("Courier New", Font.PLAIN, 22));
		lblAuctionEndsIn2.setBounds(406, 219, 152, 48);
		tradingAgentTab.add(lblAuctionEndsIn2);

		JButton btnRunStopWorker2 = new JButton("Run");
//		btnRunStopWorker2.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				if(btnRunStopWorker2.isEnabled()) {
//					if(btnRunStopWorker2.getText().equals("Run")) { // run worker
//						autobidWorkers[1] = new AutobidPlayerOnSaleWorker(
//								usernameField.getText(),
//								new String(passwordField.getPassword()),
//								teamIDField.getText(),
//								txtPlayerID2.getText(),
//								Integer.parseInt(txtMaxPrice2.getText()),
//								lblAuctionEndsIn2,
//								lblAgentStatus2,
//								chckbxUseVisibleBrowser.isSelected(),
//								BrowserType.valueOf(comboBoxBrowser.getSelectedItem().toString()) // browser type
//								);
//
//						autobidWorkers[1].addPropertyChangeListener(
//								new PropertyChangeListener() {
//									public  void propertyChange(PropertyChangeEvent evt) {
//										if ("state".equals(evt.getPropertyName())) {
//											if(autobidWorkers[1].getState().equals(SwingWorker.StateValue.DONE)) {
//												btnRunStopWorker2.setText("Run");
//												txtPlayerID2.setEnabled(true);
//												txtMaxPrice2.setEnabled(true);
//												btnRunStopWorker2.setEnabled(true);
//											}
//										}
//									}
//								});
//
//						autobidWorkers[1].execute();
//						btnRunStopWorker2.setText("Stop");
//						txtPlayerID2.setEnabled(false);
//						txtMaxPrice2.setEnabled(false);
//					} else { // stop worker
//						btnRunStopWorker2.setEnabled(false);
//						autobidWorkers[1].cancel(true);
//					}
//				}
//			}
//		});
		btnRunStopWorker2.setFont(new Font("Courier New", Font.PLAIN, 22));
		btnRunStopWorker2.setBounds(12, 218, 97, 48);
		btnRunStopWorker2.setEnabled(false);
		tradingAgentTab.add(btnRunStopWorker2);

		lblAuctionEndsIn1 = new JLabel("");
		lblAuctionEndsIn1.setFont(new Font("Courier New", Font.PLAIN, 22));
		lblAuctionEndsIn1.setBounds(406, 137, 152, 45);
		tradingAgentTab.add(lblAuctionEndsIn1);

		JLabel lblStaffid = new JLabel("StaffID");
		lblStaffid.setFont(new Font("Courier New", Font.PLAIN, 20));
		lblStaffid.setBounds(123, 368, 103, 31);
		tradingAgentTab.add(lblStaffid);

		JLabel label_4 = new JLabel("Max Price");
		label_4.setFont(new Font("Courier New", Font.PLAIN, 20));
		label_4.setBounds(278, 368, 116, 31);
		tradingAgentTab.add(label_4);

		JLabel label_5 = new JLabel("Ends in");
		label_5.setFont(new Font("Courier New", Font.PLAIN, 20));
		label_5.setBounds(406, 368, 152, 31);
		tradingAgentTab.add(label_5);

		JLabel label_6 = new JLabel("Trading Agent Status");
		label_6.setFont(new Font("Courier New", Font.PLAIN, 20));
		label_6.setBounds(570, 368, 680, 31);
		tradingAgentTab.add(label_6);

		JLabel lblStaffAuctionEndsIn1 = new JLabel("");
		lblStaffAuctionEndsIn1.setFont(new Font("Courier New", Font.PLAIN, 22));
		lblStaffAuctionEndsIn1.setBounds(406, 412, 152, 45);
		tradingAgentTab.add(lblStaffAuctionEndsIn1);

		JLabel lblStaffAgentStatus1 = new JLabel("");
		lblStaffAgentStatus1.setForeground(Color.RED);
		lblStaffAgentStatus1.setFont(new Font("Courier New", Font.PLAIN, 16));
		lblStaffAgentStatus1.setBounds(570, 412, 680, 45);
		tradingAgentTab.add(lblStaffAgentStatus1);

		JButton btnRunStopStaffWorker1 = new JButton("Run");
//		btnRunStopStaffWorker1.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				if(btnRunStopStaffWorker1.isEnabled()) {
//					if(btnRunStopStaffWorker1.getText().equals("Run")) { // run worker
//						autobidStaffWorkers[0] = new AutobidStaffOnSaleWorker(
//								usernameField.getText(),
//								new String(passwordField.getPassword()),
//								teamIDField.getText(),
//								txtStaffID1.getText(),
//								Integer.parseInt(txtStaffMaxPrice1.getText()),
//								30,
//								true,
//								15,
//								lblStaffAuctionEndsIn1,
//								lblStaffAgentStatus1,
//								chckbxUseVisibleBrowser.isSelected(),
//								BrowserType.valueOf(comboBoxBrowser.getSelectedItem().toString()) // browser type
//								);
//
//						autobidStaffWorkers[0].addPropertyChangeListener(
//								new PropertyChangeListener() {
//									public  void propertyChange(PropertyChangeEvent evt) {
//										if ("state".equals(evt.getPropertyName())) {
//											if(autobidStaffWorkers[0].getState().equals(SwingWorker.StateValue.DONE)) {
//												btnRunStopWorker1.setText("Run");
//												txtStaffID1.setEnabled(true);
//												txtStaffMaxPrice1.setEnabled(true);
//												btnRunStopStaffWorker1.setEnabled(true);
//											}
//										}
//									}
//								});
//
//						autobidStaffWorkers[0].execute();
//						btnRunStopWorker1.setText("Stop");
//						txtStaffID1.setEnabled(false);
//						txtStaffMaxPrice1.setEnabled(false);
//					} else { // stop worker
//						btnRunStopWorker1.setEnabled(false);
//						autobidStaffWorkers[0].cancel(true);
//					}
//				}
//			}
//		});
		btnRunStopStaffWorker1.setFont(new Font("Courier New", Font.PLAIN, 22));
		btnRunStopStaffWorker1.setBounds(12, 411, 97, 48);
		btnRunStopStaffWorker1.setEnabled(false);
		tradingAgentTab.add(btnRunStopStaffWorker1);

		JButton btnRunStopStaffWorker2 = new JButton("Run");
		btnRunStopStaffWorker2.setEnabled(false);
		btnRunStopStaffWorker2.setFont(new Font("Courier New", Font.PLAIN, 22));
		btnRunStopStaffWorker2.setBounds(12, 494, 97, 48);
		tradingAgentTab.add(btnRunStopStaffWorker2);

		txtStaffID1 = new JTextField();
		txtStaffID1.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtStaffID1.setColumns(10);
		txtStaffID1.setBounds(123, 412, 148, 48);
		txtStaffID1.setEnabled(false);
		tradingAgentTab.add(txtStaffID1);

		txtStaffID2 = new JTextField();
		txtStaffID2.setEnabled(false);
		txtStaffID2.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtStaffID2.setColumns(10);
		txtStaffID2.setBounds(123, 495, 148, 48);
		tradingAgentTab.add(txtStaffID2);

		txtStaffMaxPrice2 = new JTextField();
		txtStaffMaxPrice2.setEnabled(false);
		txtStaffMaxPrice2.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtStaffMaxPrice2.setColumns(10);
		txtStaffMaxPrice2.setBounds(278, 495, 116, 48);
		tradingAgentTab.add(txtStaffMaxPrice2);

		txtStaffMaxPrice1 = new JTextField();
		txtStaffMaxPrice1.setFont(new Font("Courier New", Font.PLAIN, 22));
		txtStaffMaxPrice1.setColumns(10);
		txtStaffMaxPrice1.setBounds(278, 412, 116, 48);
		txtStaffMaxPrice1.setEnabled(false);
		tradingAgentTab.add(txtStaffMaxPrice1);

		JLabel lblPlayersSectionHeader = new JLabel("Players Trading Agents");
		lblPlayersSectionHeader.setFont(new Font("Courier New", Font.BOLD, 32));
		lblPlayersSectionHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayersSectionHeader.setBounds(12, 13, 1238, 53);
		tradingAgentTab.add(lblPlayersSectionHeader);

		JLabel lblStaffSectionHeader = new JLabel("Staff Trading Agents");
		lblStaffSectionHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblStaffSectionHeader.setFont(new Font("Courier New", Font.BOLD, 32));
		lblStaffSectionHeader.setBounds(12, 307, 1238, 53);
		tradingAgentTab.add(lblStaffSectionHeader);

		JLabel lblStaffAuctionEndsIn2 = new JLabel("");
		lblStaffAuctionEndsIn2.setFont(new Font("Courier New", Font.PLAIN, 22));
		lblStaffAuctionEndsIn2.setBounds(406, 494, 152, 48);
		tradingAgentTab.add(lblStaffAuctionEndsIn2);

		JLabel lblStaffAgentStatus2 = new JLabel("");
		lblStaffAgentStatus2.setForeground(Color.BLUE);
		lblStaffAgentStatus2.setFont(new Font("Courier New", Font.PLAIN, 16));
		lblStaffAgentStatus2.setBounds(570, 494, 680, 48);
		tradingAgentTab.add(lblStaffAgentStatus2);

		JPanel draftAndPlayersSkills = new JPanel();
		tabbedPane.addTab("Draft&PlayerSkills", null, draftAndPlayersSkills, null);
		draftAndPlayersSkills.setLayout(null);

		JLabel lblOutputFile = new JLabel("Output File");
		lblOutputFile.setBounds(405, 58, 76, 16);
		draftAndPlayersSkills.add(lblOutputFile);

		textFieldOutputFile = new JTextField();
		textFieldOutputFile.setText("playersSkills.csv");
		textFieldOutputFile.setBounds(481, 55, 116, 22);
		draftAndPlayersSkills.add(textFieldOutputFile);
		textFieldOutputFile.setColumns(10);

		JLabel lblPlayersToScout = new JLabel("Players to scout");
		lblPlayersToScout.setBounds(12, 17, 110, 16);
		draftAndPlayersSkills.add(lblPlayersToScout);

		JScrollPane scrollPanePlayersToScout = new JScrollPane();
		scrollPanePlayersToScout.setBounds(12, 90, 585, 364);
		draftAndPlayersSkills.add(scrollPanePlayersToScout);

		JTextArea textAreaPlayersToScout = new JTextArea();
		textAreaPlayersToScout.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPanePlayersToScout.setViewportView(textAreaPlayersToScout);

		JScrollPane scrollPaneLogArea = new JScrollPane();
		scrollPaneLogArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneLogArea.setBounds(12, 520, 1257, 170);
		draftAndPlayersSkills.add(scrollPaneLogArea);

		JCheckBox chckbxSkipBotsretired = new JCheckBox("Skip bots&retired");
		chckbxSkipBotsretired.setSelected(true);
		chckbxSkipBotsretired.setBounds(12, 56, 134, 25);
		draftAndPlayersSkills.add(chckbxSkipBotsretired);

		JCheckBox chckbxInviteToNt = new JCheckBox("Invite to NT");
		chckbxInviteToNt.setSelected(true);
		chckbxInviteToNt.setBounds(161, 56, 113, 25);
		draftAndPlayersSkills.add(chckbxInviteToNt);

		JTextArea textAreaLog = new JTextArea();
		textAreaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPaneLogArea.setViewportView(textAreaLog);

		JProgressBar draftAndPlayersProgressBar = new JProgressBar();
		draftAndPlayersProgressBar.setFont(new Font("Courier New", Font.BOLD, 20));
		draftAndPlayersProgressBar.setStringPainted(true);
		draftAndPlayersProgressBar.setBounds(12, 467, 1257, 40);
		draftAndPlayersSkills.add(draftAndPlayersProgressBar);

		JButton btnStartScoutingPlayers = new JButton("Start scouting players");
		btnStartScoutingPlayers.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				//xxx
				List<String> playerUrls = SwingUIHelper.getListOfLinesFromJTextArea(textAreaPlayersToScout);

				GetSkillsOfPlayersByInvitingThemToNTWorker scoutPlayersWorker 
				= new GetSkillsOfPlayersByInvitingThemToNTWorker(
						usernameField.getText(), 
						new String(passwordField.getPassword()), 
						teamIDField.getText(), 
						playerUrls, 
						Integer.parseInt(textFieldMiniumPot.getText()),
						chckbxSkipBotsretired.isSelected(),
						chckbxInviteToNt.isSelected(),
						new File(textFieldOutputFile.getText()), 
						textAreaLog, 
						chckbxUseVisibleBrowser.isSelected(),
						BrowserType.valueOf(comboBoxBrowser.getSelectedItem().toString()) // browser type
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
		draftAndPlayersSkills.add(btnStartScoutingPlayers);

		JLabel lblMinimumPotential = new JLabel("Min potential");
		lblMinimumPotential.setBounds(273, 58, 84, 16);
		draftAndPlayersSkills.add(lblMinimumPotential);

		textFieldMiniumPot = new JTextField();
		textFieldMiniumPot.setText("5");
		textFieldMiniumPot.setBounds(357, 55, 26, 22);
		draftAndPlayersSkills.add(textFieldMiniumPot);
		textFieldMiniumPot.setColumns(10);

		JButton btnLoadFromFile = new JButton("Load from file ...");
		btnLoadFromFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Text files", "txt");
				fc.setFileFilter(filter);


				// Show open dialog; this method does not return until the dialog is closed
				fc.showOpenDialog(frmBbassistant);
				File selFile = fc.getSelectedFile();

				SwingUIHelper.loadLinesFromFileToTextArea(selFile, textAreaPlayersToScout ,textAreaLog);
			}
		});
		btnLoadFromFile.setBounds(108, 13, 134, 20);
		draftAndPlayersSkills.add(btnLoadFromFile);

		JLabel outputFileDraftSummaryLbl = new JLabel("Output File");
		outputFileDraftSummaryLbl.setBounds(639, 29, 76, 16);
		draftAndPlayersSkills.add(outputFileDraftSummaryLbl);

		textFieldOutputFileDraftSummary = new JTextField();
		textFieldOutputFileDraftSummary.setText("playersURLs.txt");
		textFieldOutputFileDraftSummary.setColumns(10);
		textFieldOutputFileDraftSummary.setBounds(715, 26, 116, 22);
		textFieldOutputFileDraftSummary.setEnabled(false);
		draftAndPlayersSkills.add(textFieldOutputFileDraftSummary);

		JLabel seasonLbl = new JLabel("Season");
		seasonLbl.setBounds(843, 29, 116, 16);
		draftAndPlayersSkills.add(seasonLbl);

		textFieldseason = new JTextField();
		textFieldseason.setText("0");
		textFieldseason.setColumns(10);
		textFieldseason.setBounds(912, 26, 116, 22);
		textFieldseason.setEnabled(false);
		draftAndPlayersSkills.add(textFieldseason);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(639, 90, 134, 364);
		draftAndPlayersSkills.add(scrollPane);

		JTextArea textAreaLeaguIDsNotexpanded = new JTextArea();
		textAreaLeaguIDsNotexpanded.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textAreaLeaguIDsNotexpanded.setText("1277\r\n1278-1281\r\n1282-1297\r\n7369-7432");
		scrollPane.setViewportView(textAreaLeaguIDsNotexpanded);
		textAreaLeaguIDsNotexpanded.setEnabled(false);

		JLabel lblLeagueIDs = new JLabel("League IDs");
		lblLeagueIDs.setBounds(639, 58, 110, 16);
		draftAndPlayersSkills.add(lblLeagueIDs);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(776, 90, 491, 364);
		draftAndPlayersSkills.add(scrollPane_1);

		JTextArea textAreaLeaguIDsExpanded = new JTextArea();
		textAreaLeaguIDsExpanded.setLineWrap(true);
		textAreaLeaguIDsExpanded.setEditable(false);
		textAreaLeaguIDsExpanded.setEnabled(false);
		textAreaLeaguIDsExpanded.setWrapStyleWord(true);
		scrollPane_1.setViewportView(textAreaLeaguIDsExpanded);
		textAreaLeaguIDsExpanded.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JLabel lblLeagueIdsExpanded = new JLabel("League IDs Expanded");
		lblLeagueIdsExpanded.setBounds(1133, 58, 134, 16);
		draftAndPlayersSkills.add(lblLeagueIdsExpanded);

		JButton btnGetPlayerURLFromDraft = new JButton("Get player URLs");
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
		draftAndPlayersSkills.add(btnGetPlayerURLFromDraft);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(619, 14, 2, 440);
		draftAndPlayersSkills.add(separator);

		JPanel footerPanel = new JPanel();
		splitPane.setRightComponent(footerPanel);
		footerPanel.setLayout(null);

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
		footerPanel.add(lblPowerdBySpaciulis);

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
		footerPanel.add(lblEmail);

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
		footerPanel.add(lblYoutube);
	}


	void setFrameVisible(boolean b) {
		this.frmBbassistant.setVisible(b);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldNimbus", Boolean.FALSE);

		// load app
		BuzzerBeaterAssistant window = new BuzzerBeaterAssistant();
		window.setVisibleBrowser(false);
		window.loadUserInfo();
		window.setFrameVisible(true);
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

			usernameField.setText(userInfo.get(0));
			passwordField.setText(userInfo.get(1));
			teamIDField.setText(userInfo.get(2));
			comboBoxBrowser.setSelectedItem(BrowserType.valueOf(userInfo.get(3)));
			
			if(userInfo.size() == 5 && userInfo.get(4).toLowerCase().trim().equals("true"))
				chckbxUseVisibleBrowser.setSelected(true);
			else
				chckbxUseVisibleBrowser.setSelected(false);

			lblActionresult.setForeground(Color.BLUE);
			lblActionresult.setText("Info loaded!" + System.getProperty("line.separator"));

			return;
		} catch (Exception exc) {
			lblActionresult.setForeground(Color.RED);
			lblActionresult.setText("ERROR: Info not loaded!" + System.getProperty("line.separator"));
			return;
		}
	}
}
