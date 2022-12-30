package org.example;

import org.example.base.Packet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

public class Server implements Runnable{

    private Integer port;

    private ServerSocket serverSocket;

    private Socket clientSocket;

    private ThreadPoolExecutor serverPool;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Server() {
    }

    public static Server create(Integer port, ThreadPoolExecutor serverPool) throws IOException {
        Server server = new Server();
        server.port = port;
        server.serverSocket = new ServerSocket(port);
        server.serverPool = serverPool;
        return server;
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

    @Override
    public void run(){
        try {
            clientSocket = serverSocket.accept();

            outputStream = clientSocket.getOutputStream();
            inputStream = new BufferedInputStream(clientSocket.getInputStream());

            while (true) {
                byte[] data = readInput(inputStream);
                Packet packet = Packet.parse(data);

                if (packet.getType() == 2) {
                    Packet awesomePacket = Packet.create(2);
                    awesomePacket.setValue(1,"That's all");
                    outputStream.write(awesomePacket.toByteArray());
                    synchronized (serverPool) {
                        serverPool.notifyAll();
                    }
                    break;
                }

                String value1 = packet.getValue(1, String.class);
                System.out.println(value1);

                Packet response = Packet.create(1);
                response.setValue(1,"Thanks for message");

                outputStream.write(response.toByteArray());
                outputStream.flush();
            }
        }  catch (IOException e) {
            System.out.println("Something went wrong. Reason: " + e.getMessage());
        }
    }
}
