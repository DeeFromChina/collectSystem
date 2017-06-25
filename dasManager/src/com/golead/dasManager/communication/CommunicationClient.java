package com.golead.dasManager.communication;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

import org.apache.log4j.Logger;

public class CommunicationClient extends Thread {
   public static int                   CONN_STATUS_CLOSE   = 0;
   public static int                   CONN_STATUS_CONNECT = 1;

   private static Logger               logger              = Logger.getLogger(CommunicationClient.class);

   private String                      host;
   private int                         port;
   private Channel                     channel;
   private EventLoopGroup              group;
   private Bootstrap                   bootstrap;
   private CommunicationClientCallback communicationClientCallback;

   public CommunicationClient(String host, int port, CommunicationClientCallback communicationClientCallback) {
      this.host = host;
      this.port = port;
      this.communicationClientCallback = communicationClientCallback;
   }

   public void run() {
      group = new NioEventLoopGroup();
      try {
         bootstrap = new Bootstrap();
         bootstrap.group(group).channel(NioSocketChannel.class).handler(new CommunicationClientIntializer(communicationClientCallback));

         // 连接服务端
         ChannelFuture channelFuture=bootstrap.connect(host, port);
         channelFuture.sync();
         channel = channelFuture.channel();
         channel.closeFuture().sync();
      } catch (InterruptedException e) {
         logger.error(e.getMessage());
      }
   }

   public void connect() {
   }

   public boolean isOpen() {
      return channel.isOpen();
   }

   public void send(String command) {
      channel.writeAndFlush(command + "\r\n");
   }

   public void sendList(List<String> list) {
      channel.writeAndFlush(list);
   }

   public void close() {
      group.shutdownGracefully();
   }
}
