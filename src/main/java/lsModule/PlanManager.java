package lsModule;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class PlanManager {

    public static void main(String[] args){
        Socket socket = null;
        Scanner input = new Scanner(System.in);
        String message = null;

        try{
            socket = new Socket();
            System.out.println("[연결 요청]");
            socket.connect(new InetSocketAddress("localhost",5003));
            System.out.println("[연결 성공]");
            byte[] bytes = null;
            OutputStream os = socket.getOutputStream();


            for(int i=0; i<20; i++) {
                System.out.print("[보낼 메시지를 입력하세요]: ");
                message = input.nextLine();
                bytes = message.getBytes("UTF-8");
                os.write(bytes);
                os.flush();
                System.out.println("[데이터 보내기 성공]");
            }
        }catch(Exception e ){}

        if(!socket.isClosed()){
            try{
                socket.close();
            }catch(IOException e1){}
        }
    }
}
