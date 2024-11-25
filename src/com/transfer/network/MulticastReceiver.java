package com.transfer.network;

import java.io.FileOutputStream;
import java.io.IOException;
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
        try (MulticastSocket socket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(multicastAddress);
            socket.joinGroup(group);

            System.out.println("En attente de fichiers...");
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            String fileName = null;
            FileOutputStream fos = null; // Déclaré ici pour gérer sa durée de vie
            try {
                while (true) {
                    socket.receive(packet);

                    String data = new String(packet.getData(), 0, packet.getLength());
                    if (data.equals("END")) {
                        System.out.println("Fin de réception du fichier.");
                        break;
                    } else if (fileName == null) {
                        fileName = data;
                        System.out.println("Nom du fichier reçu : " + fileName);
                        fos = new FileOutputStream("recu_" + fileName); // Ouverture du flux ici
                    } else {
                        if (fos != null) {
                            fos.write(packet.getData(), 0, packet.getLength());
                            System.out.println("Paquet reçu, taille : " + packet.getLength() + " octets.");
                        }
                    }
                }
            } finally {
                if (fos != null) {
                    fos.close(); // Fermeture explicite du flux
                }
            }

            socket.leaveGroup(group);
            System.out.println("Réception terminée.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du fichier : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
