package com.demo.utils.nioTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * @author yaoyu
 * @date 16/4/18
 * @说明:
 */
public class TestNio {
    public static void main(String[] args) {
        try {
            DatagramChannel channel=DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(9999));
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            buffer.clear();
            channel.receive(buffer);
        }catch (Exception e){

        }
    }


    private void  test1(){
        try {
            DatagramChannel channel=DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(9999));
            String newData="fsfdsffassssssss";
            System.currentTimeMillis();
            ByteBuffer buffer=ByteBuffer.allocate(50);
            buffer.clear();
            buffer.put(newData.getBytes());
            buffer.flip();

            int byteSend=channel.send(buffer,new InetSocketAddress("www.baidu.com",80));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void  test2(){
        try {
            Pipe pipe=Pipe.open();
            Pipe.SinkChannel sinkChannel=pipe.sink();

            String newData="fsfdsffassssssss";
            System.currentTimeMillis();
            ByteBuffer buffer=ByteBuffer.allocate(50);
            buffer.clear();
            buffer.put(newData.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()){
                sinkChannel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
