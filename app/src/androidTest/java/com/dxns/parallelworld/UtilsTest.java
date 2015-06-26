package com.dxns.parallelworld;

import android.test.InstrumentationTestCase;

import com.dxns.parallelworld.util.MD5Utils;

/**
 * @author kingty
 * @title UtilsTest
 * @description
 * @modifier
 * @date
 * @since 15/6/26 上午10:56
 */
public class UtilsTest extends InstrumentationTestCase {

    public void testMd5(){
        assertEquals(MD5Utils.generate("sss"),"492d8954e89968f703292937511991c76f25678e96504774");

    }

}
