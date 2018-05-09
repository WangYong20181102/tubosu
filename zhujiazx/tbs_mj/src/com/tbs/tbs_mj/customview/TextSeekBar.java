/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package com.tbs.tbs_mj.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.tbs.tbs_mj.R;



/**
 * The RangeBar is a double-sided version of a {@link android.widget.SeekBar}
 * with discrete values. Whereas the thumb for the SeekBar can be dragged to any
 * position in the bar, the RangeBar only allows its thumbs to be dragged to
 * discrete positions (denoted by tick marks) in the bar. When released, a
 * RangeBar thumb will snap to the nearest tick mark.
 * <p>
 * Clients of the RangeBar can attach a
 * been moved.
 */
public class TextSeekBar extends BaseSeekBar {

    // Default values for variables
    private static final int DEFAULT_TICK_COUNT = 3;
    private static final int DEFAULT_THUMB_IMAGE_NORMAL = R.drawable.spot_spot;
    private static final int DEFAULT_THUMB_IMAGE_PRESSED = R.drawable.spot_on;

    private int mThumbImageNormal = DEFAULT_THUMB_IMAGE_NORMAL;
    private int mThumbImagePressed = DEFAULT_THUMB_IMAGE_PRESSED;
    // setTickCount only resets indices before a thumb has been pressed or a
    // setThumbIndices() is called, to correspond with intended usage
    private boolean mFirstSetTickCount = true;

    private TextThumb mLeftThumb;
    private TextThumb mRightThumb;
    private TextBar mBar;
    private ConnectingLine mConnectingLine;

    private String mCircleText;
    private int mLeftIndex = 0;

    // Constructors ////////////////////////////////////////////////////////////

    public TextSeekBar(Context context) {
        super(context);
    }

    public TextSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        rangeBarInit(context,attrs);
    }

    public TextSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        rangeBarInit(context,attrs);
    }

    // View Methods ////////////////////////////////////////////////////////////

    @Override
    public Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        final Bundle bundle = new Bundle();

        bundle.putInt("THUMB_IMAGE_NORMAL", mThumbImageNormal);
        bundle.putInt("THUMB_IMAGE_PRESSED", mThumbImagePressed);

        bundle.putInt("LEFT_INDEX", mLeftIndex);

        bundle.putBoolean("FIRST_SET_TICK_COUNT", mFirstSetTickCount);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {

            final Bundle bundle = (Bundle) state;

            mThumbImageNormal = bundle.getInt("THUMB_IMAGE_NORMAL");
            mThumbImagePressed = bundle.getInt("THUMB_IMAGE_PRESSED");

            mLeftIndex = bundle.getInt("LEFT_INDEX");
            mFirstSetTickCount = bundle.getBoolean("FIRST_SET_TICK_COUNT");

            setThumbIndices(mLeftIndex);

            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));

        } else {

            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        final Context ctx = getContext();

        // This is the initial point at which we know the size of the View.

        // Create the two thumb objects.
        final float yPos = h / 2f;
        mLeftThumb = new TextThumb(ctx,
                               yPos,
                               mThumbColorNormal,
                               mThumbColorPressed,
                               mThumbRadiusDP,
                               mThumbImageNormal,
                               mThumbImagePressed);
        mLeftThumb.setCircleText(this.mCircleText);
        mRightThumb = new TextThumb(ctx,
                yPos,
                mThumbColorNormal,
                mThumbColorPressed,
                mThumbRadiusDP,
                mThumbImageNormal,
                mThumbImagePressed);
        // Create the underlying bar.
        final float marginLeft = mLeftThumb.getHalfWidth();
        final float barLength = w - 2 * marginLeft;
        mBar = new TextBar(ctx, marginLeft, yPos, barLength, mTickCount, mTickHeightDP, mBarWeight, mBarColor);

        // Initialize thumbs to the desired indices
        mLeftThumb.setX(marginLeft + (mLeftIndex / (float) (mTickCount - 1)) * barLength);

        // Set the thumb indices.
        final int newLeftIndex = mBar.getNearestTickIndex(mLeftThumb);

        // Call the listener.
        if (newLeftIndex != mLeftIndex ) {

            mLeftIndex = newLeftIndex;

            if (mListener != null) {
                mListener.onIndexChangeListener(this, mLeftIndex, -1);
            }
        }

        // Create the line connecting the two thumbs.
        mConnectingLine = new ConnectingLine(ctx, yPos, mConnectingLineWeight, mConnectingLineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        mBar.draw(canvas);

        mConnectingLine.draw(canvas, mLeftThumb,mRightThumb);

        mLeftThumb.draw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // If this View is not enabled, don't allow for touch interactions.
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                onActionDown(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_MOVE:
                onActionMove(event.getX());
                this.getParent().requestDisallowInterceptTouchEvent(true);
                return true;

            default:
                return false;
        }
    }

    /**
     * Sets the number of ticks in the RangeBar.
     * 
     * @param tickCount Integer specifying the number of ticks.
     */
    public void setTickCount(int tickCount) {

        if (isValidTickCount(tickCount)) {
            mTickCount = tickCount;

            // Prevents resetting the indices when creating new activity, but
            // allows it on the first setting.
            if (mFirstSetTickCount) {
                mLeftIndex = 0;
                if (mListener != null) {
                    mListener.onIndexChangeListener(this, mLeftIndex, -1);
                }
            }
            if (indexOutOfRange(mLeftIndex)) {
                mLeftIndex = 0;

                if (mListener != null)
                    mListener.onIndexChangeListener(this, mLeftIndex, -1);
            }

            createBar();
            createThumbs();
        }
        else {
            throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
        }
    }



    /**
     * Sets the height of the ticks in the range bar.
     * 
     * @param tickHeight Float specifying the height of each tick mark in dp.
     */
    public void setTickHeight(float tickHeight) {

        mTickHeightDP = tickHeight;
        createBar();
    }

    /**
     * Set the weight of the bar line and the tick lines in the range bar.
     * 
     * @param barWeight Float specifying the weight of the bar and tick lines in
     *            px.
     */
    public void setBarWeight(float barWeight) {

        mBarWeight = barWeight;
        createBar();
    }


//    private void onStopTrackingTouch(SeekBar mSeekBar, TextView tvStage) {
//        // TODO Auto-generated method stub
//        int seekProgress = mSeekBar.getProgress();
//        if(seekProgress<13){
//            mSeekBar.setProgress(0);
////            tvStage.setTextColor();
//
//        }else if(seekProgress>=13 && seekProgress<38){
//            mSeekBar.setProgress(25);
//        }else if(seekProgress>=38 && seekProgress<63){
//            mSeekBar.setProgress(50);
//        }else if(seekProgress>=63 && seekProgress<88){
//            mSeekBar.setProgress(75);
//        }else if(seekProgress>=88){
//            mSeekBar.setProgress(100);
//        }
//    }


    /**
     * Set the color of the bar line and the tick lines in the range bar.
     * 
     * @param barColor Integer specifying the color of the bar line.
     */
    public void setBarColor(int barColor) {

        mBarColor = barColor;
        createBar();
    }

    /**
     * Set the weight of the connecting line between the thumbs.
     * 
     * @param connectingLineWeight Float specifying the weight of the connecting
     *            line.
     */
    public void setConnectingLineWeight(float connectingLineWeight) {

        mConnectingLineWeight = connectingLineWeight;
        createConnectingLine();
    }

    /**
     * Set the color of the connecting line between the thumbs.
     * 
     * @param connectingLineColor Integer specifying the color of the connecting
     *            line.
     */
    public void setConnectingLineColor(int connectingLineColor) {

        mConnectingLineColor = connectingLineColor;
        createConnectingLine();
    }

    /**
     * If this is set, the thumb images will be replaced with a circle of the
     * specified radius. Default width = 20dp.
     * 
     * @param thumbRadius Float specifying the radius of the thumbs to be drawn.
     */
    public void setThumbRadius(float thumbRadius) {

        mThumbRadiusDP = thumbRadius;
        createThumbs();
    }

    /**
     * Sets the normal thumb picture by taking in a reference ID to an image.
     * 
     */
    public void setThumbImageNormal(int thumbImageNormalID) {
        mThumbImageNormal = thumbImageNormalID;
        createThumbs();
    }

    /**
     * Sets the pressed thumb picture by taking in a reference ID to an image.
     * 
     */
    public void setThumbImagePressed(int thumbImagePressedID)
    {
        mThumbImagePressed = thumbImagePressedID;
        createThumbs();
    }

    /**
     * If this is set, the thumb images will be replaced with a circle. The
     * normal image will be of the specified color.
     * 
     * @param thumbColorNormal Integer specifying the normal color of the circle
     *            to be drawn.
     */
    public void setThumbColorNormal(int thumbColorNormal)
    {
        mThumbColorNormal = thumbColorNormal;
        createThumbs();
    }

    /**
     * Does all the functions of the constructor for RangeBar. Called by both
     * RangeBar constructors in lieu of copying the code for each constructor.
     *
     * @param context Context from the constructor.
     * @param attrs AttributeSet from the constructor.
     * @return none
     */
    @Override
    protected void rangeBarInit(Context context, AttributeSet attrs) {
        super.rangeBarInit(context,attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SeekBar, 0, 0);
        try {

            // Sets the values of the user-defined attributes based on the XML
            // attributes.
            final Integer tickCount = ta.getInteger(R.styleable.SeekBar_tickCount, DEFAULT_TICK_COUNT);

            if (isValidTickCount(tickCount)) {

                // Similar functions performed above in setTickCount; make sure
                // you know how they interact
                mTickCount = tickCount;
                mLeftIndex = 0;

                if (mListener != null) {
                    mListener.onIndexChangeListener(this, mLeftIndex, -1);
                }

            }
            mThumbColorNormal = ta.getColor(R.styleable.SeekBar_thumbColorNormal, DEFAULT_THUMB_COLOR_NORMAL);
            mThumbColorPressed = ta.getColor(R.styleable.SeekBar_thumbColorPressed,
                    DEFAULT_THUMB_COLOR_PRESSED);

        } finally {

            ta.recycle();
        }

    }

    /**
     * If this is set, the thumb images will be replaced with a circle. The
     * pressed image will be of the specified color.
     * 
     * @param thumbColorPressed Integer specifying the pressed color of the
     *            circle to be drawn.
     */
    public void setThumbColorPressed(int thumbColorPressed)
    {
        mThumbColorPressed = thumbColorPressed;
        createThumbs();
    }

    /**
     * Sets the location of each thumb according to the developer's choice.
     * Numbered from 0 to mTickCount - 1 from the left.
     * 
     * @param leftThumbIndex Integer specifying the index of the left thumb
     */
    public void setThumbIndices(int leftThumbIndex)
    {
        if (indexOutOfRange(leftThumbIndex)) {
        	
            throw new IllegalArgumentException("A thumb index is out of bounds. Check that it is between 0 and mTickCount - 1");
        
        } else {
        	
            if (mFirstSetTickCount)
                mFirstSetTickCount = false;

            mLeftIndex = leftThumbIndex;
            createThumbs();

            if (mListener != null) {
                mListener.onIndexChangeListener(this, mLeftIndex, -1);
            }
        }

        invalidate();
        requestLayout();
    }

    /**
     * Gets the index of the left-most thumb.
     * 
     * @return the 0-based index of the left thumb
     */
    public int getLeftIndex() {
        return mLeftIndex;
    }

    /**
     * Creates a new mBar
     * 
     */
    private void createBar() {
        mBar = new TextBar(getContext(),
                       getMarginLeft(),
                       getYPos(),
                       getBarLength(),
                       mTickCount,
                       mTickHeightDP,
                       mBarWeight,
                       mBarColor);
        invalidate();
    }

    /**
     * Creates a new ConnectingLine.
     */
    private void createConnectingLine() {

        mConnectingLine = new ConnectingLine(getContext(),
                                             getYPos(),
                                             mConnectingLineWeight,
                                             mConnectingLineColor);
        invalidate();
    }

    /**
     * Creates two new Thumbs.
     */
    private void createThumbs() {

        Context ctx = getContext();
        float yPos = getYPos();

        mLeftThumb = new TextThumb(ctx,
                               yPos,
                               mThumbColorNormal,
                               mThumbColorPressed,
                               mThumbRadiusDP,
                               mThumbImageNormal,
                               mThumbImagePressed);

        float marginLeft = getMarginLeft();
        float barLength = getBarLength();

        // Initialize thumbs to the desired indices
        mLeftThumb.setX(marginLeft + (mLeftIndex / (float) (mTickCount - 1)) * barLength);

        invalidate();
    }

    /**
     * Get marginLeft in each of the public attribute methods.
     * 
     * @return float marginLeft
     */
    private float getMarginLeft() {
        return ((mLeftThumb != null) ? mLeftThumb.getHalfWidth() : 0);
    }


    /**
     * Get barLength in each of the public attribute methods.
     *
     * @return float barLength
     */
    private float getBarLength() {
        return (getWidth() - 2 * getMarginLeft());
    }
    /**
     * Returns if either index is outside the range of the tickCount.
     *
     * @param leftThumbIndex Integer specifying the left thumb index.
     * @return boolean If the index is out of range.
     */
    private boolean indexOutOfRange(int leftThumbIndex) {
        return (leftThumbIndex < 0 || leftThumbIndex >= mTickCount);
    }
    /**
     * Handles a {@link MotionEvent#ACTION_DOWN} event.
     * 
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     */
    private void onActionDown(float x, float y) {

        if (!mLeftThumb.isPressed() && mLeftThumb.isInTargetZone(x, y)) {

            pressThumb(mLeftThumb);

        }
    }

    /**
     * Handles a {@link MotionEvent#ACTION_UP} or
     * {@link MotionEvent#ACTION_CANCEL} event.
     * 
     * @param x the x-coordinate of the up action
     * @param y the y-coordinate of the up action
     */
    private void onActionUp(float x, float y) {
//        if(){
//            mLeftThumb = FIXME
//        }

        if (mLeftThumb.isPressed()) {

            releaseThumb(mLeftThumb);

		} else {

            mLeftThumb.setX(x);
            releaseThumb(mLeftThumb);

	        // Get the updated nearest tick marks for each thumb.
	        final int newLeftIndex = mBar.getNearestTickIndex(mLeftThumb);

	        // If either of the indices have changed, update and call the listener.
	        if (newLeftIndex != mLeftIndex ) {

	            mLeftIndex = newLeftIndex;

	            if (mListener != null) {
	                mListener.onIndexChangeListener(this, mLeftIndex, -1);
	            }
	        }
		}
//        System.out.println("-------->>>" + mLeftIndex);
        onDragPositionListener.onDragPositionListener(mLeftIndex);

	}


    private OnDragPositionListener onDragPositionListener;
    public interface OnDragPositionListener{
        public void onDragPositionListener(int position);
    }
    public void setOnDragPositionListener(OnDragPositionListener onDragPositionListener) {
        this.onDragPositionListener = onDragPositionListener;
    }

    /**
     * Handles a {@link MotionEvent#ACTION_MOVE} event.
     * 
     * @param x the x-coordinate of the move event
     */
    private void onActionMove(float x) {

        // Move the pressed thumb to the new x-position.
        if (mLeftThumb.isPressed()) {
            moveThumb(mLeftThumb, x);
        }

        // Get the updated nearest tick marks for each thumb.
        final int newLeftIndex = mBar.getNearestTickIndex(mLeftThumb);

        // If either of the indices have changed, update and call the listener.
        if (newLeftIndex != mLeftIndex) {

            mLeftIndex = newLeftIndex;

            if (mListener != null) {
                mListener.onIndexChangeListener(this, mLeftIndex, -1);
            }
        }
    }

    /**
     * Set the thumb to be in the pressed state and calls invalidate() to redraw
     * the canvas to reflect the updated state.
     * 
     * @param thumb the thumb to press
     */
    private void pressThumb(TextThumb thumb) {
        if (mFirstSetTickCount == true)
            mFirstSetTickCount = false;
        thumb.press();
        invalidate();
    }

    /**
     * Set the thumb to be in the normal/un-pressed state and calls invalidate()
     * to redraw the canvas to reflect the updated state.
     * 
     * @param thumb the thumb to release
     */
    private void releaseThumb(TextThumb thumb) {

        final float nearestTickX = mBar.getNearestTickCoordinate(thumb);
        thumb.setX(nearestTickX);
        thumb.release();
        invalidate();
    }

    /**
     * Moves the thumb to the given x-coordinate.
     * 
     * @param thumb the thumb to move
     * @param x the x-coordinate to move the thumb to
     */
    private void moveThumb(TextThumb thumb, float x) {

        // If the user has moved their finger outside the range of the bar,
        // do not move the thumbs past the edge.
        if (x < mBar.getLeftX() || x > mBar.getRightX()) {
            // Do nothing.
        } else {
            thumb.setX(x);
            invalidate();
        }
    }
    public void setCircleText(String text){
        if(mLeftThumb!=null){
            mLeftThumb.setCircleText(text);
            invalidate();
        }
        this.mCircleText = text;
    }
    public void setCircleTextSize(float size){
        if(mLeftThumb!=null){
            mLeftThumb.setCircleTextSize(size);
        }
    }
}
