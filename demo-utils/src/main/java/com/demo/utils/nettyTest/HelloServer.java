package com.demo.utils.nettyTest;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @author yaoyu
 * @date 16/3/22
 * @说明:
 */
public class HelloServer {
    public static void main(String[] args) {
        //server
        ServerBootstrap bootstrap=new ServerBootstrap
                (new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new HelloServerHandler());
            }
        });

        bootstrap.bind(new InetSocketAddress(8000));
    }

    public static class HelloServerHandler extends SimpleChannelHandler{
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("hello world,I am server");
        }
    }
}
