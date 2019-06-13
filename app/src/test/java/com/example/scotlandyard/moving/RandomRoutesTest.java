package com.example.scotlandyard.moving;

import com.example.scotlandyard.map.Route;
import com.example.scotlandyard.map.Routes;
import com.example.scotlandyard.map.ValidatedRoute;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static com.example.scotlandyard.map.routetypes.BicycleRoutes.getBicycle;
import static com.example.scotlandyard.map.routetypes.FootRoutes.getByFoot;

@RunWith(Parameterized.class)
public class RandomRoutesTest {

    private static final Route[] BY_FOOT = getByFoot();
    private static final Route[] BICYCLE = getBicycle();

    @Parameterized.Parameters(name = "{index}: Bot at {0}, other players at {1}")
    public static Iterable data() {
        return Arrays.asList(new Object[][]{
                /*
                bot position
                player positions
                possible resulting routes
                */
                {1, 4, Arrays.asList(new Route[]{BY_FOOT[0]})},
                {24, 27, Arrays.asList(new Route[]{BY_FOOT[17], BY_FOOT[20], BY_FOOT[25], BY_FOOT[27]})},
                {1, 2, Arrays.asList(new Route[]{BICYCLE[0]})},
        });
    }

    private int currentPosition;
    private int notToInclude;
    private List<Route> possibleResults;
    private ValidatedRoute result;

    public RandomRoutesTest(int currentPosition, int notToInclude, List<Route> possibleResults) {
        this.currentPosition = currentPosition;
        this.notToInclude = notToInclude;
        this.possibleResults = possibleResults;
    }

    @Before
    public void setUp() {
        result = Routes.getRandomRoute(currentPosition, notToInclude);
    }

    @Test
    public void testIfInPossibleRouteSet() {
        Assert.assertTrue(possibleResults.contains(result.getRoute()));
    }

    @After
    public void tearDown() {
        result = null;
    }
}
