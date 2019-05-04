package com.example.scotlandyard.TestMotions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestMotions {
    @Parameterized.Parameters
    public static Iterable<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {/*mrx? - from point - to point - valid? - do randomly maybe...*/}
        });
    }
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}