package com.golead.dasManager.communication;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.log4j.Logger;

public class CommunicationClientHandler extends SimpleChannelInboundHandler<String> {

   private static Logger               logger = Logger.getLogger(CommunicationClientHandler.class);

   private CommunicationClientCallback communicationClientCallback;

   private StringBuffer                sb     = new StringBuffer("");

   public CommunicationClientHandler(CommunicationClientCallback communicationClientCallback) {
      this.communicationClientCallback = communicationClientCallback;
   }

   @Override
   protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
      CommunicationReturnValue crv = new CommunicationReturnValue(message);
      if (crv.getCommand().equals(CommunicationClientCallback.COMMAND_LOG)) {
         sb.append(crv.getReturnString() + "###");
         if ("end".equals(crv.getReturnString())) {
            communicationClientCallback.command(ctx.channel().remoteAddress().toString(), CommunicationClientCallback.COMMAND_LOG + ":" + sb.toString());
            sb = new StringBuffer("");
         }
      }
      else if (crv.getCommand().equals(CommunicationClientCallback.COMMAND_FILE_LIST)) {
         communicationClientCallback.command(ctx.channel().remoteAddress().toString(), CommunicationClientCallback.COMMAND_FILE_LIST + ":" + message);
      }
      else {
         logger.info("数据服务端：" + ctx.channel().remoteAddress().toString() + "接收命令：" + message);
         communicationClientCallback.command(ctx.channel().remoteAddress().toString(), CommunicationClientCallback.COMMAND_MESSAGE + ":" + message);
      }
   }

   @Override
   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
      logger.info("数据服务端" + ctx.channel().remoteAddress().toString() + "连接成功。");
      communicationClientCallback.command(ctx.channel().localAddress().toString(), CommunicationClientCallback.COMMAND_CONNECT + ":成功");
   }

   @Override
   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      super.channelInactive(ctx);
      logger.info("与数据服务端" + ctx.channel().remoteAddress().toString() + "断开。");
      communicationClientCallback.command(ctx.channel().localAddress().toString(), CommunicationClientCallback.COMMAND_DISCONNECT + ":断开");
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, java.lang.Throwable cause) throws Exception {
      logger.info("错误：" + cause.getMessage());
   }
}