package com.example.netty_learning.chapter1_TraditionalIO;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {

    /**
     * 从服务端代码中我们可以看到，在传统的 IO 模型中，每个连接创建成功之后都需要一个线程来维护，
     * 每个线程包含一个 while 死循环，那么 1w 个连接对应 1w 个线程，继而 1w 个 while 死循环，这就带来如下几个问题：
     *
     * 1. 线程资源受限：
     *      线程是操作系统中非常宝贵的资源，同一时刻有大量的线程处于阻塞状态是非常严重的资源浪费，操作系统耗不起
     * 2. 线程切换效率低下：
     *      单机 CPU 核数固定，线程爆炸之后操作系统频繁进行线程切换，应用性能急剧下降。
     * 3. 除了以上两个问题，
     *      IO 编程中，我们看到数据读写是以字节流为单位。
     *
     * 为了解决这三个问题，JDK 在 1.4 之后提出了 chapter2_NIO。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // 监听8000端口
        ServerSocket serverSocket = new ServerSocket(8000);

        // 接收新连接线程
        new Thread(

            () -> {

                while (true){
                    try{

                        // (1) 阻塞方法获取新的连接  accept()
                        Socket socket = serverSocket.accept();

                        // (2) 每一个新的连接都创建一个线程，负责读取数据
                        new Thread(
                                ()->{
                                    try{
                                        int len;
                                        byte[] data = new byte[1024];
                                        InputStream inputStream = socket.getInputStream();

                                        // (3) 按字节流方式读取数据
                                        while((len = inputStream.read(data)) != -1){
                                            System.out.println(new String(data,0,len));
                                        }
                                    }catch (IOException e){

                                    }
                                }
                        ).start();

                    }catch (IOException e){

                    }
                }

            }

        ).start();

    }

}
