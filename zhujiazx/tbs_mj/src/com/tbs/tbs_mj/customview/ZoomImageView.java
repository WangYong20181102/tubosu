package com.tbs.tbs_mj.customview;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class ZoomImageView extends View implements Observer {

	private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

	private final Rect mRectSrc = new Rect();

	private final Rect mRectDst = new Rect();

	private final AspectQuotient mAspectQuotient = new AspectQuotient();

	private Bitmap mBitmap;

	private ZoomState mState;

	private BasicZoomControl mZoomControl;
	private BasicZoomListener mZoomListener;

	public ZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mZoomControl = new BasicZoomControl();

		mZoomListener = new BasicZoomListener();
		mZoomListener.setZoomControl(mZoomControl);

		setZoomState(mZoomControl.getZoomState());

		setOnTouchListener(mZoomListener);

		mZoomControl.setAspectQuotient(getAspectQuotient());
	}

	public void zoomImage(float f, float x, float y) {
		mZoomControl.zoom(f, x, y);
	}

	public void setImage(Bitmap bitmap) {
		mBitmap = bitmap;

		mAspectQuotient.updateAspectQuotient(getWidth(), getHeight(),
				mBitmap.getWidth(), mBitmap.getHeight());
		mAspectQuotient.notifyObservers();

		invalidate();
	}

	private void setZoomState(ZoomState state) {
		if (mState != null) {
			mState.deleteObserver(this);
		}

		mState = state;
		mState.addObserver(this);

		invalidate();
	}

	private AspectQuotient getAspectQuotient() {
		return mAspectQuotient;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null && mState != null) {

			Log.d("ZoomImageView", "OnDraw");

			final float aspectQuotient = mAspectQuotient.get();

			final int viewWidth = getWidth();
			final int viewHeight = getHeight();
			final int bitmapWidth = mBitmap.getWidth();
			final int bitmapHeight = mBitmap.getHeight();

			Log.d("ZoomImageView", "viewWidth = " + viewWidth);
			Log.d("ZoomImageView", "viewHeight = " + viewHeight);
			Log.d("ZoomImageView", "bitmapWidth = " + bitmapWidth);
			Log.d("ZoomImageView", "bitmapHeight = " + bitmapHeight);

			final float panX = mState.getPanX();
			final float panY = mState.getPanY();
			final float zoomX = mState.getZoomX(aspectQuotient) * viewWidth
					/ bitmapWidth;
			final float zoomY = mState.getZoomY(aspectQuotient) * viewHeight
					/ bitmapHeight;

			mRectSrc.left = (int) (panX * bitmapWidth - viewWidth / (zoomX * 2));
			mRectSrc.top = (int) (panY * bitmapHeight - viewHeight
					/ (zoomY * 2));
			mRectSrc.right = (int) (mRectSrc.left + viewWidth / zoomX);
			mRectSrc.bottom = (int) (mRectSrc.top + viewHeight / zoomY);
			mRectDst.left = 0;
			mRectDst.top = 0;
			mRectDst.right = getWidth();
			mRectDst.bottom = getHeight();
			if (mRectSrc.left < 0) {
				mRectDst.left += -mRectSrc.left * zoomX;
				mRectSrc.left = 0;
			}
			if (mRectSrc.right > bitmapWidth) {
				mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
				mRectSrc.right = bitmapWidth;
			}
			if (mRectSrc.top < 0) {
				mRectDst.top += -mRectSrc.top * zoomY;
				mRectSrc.top = 0;
			}
			if (mRectSrc.bottom > bitmapHeight) {
				mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
				mRectSrc.bottom = bitmapHeight;
			}

			mRectDst.left = 0;
			mRectDst.top = 0;
			mRectDst.right = viewWidth;
			mRectDst.bottom = viewHeight;
			canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		mAspectQuotient.updateAspectQuotient(right - left, bottom - top,
				mBitmap.getWidth(), mBitmap.getHeight());
		mAspectQuotient.notifyObservers();
	}

	@Override
	public void update(Observable observable, Object data) {
		invalidate();
	}

	private class BasicZoomListener implements View.OnTouchListener {

		private BasicZoomControl mZoomControl;

		private float mFirstX = -1;
		private float mFirstY = -1;
		private float mSecondX = -1;
		private float mSecondY = -1;

		private int mOldCounts = 0;

		public void setZoomControl(BasicZoomControl control) {
			mZoomControl = control;
		}

		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mOldCounts = 1;
				mFirstX = event.getX();
				mFirstY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE: {
				float fFirstX = event.getX();
				float fFirstY = event.getY();

				int nCounts = event.getPointerCount();

				if (1 == nCounts) {
					mOldCounts = 1;
					float dx = (fFirstX - mFirstX) / v.getWidth();
					float dy = (fFirstY - mFirstY) / v.getHeight();
					mZoomControl.pan(-dx, -dy);
				} else if (1 == mOldCounts) {
					mSecondX = event.getX(event.getPointerId(nCounts - 1));
					mSecondY = event.getY(event.getPointerId(nCounts - 1));
					mOldCounts = nCounts;
				} else {
					float fSecondX = event
							.getX(event.getPointerId(nCounts - 1));
					float fSecondY = event
							.getY(event.getPointerId(nCounts - 1));

					double nLengthOld = getLength(mFirstX, mFirstY, mSecondX,
							mSecondY);
					double nLengthNow = getLength(fFirstX, fFirstY, fSecondX,
							fSecondY);

					float d = (float) ((nLengthNow - nLengthOld) / v.getWidth());

					mZoomControl.zoom((float) Math.pow(20, d),
							((fFirstX + fSecondX) / 2 / v.getWidth()),
							((fFirstY + fSecondY) / 2 / v.getHeight()));

					mSecondX = fSecondX;
					mSecondY = fSecondY;
				}
				mFirstX = fFirstX;
				mFirstY = fFirstY;

				break;
			}

			}

			return true;
		}

		private double getLength(float x1, float y1, float x2, float y2) {
			return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
		}
	}

	private class BasicZoomControl implements Observer {

		private static final float MIN_ZOOM = 1;

		private static final float MAX_ZOOM = 16;

		private final ZoomState mState = new ZoomState();

		private AspectQuotient mAspectQuotient;

		public void setAspectQuotient(AspectQuotient aspectQuotient) {
			if (mAspectQuotient != null) {
				mAspectQuotient.deleteObserver(this);
			}

			mAspectQuotient = aspectQuotient;
			mAspectQuotient.addObserver(this);
		}

		public ZoomState getZoomState() {
			return mState;
		}

		public void zoom(float f, float x, float y) {

			final float aspectQuotient = mAspectQuotient.get();

			final float prevZoomX = mState.getZoomX(aspectQuotient);
			final float prevZoomY = mState.getZoomY(aspectQuotient);

			mState.setZoom(mState.getZoom() * f);
			limitZoom();

			final float newZoomX = mState.getZoomX(aspectQuotient);
			final float newZoomY = mState.getZoomY(aspectQuotient);

			mState.setPanX(mState.getPanX() + (x - .5f)
					* (1f / prevZoomX - 1f / newZoomX));
			mState.setPanY(mState.getPanY() + (y - .5f)
					* (1f / prevZoomY - 1f / newZoomY));

			limitPan();

			mState.notifyObservers();
		}

		public void pan(float dx, float dy) {
			final float aspectQuotient = mAspectQuotient.get();

			mState.setPanX(mState.getPanX() + dx
					/ mState.getZoomX(aspectQuotient));
			mState.setPanY(mState.getPanY() + dy
					/ mState.getZoomY(aspectQuotient));

			limitPan();

			mState.notifyObservers();
		}

		private float getMaxPanDelta(float zoom) {
			return Math.max(0f, .5f * ((zoom - 1) / zoom));
		}

		private void limitZoom() {
			if (mState.getZoom() < MIN_ZOOM) {
				mState.setZoom(MIN_ZOOM);
			} else if (mState.getZoom() > MAX_ZOOM) {
				mState.setZoom(MAX_ZOOM);
			}
		}

		private void limitPan() {
			final float aspectQuotient = mAspectQuotient.get();

			final float zoomX = mState.getZoomX(aspectQuotient);
			final float zoomY = mState.getZoomY(aspectQuotient);

			final float panMinX = .5f - getMaxPanDelta(zoomX);
			final float panMaxX = .5f + getMaxPanDelta(zoomX);
			final float panMinY = .5f - getMaxPanDelta(zoomY);
			final float panMaxY = .5f + getMaxPanDelta(zoomY);

			if (mState.getPanX() < panMinX) {
				mState.setPanX(panMinX);
			}
			if (mState.getPanX() > panMaxX) {
				mState.setPanX(panMaxX);
			}
			if (mState.getPanY() < panMinY) {
				mState.setPanY(panMinY);
			}
			if (mState.getPanY() > panMaxY) {
				mState.setPanY(panMaxY);
			}
		}

		public void update(Observable observable, Object data) {
			limitZoom();
			limitPan();
		}
	}

	private class AspectQuotient extends Observable {

		private float mAspectQuotient;

		public float get() {
			return mAspectQuotient;
		}

		public void updateAspectQuotient(float viewWidth, float viewHeight,
				float contentWidth, float contentHeight) {
			final float aspectQuotient = (contentWidth / contentHeight)
					/ (viewWidth / viewHeight);

			if (aspectQuotient != mAspectQuotient) {
				mAspectQuotient = aspectQuotient;
				setChanged();
			}
		}
	}

	private class ZoomState extends Observable {
		private float mZoom;

		private float mPanX;

		private float mPanY;

		public float getPanX() {
			return mPanX;
		}

		public float getPanY() {
			return mPanY;
		}

		public float getZoom() {
			return mZoom;
		}

		public float getZoomX(float aspectQuotient) {
			return Math.min(mZoom, mZoom * aspectQuotient);
		}

		public float getZoomY(float aspectQuotient) {
			return Math.min(mZoom, mZoom / aspectQuotient);
		}

		public void setPanX(float panX) {
			if (panX != mPanX) {
				mPanX = panX;
				setChanged();
			}
		}

		public void setPanY(float panY) {
			if (panY != mPanY) {
				mPanY = panY;
				setChanged();
			}
		}

		public void setZoom(float zoom) {
			if (zoom != mZoom) {
				mZoom = zoom;
				setChanged();
			}
		}
	}
}
