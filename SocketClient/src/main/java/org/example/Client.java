package org.example;

import org.example.base.Packet;

import java.io.*;
import java.net.Socket;

public class Client {

    private String host;
    private Integer port;
    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Client(){}

    public static Client initConnection(String host, Integer port) throws IOException {
        Client client = new Client();
        client.host = host;
        client.port = port;
        client.socket = new Socket(host,port);
        client.inputStream = client.socket.getInputStream();
        client.outputStream = client.socket.getOutputStream();
        return client;
    }

    private byte[] extendArray(byte[] oldArray) {
        int oldSize = oldArray.length;
        byte[] newArray = new byte[oldSize * 2];
        System.arraycopy(oldArray,0,newArray,0,oldSize);
        return newArray;
    }

    private byte[] readInput(InputStream stream) throws IOException {
        int b;
        byte[] buffer = new byte[10];
        int counter = 0;
        while ((b = stream.read()) > -1) {
            buffer[counter++] = (byte) b;
            if(counter >= buffer.length) {
                buffer = extendArray(buffer);
            }
            if(counter > 1 && Packet.compareEOP(buffer,counter - 1)) {
                break;
            }
        }
        byte[] data = new byte[counter];
        System.arraycopy(buffer, 0, data, 0, counter);
        return data;
    }

    public void sendMessage(Packet packet) throws IOException {
        outputStream.write(packet.toByteArray());
        outputStream.flush();

        byte[] data = readInput(inputStream);
        Packet responsePacket = Packet.parse(data);

        String value1 = responsePacket.getValue(1, String.class);
        System.out.println(value1);

    }
}
