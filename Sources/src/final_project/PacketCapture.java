/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package final_project;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

/**
 *
 * @author sahil
 */
public class PacketCapture {
    public static void main(String[] args) throws IOException {
		List<PcapIf> devs = new ArrayList<>(); // Will be filled with NICs
		StringBuilder errbuf = new StringBuilder(); // For any error msgs
		int r = Pcap.findAllDevs(devs, errbuf);
		if (r != Pcap.OK || devs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s",
					errbuf.toString());
			return;
		}
		System.out.println("Network devices found:");
		int i = 0;
		for (PcapIf device : devs) {
			String description = (device.getDescription() != null) ? device
					.getDescription() : "No description available";
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(),
					description);
		}
		PcapIf device = devs.get(2); // Get the interface on which you think traffic is located
		System.out.printf("\nChoosing '%s' on your behalf:\n",
				(device.getDescription() != null) ? device.getDescription()
						: device.getName());
                final byte[] maca = device.getHardwareAddress(); 
                String macI = asString(maca);
                ArrayList<String> reservedIp = new ArrayList<String>();
                reservedIp.add("192.168.1.4");
                reservedIp.add("192.168.1.1");
                reservedIp.add("192.168.1.7");
                reservedIp.add("172.16.0.3");
                System.out.printf("%s=%s\n", device.getName(), macI); 
		int snaplen = 64 * 1024;
		int flags = Pcap.MODE_PROMISCUOUS; 
		int timeout = 10 * 1000;
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
		if (pcap == null) {
			System.err.printf("Error while opening device for capture: "
					+ errbuf.toString());
			return;
		}
		PcapPacketHandler<String> jpacketHandler;
        jpacketHandler = new PcapPacketHandler<String>() {
            public void nextPacket(PcapPacket packet, String user) {
                byte[] data = packet.getByteArray(0, packet.size()); // the package data
                byte[] sIP = new byte[4];
                byte[] dIP = new byte[4];
                Ip4 ip = new Ip4();
                int payload;
                Ethernet ethernet = new Ethernet();
                if (packet.hasHeader(ip) == true) {
                    sIP = packet.getHeader(ip).source();
                    dIP = packet.getHeader(ip).destination();
                    byte[] eth = packet.getHeader(ethernet).source();
                    String sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
                    String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);
                    String mac = FormatUtils.mac(eth);
                    System.out.println(mac);
                    System.out.println("srcIP=" + sourceIP +
                            " dstIP=" + destinationIP +
                            " caplen=" + packet.getCaptureHeader().caplen());
                    if (sourceIP.equals(destinationIP)) {
                        System.err.println("Source and Destination address same : Possible IP spoofing : Land Attack");
                    }
                    ipSpoofing(mac, macI, sourceIP, reservedIp);
                }
                Tcp tcp =new Tcp();
                if (packet.hasHeader(tcp) == true) {
                    payload = ip.getOffset() + ip.size();
                    JBuffer buffer = new JBuffer(payload);
                    buffer.peer(packet, payload, packet.size() - payload);
                    String dataS = buffer.toHexdump();
                    //System.out.println(dataS);
                    tcp = packet.getHeader(tcp);
                    synFin(tcp, packet);
                    fin(tcp, packet);
                    nullPacket(tcp, packet);
                    reserved(tcp, packet);
                    portCheck(tcp, packet);
                    ackCheck(tcp, packet);
                    synCheck(tcp, packet, dataS, reservedIp);
                    destinationCheck(packet);
                }
                Udp udp = new Udp();
                if (packet.hasHeader(udp) == true) {
                    portCheckUdp(udp, packet);
                }
                Icmp icmp =new Icmp(); 
                if (packet.hasHeader(icmp) == true) {
                    payload = ip.getOffset() + ip.size();
                    echoCheck(payload, packet);
                }
                Ip6 ip6 = new Ip6();
                if (packet.hasHeader(ip6) == true) {
                   // System.err.println("IPv6 packet detected : suspicious");
                }
                /*PacketWrap pk = new PacketWrap(0.0,1,2,2,0.0,0.0,0,0.0,0.0,0.0,0.0,0,
                        0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,123.0,6.0,1.0,1.0,0.0,0.0,
                        0.05,0.07,0.0,255.0,26.0,0.1,0.05,0.0,0.0,1.0,1.0,0.0,0.0);
                AnomalyCheck ac = new AnomalyCheck();
                ac.anomalyCheck(pk);*/
            }
        };
		// capture first 10 packages
		pcap.loop(-1, jpacketHandler, "jNetPcap");
		pcap.close();
	}
    private static String asString(final byte[] mac) {  
        final StringBuilder buf = new StringBuilder();  
        for (byte b : mac) {  
            if (buf.length() != 0) {  
                buf.append(':');  
            }  
            if (b >= 0 && b < 16) {  
                buf.append('0');  
            }  
            buf.append(Integer.toHexString((b < 0) ? b + 256 : b).toUpperCase());  
        }  
        return buf.toString();  
    } 
    private static void ipSpoofing(String mac, String macI, String sourceIP, ArrayList<String> reservedIp) {
        if (! mac.equals(macI) && !reservedIp.contains(sourceIP)) {
            if (sourceIP.startsWith("10.") || sourceIP.startsWith("192.168.") || 
                sourceIP.startsWith("169.254.")) {
                System.err.println("Possible IP spoofing using private networks detected");
            }
            else if (sourceIP.startsWith("172.")) {
                for(int i=16; i<=31; i++) {
                    if (sourceIP.startsWith("172."+i+".")) {
                        System.err.println("Possible IP spoofing using private networks detected");
                        break;
                    }
                }
            }
        }
    }
    private static void synFin(Tcp tcp, PcapPacket packet) {
        if (tcp.flags_FIN() == true && tcp.flags_SYN() == true) {
            System.err.println("Malicious packet containing illegal syn-fin combination");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    private static void fin(Tcp tcp, PcapPacket packet) {
        if (tcp.flags_FIN() == true && tcp.flags_ACK() == false && tcp.flags_CWR() == false && 
                tcp.flags_ECE() == false && tcp.flags_PSH() == false && tcp.flags_RST() == false &&
                 tcp.flags_SYN() == false && tcp.flags_URG() == false) {
                   System.err.println("Malicious packet containing only fin bit found : "
                           + "Possible attack - port scan, network mapping, stealth activities");
                   Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    private static void nullPacket(Tcp tcp, PcapPacket packet){
        if (tcp.flags_FIN() == false && tcp.flags_ACK() == false && tcp.flags_CWR() == false && 
            tcp.flags_ECE() == false && tcp.flags_PSH() == false && tcp.flags_RST() == false &&
            tcp.flags_SYN() == false && tcp.flags_URG() == false) {
                System.err.println("Malicious null packet found : illegal");
                Ethernet ethernet = new Ethernet();
                System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    private static void reserved(Tcp tcp, PcapPacket packet) {
        int res = tcp.reserved();
        if (res > 0) {
            System.err.println("reserved bit set : possibly crafted packet");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    private static void portCheck(Tcp tcp, PcapPacket packet) {
        if (tcp.source() == 0) {
            System.err.println("Source Port cannot be zero : illegal");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
        else if (tcp.destination() == 0) {
            System.err.println("Destination Port cannot be zero : illegal");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    public static void ackCheck(Tcp tcp, PcapPacket packet) {
        long ack = tcp.ack();
        if (tcp.flags_ACK() == true && ack == 0 || tcp.flags_ACK() == false && ack != 0) {
            System.err.println("Illegal since acknowledgement number cannot be zero when ack flag is set");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    private static void synCheck(Tcp tcp, PcapPacket packet, String data, ArrayList<String> reservedIp) {
        if (tcp.flags_FIN() == false && tcp.flags_ACK() == false && tcp.flags_CWR() == false && 
            tcp.flags_ECE() == false && tcp.flags_PSH() == false && tcp.flags_RST() == false &&
            tcp.flags_SYN() == true && tcp.flags_URG() == false && !reservedIp.contains(FormatUtils.ip(packet.getHeader(new Ip4()).source()))) {
            if (!data.equals(null)) {
                System.err.println("Illegal : SYN only packet should not contain any data");
                Ethernet ethernet = new Ethernet();
                System.err.println("MAC address of the suspicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
            }
        }
    }
    private static void destinationCheck(PcapPacket packet) {
        String dest = FormatUtils.ip(packet.getHeader(new Ip4()).destination());
        if (dest.endsWith(".0") || dest.endsWith(".255")) {
            System.err.println("Packets should not use destination address that is a broadcast address");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    private static void portCheckUdp(Udp udp, PcapPacket packet) {
        if (udp.source() == 0) {
            System.err.println("Source Port cannot be zero : illegal");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
        else if (udp.destination() == 0) {
            System.err.println("Destination Port cannot be zero : illegal");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
    private static void echoCheck(int payload, PcapPacket packet) {
        if (payload > (65507/2)+1 ) { /* maximum allowable ip packet size is 65535 bytes
                                         including ip header of 20 bytes, icmp contains a pseudo header of 
                                        8 bytes along with this ip header*/
            System.err.println("Unusual large size of echo request : suspicious");
            Ethernet ethernet = new Ethernet();
            System.err.println("MAC address of the malicious agent : " +FormatUtils.mac(packet.getHeader(ethernet).source()));
        }
    }
}
