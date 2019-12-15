package com.npu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        //创建一个线程池
        //如果有客户端连接，创建一个线程与之通信（单独写方法）
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        while(true){
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端了");

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    public static void handler(Socket socket){
        try{
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while(true){
                int read=inputStream.read(bytes);
                if(read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
