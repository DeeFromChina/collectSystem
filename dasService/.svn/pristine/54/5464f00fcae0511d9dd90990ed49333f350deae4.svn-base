package com.golead.dasService.service;

import org.apache.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.golead.dasService.handler.SimpleClientHandler;

public class ReceiveMessage extends Thread {
   
   private static Logger logger = Logger.getLogger(ReceiveMessage.class);
   
   private Integer               port;
   
   private String                host;
   
   public ReceiveMessage(Integer port,String host){
      this.port = port;
      this.host = host;
   }
   
   public void run() {
      connect(port, host);
   }
   
   public void connect(int port,String host){  
      logger.info("数据加载服务未启动。");
      EventLoopGroup group = new NioEventLoopGroup();  
        
      try {  
        Bootstrap bootstrap = new Bootstrap();  
        bootstrap.group(group)  
        .channel(NioSocketChannel.class)  
        .option(ChannelOption.TCP_NODELAY, true)  
        .handler(new ChannelInitializer<SocketChannel>() {  
    
          @Override  
          protected void initChannel(SocketChannel ch) throws Exception {  
            ch.pipeline().addLast(new SimpleClientHandler());  
          }  
        });  
        //发起异步链接操作  
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();  
          
        channelFuture.channel().closeFuture().sync();
        logger.info("数据加载服务已启动。");
      } catch (InterruptedException e) {  
        // TODO Auto-generated catch block  
        e.printStackTrace();  
      }finally{  
        //关闭，释放线程资源  
        group.shutdownGracefully();  
      }  
    }

}
