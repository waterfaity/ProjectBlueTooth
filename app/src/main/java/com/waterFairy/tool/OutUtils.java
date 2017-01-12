package com.waterfairy.tool;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Message;
import android.util.Log;

import jp.co.omron.healthcare.oc.device.OCLCommunicationLog;
import jp.co.omron.healthcare.oc.device.ohq.OHQWlCommand;

/**
 * Created by water_fairy on 2016/12/14.
 */

public class OutUtils {
    public static final String HEAD = "omron";
    public static final String WRITE = HEAD + "_xu_write";
    public static final String WRITE_B = HEAD + "_xu_write_b";
    public static final String READ = HEAD + "_xu_read";

    public static void printWrite(byte[] bytes) {
        printBYTE(WRITE, bytes);
    }

    public static void printWrite(BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) return;
        printUUID(WRITE, characteristic);
        printBYTE(WRITE, characteristic.getValue());
    }

    public static void printWriteB(BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) return;
        printUUID(WRITE_B, characteristic);
        printBYTE(WRITE_B, characteristic.getValue());
    }

    public static void printRead(byte[] bytes) {
        printBYTE(READ, bytes);
    }

    public static void printRead(BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) return;
        printUUID(READ, characteristic);
        printBYTE(READ, characteristic.getValue());
    }

    private static void printBYTE(String type, byte[] bytes) {
        if (bytes == null) {
            return;
        }
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        Log.i(type, "printBYTE: " + sb.toString());

    }

    private static void printUUID(String type, BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) {
            return;
        }
        Log.i(type, "printUUID: " + characteristic.getUuid().toString());
    }

    public static void operateCommand(OHQWlCommand ohqWlCommand) {
        if (ohqWlCommand == null) {
            return;
        }
        Log.i(HEAD + "_xu_write_command", "operateCommand: ");
        byte[] reqCommandPacketData = ohqWlCommand.getReqCommandPacketData();
        int wlCommandRetryNum = ohqWlCommand.getWlCommandRetryNum();
        printBYTE(HEAD + "_xu_write_command_byte", reqCommandPacketData);
        Log.i(HEAD + "_xu_write_num", "" + wlCommandRetryNum);
    }

    public static void printByte(byte aByte) {
        Log.i(HEAD + "_xu_byte", "printByte: " + Integer.toHexString(0xFF & aByte));
    }

    public static void printBoolean(boolean aBel) {
        Log.i(HEAD + "_xu_boolean", "printBoolean: " + aBel);

    }

    public static void printInt(int aInt) {
        Log.i(HEAD + "_xu_int", "printInt: " + aInt);

    }

    private static void printRun(int run) {
        Log.i(HEAD + "_xu_run", "printRun: " + run);
    }

    public static void printString(String string) {
        Log.i(HEAD + "_xu_string", "printString: "+string);
    }

    public static void printMsg(Message message) {
        if (message.what == 0x03) {
            try {
                OCLCommunicationLog obj = (OCLCommunicationLog) message.obj;
                if (obj != null) {
                    Log.i(HEAD + "_xu", "printWhat: " + message.what);
                    Log.i(HEAD + "_xu", "printMsg:getCommunicationStartTime " + obj.getCommunicationStartTime());
                    Log.i(HEAD + "_xu", "printMsg:getLogAcquisitionTime " + obj.getLogAcquisitionTime());
                    Log.i(HEAD + "_xu", "printMsg:getUserId " + obj.getUserId());
                    printRead(obj.getLog());
                    Log.i(HEAD + "_xu", "printMsg: " + message.what);
                }
            } catch (Exception e) {
                Log.i(HEAD + "_xu", "printMsg: 错误");
            }
        }
    }

    public static void printRun1() {
        printRun(1);
    }

    public static void printRun2() {
        printRun(2);
    }

    public static void printRun3() {
        printRun(3);
    }

    public static void printRun4() {
        printRun(4);
    }

    public static void printRun5() {
        printRun(5);
    }

    public static void printRun6() {
        printRun(6);
    }

    public static void printRun7() {
        printRun(7);
    }

    public static void printRun8() {
        printRun(8);
    }

    public static void printRun9() {
        printRun(9);
    }

    public static void printRun10() {
        printRun(10);
    }

    public static void printRun11() {
        printRun(11);
    }

    public static void printRun12() {
        printRun(12);
    }

    public static void printRun13() {
        printRun(13);
    }

    public static void printRun14() {
        printRun(14);
    }

    public static void printRun15() {
        printRun(15);
    }

    public static void printRun16() {
        printRun(16);
    }

    public static void printRun17() {
        printRun(17);
    }

    public static void printRun18() {
        printRun(18);
    }

    public static void printRun19() {
        printRun(19);
    }

    public static void printRun20() {
        printRun(20);
    }

    public static void printRun21() {
        printRun(21);
    }

    public static void printRun22() {
        printRun(22);
    }

    public static void printRun23() {
        printRun(23);
    }

    public static void printRun24() {
        printRun(24);
    }

    public static void printRun25() {
        printRun(25);
    }

    public static void printRun26() {
        printRun(26);
    }

    public static void printRun27() {
        printRun(27);
    }

    public static void printRun28() {
        printRun(28);
    }

    public static void printRun29() {
        printRun(29);
    }

    public static void printRun30() {
        printRun(30);
    }

    public static void printRun31() {
        printRun(31);
    }

    public static void printRun32() {
        printRun(32);
    }

    public static void printRun33() {
        printRun(33);
    }

    public static void printRun34() {
        printRun(34);
    }

    public static void printRun35() {
        printRun(35);
    }

    public static void printRun36() {
        printRun(36);
    }

    public static void printRun37() {
        printRun(37);
    }

    public static void printRun38() {
        printRun(38);
    }

    public static void printRun39() {
        printRun(39);
    }

    public static void printRun41() {
        printRun(41);
    }

    public static void printRun42() {
        printRun(42);
    }

    public static void printRun43() {
        printRun(43);
    }

    public static void printRun44() {
        printRun(44);
    }

    public static void printRun45() {
        printRun(45);
    }

    public static void printRun46() {
        printRun(46);
    }

    public static void printRun47() {
        printRun(47);
    }

    public static void printRun48() {
        printRun(48);
    }

    public static void printRun49() {
        printRun(49);
    }

    public static void printRun50() {
        printRun(50);
    }

    public static void printRun71() {
        printRun(71);
    }

    public static void printRun70() {
        printRun(70);
    }

    public static void printRun68() {
        printRun(68);
    }

    public static void printRun67() {
        printRun(67);
    }

    public static void printRun66() {
        printRun(66);
    }

    public static void printRun65() {
        printRun(65);
    }

    public static void printRun64() {
        printRun(64);
    }

    public static void printRun63() {
        printRun(63);
    }

    public static void printRun62() {
        printRun(62);
    }

    public static void printRun61() {
        printRun(61);
    }

    public static void printRun60() {
        printRun(60);
    }

    public static void printRun59() {
        printRun(59);
    }

    public static void printRun58() {
        printRun(58);
    }

    public static void printRun57() {
        printRun(57);
    }

    public static void printRun56() {
        printRun(56);
    }

    public static void printRun55() {
        printRun(55);
    }

    public static void printRun54() {
        printRun(54);
    }

    public static void printRun53() {
        printRun(53);
    }

    public static void printRun52() {
        printRun(52);
    }

    public static void printRun51() {
        printRun(51);
    }


    public static void printWrite1(byte[] bytes) {
        printBYTE(WRITE + 1, bytes);
    }

    public static void printWrite2(byte[] bytes) {
        printBYTE(WRITE + 2, bytes);
    }

    public static void printWrite3(byte[] bytes) {
        printBYTE(WRITE + 3, bytes);
    }

    public static void printWrite4(byte[] bytes) {
        printBYTE(WRITE + 4, bytes);
    }

    public static void printWrite5(byte[] bytes) {
        printBYTE(WRITE + 5, bytes);
    }

    public static void printWrite6(byte[] bytes) {
        printBYTE(WRITE + 6, bytes);
    }

    public static void printWrite7(byte[] bytes) {
        printBYTE(WRITE + 7, bytes);
    }

    public static void printWrite8(byte[] bytes) {
        printBYTE(WRITE + 8, bytes);
    }

    public static void printWrite9(byte[] bytes) {
        printBYTE(WRITE + 9, bytes);
    }

    public static void printWrite10(byte[] bytes) {
        printBYTE(WRITE + 10, bytes);
    }

    public static void printWrite11(byte[] bytes) {
        printBYTE(WRITE + 11, bytes);
    }

    public static void printWrite12(byte[] bytes) {
        printBYTE(WRITE + 12, bytes);
    }

    public static void printWrite13(byte[] bytes) {
        printBYTE(WRITE + 13, bytes);
    }

    public static void printWrite14(byte[] bytes) {
        printBYTE(WRITE + 14, bytes);
    }

    public static void printWrite15(byte[] bytes) {
        printBYTE(WRITE + 15, bytes);
    }

    public static void printWrite16(byte[] bytes) {
        printBYTE(WRITE + 16, bytes);
    }

    public static void printWrite17(byte[] bytes) {
        printBYTE(WRITE + 17, bytes);
    }

    public static void printWrite18(byte[] bytes) {
        printBYTE(WRITE + 18, bytes);
    }

    public static void printWrite19(byte[] bytes) {
        printBYTE(WRITE + 19, bytes);
    }

    public static void printWrite20(byte[] bytes) {
        printBYTE(WRITE + 20, bytes);
    }

    public static void printWrite21(byte[] bytes) {
        printBYTE(WRITE + 21, bytes);
    }

    public static void printWrite22(byte[] bytes) {
        printBYTE(WRITE + 22, bytes);
    }

    public static void printWrite23(byte[] bytes) {
        printBYTE(WRITE + 23, bytes);
    }


}
