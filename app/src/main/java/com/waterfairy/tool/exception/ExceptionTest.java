package com.waterfairy.tool.exception;

/**
 * Created by water_fairy on 2017/1/6.
 */

public class ExceptionTest {
    public static void test() {

        Integer integer = null;
        if (integer == null) throw new NullPointerException("空指针");
    }

}
