package com.nyqustdata.rpc.codec;

import com.nyqustdata.rpc.Constants;
import com.nyqustdata.rpc.compress.Compressor;
import com.nyqustdata.rpc.compress.CompressorFactory;
import com.nyqustdata.rpc.protocol.Header;
import com.nyqustdata.rpc.protocol.Message;
import com.nyqustdata.rpc.serialization.Serialization;
import com.nyqustdata.rpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoRpcEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Message message, ByteBuf byteBuf) throws Exception {
        Header header = message.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getExtraInfo());
        byteBuf.writeLong(header.getMessageId());
        Object content = message.getContent();
        if (Constants.isHeartBeat(header.getExtraInfo())) {
            byteBuf.writeInt(0);
            return;
        }
        Serialization serialization = SerializationFactory.get(header.getExtraInfo());
        Compressor compressor = CompressorFactory.get(header.getExtraInfo());
        byte[] payLoad = compressor.compress(serialization.serialize(content));
        byteBuf.writeInt(payLoad.length);
        byteBuf.writeBytes(payLoad);


    }
}
