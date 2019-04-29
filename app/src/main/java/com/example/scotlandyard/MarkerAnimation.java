/*
Copyright 2013 Google Inc.
Licensed under Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0.html
*/

package com.example.scotlandyard;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * @author René
 * this class contains two animation-methods for the marker-markers.
 * The code is based on an exaple by Google Inc.
 * License text is placed at the beginning of the file
 */
public class MarkerAnimation {

    private MarkerAnimation() {

    }


    /**
     * animates the given marker between its current position ans @finalPosition for the time span @duration
     *
     * @param marker             ...............marker on the map (contains current position)
     * @param finalPosition      ........position to be reached at the end of the animation
     * @param latLngInterpolator ...an Interpolator to calculate the values during the animation
     * @param duration           .............time the animation should last
     * @param icon               .................marker icon during the animation
     */
    static void moveMarkerToTarget(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, float duration, int icon, final int finalIcon) {
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
                    marker.setIcon(BitmapDescriptorFactory.fromResource(finalIcon));
                }
            }
        });
    }

    /**
     * animates the given marker between its current position ans @finalPosition for the time span @duration
     *
     * @param marker             ...............marker on the map (contains current position)
     * @param route              ................intermediate points between current position and final position
     * @param finalPosition      ........position to be reached at the end of the animation
     * @param latLngInterpolator ...an Interpolator to calculate the values during the animation
     * @param duration           .............time the animation should last
     * @param icon               .................marker icon during the animation
     */
    static void moveMarkerToTarget(final Marker marker, final ArrayList<LatLng> route, final ArrayList<Float> timeSlices, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, int icon, boolean randEvent, Context context, int finalIcon) {
        final Handler handler = new Handler();
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon));
        ArrayList<MarkerMotion> motions = new ArrayList<>();
        for (int i = 0; i < timeSlices.size(); i++) {
            LatLng next;
            if (i < route.size())
                next = route.get(i);
            else
                next = finalPosition;

            MarkerMotion motion;
            if (i == timeSlices.size() - 1)
                motion = new MarkerMotion(marker, next, latLngInterpolator, timeSlices.get(i), finalIcon);
            else
                motion = new MarkerMotion(marker, next, latLngInterpolator, timeSlices.get(i), icon);
            motion.setHandler(handler);
            // if random event "GoBack", then define where to show the toast
            if (randEvent && i == route.size() / 2) {
                motion.setShowToast(true, context);
            }
            motions.add(motion);
        }
        int i = 0;
        for (MarkerMotion motion : motions) {
            if (i < motions.size() - 1) {
                motion.setNextMotion(motions.get(i + 1));
            }
            i++;
        }
        handler.post(motions.get(0));
    }
}

class MarkerMotion implements Runnable {

    private Marker marker;
    private LatLng nextPoint;
    private LatLngInterpolator latLngInterpolator;
    private float duration;
    private long elapsed;
    private long start;
    private float t;
    private Interpolator interpolator;
    private MarkerMotion nextMotion;
    private Handler handler;
    private LatLng current;
    private boolean showToast;
    private Context toastContext;
    private int finalIcon;

    public MarkerMotion(Marker marker, LatLng nextPoint, LatLngInterpolator latLngInterpolator, float duration, int finalIcon) {
        this.marker = marker;
        this.nextPoint = nextPoint;
        this.latLngInterpolator = latLngInterpolator;
        this.duration = duration;
        this.elapsed = 0l;
        this.t = 0f;
        this.interpolator = new AccelerateDecelerateInterpolator();
        this.nextMotion = null;
        this.current = marker.getPosition();
        this.start = SystemClock.uptimeMillis();
        this.showToast = false;
        this.toastContext = null;
        this.finalIcon = finalIcon;
    }

    public void setNextMotion(MarkerMotion nextMotion) {
        this.nextMotion = nextMotion;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
        this.current = marker.getPosition();
    }

    public void setStart() {
        this.start = SystemClock.uptimeMillis();
    }

    public void setShowToast(boolean showToast, Context context) {
        this.showToast = showToast;
        this.toastContext = context;
    }

    @Override
    public void run() {
        elapsed = SystemClock.uptimeMillis() - start;
        t = elapsed / duration;
        float v = interpolator.getInterpolation(t);
        marker.setPosition(latLngInterpolator.interpolate(v, current, nextPoint));
        if (t < 1.0) {
            handler.postDelayed(this, 16);
        } else {
            marker.setPosition(nextPoint);
            if (showToast) {
                Toast.makeText(toastContext, R.string.randEventGoBack, Toast.LENGTH_LONG).show();
            }
            if (nextMotion != null) {
                nextMotion.setMarker(marker);
                nextMotion.setStart();
                handler.post(nextMotion);
            } else {
                marker.setIcon(BitmapDescriptorFactory.fromResource(finalIcon));
            }
        }
    }
}