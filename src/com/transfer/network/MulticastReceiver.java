package com.transfer.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {
    private final String multicastAddress;
    private final int port;

    public MulticastReceiver(String multicastAddress, int port) {
        this.multicastAddress = multicastAddress;
        this.port = port;
    }

    public void startReception() {
        try {
            MulticastSocket socket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(multicastAddress);
            socket.joinGroup(group);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            System.out.println("En attente de fichiers...");
            socket.receive(packet);

            String receivedData = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Fichier reçu : " + receivedData);

            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
