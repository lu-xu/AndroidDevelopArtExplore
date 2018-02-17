package com.xl.androiddevelopartexplore;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    private static final String TAG = "ExampleUnitTest";

    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
        //2014/10/01 00:00:00
        //1412092800
        //2018/3/19 00:00:00
        //1521388800
//        System.out.println("spacing days is " + culculateDate(1412092800, System.currentTimeMillis() / 1000));
        System.out.println("spacing days is " + culculateDate(1412092800, 1521388800));
    }

    private long culculateDate(long before, long after) {
        long spacing = after - before;
        return spacing / 86400;
    }
}