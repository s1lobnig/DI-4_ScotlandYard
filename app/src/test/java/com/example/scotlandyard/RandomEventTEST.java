package com.example.scotlandyard;

import com.example.scotlandyard.map.motions.RandomEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RandomEventTEST {
    private RandomEvent toTest;
    private static int iterations = 0;

    public RandomEventTEST(){
        toTest = new RandomEvent();
        this.iterations++;
    }



    @Test
    public void testRandomEventsIDSuccess(){
        int i;
        boolean equal = false;
        for(i = 0; i<=this.iterations; i++){
            toTest = new RandomEvent();
            if(toTest.getID() < 3)
                equal = true;

            Assert.assertEquals(equal, true);
            equal = false;
        }
    }

    @Test
    public void testRandomEventsRandomNumberSuccess(){
        int i = 0;
        boolean equal = false;
        for(i = 0; i<=this.iterations; i++){
            toTest = new RandomEvent();
            if(toTest.getSecID() < 4)
                equal = true;

            Assert.assertEquals(equal, true);
            equal = false;
        }
    }



}
