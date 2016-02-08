/**
 * 
 */
package netcap.control;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import jpcap.JpcapWriter;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.IPv6Option;
import jpcap.packet.Packet;
import netcap.model.NetCapModel;

/**
 * @author Administrator
 *         MVC模式中的Control组件，负责从NetCapModel中获得数据并更新UI，以及从UI读取数据并传递给Model
 */
public class NetCapControl {

	//menu items
	private javax.swing.JMenuItem openFileMenuItem;
	private javax.swing.JMenuItem saveFileMenuItem;
	
	// input component
	private javax.swing.JTextField filterTextField;
	private javax.swing.JCheckBox isPromiscCheckBox;
	private javax.swing.JComboBox selectDeviceComboBox;
	private javax.swing.JButton startButton;
	private javax.swing.JTextField neededPacketNumTextField;

	/**
	 * 判断当前属于启动还是停止抓包
	 */
	private boolean isStart = false;
	// output component
	private javax.swing.JTree packageInfoTree;
	private javax.swing.JList packageList;
	// state component
	private javax.swing.JLabel byteRateLabel;
	private javax.swing.JLabel packageRateLabel;
	private javax.swing.JLabel arpPacketNumLabel;
	private javax.swing.JLabel elapsedTimeLabel;
	private javax.swing.JLabel icmpPacketNumLabel;
	private javax.swing.JLabel ipv4PacketNumLabel;
	private javax.swing.JLabel ipv6PacketNumLabel;
	private javax.swing.JLabel totalPacketNumLabel;
	private javax.swing.JLabel tcpPacketNumLabel;
	private javax.swing.JLabel udpPacketNumLabel;
	//
	private java.awt.Canvas rateCanvas;

	private NetCapModel myModel = new NetCapModel(this);
	private Thread modelThread = null;

	// swing ui model
	private DefaultComboBoxModel cbModel = new DefaultComboBoxModel();
	private DefaultListModel listModel = new DefaultListModel();
	private DefaultTreeModel treeModel;
	
	private volatile long elapsedTime = 0;
	private StateTimer timer;

	public NetCapControl() {
		super();

	}

	/**
	 * 将ui组件的值传递给model，开始抓包
	 * 
	 * @return true：启动或停止model成功 false：启动或停止model失败
	 */
	public boolean doCap() {
		// 如果已经启动，则停止
		if (!this.isStart) {
			//开始抓包
			this.startCap();
			//开始统计
			this.startTimer();
		} else {
			this.stopCap();
			
//			this.stopTimer();
		}

		return true;
	}

	/**
	 * 设置ui组件的初始值（例如设备的下拉列表）
	 */
	public void initComponentsValue() {
		// 设置combox的值
		this.setDeviceComboBoxValue();
		//
		this.neededPacketNumTextField.setText(""+this.myModel.getNeededPacketNum());
		// 设置packetlist的model
		this.packageList.setModel(this.listModel);
		//
		this.treeModel = (DefaultTreeModel) this.packageInfoTree.getModel();
		if (this.treeModel == null) {
			this.treeModel = new DefaultTreeModel(null);
			this.packageInfoTree.setModel(this.treeModel);
		}
	}

	public void seletePacketList(int packetIndex) {
		Packet p = this.myModel.getPacketByIndex(packetIndex);
		
		if(p != null )
			this.updatePacketInfoTree(p);
	}

	public void fireNewPacket(int index, Packet p) {
		//
		this.listModel.addElement("Packet" + index);
	}

	/**
	 * 完成抓包后的其他工作（重置标记位、修改ui组件状态，等等）
	 */
	public void finishCap() {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				selectDeviceComboBox.setEnabled(true);
				filterTextField.setEnabled(true);
				isPromiscCheckBox.setEnabled(true);
				
				openFileMenuItem.setEnabled(true);
				saveFileMenuItem.setEnabled(true);
				
				startButton.setText("开始抓包");
				startButton.setEnabled(true);
				
				
			}

		});

		this.stopTimer();
		this.isStart = false;
	}
	
	
	public void createDumpFile(File saveFile){
		if(saveFile == null)return;
		
		System.out.println(saveFile.getAbsolutePath());
		
		if(saveFile.isFile() && saveFile.exists()){
			saveFile.delete();
		}
//			System.out.println("not file");
		try {
			saveFile.createNewFile();
			
			int[] indices = this.packageList.getSelectedIndices();
			if(indices==null || indices.length ==0 ){
				//save all packets
				this.myModel.saveAllPacketsIntoFile(saveFile.getAbsolutePath());
			}else{
				this.myModel.savePacketIntoFile(saveFile.getAbsolutePath(), indices);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openDumpFile(File openFile){
		if(openFile == null || !openFile.isFile() || !openFile.exists())return;
		
		//先清空列表和树
		this.listModel.clear();
		this.treeModel.setRoot(null);
		
		this.myModel.getPacketsFromFile(openFile.getAbsolutePath());
	}

	public void cleanModelState(){
		
	}
	
	/**
	 * 关闭程序，主要做清理工作
	 */
	public void terminateProgram(){
		this.modelThread.interrupt();
		this.timer.stopTiming();
	}
	/**
	 * 根据用户的选择，更新树视图
	 * 
	 * @param p
	 *            被选择的packet
	 */
	private void updatePacketInfoTree(Packet p) {
		// 根节点
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Packet");
		this.treeModel.setRoot(root);
		// header of packet
		DefaultMutableTreeNode header = new DefaultMutableTreeNode("Header:"
				+ Arrays.toString(p.header));
		this.treeModel.insertNodeInto(header, root, 0);

		// 数据链路层
		DefaultMutableTreeNode datalinkNode;
		// 如果是以太帧
		if (p.datalink instanceof jpcap.packet.EthernetPacket) {
			EthernetPacket ep = (EthernetPacket) p.datalink;
			datalinkNode = new DefaultMutableTreeNode("以太帧（Ethernet）");
			// 以太帧的属性
			DefaultMutableTreeNode frametypeNode = new DefaultMutableTreeNode(
					"帧类型（frametype）:" + ep.frametype);
			switch(ep.frametype){
			case EthernetPacket.ETHERTYPE_ARP:
				frametypeNode = new DefaultMutableTreeNode(
						"帧类型（frametype）：ARP" + "("+ep.frametype+")");
				break;
			case EthernetPacket.ETHERTYPE_IP:
				frametypeNode = new DefaultMutableTreeNode(
						"帧类型（frametype）：IPV4" + "("+ep.frametype+")");
				break;
			case EthernetPacket.ETHERTYPE_IPV6:
				frametypeNode = new DefaultMutableTreeNode(
						"帧类型（frametype）：IPV6" + "("+ep.frametype+")");
				break;
			case EthernetPacket.ETHERTYPE_LOOPBACK:
				frametypeNode = new DefaultMutableTreeNode(
						"帧类型（frametype）：LOOPBACK" + "("+ep.frametype+")");
				break;
			case EthernetPacket.ETHERTYPE_PUP:
				frametypeNode = new DefaultMutableTreeNode(
						"帧类型（frametype）：PUP Protocol" + "("+ep.frametype+")");
				break;
			case EthernetPacket.ETHERTYPE_REVARP:
				frametypeNode = new DefaultMutableTreeNode(
						"帧类型（frametype）：Reverse Address" + "("+ep.frametype+")");
				break;
			case EthernetPacket.ETHERTYPE_VLAN:
				frametypeNode = new DefaultMutableTreeNode(
						"帧类型（frametype）：IEEE 802.1Q VLAN" + "("+ep.frametype+")");
				break;
				default:
					frametypeNode = new DefaultMutableTreeNode(
							"帧类型（frametype）：未知帧类型" + "("+ep.frametype+")");
					break;
			}
			DefaultMutableTreeNode dstmacNode = new DefaultMutableTreeNode(
					"目的MAC地址：" + ep.getDestinationAddress());
			DefaultMutableTreeNode srcmacNode = new DefaultMutableTreeNode(
					"源MAC地址：" + ep.getSourceAddress());
			//
			datalinkNode.add(frametypeNode);
			datalinkNode.add(dstmacNode);
			datalinkNode.add(srcmacNode);

		} else {
			datalinkNode = new DefaultMutableTreeNode("未知帧类型");
		}
		//
		this.treeModel.insertNodeInto(datalinkNode, root, 1);

		// if ARP报文
		if (p instanceof jpcap.packet.ARPPacket) {
			ARPPacket arp = (ARPPacket) p;
			DefaultMutableTreeNode arpNode = new DefaultMutableTreeNode("ARP报文");

			DefaultMutableTreeNode senderMacAddrNode = new DefaultMutableTreeNode(
					"发送方MAC地址：" + arp.getSenderHardwareAddress());
			DefaultMutableTreeNode senderProAddrNode = new DefaultMutableTreeNode(
					"发送方网络地址：" + arp.getSenderProtocolAddress());

			DefaultMutableTreeNode targetMacAddrNode = new DefaultMutableTreeNode(
					"目的MAC地址：" + arp.getTargetHardwareAddress());
			DefaultMutableTreeNode targetProAddrNode = new DefaultMutableTreeNode(
					"目的网络地址：" + arp.getTargetProtocolAddress());

			DefaultMutableTreeNode prototype = new DefaultMutableTreeNode(
					"网络层协议类型："
							+ (arp.prototype == ARPPacket.PROTOTYPE_IP ? "IP"
									: "未知"));

			DefaultMutableTreeNode operation;
			if (arp.operation == ARPPacket.ARP_REQUEST)
				operation = new DefaultMutableTreeNode("Operation：ARP请求");
			else if (arp.operation == ARPPacket.ARP_REPLY)
				operation = new DefaultMutableTreeNode("Operation：ARP应答");
			else
				operation = new DefaultMutableTreeNode("Operation：未知");

			DefaultMutableTreeNode hardtype;
			if (arp.hardtype == ARPPacket.HARDTYPE_ETHER)
				hardtype = new DefaultMutableTreeNode("链路层类型：以太网");
			else if (arp.hardtype == ARPPacket.HARDTYPE_FRAMERELAY)
				hardtype = new DefaultMutableTreeNode("链路层类型：帧中继");
			else if (arp.hardtype == ARPPacket.HARDTYPE_IEEE802)
				hardtype = new DefaultMutableTreeNode("链路层类型：IEEE802");
			else
				hardtype = new DefaultMutableTreeNode("链路层类型：未知");

			arpNode.add(senderMacAddrNode);
			arpNode.add(senderProAddrNode);
			arpNode.add(targetMacAddrNode);
			arpNode.add(targetProAddrNode);
			arpNode.add(prototype);
			arpNode.add(operation);
			arpNode.add(hardtype);
			//
			this.treeModel.insertNodeInto(arpNode, datalinkNode, datalinkNode
					.getChildCount());
		} else if (p instanceof IPPacket) {
			// if IP 报文

			IPPacket ipp = (IPPacket) p;
			DefaultMutableTreeNode ipNode = new DefaultMutableTreeNode("IP报文");

			DefaultMutableTreeNode versionNode = new DefaultMutableTreeNode(
					"版本（version）：" + ipp.version);

			
			// if ip v4
			if (ipp.version == 4) {
				DefaultMutableTreeNode tosNode = new DefaultMutableTreeNode(
						"服务类型（TOS）：" + ipp.rsv_tos);
				DefaultMutableTreeNode lengthNode = new DefaultMutableTreeNode(
						"总长度（total length）：" + ipp.length);
				DefaultMutableTreeNode identNode = new DefaultMutableTreeNode(
						"标识（Identification）:" + ipp.ident);
				// flags
				String flags = "0";
				String temp = "";
				if (ipp.dont_frag) {
					flags = flags + "0";
					temp = temp + "不分段；";
				} else {
					flags = flags + "1";
					temp = temp + "分段；";
				}
				if (ipp.more_frag) {
					flags = flags + "0";
					temp = temp + "后续分段";
				} else {
					flags = flags + "1";
					temp = temp + "无后续分段";
				}
				flags = flags + " " + temp;

				DefaultMutableTreeNode flagsNode = new DefaultMutableTreeNode(
						"标记位（flags）：" + flags);

				//
				DefaultMutableTreeNode offsetNode = new DefaultMutableTreeNode(
						"片段偏移量（fragment offset）:" + ipp.offset);

				DefaultMutableTreeNode hoplimitNode = new DefaultMutableTreeNode(
						"TTL：" + ipp.hop_limit);

				//
				DefaultMutableTreeNode protocolNode;
				switch (ipp.protocol) {
				
				case IPPacket.IPPROTO_ICMP:
					protocolNode = new DefaultMutableTreeNode(
							"协议类型（Protocol）：ICMP" + "(" + ipp.protocol + ")");
					break;
				case IPPacket.IPPROTO_IGMP:
					protocolNode = new DefaultMutableTreeNode(
							"协议类型（Protocol）：IGMP" + "(" + ipp.protocol + ")");
					break;
				case IPPacket.IPPROTO_IP:
					protocolNode = new DefaultMutableTreeNode(
							"协议类型（Protocol）：IP OVER IP" + "(" + ipp.protocol
									+ ")");
					break;
				case IPPacket.IPPROTO_IPv6:
					protocolNode = new DefaultMutableTreeNode(
							"协议类型（Protocol）：IPv6" + "(" + ipp.protocol + ")");
					break;

				case IPPacket.IPPROTO_TCP:
					protocolNode = new DefaultMutableTreeNode(
							"协议类型（Protocol）：TCP" + "(" + ipp.protocol + ")");
					break;
				case IPPacket.IPPROTO_UDP:
					protocolNode = new DefaultMutableTreeNode(
							"协议类型（Protocol）：UDP" + "(" + ipp.protocol + ")");
					break;
				default:
					protocolNode = new DefaultMutableTreeNode(
							"协议类型（Protocol）：未知" + "(" + ipp.protocol + ")");
					break;
				}
				// DefaultMutableTreeNode protocolNode = new
				// DefaultMutableTreeNode("协议类型（Protocol）："+ipp.protocol);

				// header checksum
				// DefaultMutableTreeNode headerChecksumNode = new
				// DefaultMutableTreeNode("报头校验和（header checksum）："+ipp.);

				// option
				DefaultMutableTreeNode optionNode = new DefaultMutableTreeNode(
						"Option:" + Arrays.toString(ipp.option));
				
				//加入到树中
				ipNode.add(versionNode);
				ipNode.add(tosNode);
				ipNode.add(lengthNode);
				ipNode.add(identNode);
				ipNode.add(flagsNode);
				ipNode.add(offsetNode);
				ipNode.add(hoplimitNode);
				ipNode.add(protocolNode);
				ipNode.add(optionNode);
				
				///////////////////////////////////////////////////////////
				//如果是隧道ipv6，则需专门处理
				if( ipp.protocol == IPPacket.IPPROTO_IPv6){
					ipNode.add(this.parseTunnelIpv6(ipp));
				}
				
			} else if (ipp.version == 6) {
				DefaultMutableTreeNode trafficClassNode = new DefaultMutableTreeNode(
						"通信流类别（traffic class）：" + ipp.priority);
				DefaultMutableTreeNode flowlabelNode = new DefaultMutableTreeNode(
						"流标签（flowlabel）：" + ipp.flow_label);
				DefaultMutableTreeNode payloadLengthNode = new DefaultMutableTreeNode(
						"负荷长度（payload length）：" + ipp.length);
				
				//
				DefaultMutableTreeNode nextHeaderNode = this.getIpv6NextHeader(ipp.protocol);
				

				DefaultMutableTreeNode hoplimitNode = new DefaultMutableTreeNode(
						"跳数（hop limit）：" + ipp.hop_limit);
				
				DefaultMutableTreeNode oneOptionNode;
				DefaultMutableTreeNode optionsNode = new DefaultMutableTreeNode("IPV6扩展报头");
				{
					List<IPv6Option> options = ipp.options;
					for(IPv6Option opt:options){
						oneOptionNode = this.getIpv6Option(opt.type);
						
						optionsNode.add(oneOptionNode);
					}
				}//ipv6 options

				ipNode.add(versionNode);
				ipNode.add(trafficClassNode);
				ipNode.add(flowlabelNode);
				ipNode.add(payloadLengthNode);
				ipNode.add(nextHeaderNode);
				ipNode.add(hoplimitNode);
				ipNode.add(optionsNode);
			}//end ip version check

			// ip地址
			DefaultMutableTreeNode srcNode = new DefaultMutableTreeNode(
					"源IP地址：" + ipp.src_ip.getHostAddress());
			DefaultMutableTreeNode dstNode = new DefaultMutableTreeNode(
					"目的IP地址：" + ipp.dst_ip.getHostAddress());

			//
			ipNode.add(srcNode);
			ipNode.add(dstNode);
			
			//
			this.treeModel.insertNodeInto(ipNode, datalinkNode, datalinkNode
					.getChildCount());
		}
		//end ip packet
		
		

	}//end updataPacketInfoTree method

	/**
	 * 生成传输层报文的树节点
	 * @param ipp
	 * ip报文对象
	 * @return
	 * 传输层的树节点
	 */
	private DefaultMutableTreeNode genTransferLayerNode( IPPacket ipp){
		return null;
	}
	
	/**
	 * 由于jpcap未提供隧道ipv6的封装，故专门写一函数，针对ipv4中作为负荷的ipv6内容的字节流进行解析
	 * @param ipp
	 * @return
	 */
	private DefaultMutableTreeNode parseTunnelIpv6(IPPacket ipp){
		byte[] ipv6byte = ipp.data;
		if(ipv6byte.length <=40){
			return null;
		}
		
		DefaultMutableTreeNode ipv6Node = new DefaultMutableTreeNode("隧道IPv6报文");
		
		//version
		int version = ipv6byte[0]>>>4;
		ipv6Node.add(new DefaultMutableTreeNode("版本（version）："+ version));
		
		//Traffic Class
		int trafficClass = ipv6byte[0]<<4 + ipv6byte[1]>>>4;
		ipv6Node.add(new DefaultMutableTreeNode("通信流类别（Traffic Class）："+ trafficClass));
		
		//flow label
		String flowLabel = "0x" + (ipv6byte[1] & 0x0f) + (ipv6byte[2]>>>4) +  (ipv6byte[2]& 0x0f) + (ipv6byte[3]>>>4) + (ipv6byte[3]& 0x0f);
		ipv6Node.add(new DefaultMutableTreeNode("流标签（Flow Label）："+ flowLabel));
		
		//Payload
		int payloadLength = ipv6byte[4]<<8 + ipv6byte[5]&0xff;
		ipv6Node.add(new DefaultMutableTreeNode("有效载荷长度（Payload Length）："+ payloadLength));
		
		//next header
		int nextHeader = ipv6byte[6]&0xff;
		DefaultMutableTreeNode nextHeaderNode = this.getIpv6NextHeader((short)nextHeader);
//		nextHeaderNode.add(this.getIpv6Option(ipv6byte[6]));
		ipv6Node.add(nextHeaderNode);
		
		
		//hop limit
		int hopLimit = ipv6byte[7]&0xff;
		ipv6Node.add(new DefaultMutableTreeNode("跳数限制（Hop Limit）："+ hopLimit));
		
		//source address
		byte[] sourceAddByte = Arrays.copyOfRange(ipv6byte, 8, 24);
		String sourceAdd;
		try {
			sourceAdd = InetAddress.getByAddress(sourceAddByte).toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sourceAdd = "0::0";
		}
		ipv6Node.add(new DefaultMutableTreeNode("源地址（Source Address）："+ sourceAdd));
		
		//target address
		byte[] destAddByte = Arrays.copyOfRange(ipv6byte, 24, 40);
		String destAdd;
		try {
			destAdd = InetAddress.getByAddress(destAddByte).toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			destAdd = "0::0";
		}
		ipv6Node.add(new DefaultMutableTreeNode("目的地址（Destination Address）："+ destAdd));
		
		return ipv6Node;
	}
	
	/**
	 * 生成ipv6扩展报头的树节点
	 * @param opt
	 * 扩展报头
	 * @return
	 * 表示该扩展包头的树节点
	 */
	private DefaultMutableTreeNode getIpv6Option(byte type){
		DefaultMutableTreeNode oneOptionNode;
		
		switch(type){
		case IPv6Option.AH_OPTION:
			oneOptionNode = new DefaultMutableTreeNode("认证报头"+"("+type+")");
			break;
		case IPv6Option.DESTINATION_OPTION:
			oneOptionNode = new DefaultMutableTreeNode("目标选项报头"+"("+type+")");
			break;
		case IPv6Option.ESP_OPTION:
			oneOptionNode = new DefaultMutableTreeNode("封装安全有些载荷报头"+"("+type+")");
			break;
		case IPv6Option.FRAGMENT_OPTION:
			oneOptionNode = new DefaultMutableTreeNode("分段报头"+"("+type+")");
			break;
		case IPv6Option.HOP_BY_HOP_OPTION:
			oneOptionNode = new DefaultMutableTreeNode("逐跳选项报头"+"("+type+")");
			break;
		case IPv6Option.NONE_OPTION:
			oneOptionNode = new DefaultMutableTreeNode("无扩展报头"+"("+type+")");
			break;
		case IPv6Option.ROUTING_OPTION:
			oneOptionNode = new DefaultMutableTreeNode("路由报头"+"("+type+")");
			break;
			default:
				oneOptionNode = new DefaultMutableTreeNode("未知的扩展报头"+"("+type+")");
				break;
		}
		
		return oneOptionNode;
	}
	
	private DefaultMutableTreeNode getIpv6NextHeader(short nextheader){
		DefaultMutableTreeNode nextHeaderNode;
		switch (nextheader) {
		case IPPacket.IPPROTO_TCP:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：TCP" + "("
							+ nextheader + ")");
			break;
		case IPPacket.IPPROTO_UDP:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：UDP" + "("
							+ nextheader + ")");
			break;
		case IPPacket.IPPROTO_HOPOPT:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：IPv6 hop-by-hop" + "("
							+ nextheader + ")");
			break;
		case IPPacket.IPPROTO_IPv6_Frag:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：fragment header for IPv6" + "("
							+ nextheader + ")");
			break;
		case IPPacket.IPPROTO_IPv6_ICMP:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：IPv6 ICMP" + "(" + nextheader
							+ ")");
			break;
		case IPPacket.IPPROTO_IPv6_NoNxt:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：no next header header for IPv6"
							+ "(" + nextheader + ")");
			break;
		case IPPacket.IPPROTO_IPv6_Opts:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：destination option for IPv6"
							+ "(" + nextheader + ")");
			break;
		case IPPacket.IPPROTO_IPv6_Route:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：routing header for IPv6" + "("
							+ nextheader + ")");
			break;
		default:
			nextHeaderNode = new DefaultMutableTreeNode(
					"下一报头（next header）：未知IPV6报头" + "(" + nextheader
							+ ")");
		}
		
		return nextHeaderNode;
	}
	
	private void startCap() {
		// 设置ui组件的状态
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				//清空list和tree的内容
				listModel.removeAllElements();
				treeModel.setRoot(null);
				
				selectDeviceComboBox.setEnabled(false);
				filterTextField.setEnabled(false);
				isPromiscCheckBox.setEnabled(false);
				openFileMenuItem.setEnabled(false);
				saveFileMenuItem.setEnabled(false);
				
				startButton.setText("停止抓包");
			}

		});

		// 获得UI组件的值
		try {
			this.myModel.setDevIndex(this.selectDeviceComboBox
					.getSelectedIndex());
			this.myModel.setFilterString(this.filterTextField.getText());
			this.myModel.setPromisc(this.isPromiscCheckBox.isSelected());
			this.myModel.setTimeout(-1);
			this.myModel.setNeededPacketNum(Integer.parseInt(this.neededPacketNumTextField.getText()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 启动model类，开始抓包
		this.modelThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				myModel.initCap();
				myModel.resetState();
				myModel.startCap();
			}

		});
		this.modelThread.setDaemon(true);
		this.modelThread.start();

		this.isStart = true;
	}

	private void stopCap() {
		this.myModel.stopCap();
		// disable 按钮，直到后台抓包线程停止
		startButton.setEnabled(false);

		// 轮询检测线程状态
		while (this.modelThread != null && this.modelThread.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.finishCap();
	}
	
	private void startTimer(){
		this.timer = new StateTimer();
		timer.setDaemon(true);
		timer.start();
	}
	
	private void stopTimer(){
		this.timer.stopTiming();
	}
	
	

	/**
	 * 从model读入的设备信息，设置combox UI组件
	 */
	private void setDeviceComboBoxValue() {
		if (this.selectDeviceComboBox.getModel() != this.cbModel)
			this.selectDeviceComboBox.setModel(this.cbModel);

		NetworkInterface[] devices = this.myModel.getDevices();
		for (NetworkInterface device : devices) {
			this.cbModel.addElement(new String(device.description));
		}
	}

	//用于计时
	class StateTimer extends Thread{

		private long startTime;
		private long curTime;
		
		private boolean keep = true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.startTime = System.currentTimeMillis();
			while(this.keep){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.curTime = System.currentTimeMillis();
				elapsedTime = (this.curTime - this.startTime)/1000;
				//
				elapsedTimeLabel.setText(""+(float)elapsedTime);
				byteRateLabel.setText(""+(float)myModel.getReceivedBytes()/elapsedTime);
				packageRateLabel.setText(""+(float)myModel.getReceivedPacketNum()/elapsedTime);
				totalPacketNumLabel.setText(""+myModel.getTotalPacketNum());
				//
				ipv4PacketNumLabel.setText(""+myModel.getIpv4PacketNum());
				ipv6PacketNumLabel.setText(""+myModel.getIpv6PacketNum());
				//
				arpPacketNumLabel.setText(""+myModel.getArpPacketNum());
				icmpPacketNumLabel.setText(""+myModel.getIcmpPacketNum());
				//
				tcpPacketNumLabel.setText(""+myModel.getTcpPacketNum());
				udpPacketNumLabel.setText(""+myModel.getUdpPacketNum());
				
				
			}
			
		}
		
		public boolean stopTiming(){
			this.keep = false;
			return keep;
		}
		
		
		
	}
	
	
	
	
	
	
	// setter and getter
	public javax.swing.JTextField getFilterTextField() {
		return filterTextField;
	}

	public void setFilterTextField(javax.swing.JTextField filterTextField) {
		this.filterTextField = filterTextField;
	}

	public javax.swing.JCheckBox getIsPromiscCheckBox() {
		return isPromiscCheckBox;
	}

	public void setIsPromiscCheckBox(javax.swing.JCheckBox isPromiscCheckBox) {
		this.isPromiscCheckBox = isPromiscCheckBox;
	}

	public javax.swing.JComboBox getSelectDeviceComboBox() {
		return selectDeviceComboBox;
	}

	public void setSelectDeviceComboBox(
			javax.swing.JComboBox selectDeviceComboBox) {
		this.selectDeviceComboBox = selectDeviceComboBox;
		// 设置combox model
		this.selectDeviceComboBox.setModel(this.cbModel);
	}

	public javax.swing.JButton getStartButton() {
		return startButton;
	}

	public void setStartButton(javax.swing.JButton startButton) {
		this.startButton = startButton;
	}

	public javax.swing.JTree getPackageInfoTree() {
		return packageInfoTree;
	}

	public void setPackageInfoTree(javax.swing.JTree packageInfoTree) {
		this.packageInfoTree = packageInfoTree;
	}

	public javax.swing.JList getPackageList() {
		return packageList;
	}

	public void setPackageList(javax.swing.JList packageList) {
		this.packageList = packageList;
	}

	public javax.swing.JLabel getByteRateLabel() {
		return byteRateLabel;
	}

	public void setByteRateLabel(javax.swing.JLabel byteRateLabel) {
		this.byteRateLabel = byteRateLabel;
	}

	public javax.swing.JLabel getPackageRateLabel() {
		return packageRateLabel;
	}

	public void setPackageRateLabel(javax.swing.JLabel packageRateLabel) {
		this.packageRateLabel = packageRateLabel;
	}

	public java.awt.Canvas getRateCanvas() {
		return rateCanvas;
	}

	public void setRateCanvas(java.awt.Canvas rateCanvas) {
		this.rateCanvas = rateCanvas;
	}

	public javax.swing.JLabel getArpPacketNumLabel() {
		return arpPacketNumLabel;
	}

	public void setArpPacketNumLabel(javax.swing.JLabel arpPacketNumLabel) {
		this.arpPacketNumLabel = arpPacketNumLabel;
	}

	public javax.swing.JLabel getElapsedTimeLabel() {
		return elapsedTimeLabel;
	}

	public void setElapsedTimeLabel(javax.swing.JLabel elapsedTimeLabel) {
		this.elapsedTimeLabel = elapsedTimeLabel;
	}

	public javax.swing.JLabel getIcmpPacketNumLabel() {
		return icmpPacketNumLabel;
	}

	public void setIcmpPacketNumLabel(javax.swing.JLabel icmpPacketNumLabel) {
		this.icmpPacketNumLabel = icmpPacketNumLabel;
	}

	public javax.swing.JLabel getIpv4PacketNumLabel() {
		return ipv4PacketNumLabel;
	}

	public void setIpv4PacketNumLabel(javax.swing.JLabel ipv4PacketNumLabel) {
		this.ipv4PacketNumLabel = ipv4PacketNumLabel;
	}

	public javax.swing.JLabel getIpv6PacketNumLabel() {
		return ipv6PacketNumLabel;
	}

	public void setIpv6PacketNumLabel(javax.swing.JLabel ipv6PacketNumLabel) {
		this.ipv6PacketNumLabel = ipv6PacketNumLabel;
	}

	public javax.swing.JLabel getTotalPacketNumLabel() {
		return totalPacketNumLabel;
	}

	public void setTotalPacketNumLabel(javax.swing.JLabel totalPacketNumLabel) {
		this.totalPacketNumLabel = totalPacketNumLabel;
	}

	public javax.swing.JLabel getTcpPacketNumLabel() {
		return tcpPacketNumLabel;
	}

	public void setTcpPacketNumLabel(javax.swing.JLabel tcpPacketNumLabel) {
		this.tcpPacketNumLabel = tcpPacketNumLabel;
	}

	public javax.swing.JLabel getUdpPacketNumLabel() {
		return udpPacketNumLabel;
	}

	public void setUdpPacketNumLabel(javax.swing.JLabel udpPacketNumLabel) {
		this.udpPacketNumLabel = udpPacketNumLabel;
	}


	public long getElapsedTime() {
		return elapsedTime;
	}

	public javax.swing.JTextField getNeededPacketNumTextField() {
		return neededPacketNumTextField;
	}

	public void setNeededPacketNumTextField(
			javax.swing.JTextField neededPacketNumTextField) {
		this.neededPacketNumTextField = neededPacketNumTextField;
	}

	public javax.swing.JMenuItem getOpenFileMenuItem() {
		return openFileMenuItem;
	}

	public void setOpenFileMenuItem(javax.swing.JMenuItem openFileMenuItem) {
		this.openFileMenuItem = openFileMenuItem;
	}

	public javax.swing.JMenuItem getSaveFileMenuItem() {
		return saveFileMenuItem;
	}

	public void setSaveFileMenuItem(javax.swing.JMenuItem saveFileMenuItem) {
		this.saveFileMenuItem = saveFileMenuItem;
	}
}
