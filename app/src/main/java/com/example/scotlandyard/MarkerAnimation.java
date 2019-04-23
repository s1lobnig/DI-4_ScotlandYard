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

public class MarkerAnimation {
    private static final double EPSILON = 1e-5;

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
                    t = elapsed / timeSlices.get(timeSlices.size()-1);
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
        if (Math.abs(current.latitude - point.latitude) <= EPSILON && Math.abs(current.longitude - point.longitude) <= EPSILON)
            return true;
        return false;
    }
}