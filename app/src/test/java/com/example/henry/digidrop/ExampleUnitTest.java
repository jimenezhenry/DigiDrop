package com.example.henry.digidrop;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final String TAG = ExampleUnitTest.class.getSimpleName();

    Context context;

    @Before
    public void setUp() {
        context = new MockContext();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


}