package com.golead.disService.service;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.golead.disService.comm.handler.SimpleServerHandler;

public class SendMessage extends Thread{
   
   private static Logger logger = Logger.getLogger(SendMessage.class);
   
   private Integer               port;
   
   private String                host;
   
   public SendMessage(Integer port,String host){
      this.port = port;
      this.host = host;
   }
   
   public void run() {
      nettyServer();
   }
   
   public void nettyServer(){
       logger.info("数据加载服务未启动。");
       EventLoopGroup bossGroup = new NioEventLoopGroup();
       EventLoopGroup workerGroup = new NioEventLoopGroup();
       
       try {
         ServerBootstrap serverBootstrap = new ServerBootstrap();
         serverBootstrap.group(bossGroup,workerGroup)
           .channel(NioServerSocketChannel.class)
           .option(ChannelOption.SO_BACKLOG, 1024)
           .childHandler(new ChildChannelHandler());
         
         //绑定端口、同步等待（放出一端口等客户端链接）
         ChannelFuture futrue = serverBootstrap.bind(port).sync();
         
         //等待服务监听端口关闭
         futrue.channel().closeFuture().sync();
         logger.info("数据加载服务已启动。");
       } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
       }finally{
         //退出，释放线程等相关资源
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
       }
   }
   
   private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
       @Override
       protected void initChannel(SocketChannel ch) throws Exception {

         ch.pipeline().addLast(new SimpleServerHandler());
       }
     }
}
