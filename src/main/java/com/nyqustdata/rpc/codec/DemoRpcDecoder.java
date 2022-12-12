package com.nyqustdata.rpc.codec;

import com.nyqustdata.rpc.Constants;
import com.nyqustdata.rpc.compress.Compressor;
import com.nyqustdata.rpc.compress.CompressorFactory;
import com.nyqustdata.rpc.protocol.Header;
import com.nyqustdata.rpc.protocol.Message;
import com.nyqustdata.rpc.protocol.Request;
import com.nyqustdata.rpc.protocol.Response;
import com.nyqustdata.rpc.serialization.Serialization;
import com.nyqustdata.rpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoRpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < Constants.HEADER_SIZE) {
            return;
        }

        byteBuf.markReaderIndex();

        short magic = byteBuf.readShort();
        if (magic != Constants.MAGIC) {
            byteBuf.resetReaderIndex();
            throw new RuntimeException("magic number error: " + magic);
        }
        byte version = byteBuf.readByte();
        byte extraInfo = byteBuf.readByte();
        long messageId = byteBuf.readLong();
        int size = byteBuf.readInt();

        Object body = null;

        if (!Constants.isHeartBeat(extraInfo)) {
            if (byteBuf.readableBytes() < size) {
                byteBuf.resetReaderIndex();
                return;
            }
            byte[] payload = new byte[size];
            byteBuf.readBytes(payload);
            Serialization serialization = SerializationFactory.get(extraInfo);
            Compressor compressor = CompressorFactory.get(extraInfo);
            if (Constants.isRequest(extraInfo)) {
                body = serialization.deserialize(compressor.unCompress(payload), Request.class);
            } else {
                body = serialization.deserialize(compressor.unCompress(payload), Response.class);
            }
        }

        Header header = new Header(magic, version, extraInfo, messageId, size);
        Message message = new Message(header, body);
        list.add(message);

    }
}
