package com.zxy.howtomcatworks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception{
        System.out.println("Server Main：" + System.currentTimeMillis());
        ServerSocket ss= new ServerSocket(1090);
        ExecutorService service = Executors.newCachedThreadPool();
        while (true) {
            Socket socket = ss.accept();
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("获取Socket：" + System.currentTimeMillis());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line = null;
                        StringBuilder sb = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            sb.append(line+"\n");
                            if(sb.toString().contains("Connection: Close")){
                                break;
                            }
                        }

                        System.out.println("请求内容：" + sb.toString());

                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
                        writer.write("当前服务器时间：" + format.format(new Date()) + " 随机数：" + new Random().nextInt(10000));
                        writer.close();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
