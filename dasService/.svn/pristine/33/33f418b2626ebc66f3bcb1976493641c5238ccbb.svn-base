package com.golead.dasService.communication;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.log4j.Logger;

import com.golead.dasService.DasService;

public class CommunicationService extends Thread {

   private static Logger         logger = Logger.getLogger(CommunicationService.class);

   private ServerBootstrap       bootstrap;

   private CommunicationCallback communicationCallback;

   private Integer               port;

   public CommunicationService(Integer port,CommunicationCallback communicationCallback) {
      this.communicationCallback = communicationCallback;
      this.port = port;
   }

   public void run() {
      EventLoopGroup bossGroup = new NioEventLoopGroup();
      EventLoopGroup workerGroup = new NioEventLoopGroup();
      bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup);
      bootstrap.channel(NioServerSocketChannel.class);
      bootstrap.childHandler(new CommunicationServerInitializer(communicationCallback));

      // 服务器绑定端口监听
      ChannelFuture channelFuture;
      try {
         channelFuture = bootstrap.bind(port).sync();
         logger.info("数据通讯已启动。");
         channelFuture.channel().closeFuture().sync();
      } catch (InterruptedException e) {
         e.printStackTrace();
         logger.error("数据通讯启动失败：" + e.getMessage());
      } finally {// 监听服务器关闭监听
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
      }
   }
}
