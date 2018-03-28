package lsModule;

import Example.ProducerThread;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class MessageReceiver implements Runnable{
    private String message;
    private Socket socket = null;
    private Map<String, Long> jmxTopics;
    private String hostname;
    private int port;

    public MessageReceiver(Map map, String hostname, int port) throws IOException {
        this.jmxTopics=map;
        this.hostname=hostname;
        this.port=port;

    }

    public void run() {
        int i = 0;
        byte[] bytes = null;
        String topic = null;

        //socket
        try {
            InputStream is = socket.getInputStream();

            while (true) {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(hostname,port));
                System.out.println("[연결 기다림]");
                this.socket = serverSocket.accept();
                System.out.println("[연결 수락함]");

                bytes = new byte[100];
                int readByteCount = is.read(bytes);
                message = new String(bytes, 0, readByteCount, "UTF-8");
                //json message parsing - type --> if/else if
                //ParsingThread ps = new ParsingThread(message, jmxTopics); --> Creation and Deletion 까지 수행

            }
        } catch (Exception e) {
            System.out.println("에러발생");
        }
    }

    class ParsingThread implements Runnable{

        @Override
        public void run() {
            //parsing
            //if creation else deletion
        }
    }

}
