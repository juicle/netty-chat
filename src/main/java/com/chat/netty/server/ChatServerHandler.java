package com.chat.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义一个服务器端业务处理类
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static List<Channel> channelList = new ArrayList<>();

    /**
     * 读取数据
     *
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel inChannel = channelHandlerContext.channel();
        channelList.forEach(channel -> {
            if(channel != inChannel){
                channel.writeAndFlush("[" + inChannel.remoteAddress().toString().substring(1) + "]" + "说:" + s + "\n");
            }
        });
    }

    /**
     * 通道就绪
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        channelList.add(channel);
        System.out.println("[Server] : " + channel.remoteAddress().toString().substring(1) + "上线");
    }

    /**
     * 通道未就绪
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        channelList.remove(channel);
        System.out.println("[Server] : " + channel.remoteAddress().toString().substring(1) + "离线");
    }
}
