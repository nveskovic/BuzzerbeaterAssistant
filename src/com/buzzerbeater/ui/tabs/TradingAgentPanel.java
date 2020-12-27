package com.buzzerbeater.ui.tabs;

import com.buzzerbeater.ui.BuzzerBeaterAssistant;
import com.buzzerbeater.utils.BrowserType;
import com.buzzerbeater.workers.AutobidPlayerOnSaleWorker;
import com.buzzerbeater.workers.AutobidStaffOnSaleWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class TradingAgentPanel extends JPanel {
    public static java.util.List allowedIDs = Arrays.asList(
            /*spaciulis*/ "100438",
            /*scorpyon*/ "210789",
            /*martinenko*/ "100152"
    );
    private JButton btnRunStopStaffWorker1;
    private JTextField txtStaffID1;
    private JTextField txtStaffID2;
    private JTextField txtStaffMaxPrice2;
    private JTextField txtStaffMaxPrice1;
    private JButton btnRunStopWorker1;
    private JTextField txtMaxPrice2;
    private JLabel lblAgentStatus2;
    private JLabel lblAuctionEndsIn2;
    private JButton btnRunStopWorker2;
    private JLabel lblStaffAuctionEndsIn1;
    private JLabel lblStaffAgentStatus1;
    private JTextField txtPlayerID1;
    private JTextField txtMaxPrice1;
    private JLabel lblAgentStatus1;
    private JTextField txtPlayerID2;
    private JLabel lblAuctionEndsIn1;

    AutobidPlayerOnSaleWorker[] autobidWorkers = new AutobidPlayerOnSaleWorker[2];
    AutobidStaffOnSaleWorker[] autobidStaffWorkers = new AutobidStaffOnSaleWorker[2];

    public TradingAgentPanel() {
        setLayout(null);
        setName("TradingAgent");

        JLabel label = new JLabel("PlayerID");
        label.setFont(new Font("Courier New", Font.PLAIN, 20));
        label.setBounds(123, 92, 103, 31);
        add(label);

        JLabel label_1 = new JLabel("Max Price");
        label_1.setFont(new Font("Courier New", Font.PLAIN, 20));
        label_1.setBounds(278, 92, 116, 31);
        add(label_1);

        JLabel label_2 = new JLabel("Trading Agent Status");
        label_2.setFont(new Font("Courier New", Font.PLAIN, 20));
        label_2.setBounds(570, 92, 680, 31);
        add(label_2);

        JLabel lblEndsIn = new JLabel("Ends in");
        lblEndsIn.setFont(new Font("Courier New", Font.PLAIN, 20));
        lblEndsIn.setBounds(406, 92, 152, 31);
        add(lblEndsIn);

        txtPlayerID1 = new JTextField();
        txtPlayerID1.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtPlayerID1.setColumns(10);
        txtPlayerID1.setBounds(123, 136, 148, 48);
        txtPlayerID1.setEnabled(true);
        add(txtPlayerID1);

        txtMaxPrice1 = new JTextField();
        txtMaxPrice1.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtMaxPrice1.setColumns(10);
        txtMaxPrice1.setBounds(278, 136, 116, 48);
        txtMaxPrice1.setEnabled(true);
        add(txtMaxPrice1);

        lblAgentStatus1 = new JLabel("");
        lblAgentStatus1.setForeground(new Color(0, 128, 0));
        lblAgentStatus1.setFont(new Font("Courier New", Font.PLAIN, 16));
        lblAgentStatus1.setBounds(570, 137, 680, 45);
        add(lblAgentStatus1);

        btnRunStopWorker1 = new JButton("Run");
        btnRunStopWorker1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                playerAutoBidAdaptorMethod(
                        btnRunStopWorker1,
                        autobidWorkers[0],
                        txtPlayerID1,
                        txtMaxPrice1);
            }
        });
        btnRunStopWorker1.setFont(new Font("Courier New", Font.PLAIN, 22));
        btnRunStopWorker1.setBounds(12, 135, 97, 48);
        btnRunStopWorker1.setEnabled(true);
        add(btnRunStopWorker1);

        txtPlayerID2 = new JTextField();
        txtPlayerID2.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtPlayerID2.setColumns(10);
        txtPlayerID2.setBounds(123, 219, 148, 48);
        txtPlayerID2.setEnabled(false);
        add(txtPlayerID2);

        txtMaxPrice2 = new JTextField();
        txtMaxPrice2.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtMaxPrice2.setColumns(10);
        txtMaxPrice2.setBounds(278, 219, 116, 48);
        txtMaxPrice2.setEnabled(false);
        add(txtMaxPrice2);

        lblAgentStatus2 = new JLabel("");
        lblAgentStatus2.setFont(new Font("Courier New", Font.PLAIN, 16));
        lblAgentStatus2.setForeground(Color.BLUE);
        lblAgentStatus2.setBounds(570, 219, 680, 48);
        add(lblAgentStatus2);

        lblAuctionEndsIn2 = new JLabel("");
        lblAuctionEndsIn2.setFont(new Font("Courier New", Font.PLAIN, 22));
        lblAuctionEndsIn2.setBounds(406, 219, 152, 48);
        add(lblAuctionEndsIn2);

        btnRunStopWorker2 = new JButton("Run");
//		btnRunStopWorker2.addMouseListener(new MouseAdapter() {
//			@Override
//            public void mouseClicked(MouseEvent arg0) {
//                playerAutoBidAdaptorMethod(
//                        btnRunStopWorker2,
//                        autobidWorkers[1],
//                        txtPlayerID2,
//                        txtMaxPrice2);
//            }
//		});
        btnRunStopWorker2.setFont(new Font("Courier New", Font.PLAIN, 22));
        btnRunStopWorker2.setBounds(12, 218, 97, 48);
        btnRunStopWorker2.setEnabled(false);
        add(btnRunStopWorker2);

        lblAuctionEndsIn1 = new JLabel("");
        lblAuctionEndsIn1.setFont(new Font("Courier New", Font.PLAIN, 22));
        lblAuctionEndsIn1.setBounds(406, 137, 152, 45);
        add(lblAuctionEndsIn1);

        // Staff section

        JLabel lblStaffid = new JLabel("StaffID");
        lblStaffid.setFont(new Font("Courier New", Font.PLAIN, 20));
        lblStaffid.setBounds(123, 368, 103, 31);
        add(lblStaffid);

        JLabel label_4 = new JLabel("Max Price");
        label_4.setFont(new Font("Courier New", Font.PLAIN, 20));
        label_4.setBounds(278, 368, 116, 31);
        add(label_4);

        JLabel label_5 = new JLabel("Ends in");
        label_5.setFont(new Font("Courier New", Font.PLAIN, 20));
        label_5.setBounds(406, 368, 152, 31);
        add(label_5);

        JLabel label_6 = new JLabel("Trading Agent Status");
        label_6.setFont(new Font("Courier New", Font.PLAIN, 20));
        label_6.setBounds(570, 368, 680, 31);
        add(label_6);

        lblStaffAuctionEndsIn1 = new JLabel("");
        lblStaffAuctionEndsIn1.setFont(new Font("Courier New", Font.PLAIN, 22));
        lblStaffAuctionEndsIn1.setBounds(406, 412, 152, 45);
        add(lblStaffAuctionEndsIn1);

        lblStaffAgentStatus1 = new JLabel("");
        lblStaffAgentStatus1.setForeground(Color.RED);
        lblStaffAgentStatus1.setFont(new Font("Courier New", Font.PLAIN, 16));
        lblStaffAgentStatus1.setBounds(570, 412, 680, 45);
        add(lblStaffAgentStatus1);

        btnRunStopStaffWorker1 = new JButton("Run");
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
        add(btnRunStopStaffWorker1);

        JButton btnRunStopStaffWorker2 = new JButton("Run");
        btnRunStopStaffWorker2.setEnabled(false);
        btnRunStopStaffWorker2.setFont(new Font("Courier New", Font.PLAIN, 22));
        btnRunStopStaffWorker2.setBounds(12, 494, 97, 48);
        add(btnRunStopStaffWorker2);

        txtStaffID1 = new JTextField();
        txtStaffID1.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtStaffID1.setColumns(10);
        txtStaffID1.setBounds(123, 412, 148, 48);
        txtStaffID1.setEnabled(false);
        add(txtStaffID1);

        txtStaffID2 = new JTextField();
        txtStaffID2.setEnabled(false);
        txtStaffID2.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtStaffID2.setColumns(10);
        txtStaffID2.setBounds(123, 495, 148, 48);
        add(txtStaffID2);

        txtStaffMaxPrice2 = new JTextField();
        txtStaffMaxPrice2.setEnabled(false);
        txtStaffMaxPrice2.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtStaffMaxPrice2.setColumns(10);
        txtStaffMaxPrice2.setBounds(278, 495, 116, 48);
        add(txtStaffMaxPrice2);

        txtStaffMaxPrice1 = new JTextField();
        txtStaffMaxPrice1.setFont(new Font("Courier New", Font.PLAIN, 22));
        txtStaffMaxPrice1.setColumns(10);
        txtStaffMaxPrice1.setBounds(278, 412, 116, 48);
        txtStaffMaxPrice1.setEnabled(false);
        add(txtStaffMaxPrice1);

        JLabel lblPlayersSectionHeader = new JLabel("Players Trading Agents");
        lblPlayersSectionHeader.setFont(new Font("Courier New", Font.BOLD, 32));
        lblPlayersSectionHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblPlayersSectionHeader.setBounds(12, 13, 1238, 53);
        add(lblPlayersSectionHeader);

        JLabel lblStaffSectionHeader = new JLabel("Staff Trading Agents");
        lblStaffSectionHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblStaffSectionHeader.setFont(new Font("Courier New", Font.BOLD, 32));
        lblStaffSectionHeader.setBounds(12, 307, 1238, 53);
        add(lblStaffSectionHeader);

        JLabel lblStaffAuctionEndsIn2 = new JLabel("");
        lblStaffAuctionEndsIn2.setFont(new Font("Courier New", Font.PLAIN, 22));
        lblStaffAuctionEndsIn2.setBounds(406, 494, 152, 48);
        add(lblStaffAuctionEndsIn2);

        JLabel lblStaffAgentStatus2 = new JLabel("");
        lblStaffAgentStatus2.setForeground(Color.BLUE);
        lblStaffAgentStatus2.setFont(new Font("Courier New", Font.PLAIN, 16));
        lblStaffAgentStatus2.setBounds(570, 494, 680, 48);
        add(lblStaffAgentStatus2);
    }

    // mouse adaptors
    private void playerAutoBidAdaptorMethod(
            JButton button,
            AutobidPlayerOnSaleWorker worker,
            JTextField playerIDTextField,
            JTextField maxPriceTextField
    ) {
        if(button.isEnabled()) {
            if(button.getText().equals("Run")) { // run worker
                worker = new AutobidPlayerOnSaleWorker(
                        BuzzerBeaterAssistant.USERNAME,
                        BuzzerBeaterAssistant.PASSWORD,
                        BuzzerBeaterAssistant.TEAMID,
                        playerIDTextField.getText(),
                        Integer.parseInt(maxPriceTextField.getText()),
                        lblAuctionEndsIn1,
                        lblAgentStatus1,
                        BuzzerBeaterAssistant.USEVISIBLEBROWSER,
                        BuzzerBeaterAssistant.BROWSERTYPE // browser type
                );

                worker.addPropertyChangeListener(
                        new PropertyChangeListener() {
                            public  void propertyChange(PropertyChangeEvent evt) {
                                if ("state".equals(evt.getPropertyName())) {
                                    if(evt.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                                        button.setText("Run");
                                        playerIDTextField.setEnabled(true);
                                        maxPriceTextField.setEnabled(true);
                                        button.setEnabled(true);
                                    }
                                }
                            }
                        });

                worker.execute();
                button.setText("Stop");
                playerIDTextField.setEnabled(false);
                maxPriceTextField.setEnabled(false);
            } else { // stop worker
                button.setEnabled(false);
                worker.cancel(true);
            }
        }
    }
}
