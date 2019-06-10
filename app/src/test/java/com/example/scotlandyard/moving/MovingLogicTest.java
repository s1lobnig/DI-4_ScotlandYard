package com.example.scotlandyard.moving;

import com.example.scotlandyard.map.Routes;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static com.example.scotlandyard.map.routetypes.BicycleRoutes.BICYCLE;
import static com.example.scotlandyard.map.routetypes.BusRoutes.BUS;
import static com.example.scotlandyard.map.routetypes.FootRoutes.BY_FOOT;
import static com.example.scotlandyard.map.routetypes.TaxiRoutes.TAXI_DRAGAN;

@RunWith(Parameterized.class)
public class MovingLogicTest {
    @Parameterized.Parameters(name = "{index}: ")
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                /*

                */
                {
                    
                },
        });
    }

}
