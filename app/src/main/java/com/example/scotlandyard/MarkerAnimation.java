/*
Copyright 2013 Google Inc.
Licensed under Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0.html
*/

package com.example.scotlandyard;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * @author René
 * this class contains two animation-methods for the player-markers.
 * The code is based on an exaple by Google Inc.
 * License text is placed at the beginning of the file
 */
public class MarkerAnimation {

    private MarkerAnimation(){

    }

    private static final double EPSILON = 1e-5;

    /**
     * animates the given marker between its current position ans @finalPosition for the time span @duration
     * @param marker ...............marker on the map (contains current position)
     * @param finalPosition ........position to be reached at the end of the animation
     * @param latLngInterpolator ...an Interpolator to calculate the values during the animation
     * @param duration .............time the animation should last
     * @param icon .................marker icon during the animation
     */
    static void moveMarkerToTarget(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, float duration, int icon) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = duration;
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));

                if (t < 1) {
                    handler.postDelayed(this, 16);
                } else {
                    if (!marker.getPosition().equals(finalPosition)) {
                        marker.setPosition(finalPosition);
                    }
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.player));
                }
            }
        });
    }

    /**
     * animates the given marker between its current position ans @finalPosition for the time span @duration
     * @param marker ...............marker on the map (contains current position)
     * @param route ................intermediate points between current position and final position
     * @param finalPosition ........position to be reached at the end of the animation
     * @param latLngInterpolator ...an Interpolator to calculate the values during the animation
     * @param duration .............time the animation should last
     * @param icon .................marker icon during the animation
     */
    static void moveMarkerToTarget(final Marker marker, final ArrayList<LatLng> route, final ArrayList<Float> timeSlices, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, final float duration, int icon) {
        //final LatLng[] startPosition = {marker.getPosition()};
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = duration;
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;
            int i = 0;
            float elpasedTime = 0;
            LatLng next = route.get(i);
            LatLng current = marker.getPosition();

            @Override
            public void run() {
                elapsed = SystemClock.uptimeMillis() - start;
                if (i < timeSlices.size())
                    t = elapsed / timeSlices.get(i);
                else
                    t = elapsed / timeSlices.get(timeSlices.size() - 1);
                elpasedTime = elapsed / duration;
                v = interpolator.getInterpolation(t);
                if (closeEnough(marker, next)) {
                    marker.setPosition(next);
                    current = next;
                    i++;
                    if (i >= route.size()) {
                        next = finalPosition;
                        elpasedTime = 1f;
                    } else {
                        next = route.get(i);
                    }
                    Log.d("ROUTE_ANIMATION_NP", "" + i);
                }
                marker.setPosition(latLngInterpolator.interpolate(v, current, next));

                Log.d("ROUTE_ANIMATION", "" + marker.getPosition() + " --> " + next + "; deltaLat: " + (marker.getPosition().latitude - next.latitude) + "; deltaLng: " + (marker.getPosition().longitude - next.longitude));
                if (elpasedTime < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    if (i >= route.size()) {
                        if (!marker.getPosition().equals(finalPosition)) {
                            marker.setPosition(finalPosition);
                        }
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.player));
                    }
                }
            }
        });
    }

    private static boolean closeEnough(Marker marker, LatLng point) {
        LatLng current = marker.getPosition();
        return Math.abs(current.latitude - point.latitude) <= EPSILON && Math.abs(current.longitude - point.longitude) <= EPSILON;
    }
}