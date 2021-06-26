package com.zxy.howtomcatworks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <option name="delegatedBuild" value="false"/>
 */
public class TestMain {

    public static void main(String[] args)throws Exception {
        Socket socket = new Socket("127.0.0.1",1090);
        socket.setSoTimeout(10000);
        OutputStream stream = socket.getOutputStream();
        String s = "客户端 "+System.currentTimeMillis();
        stream.write(s.getBytes());
        socket.shutdownOutput();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null){
                sb.append(line);
            }
            System.out.println("响应内容："+sb.toString());
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
