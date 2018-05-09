package com.tbs.tbs_mj.anim;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class ScaleAnimEffect {
	private long duration;
	private float fromXScale;
	private float fromYScale;
	private float toXScale;
	private float toYScale;

	public void setAttributs(float paramFloat1, float paramFloat2,
			float paramFloat3, float paramFloat4, long paramLong) {
		this.fromXScale = paramFloat1;
		this.fromYScale = paramFloat3;
		this.toXScale = paramFloat2;
		this.toYScale = paramFloat4;
		this.duration = paramLong;
	}

	public Animation createAnimation() {
		ScaleAnimation localScaleAnimation = new ScaleAnimation(
				this.fromXScale, this.toXScale, this.fromYScale, this.toYScale,
				Animation.RELATIVE_TO_SELF, 5F, Animation.RELATIVE_TO_SELF, 5F);
		localScaleAnimation.setFillAfter(true);
		localScaleAnimation.setInterpolator(new AccelerateInterpolator());
		localScaleAnimation.setDuration(this.duration);
		return localScaleAnimation;
	}

}
