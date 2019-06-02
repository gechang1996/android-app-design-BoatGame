package edu.msu.gechang1.project1;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * The view we will draw out hatter in
 */
public class ShipPieceView extends View {


    /**
     * Image left margin in pixels
     */
    private float marginLeft = 0;

    /**
     * Image top margin in pixels
     */
    private float marginTop = 0;
    /**
     * Image drawing scale
     */
    private float imageScale = 1;
    /**
     * The bitmap to draw the stuff
     */
    private Bitmap ShipBitmap=null;
    private Bitmap XBitmap=null;
    private Bitmap MissBitmap=null;

    private boolean ShowBoat=false;
    private boolean ShowX=false;
    private boolean ShowMiss=false;

    private Paint fillPaint = new Paint();
    private Paint strokePaint = new Paint();
    float SCALE_IN_VIEW = 1.0f;
    private int playSize;
    private int marginX;
    private int marginY;
    private float playAreaX = 0f;
    private float playAreaY = 0f;
    private float playAreaScale = 1f;
    private float boatHeight;
    private float boatWidth;
    private float MissHeight;
    private float MissWidth;
    private float XHeight;
    private float XWidth;

    public boolean isShowBoat() {
        return ShowBoat;
    }

    public boolean isShowX() {
        return ShowX;
    }

    public boolean isShowMiss() {
        return ShowMiss;
    }

    public void setShowX(boolean showX) {
        ShowX = showX;
    }

    public void setShowMiss(boolean showMiss) {
        ShowMiss = showMiss;
    }

    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();
    public ShipPieceView(Context context) {
        super(context);
        init(null, 0);
    }

    public ShipPieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ShipPieceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // fill
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(0xFF8080FF);
        // stroke
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(1);
        ShipBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.patrolboat);
        boatHeight=ShipBitmap.getHeight();
        boatWidth=ShipBitmap.getWidth();
        XBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.hitmarker);
        XHeight=XBitmap.getHeight();
        XWidth=XBitmap.getWidth();
        MissBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.missmarker);
        MissHeight=MissBitmap.getHeight();
        MissWidth=MissBitmap.getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int wid = getWidth();
        int hit = getHeight();
        int minDim = wid < hit ? wid : hit;
        playSize = (int)(minDim * SCALE_IN_VIEW);
        marginX = (wid - playSize) / 2;
        marginY = (hit - playSize) / 2;

        canvas.save();
        canvas.translate(playAreaX,  playAreaY);
        canvas.scale(playAreaScale,playAreaScale);
        canvas.drawRect(0, 0, wid, hit, fillPaint);
        canvas.drawRect(0, 0, wid, hit, strokePaint);
        if (ShowBoat == true){


            canvas.drawBitmap(ShipBitmap, wid/2-boatWidth/2, hit/2-boatHeight/2, null);

        }
        if (ShowMiss==true)
        {
            // What is the scaled image size?
            // Determine the top and left margins to center
            marginLeft = (wid - MissWidth/2) / 2;
            marginTop = (hit - MissHeight/2) / 2;
            canvas.translate(marginLeft,marginTop);
            canvas.scale(0.5f,0.5f);
            canvas.drawBitmap(MissBitmap, 0,0, null);

        }
        if (ShowX == true)
        {

            float X_Scale=boatHeight/XHeight;
            // What is the scaled image size?
            // Determine the top and left margins to center
            marginLeft = (wid - XWidth*X_Scale) / 2;
            marginTop = (hit - XHeight*X_Scale) / 2;
            canvas.translate(marginLeft,marginTop);
            canvas.scale(X_Scale,X_Scale);
            canvas.drawBitmap(XBitmap, 0,0, null);

        }
        canvas.restore();

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
                ((PlaceShip1)getContext()).UpdateBoatRest(getId());
                ((PlaceShip1)getContext()).CheckDoneButton();
                ((PlaceShip1)getContext()).CheckHitOrMiss(getId());
                ((PlaceShip1)getContext()).CheckWhoWin();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(event);
                    touch2.copyToLast();
                    ((PlaceShip1)getContext()).UpdateBoatRest(getId());
                    ((PlaceShip1)getContext()).CheckDoneButton();
                    ((PlaceShip1)getContext()).CheckHitOrMiss(getId());
                    ((PlaceShip1)getContext()).CheckWhoWin();
                    return true;
                }


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
//                move();
                return true;

        }

        return super.onTouchEvent(event);
    }
    //    /**
//     * Get the positions for the two touches and put them
//     * into the appropriate touch objects.
//     * @param event the motion event
//     */
    private void getPositions(MotionEvent event) {
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            // Convert to image coordinates
            float x = event.getX(i) ;
            float y = event.getY(i) ;



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
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the
     * two possible touches.
     */
    private class Touch {
        /**
         * Change in x value from previous
         */
        public float dX = 0;

        /**
         * Change in y value from previous
         */
        public float dY = 0;
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
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }
    /**
     * Handle movement of the touches
     */
    private void move() {

    }
    public void ClearAll()
    {
        ShowBoat=false;
        ShowMiss=false;
        ShowX=false;
    }
    public void SetBoatX()
    {
        ShowBoat=true;
        ShowX=true;
    }
    public void SetMiss()
    {
        ShowMiss=true;
    }
    public boolean getShowBoat()
    {

        return ShowBoat;
    }
    public void setShowBoat(boolean bool)
    {
        ShowBoat=bool;
    }




}