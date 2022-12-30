package org.example;

import org.example.base.Packet;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = Client.initConnection("localhost",4444);

        Packet packet1 = Packet.create(1);
        packet1.setValue(1,"I am your client");
        client.sendMessage(packet1);

        Packet packet2 = Packet.create(1);
        packet2.setValue(1,"Hello");
        client.sendMessage(packet2);

        Packet packet3 = Packet.create(1);
        packet3.setValue(1,"I'm done");
        client.sendMessage(packet3);

        Packet endPacket = Packet.create(2);
        client.sendMessage(endPacket);
    }
}
