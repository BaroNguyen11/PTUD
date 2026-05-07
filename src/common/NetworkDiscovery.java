package common;

import java.net.*;
import java.util.Enumeration;

/**
 * Tự động tìm kiếm Server trong mạng LAN bằng UDP Broadcast.
 */
public class NetworkDiscovery {
    private static final int DISCOVERY_PORT = 8888;
    private static final String DISCOVER_REQUEST = "DISCOVER_RMI_SERVER";
    private static final String DISCOVER_RESPONSE_PREFIX = "RMI_SERVER_IP:";

    /**
     * Dùng cho Server: Mở UDP Socket lắng nghe request từ Client.
     */
    public static void startServerDiscoveryListener() {
        Thread listenerThread = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(DISCOVERY_PORT, InetAddress.getByName("0.0.0.0"))) {
                socket.setBroadcast(true);
                System.out.println("UDP Discovery Listener started on port " + DISCOVERY_PORT);

                while (true) {
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                    socket.receive(packet);

                    String message = new String(packet.getData()).trim();
                    if (message.equals(DISCOVER_REQUEST)) {
                        String serverIp = getLocalIpAddress();
                        byte[] sendData = (DISCOVER_RESPONSE_PREFIX + serverIp).getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                        socket.send(sendPacket);
                        System.out.println("Sent discovery response to " + packet.getAddress().getHostAddress() + " - IP: " + serverIp);
                    }
                }
            } catch (Exception ex) {
                System.err.println("Discovery Listener Error: " + ex.getMessage());
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /**
     * Dùng cho Client: Gửi UDP broadcast để tìm IP của Server.
     * @return IP của Server nếu tìm thấy, hoặc null nếu timeout.
     */
    public static String discoverServer() {
        try (DatagramSocket c = new DatagramSocket()) {
            c.setBroadcast(true);
            c.setSoTimeout(3000); // Đợi tối đa 3 giây

            byte[] sendData = DISCOVER_REQUEST.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), DISCOVERY_PORT);
            c.send(sendPacket);
            System.out.println("Client: Sent broadcast discovery request.");

            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            String message = new String(receivePacket.getData()).trim();
            if (message.startsWith(DISCOVER_RESPONSE_PREFIX)) {
                String serverIp = message.substring(DISCOVER_RESPONSE_PREFIX.length());
                System.out.println("Client: Found Server at IP " + serverIp);
                return serverIp;
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Client: Discovery timeout. Server not found via broadcast.");
        } catch (Exception ex) {
            System.err.println("Client: Discovery error: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Lấy IP LAN của máy hiện tại.
     */
    private static String getLocalIpAddress() {
        try {
            java.util.Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;

                java.util.Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        String ip = addr.getHostAddress();
                        // Đồng bộ logic lọc IP LAN với ServerMain (Hỗ trợ cả dải 26. của Radmin VPN)
                        if (ip.startsWith("192.168.") || ip.startsWith("172.") || ip.startsWith("10.") || ip.startsWith("26.")) {
                            return ip;
                        }
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
}
