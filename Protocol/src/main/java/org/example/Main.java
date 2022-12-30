package org.example;

import org.example.base.Packet;

public class Main {
    public static void main(String[] args) {
        Packet packet = Packet.create((byte) 1);
        packet.setValue(1,123);
        packet.setValue(2,234F);
        packet.setValue(3,"excellent");

        byte[] encodedPacket = packet.toByteArray();

        Packet parsedPacket = Packet.parse(encodedPacket);
        int data1 = parsedPacket.getValue(1, Integer.class);
        float data2 = parsedPacket.getValue(2, Float.class);
        String data3 = parsedPacket.getValue(3, String.class);
        System.out.println("int value: " + data1);
        System.out.println("float value: " + data2);
        System.out.println("String value: " + data3);
    }
}
