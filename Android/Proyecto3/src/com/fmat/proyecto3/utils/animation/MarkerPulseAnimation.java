package com.fmat.proyecto3.utils.animation;

import java.lang.ref.WeakReference;

import android.graphics.Color;
import android.view.animation.Animation;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.ValueAnimator;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Clase que anima un punto dado en el mapa
 * @author Irving
 *
 */
public class MarkerPulseAnimation implements ValueAnimator.AnimatorListener,
		ValueAnimator.AnimatorUpdateListener {

	public enum StatusColor {
		RED, BLUE
	};

	private final WeakReference<GoogleMap> mapRef;

	private CameraPosition cameraPosition;
	private CameraUpdate camUpdate;
	private ValueAnimator animator;

	// private AnimatedTransitionState state;

	private float animatedFraction;
	private Circle circle;
	private LatLng point;
	private Double radius;

	/**
	 * Constructor, recibe el mapa sobre el cual dibujar
	 * @param map
	 */
	public MarkerPulseAnimation(GoogleMap map) {

		mapRef = new WeakReference<GoogleMap>(map);

	}

	/**
	 * Anima un punto latitud/longitud hasta un radio dado
	 * @param point
	 * @param radius
	 */
	public void animate(LatLng point, double radius) {

		this.point = point;
		this.radius = radius;

		GoogleMap map = mapRef.get();

		if (map != null && point != null) {
			circle = map.addCircle(new CircleOptions().center(point)
					.strokeColor(Color.RED));

			// Animate Camera to Marker
			cameraPosition = CameraPosition.builder().target(point).zoom(18)
					.build();

			camUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
			map.animateCamera(camUpdate);

		}

		animator = ValueAnimator.ofFloat(0f, 1f);
		animator.setDuration(5000);

		animator.setRepeatMode(-1);
		animator.setRepeatCount(Animation.INFINITE);

		animator.addListener(this);
		animator.addUpdateListener(this);

		animator.start();

	}

	/**
	 * Cance la animacion
	 */
	public void cancel() {

		if (animator != null)
			animator.cancel();

	}

	/**
	 * Coloca el color del circulo dibujado
	 * @param color
	 */
	public void setColor(int color) {

//		int color = status == StatusColor.BLUE ? Color.BLUE : Color.RED;
		
		if(circle.getStrokeColor() != color){
			circle.setStrokeColor(color);
		}

	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		// TODO Auto-generated method stub

		animatedFraction = animation.getAnimatedFraction();
		circle.setRadius(animatedFraction * radius);
		circle.setStrokeWidth(1 + animatedFraction * 7);

	}

	@Override
	public void onAnimationStart(Animator animation) {

		// map.add

	}

	@Override
	public void onAnimationEnd(Animator animation) {

		GoogleMap map = mapRef.get();

		if (map != null)
			map.clear();

	}

	@Override
	public void onAnimationCancel(Animator animation) {

	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub

	}

}
