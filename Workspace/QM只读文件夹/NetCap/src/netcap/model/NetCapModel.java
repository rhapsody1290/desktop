package netcap.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import netcap.control.NetCapControl;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.JpcapWriter;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

/**
 * 
 */

/**
 * @author Administrator
 * 
 */
public class NetCapModel implements PacketReceiver {

	/**
	 * @param nextReceiver
	 *            把捕获的包传递给下一个接受者
	 */
	public NetCapModel(NetCapControl myCtrl) {
		super();
		// TODO Auto-generated constructor stub
		this.receiver = this;
		this.myCtrl = myCtrl;
	}

	private NetworkInterface[] devices;
	private NetworkInterface device;
	private JpcapCaptor jpcap;
	private JpcapSender sender;
	private JpcapWriter jpwriter;

	/**
	 * 缓存抓到的包
	 */
	private ArrayList<Packet> packetStore;
	/**
	 * 抓包线程运行的标记位
	 */
	private boolean doCap = true;

	private final int DEFAULT_SNAPLEN = 2000;
	private final int DEFAULT_NEEDED_PACKET = 10;

	private int timeout = 10000;
	private int snaplen = DEFAULT_SNAPLEN;
	private boolean promisc = false;
	private String filterString = "";

	private boolean isNonBlock = true;
	/**
	 * 需要捕获的包数
	 */
	private int neededPacketNum = DEFAULT_NEEDED_PACKET;

	// /**
	// * 已捕获的包数
	// */
	// private volatile int receivedPacketNum = 0;

	// 统计数据
	/**
	 * 捕获的字节数
	 */
	private volatile long receivedBytes = 0;

	private volatile int totalPacketNum = 0;

	private volatile int ipv4PacketNum = 0;

	private volatile int ipv6PacketNum = 0;

	private volatile int arpPacketNum = 0;

	private volatile int icmpPacketNum = 0;

	private volatile int tcpPacketNum = 0;

	private volatile int udpPacketNum = 0;

	/**
	 * 用户选定要监听的设备序号
	 */
	private int devIndex = 0;

	private PacketReceiver receiver;
	private NetCapControl myCtrl;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// NetCapModel main = new NetCapModel();
		// main.startCap();
		// byte b = 0x35;
		// System.out.println(b>>4);
		// System.out.println(b&0x0F);
		// String s = ""+(b&0x0f)+(b>>4);
		// System.out.println(s);

		short ss = (short) 65535;
		// ss>=0 && ss<=65535
		System.out.println("ss=" + ss);
		int rr = (ss) & 0xffff;
		System.out.println("rr=" + rr);

	}

	public NetworkInterface[] getDevices() {
		devices = JpcapCaptor.getDeviceList(); // 获得设备列表
		return this.devices;
	}

	private NetworkInterface openDevice() throws IOException {

		jpcap = JpcapCaptor.openDevice(this.device, this.snaplen, this.promisc,
				this.timeout); // 打开与设备的连接
		jpcap.setNonBlockingMode(this.isNonBlock);
		// 设置过滤器
		if (filterString == null)
			jpcap.setFilter("ip", true); // 只监听B的IP数据包
		else
			jpcap.setFilter(this.filterString, true);

		sender = jpcap.getJpcapSenderInstance();
		return device;
	}

	public void startCap() {

		System.out.println("start cap on device:" + this.device.description);

		IPPacket ipPacket = null;

		if (this.timeout == -1) {
			this.jpcap.loopPacket(this.neededPacketNum, this.receiver);
			// 通知controller抓包结束
			this.myCtrl.finishCap();
		} else {
			this.jpcap.processPacket(this.neededPacketNum, this.receiver);
		}
		// while (this.doCap) {
		// ipPacket = (IPPacket) jpcap.getPacket();
		// System.out.println(ipPacket);
		// }

	}

	public void stopCap() {
		// this.doCap = false;
		this.jpcap.breakLoop();

		// finalize jpcap
		this.jpcap.close();
		// this.jpcap = null;

	}

	public void initCap() {
		// 关闭原来的captor
		if (this.jpcap != null) {
			this.jpcap.close();
			this.jpcap = null;
		}
		// 打开新的设备

		try {
			this.openDevice();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (this.packetStore == null) {
			this.packetStore = new ArrayList<Packet>(this.neededPacketNum + 1);
		} else {
			this.packetStore.clear();
			this.packetStore.ensureCapacity(this.neededPacketNum + 1);
		}
	}

	@Override
	public void receivePacket(Packet p) {
		// TODO Auto-generated method stub
		if (p == null)
			return;

		// 更新统计数据
		this.jpcap.updateStat();
		this.receivedBytes += p.len;
		this.totalPacketNum++;

		if (p instanceof IPPacket) {

			if (((IPPacket) p).version == 4) {
				this.ipv4PacketNum++;

			}

			if (((IPPacket) p).version == 6) {
				this.ipv6PacketNum++;

			}

			switch (((IPPacket) p).protocol) {
			case IPPacket.IPPROTO_ICMP:
				this.icmpPacketNum++;
				break;

			case IPPacket.IPPROTO_TCP:
				this.tcpPacketNum++;
				break;
			case IPPacket.IPPROTO_UDP:
				this.udpPacketNum++;
				break;

			}
		} else if (p instanceof ARPPacket) {
			this.arpPacketNum++;
		}

		// 传递给controller
		this.packetStore.add(p);
		this.myCtrl.fireNewPacket(this.packetStore.size() - 1, p);

		System.out.println(p);
	}

	public Packet getPacketByIndex(int index) {
		if (index >= this.packetStore.size() || index < 0)
			return null;

		return this.packetStore.get(index);
	}

	public boolean terminateModel() {
		this.jpcap.breakLoop();

		// finalize jpcap
		this.jpcap.close();

		return true;
	}

	/**
	 * 重置統計數據
	 */
	public void resetState() {
		// 统计数据
		/**
		 * 捕获的字节数
		 */
		receivedBytes = 0;

		totalPacketNum = 0;

		ipv4PacketNum = 0;

		ipv6PacketNum = 0;

		arpPacketNum = 0;

		icmpPacketNum = 0;

		tcpPacketNum = 0;

		udpPacketNum = 0;
	}

	public boolean saveAllPacketsIntoFile(String saveFilePath) {
		try {
			this.jpwriter = JpcapWriter.openDumpFile(this.jpcap, saveFilePath);
			if (this.packetStore != null)
				for (Packet p : this.packetStore)
					this.jpwriter.writePacket(p);

			this.jpwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean savePacketIntoFile(String saveFilePath, int[] packetIndices) {
		try {
			this.jpwriter = JpcapWriter.openDumpFile(this.jpcap, saveFilePath);
			for (int packetIndex : packetIndices) {
				if (this.packetStore != null
						&& packetIndex < this.packetStore.size())
					this.jpwriter
							.writePacket(this.packetStore.get(packetIndex));
			}
			this.jpwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean getPacketsFromFile(String openFilePath){
		try {
			JpcapCaptor cap = JpcapCaptor.openFile(openFilePath);
			if (this.packetStore == null) {
				this.packetStore = new ArrayList<Packet>();
			}
			this.packetStore.clear();
			
			for(int index = 0;;index++){
				Packet p = cap.getPacket();
				if(p != null){
					this.packetStore.add(index, p);
					this.myCtrl.fireNewPacket(index, p);
				}else{
					break;
				}
				System.out.println(p);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}

	// /**
	// *
	// */
	// public void recPacket() {
	// IPPacket ipPacket = null;
	//
	// if (this.timeout != -1) {
	// this.jpcap.loopPacket(-1, this.receiver);
	// }
	// while (this.doCap) {
	// ipPacket = (IPPacket) jpcap.getPacket();
	// System.out.println(ipPacket);
	// }
	// }

	// setter and getter

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public int getDevIndex() {
		return devIndex;
	}

	public void setDevIndex(int devIndex) throws IOException {

		if (devIndex >= this.devices.length) {
			throw new IOException("index out of boundrary of device list");
		}

		this.device = this.devices[devIndex];
		this.devIndex = devIndex;

	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getSnaplen() {
		return snaplen;
	}

	public void setSnaplen(int snaplen) {
		this.snaplen = snaplen;
	}

	public boolean isPromisc() {
		return promisc;
	}

	public void setPromisc(boolean promisc) {
		this.promisc = promisc;
	}

	public int getReceivedPacketNum() {
		// synchronized(this.jpcap){
		return this.jpcap.received_packets;
		// }

	}

	public int getDroppedPacketNum() {
		return this.jpcap.dropped_packets;
	}

	public long getReceivedBytes() {
		return receivedBytes;
	}

	public int getTotalPacketNum() {
		return totalPacketNum;
	}

	public int getIpv4PacketNum() {
		return ipv4PacketNum;
	}

	public int getIpv6PacketNum() {
		return ipv6PacketNum;
	}

	public int getArpPacketNum() {
		return arpPacketNum;
	}

	public int getIcmpPacketNum() {
		return icmpPacketNum;
	}

	public int getTcpPacketNum() {
		return tcpPacketNum;
	}

	public int getUdpPacketNum() {
		return udpPacketNum;
	}

	public int getNeededPacketNum() {
		return neededPacketNum;
	}

	public void setNeededPacketNum(int neededPacketNum) {
		this.neededPacketNum = neededPacketNum;
	}

}
