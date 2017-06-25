package com.golead.disManager.communication;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
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
         // ChannelFuture channelFuture = bootstrap.connect(host, port);

         ChannelPromise channelFuture = (ChannelPromise) bootstrap.connect(host, port);
         int timeOut = 20000;
         while (!channelFuture.isDone() && timeOut > 0) {
            Thread.sleep(200);
            timeOut -= 200;
         }
         if (channelFuture.isSuccess()) {
            channelFuture.sync();
            channel = channelFuture.channel();
            channel.closeFuture().sync();
         }
         else {
            String errorMessage = "";
            if (timeOut <= 0) {
               errorMessage = "连接超时：" + host + "：" + port;
            }
            else if (channelFuture.cause().getMessage().startsWith("Connection refused")) {
               errorMessage = "连接错误,客户端无法连接：" + host + "：" + port;
            }
            else {
               errorMessage = "连接错误：" + channelFuture.cause().getMessage();
            }
            logger.error(errorMessage);
            communicationClientCallback.command(host + ":" + port, CommunicationClientCallback.COMMAND_CONNECT + ":1," + errorMessage);

         }
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
