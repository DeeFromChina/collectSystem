package com.golead.dasService.communication;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class CommunicationServerInitializer extends ChannelInitializer<SocketChannel> {

   private CommunicationCallback communicationCallback;

   public CommunicationServerInitializer(CommunicationCallback communicationCallback) {
      this.communicationCallback = communicationCallback;
   }
   
   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      System.out.println(ctx.channel().localAddress().toString()+" channelActive");
      //通知您已经链接上客户端
      String str = "您已经开启与服务端链接"+" "+new Date()+" "+ctx.channel().localAddress();
      ByteBuf buf = Unpooled.buffer(str.getBytes().length);
      buf.writeBytes(str.getBytes());
      ctx.writeAndFlush(buf);
      }

   @Override
   protected void initChannel(SocketChannel ch) throws Exception {
      ChannelPipeline pipeline = ch.pipeline();

      // 以("\n")为结尾分割的 解码器
      pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

      // 字符串解码和编码
      pipeline.addLast("decoder", new StringDecoder());
      pipeline.addLast("encoder", new StringEncoder());

      // 自己的逻辑Handler
      pipeline.addLast("handler", new CommunicationServerHandler(communicationCallback));
   }
}
