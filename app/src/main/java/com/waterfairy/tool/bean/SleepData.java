package com.waterfairy.tool.bean;

import java.nio.ByteBuffer;

/**
 * Created by water_fairy on 2016/11/18.
 */

public abstract class SleepData {


    public static final byte MAX_WRITE_SIZE = 20;
    public String TAG = getClass().getSimpleName();
    public ByteBuffer buffer;
    public int crc32;
    public PacketHead head;
    public PacketBody msg;

    public abstract boolean check(ByteBuffer paramByteBuffer);

    public abstract boolean fill(byte paramByte1, byte paramByte2);

    public abstract ByteBuffer fillBuffer(ByteBuffer paramByteBuffer);

    public abstract boolean parse(ByteBuffer paramByteBuffer);

    public abstract ByteBuffer parseBuffer(ByteBuffer paramByteBuffer);

    public String toString() {
        return "Head[type:" + this.head.type + ",sec:" + this.head.senquence + "],Body[" + this.msg + "]";
    }

    public static class BasePacket {
        public ByteBuffer fillBuffer(ByteBuffer paramByteBuffer) {
            return paramByteBuffer;
        }

        public ByteBuffer parseBuffer(ByteBuffer paramByteBuffer) {
            return paramByteBuffer;
        }
    }

    public static class BaseRspPack
            extends SleepData.BasePacket {
        public static final byte SUCCESS = 0;
        public byte rspCode;
        public byte type;

        public BaseRspPack() {
        }

        public BaseRspPack(byte paramByte) {
            this.type = paramByte;
        }

        public ByteBuffer fillBuffer(ByteBuffer paramByteBuffer) {
            paramByteBuffer.put(this.rspCode);
            return paramByteBuffer;
        }

        public ByteBuffer parseBuffer(ByteBuffer paramByteBuffer) {
            this.rspCode = paramByteBuffer.get();
            return paramByteBuffer;
        }

        public String toString() {
            return "BaseRspPack{type=" + this.type + ",rspCode=" + this.rspCode + '}';
        }
    }

    public static class PacketBody {
        public SleepData.BasePacket content;
        public byte type;

        public PacketBody() {
        }

        public PacketBody(byte paramByte, SleepData.BasePacket paramBasePacket) {
            this.type = paramByte;
            this.content = paramBasePacket;
        }

        public ByteBuffer fillBuffer(SleepData.PacketHead paramPacketHead, ByteBuffer paramByteBuffer) {
            return paramByteBuffer;
        }

        public ByteBuffer parseBuffer(SleepData.PacketHead paramPacketHead, ByteBuffer paramByteBuffer) {
            return paramByteBuffer;
        }

        public String toString() {
            return "type:" + this.type;
        }
    }

    public static abstract class PacketHead {
        public static final byte VER = 0;
        private static byte mSenquence = 0;
        public byte senquence;
        public byte type;
        public byte version;

        public static byte getSenquence() {
            byte b = mSenquence;
            mSenquence = (byte) (b + 1);
            return b;
        }

        public abstract ByteBuffer fillBuffer(ByteBuffer paramByteBuffer);

        public abstract ByteBuffer parseBuffer(ByteBuffer paramByteBuffer);

        public String toString() {
            return "type:" + this.type + ",sec:" + this.senquence;
        }
    }

    public static class PacketType {
        public static final byte FA_ACK = 0;
        public static final byte FA_POST = 1;
        public static final byte FA_REQUEST = 2;
        public static final byte FA_RESPONSE = 3;
    }

    public static class UpdateDetail
            extends SleepData.BasePacket {
        public byte[] content;
        public short count;
        public int startIndex;

        public UpdateDetail() {
        }

        public UpdateDetail(int paramInt, short paramShort, byte[] paramArrayOfByte) {
            this.startIndex = paramInt;
            this.count = paramShort;
            this.content = paramArrayOfByte;
        }

        public ByteBuffer fillBuffer(ByteBuffer paramByteBuffer) {
            if (this.content != null) {
                paramByteBuffer.putInt(this.startIndex);
                paramByteBuffer.putShort(this.count);
                paramByteBuffer.put(this.content);
            }
            return paramByteBuffer;
        }

        public ByteBuffer parseBuffer(ByteBuffer paramByteBuffer) {
            this.startIndex = paramByteBuffer.getInt();
            this.count = paramByteBuffer.getShort();
            this.content = new byte[this.count & 0xFFFF];
            paramByteBuffer.get(this.content);
            return paramByteBuffer;
        }
    }

    public static abstract class UpdateSummary
            extends SleepData.BasePacket {
        public int binCrc32;
        public int desCrc32;
        public int length;

        public UpdateSummary() {
        }

        public UpdateSummary(int paramInt1, int paramInt2, int paramInt3) {
            this.length = paramInt1;
            this.desCrc32 = paramInt2;
            this.binCrc32 = paramInt3;
        }

        public abstract ByteBuffer fillBuffer(ByteBuffer paramByteBuffer);

        public abstract ByteBuffer parseBuffer(ByteBuffer paramByteBuffer);
    }

    public static class UpgradeStatus {
        public static final int FRAME_SIZE = 512;
        public static final int OK = 16;
        public static final int RECV_FAIL = 18;
        public static final int RECV_TIMEOUT = 17;
        public static final int SEND_FAIL = 32;
    }

}
