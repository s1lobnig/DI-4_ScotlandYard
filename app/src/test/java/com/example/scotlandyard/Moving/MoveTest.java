package com.example.scotlandyard.Moving;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class MoveTest {
    @Parameterized.Parameters
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                {}
        });
    }

    @Before
    public void setUp() {
    }

    @Test
    public void test1() {
    }

    @After
    public void tearDown() {
    }
}
