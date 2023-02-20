package com.buzzerbeater.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.buzzerbeater.Version;
import com.buzzerbeater.ui.tabs.*;
import com.buzzerbeater.utils.BrowserType;
import com.buzzerbeater.utils.Files;
import com.buzzerbeater.utils.Messages;
import com.buzzerbeater.utils.SwingUIHelper;
import com.buzzerbeater.workers.GetPlayersFromDraftWorker;
import com.buzzerbeater.workers.GetSkillsOfPlayersByInvitingThemToNTWorker;
import com.buzzerbeater.workers.SendMessagesToOwners;

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

public final class BuzzerBeaterAssistant {

	// #### CONFIGURATION ######################################
	// TODO: refactor to seme robust configuration (map or set)
	public static String USERNAME = "";
	public static String PASSWORD = "";
	public static String TEAMID = "";
	public static BrowserType BROWSERTYPE = BrowserType.CHROME;
	public static boolean USEVISIBLEBROWSER = true;
	// #########################################################

	private JFrame frmBbassistant;

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
		// frmBbassistant.setIconImage(Toolkit.getDefaultToolkit().getImage(BuzzerBeaterAssistant.class.getResource("./images/icon.jpg")));
		ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "images/icon.jpg");
		frmBbassistant.setIconImage(icon.getImage());
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

		// ################################################################
		// #
		// # User info tab
		// #
		// ################################################################
		UserInfoPanel userInfoPanel = new UserInfoPanel();
		tabbedPane.addTab(userInfoPanel.getName(), userInfoPanel);
		tabbedPane.setSelectedComponent(userInfoPanel);

		// ################################################################
		// # 
		// # Scout tab
		// #
		// ################################################################
		SendMessagesPanel sendMessagesPanel = new SendMessagesPanel();
		tabbedPane.addTab(sendMessagesPanel.getName(), sendMessagesPanel);

		// ################################################################
		// #
		// # NT Coach/Scout tab
		// #
		// ################################################################
		PlayersSkillsPanel playersSkillsPanel = new PlayersSkillsPanel();
		tabbedPane.addTab(playersSkillsPanel.getName(), playersSkillsPanel);


		// ################################################################
		// #
		// # Footer tab
		// #
		// ################################################################
		FooterPanel footerPanel = new FooterPanel();
		splitPane.setRightComponent(footerPanel);

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
		window.setFrameVisible(true);
	}
}
