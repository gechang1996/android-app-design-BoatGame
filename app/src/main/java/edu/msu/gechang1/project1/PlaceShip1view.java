package edu.msu.gechang1.project1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import pl.polidea.view.ZoomView;


/**
 * Custom view class for our Puzzle.
 */
public class PlaceShip1view extends View {


    private ZoomView zoomView;
    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    public Parameters getParams() {
        return params;
    }

    public void setParams(String player1,String player2,String UsingPlayer) {

        this.params.player_name=UsingPlayer;
        this.params.players_names.clear();
        this.params.players_names.add(player1);
        this.params.players_names.add(player2);
    }
    /**
     * Paint object we will use to draw a line
     */
    private Paint linePaint;

    float SCALE_IN_VIEW = 0.9f;
    float playAreaScale = 1f;
    float playAreaX = 0f;
    float playAreaY = 0f;
    private Paint fillPaint;
    int shipCount = 0;
    private float scaleFactor;

    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();

    private Bitmap shipBitmap = null;


    public PlaceShip1view(Context context) {
        super(context);
        init(null, 0,context);
    }
//
    public PlaceShip1view(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0,context);
    }

    public PlaceShip1view(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs, defStyle,context);


    }

    private void init(AttributeSet attrs, int defStyle,Context context) {

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(1);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xFF8080FF);

        PlaceShip1 tv =  (PlaceShip1) context;
        Intent my_intent=tv.getIntent();

        String player1=my_intent.getStringExtra("player1");
        String player2=my_intent.getStringExtra("player2");
        String currentPlayer=my_intent.getStringExtra("CurrentPlayer");
        setParams(player1,player2,currentPlayer);







    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);





        @SuppressLint("DrawAllocation") Paint my_paint=new Paint();
        my_paint.setColor(Color.RED);
        my_paint.setTextSize(60);
        my_paint.setTextAlign(Paint.Align.CENTER);



    }



    /**
     * Get the view state from a bundle
     * @param key key name to use in the bundle
     * @param bundle bundle to load from
     */
    public void getFromBundle(String key, Bundle bundle) {
        String currentPlayer=bundle.getString("CurrentPlayer");
        String player1=bundle.getString("player1");
        String player2=bundle.getString("player2");
        Vector<String> my_vec=new Vector<>();
        my_vec.clear();
        my_vec.add(player1);
        my_vec.add(player2);
        params.player_name=currentPlayer;
        params.players_names=my_vec;

// Ensure the options are all set

    }

    /**
     * Handle a touch event
     * @param event The touch event
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(event);
                touch1.copyToLast();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(event);
                    touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:




            case MotionEvent.ACTION_CANCEL:
                touch1.id = -1;
                touch2.id = -1;
                invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                if(id == touch2.id) {
                    touch2.id = -1;
                } else if(id == touch1.id) {
                    // Make what was touch2 now be touch1 by
                    // swapping the objects.
                    Touch t = touch1;
                    touch1 = touch2;
                    touch2 = t;
                    touch2.id = -1;
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                getPositions(event);
                move();
                return true;

        }

        return super.onTouchEvent(event);
    }
    /**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     */
    private void getPositions(MotionEvent event) {
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            // Convert to image coordinates
            float x = event.getX(i);
            float y = event.getY(i);
           // String testx = Float.toString(x);
           // Log.i("first x",testx);
            if(id == touch1.id) {
                touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
                touch2.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }

        invalidate();
    }

    /**
     * Handle movement of the touches
     */
    private void move() {
        // If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        if(touch1.id < 0) {
            return;
        }

        if(touch1.id >= 0) {
            // At least one touch
            // We are moving
            touch1.computeDeltas();

            playAreaX += touch1.dX;
            playAreaY += touch1.dY;
        }
        if(touch2.id >= 0) {
            // Two touches

            /*
             * Scaling
             */

            float old_tx = touch2.lastX-touch1.lastX;
            float old_ty = touch2.lastY-touch1.lastY;
            float new_tx = touch2.x-touch1.x;
            float new_ty = touch2.y-touch1.y;
            Scaling(old_tx,old_ty,new_tx,new_ty);
        }

    }

    /**
     * Scaling the image around the point x1, y1
     * @param x1 old touch distance x
     * @param y1 old touch distance y
     * @param x2 new touch distance x
     * @param y2 new touch distance y
     */
    public void Scaling( float x1, float y1,float x2,float y2) {
        double old_distance = Math.sqrt(Math.pow(x1,2)+Math.pow(y1,2));
        double new_distance = Math.sqrt(Math.pow(x2,2)+Math.pow(y2,2));
        double size = playAreaScale/(old_distance/new_distance);
        playAreaScale = (float) size;
    }
    private static class Parameters implements Serializable {


        public String player_name="";

        public Vector<String> players_names=new Vector<String>();


    }
    /**
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the
     * two possible touches.
     */
    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;

        /**
         * Current x location
         */
        public float x = 0;

        /**
         * Current y location
         */
        public float y = 0;

        /**
         * Previous x location
         */
        public float lastX = 0;

        /**
         * Previous y location
         */
        public float lastY = 0;

        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }

        /**
         * Change in x value from previous
         */
        public float dX = 0;

        /**
         * Change in y value from previous
         */
        public float dY = 0;

        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }

    }
    public float getX() {
        return touch1.x;
    }

    public float getY() {
        return touch1.y;
    }


    private EditText GetPlayer1(){return (EditText)findViewById(R.id.User1);}

    private EditText GetPlayer2(){return (EditText)findViewById(R.id.User2);}




    public Vector<String> getPlayers(){return params.players_names;}

    public String getCurrentPlayer(){return params.player_name;}


    }
