package com.example.scotlandyard.map.motions;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerMotion implements Runnable {

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
    private int finalIcon;
    private boolean[] markerVisibilities;

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
            if (nextMotion != null) {
                marker.setVisible(markerVisibilities[0]);
                nextMotion.setMarker(marker);
                nextMotion.setStart();
                handler.post(nextMotion);
            } else {
                marker.setIcon(BitmapDescriptorFactory.fromResource(finalIcon));
                marker.setVisible(markerVisibilities[1]);
            }
        }
    }

    public void setMarkerVisibilities(boolean[] markerVisibilities) {
        this.markerVisibilities = markerVisibilities;
    }
}