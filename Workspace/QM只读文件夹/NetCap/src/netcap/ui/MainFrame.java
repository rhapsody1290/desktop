/*
 * MainFrame.java
 *
 * Created on __DATE__, __TIME__
 */

package netcap.ui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import netcap.control.NetCapControl;
import netcap.model.NetCapModel;

/**
 * 
 * @author __USER__
 */
public class MainFrame extends javax.swing.JFrame  {

	private NetCapControl myCtrl = new NetCapControl();

	/** Creates new form MainFrame */
	public MainFrame() {
		initComponents();

		// init model and controller
		// model

		// controller

		this.myCtrl.setOpenFileMenuItem(openFileMenuItem);
		this.myCtrl.setSaveFileMenuItem(saveFileMenuItem);

		this.myCtrl.setFilterTextField(this.filterTextField);
		this.myCtrl.setIsPromiscCheckBox(this.isPromiscCheckBox);
		this.myCtrl.setNeededPacketNumTextField(neededPacketNumTextField);
		this.myCtrl.setPackageInfoTree(this.packageInfoTree);
		this.myCtrl.setPackageList(this.packageList);

		this.myCtrl.setSelectDeviceComboBox(this.selectDeviceComboBox);
		this.myCtrl.setStartButton(this.startButton);

		this.myCtrl.setPackageRateLabel(this.packageRateLabel);
		this.myCtrl.setByteRateLabel(this.byteRateLabel);
		this.myCtrl.setArpPacketNumLabel(this.arpPacketNumLabel);
		this.myCtrl.setElapsedTimeLabel(elapsedTimeLabel);
		this.myCtrl.setIcmpPacketNumLabel(icmpPacketNumLabel);
		this.myCtrl.setIpv4PacketNumLabel(ipv4PacketNumLabel);
		this.myCtrl.setIpv6PacketNumLabel(ipv6PacketNumLabel);
		this.myCtrl.setTotalPacketNumLabel(totalPacketNumLabel);
		this.myCtrl.setTcpPacketNumLabel(tcpPacketNumLabel);
		this.myCtrl.setUdpPacketNumLabel(udpPacketNumLabel);

		this.myCtrl.setRateCanvas(rateCanvas);

		//
		this.myCtrl.initComponentsValue();
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jFileChooser1 = new javax.swing.JFileChooser();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		selectDeviceComboBox = new javax.swing.JComboBox();
		filterTextField = new javax.swing.JTextField();
		isPromiscCheckBox = new javax.swing.JCheckBox();
		jLabel2 = new javax.swing.JLabel();
		startButton = new javax.swing.JButton();
		jLabel17 = new javax.swing.JLabel();
		neededPacketNumTextField = new javax.swing.JTextField();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jSplitPane1 = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		packageList = new javax.swing.JList();
		jScrollPane2 = new javax.swing.JScrollPane();
		packageInfoTree = new javax.swing.JTree();
		jPanel2 = new javax.swing.JPanel();
		scrollPane1 = new java.awt.ScrollPane();
		rateCanvas = new java.awt.Canvas();
		jPanel3 = new javax.swing.JPanel();
		jLabel3 = new javax.swing.JLabel();
		byteRateLabel = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		packageRateLabel = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		totalPacketNumLabel = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		elapsedTimeLabel = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jLabel14 = new javax.swing.JLabel();
		jLabel15 = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		ipv4PacketNumLabel = new javax.swing.JLabel();
		ipv6PacketNumLabel = new javax.swing.JLabel();
		arpPacketNumLabel = new javax.swing.JLabel();
		icmpPacketNumLabel = new javax.swing.JLabel();
		jLabel21 = new javax.swing.JLabel();
		jLabel22 = new javax.swing.JLabel();
		jLabel23 = new javax.swing.JLabel();
		jLabel24 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		tcpPacketNumLabel = new javax.swing.JLabel();
		udpPacketNumLabel = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		jLabel20 = new javax.swing.JLabel();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		openFileMenuItem = new javax.swing.JMenuItem();
		saveFileMenuItem = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenu3 = new javax.swing.JMenu();
		jMenuItem3 = new javax.swing.JMenuItem();

		setDefaultCloseOperation(3);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}
		});

		jLabel1.setText("\u9009\u62e9\u7f51\u5361");

		selectDeviceComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		selectDeviceComboBox
				.setToolTipText("\u672c\u673a\u7684\u7f51\u5361\u8bbe\u5907");

		filterTextField.setCursor(new java.awt.Cursor(
				java.awt.Cursor.TEXT_CURSOR));
		filterTextField
				.setToolTipText("\u8868\u8fbe\u5f0f\u8bed\u6cd5\u4e0eTcpDump\u4e00\u81f4");

		isPromiscCheckBox.setLabel("\u6df7\u6742\u6a21\u5f0f");

		jLabel2.setText("Filter");

		startButton.setText("\u5f00\u59cb\u6293\u5305");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startButtonActionPerformed(evt);
			}
		});

		jLabel17.setText("\u6293\u5305\u6570");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jLabel1)
														.addComponent(jLabel2))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				filterTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				232,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(
																				10,
																				10,
																				10)
																		.addComponent(
																				jLabel17)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				neededPacketNumTextField,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				61,
																				Short.MAX_VALUE)
																		.addGap(
																				6,
																				6,
																				6)
																		.addComponent(
																				isPromiscCheckBox)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				startButton))
														.addComponent(
																selectDeviceComboBox,
																0, 509,
																Short.MAX_VALUE))
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel1)
														.addComponent(
																selectDeviceComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel2)
														.addComponent(
																filterTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																neededPacketNumTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																isPromiscCheckBox)
														.addComponent(
																startButton)
														.addComponent(jLabel17))
										.addContainerGap(15, Short.MAX_VALUE)));

		jTabbedPane1.setToolTipText("");

		jSplitPane1.setDividerLocation(80);

		jScrollPane1.setViewportView(packageList);

		packageList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		packageList.setAutoscrolls(false);
		packageList.setPreferredSize(new java.awt.Dimension(50, 85));
		packageList.setValueIsAdjusting(true);
		packageList
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						packageListValueChanged(evt);
					}
				});
		jScrollPane1.setViewportView(packageList);

		jSplitPane1.setLeftComponent(jScrollPane1);

		jScrollPane2.setViewportView(packageInfoTree);

		packageInfoTree.setModel(null);
		jScrollPane2.setViewportView(packageInfoTree);

		jSplitPane1.setRightComponent(jScrollPane2);

		jTabbedPane1.addTab("\u5305", jSplitPane1);

		scrollPane1.setVisible(false);
		scrollPane1.add(rateCanvas);

		jPanel3.setBorder(javax.swing.BorderFactory
				.createTitledBorder("\u7edf\u8ba1\u6570\u636e"));

		jLabel3.setText("\u5b57\u8282\u6d41\u91cf\uff1a");

		byteRateLabel.setText("0.0");

		jLabel5.setText("Byte/s");

		jLabel6.setText("\u6293\u5305\u7387\uff1a");

		packageRateLabel.setText("0");

		jLabel8.setText("\u4e2a/\u79d2");

		jLabel4.setText("\u603b\u5305\u6570\uff1a");

		totalPacketNumLabel.setText("0");

		jLabel9.setText("\u4e2a");

		jLabel10.setText("\u8fd0\u884c\u65f6\u95f4\uff1a");

		elapsedTimeLabel.setText("0");

		jLabel12.setText("\u79d2");

		jLabel13.setText("IPv4");

		jLabel14.setText("IPv6");

		jLabel15.setText("ARP");

		jLabel16.setText("ICMP");

		ipv4PacketNumLabel.setText("0");

		ipv6PacketNumLabel.setText("0");

		arpPacketNumLabel.setText("0");

		icmpPacketNumLabel.setText("0");

		jLabel21.setText("\u4e2a");

		jLabel22.setText("\u4e2a");

		jLabel23.setText("\u4e2a");

		jLabel24.setText("\u4e2a");

		jLabel7.setText("TCP");

		jLabel11.setText("UDP");

		tcpPacketNumLabel.setText("0");

		udpPacketNumLabel.setText("0");

		jLabel19.setText("\u4e2a");

		jLabel20.setText("\u4e2a");

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(
				jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout
				.setHorizontalGroup(jPanel3Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel3Layout
										.createSequentialGroup()
										.addGap(36, 36, 36)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																jLabel10,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jLabel3,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jLabel6,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(jLabel4))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																totalPacketNumLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																18,
																Short.MAX_VALUE)
														.addComponent(
																packageRateLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																18,
																Short.MAX_VALUE)
														.addComponent(
																byteRateLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																18,
																Short.MAX_VALUE)
														.addComponent(
																elapsedTimeLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																18,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jLabel5)
														.addComponent(jLabel8)
														.addComponent(jLabel9)
														.addComponent(jLabel12))
										.addGap(30, 30, 30)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jLabel16)
														.addComponent(jLabel15)
														.addComponent(jLabel14)
														.addComponent(jLabel13))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																icmpPacketNumLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																arpPacketNumLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																ipv6PacketNumLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																ipv4PacketNumLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																34,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(14, 14, 14)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jLabel24)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jPanel3Layout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(
																				jLabel21)
																		.addComponent(
																				jLabel22)
																		.addComponent(
																				jLabel23,
																				javax.swing.GroupLayout.Alignment.TRAILING)))
										.addGap(18, 18, 18)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jLabel7)
														.addComponent(jLabel11))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				tcpPacketNumLabel)
																		.addGap(
																				21,
																				21,
																				21)
																		.addComponent(
																				jLabel19))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				udpPacketNumLabel)
																		.addGap(
																				21,
																				21,
																				21)
																		.addComponent(
																				jLabel20)))
										.addGap(171, 171, 171)));
		jPanel3Layout
				.setVerticalGroup(jPanel3Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel3Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel3Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								jLabel19)
																						.addComponent(
																								tcpPacketNumLabel))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				jPanel3Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								udpPacketNumLabel)
																						.addComponent(
																								jLabel20)))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel7)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel11))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel5)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel8)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel9)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel12))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel13)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel14)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel15)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel16))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				byteRateLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				packageRateLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				totalPacketNumLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				elapsedTimeLabel))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel3)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel6)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel4)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel10))
														.addGroup(
																jPanel3Layout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING)
																		.addGroup(
																				jPanel3Layout
																						.createSequentialGroup()
																						.addComponent(
																								ipv4PacketNumLabel)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								ipv6PacketNumLabel)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								arpPacketNumLabel)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								icmpPacketNumLabel))
																		.addGroup(
																				jPanel3Layout
																						.createSequentialGroup()
																						.addComponent(
																								jLabel21)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								jLabel22)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								jLabel23)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								jLabel24))))
										.addContainerGap(21, Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jPanel2Layout
																		.createSequentialGroup()
																		.addComponent(
																				scrollPane1,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				0,
																				Short.MAX_VALUE)
																		.addGap(
																				530,
																				530,
																				530))
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jPanel2Layout
																		.createSequentialGroup()
																		.addComponent(
																				jPanel3,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addContainerGap()))));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jPanel3,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																scrollPane1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																82,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(146, Short.MAX_VALUE)));

		jTabbedPane1.addTab("\u7edf\u8ba1\u4fe1\u606f", jPanel2);

		jMenu1.setText("File");

		openFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK));
		openFileMenuItem.setText("\u6253\u5f00");
		openFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openFileMenuItemActionPerformed(evt);
			}
		});

		jMenu1.add(openFileMenuItem);

		saveFileMenuItem
				.setIcon(new javax.swing.ImageIcon(getClass().getResource(
						"/com/sun/java/swing/plaf/windows/icons/HardDrive.gif"))); // NOI18N
		saveFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		saveFileMenuItem.setText("\u4fdd\u5b58");
		saveFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveFileMenuItemActionPerformed(evt);
			}
		});
		jMenu1.add(saveFileMenuItem);
		jMenu1.add(jSeparator1);

		jMenuItem2.setText("\u9000\u51fa");
		jMenu1.add(jMenuItem2);

		jMenuBar1.add(jMenu1);

		jMenu3.setText("Help");

		jMenuItem3.setText("\u5173\u4e8e...");
		jMenu3.add(jMenuItem3);

		jMenuBar1.add(jMenu3);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jPanel1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addGap(
																				10,
																				10,
																				10)
																		.addComponent(
																				jTabbedPane1,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				571,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jPanel1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												jTabbedPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												323, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		int returnVal = this.jFileChooser1.showOpenDialog(this);
		switch (returnVal) {
		case JFileChooser.APPROVE_OPTION:
			File saveFile = this.jFileChooser1.getSelectedFile();
			this.myCtrl.openDumpFile(saveFile);
			break;
		case JFileChooser.CANCEL_OPTION:

		case JFileChooser.ERROR_OPTION:

		}
	}

	private void saveFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

		int returnval = this.jFileChooser1.showSaveDialog(this);
		switch (returnval) {
		case JFileChooser.APPROVE_OPTION:
			File saveFile = this.jFileChooser1.getSelectedFile();
			this.myCtrl.createDumpFile(saveFile);
			break;
		case JFileChooser.CANCEL_OPTION:

		case JFileChooser.ERROR_OPTION:

		}
	}

	private void formWindowClosed(java.awt.event.WindowEvent evt) {
		// TODO add your handling code here:
		this.myCtrl.terminateProgram();
	}

	private void packageListValueChanged(
			javax.swing.event.ListSelectionEvent evt) {
		// TODO add your handling code here:
		if (!evt.getValueIsAdjusting()) {
			System.out.println(this.packageList.getSelectedIndex());
			this.myCtrl.seletePacketList(this.packageList.getSelectedIndex());
		}
	}

	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling

		this.myCtrl.doCap();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		// new controller,model

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JLabel arpPacketNumLabel;
	private javax.swing.JLabel byteRateLabel;
	private javax.swing.JLabel elapsedTimeLabel;
	private javax.swing.JTextField filterTextField;
	private javax.swing.JLabel icmpPacketNumLabel;
	private javax.swing.JLabel ipv4PacketNumLabel;
	private javax.swing.JLabel ipv6PacketNumLabel;
	private javax.swing.JCheckBox isPromiscCheckBox;
	private javax.swing.JFileChooser jFileChooser1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTextField neededPacketNumTextField;
	private javax.swing.JMenuItem openFileMenuItem;
	private javax.swing.JTree packageInfoTree;
	private javax.swing.JList packageList;
	private javax.swing.JLabel packageRateLabel;
	private java.awt.Canvas rateCanvas;
	private javax.swing.JMenuItem saveFileMenuItem;
	private java.awt.ScrollPane scrollPane1;
	private javax.swing.JComboBox selectDeviceComboBox;
	private javax.swing.JButton startButton;
	private javax.swing.JLabel tcpPacketNumLabel;
	private javax.swing.JLabel totalPacketNumLabel;
	private javax.swing.JLabel udpPacketNumLabel;
	// End of variables declaration//GEN-END:variables

}