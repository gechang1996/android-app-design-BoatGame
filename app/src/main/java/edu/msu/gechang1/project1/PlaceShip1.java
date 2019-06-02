package edu.msu.gechang1.project1;

// This is the place ship 1 activity

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class PlaceShip1 extends AppCompatActivity {

    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();
    private static final String PARAMETERS = "parameters";
    private Float scale = 1f;
    //    private static final int SaveNames=1;
    private boolean playerturn = true;
    private int firstViewNum = R.id.shipPieceView1;
    private int boatRest = 4;
    private boolean start = true;
    private boolean prepare = true;
    private boolean fight = false;
    private String Player1;
    private String Player2;
    private String CurrentPlayer;
    private Vector<Integer> P1Rest = new Vector<>();
    private Vector<Integer> P2Rest = new Vector<>();
    private Vector<Integer> P1Hit = new Vector<>();
    private Vector<Integer> P2Hit = new Vector<>();
    private Vector<Integer> P1Miss = new Vector<>();
    private Vector<Integer> P2Miss = new Vector<>();
    private int currentX = 0;
    private int currentY = 0;


    private boolean CheckEnable = true;

    private String message;
    private LinearLayout linearLayout;
    private LinearLayout wholePage;
    ScaleGestureDetector SGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_place_ship1);
        SGD = new ScaleGestureDetector(this, new ScaleListener());
        linearLayout = (LinearLayout) findViewById(R.id.PlayingArea);
        wholePage = (LinearLayout) findViewById(R.id.WholePage);

        if (savedInstanceState != null) {
            Player1 = savedInstanceState.getString("player1");
            Player2 = savedInstanceState.getString("player2");

            CurrentPlayer = savedInstanceState.getString("CurrentPlayer");

            boatRest = savedInstanceState.getInt("boatR");
            start = savedInstanceState.getBoolean("Start");
            prepare = savedInstanceState.getBoolean("Prepare");
            fight = savedInstanceState.getBoolean("Fight");
            playerturn = savedInstanceState.getBoolean("PlayerTurn");
            message = savedInstanceState.getString("message");

            CheckEnable = savedInstanceState.getBoolean("Enable");
            Button button = (Button) findViewById(R.id.PlaceShipDone);
            button.setEnabled(CheckEnable);


            boolean[] flags = savedInstanceState.getBooleanArray("allFlags");
            int boatNum = R.id.shipPieceView1;
            for (int i = 0; i < 16; i++) {
                ShipPieceView my_view = (ShipPieceView) findViewById(boatNum);
                my_view.setShowBoat(flags[3 * i]);
                my_view.setShowMiss(flags[3 * i + 1]);
                my_view.setShowX(flags[3 * i + 2]);
                boatNum += 1;
            }
            int[] p1r = savedInstanceState.getIntArray("p1rest");
            for (int i : p1r) {
                Integer i_obj = Integer.valueOf(i);
                P1Rest.add(i_obj);
            }
            int[] p2r = savedInstanceState.getIntArray("p2rest");
            for (int i : p2r) {
                Integer i_obj = Integer.valueOf(i);
                P2Rest.add(i_obj);
            }
            int[] p1h = savedInstanceState.getIntArray("p1hit");
            for (int i : p1h) {
                Integer i_obj = Integer.valueOf(i);
                P1Hit.add(i_obj);
            }
            int[] p2h = savedInstanceState.getIntArray("p2hit");
            for (int i : p2h) {
                Integer i_obj = Integer.valueOf(i);
                P2Hit.add(i_obj);
            }
            int[] p1m = savedInstanceState.getIntArray("p1miss");
            for (int i : p1m) {
                Integer i_obj = Integer.valueOf(i);
                P1Miss.add(i_obj);
            }
            int[] p2m = savedInstanceState.getIntArray("p2miss");
            for (int i : p2m) {
                Integer i_obj = Integer.valueOf(i);
                P2Miss.add(i_obj);
            }

        } else {
            Player1 = getIntent().getStringExtra("player1");
            Player2 = getIntent().getStringExtra("player2");
            CurrentPlayer = getIntent().getStringExtra("CurrentPlayer");


        }
        updateTV();


    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
//        String p1=getIntent().getStringExtra("player1");
//        String p2=getIntent().getStringExtra("player2");
//
//        String currentPlayer1=CurrentPlayer;


        bundle.putString("player1", Player1);
        bundle.putString("player2", Player2);
        bundle.putString("CurrentPlayer", CurrentPlayer);
        bundle.putInt("boatR", boatRest);
        bundle.putBoolean("Start", start);
        bundle.putBoolean("Prepare", prepare);
        bundle.putBoolean("Fight", fight);
        bundle.putBoolean("PlayerTurn", playerturn);
        bundle.putString("message", message);
        Button button = (Button) findViewById(R.id.PlaceShipDone);
        CheckEnable = button.isEnabled();
        bundle.putBoolean("Enable", CheckEnable);


        int[] p1_rest = new int[P1Rest.size()];
        int[] p2_rest = new int[P2Rest.size()];
        int[] p1_miss = new int[P1Miss.size()];
        int[] p2_miss = new int[P2Miss.size()];
        int[] p1_hit = new int[P1Hit.size()];
        int[] p2_hit = new int[P2Hit.size()];
        for (int i = 0; i < P1Hit.size(); i++) {
            p1_hit[i] = P1Hit.get(i);
        }
        for (int i = 0; i < P2Hit.size(); i++) {
            p2_hit[i] = P2Hit.get(i);
        }
        for (int i = 0; i < P1Rest.size(); i++) {
            p1_rest[i] = P1Rest.get(i);
        }
        for (int i = 0; i < P2Rest.size(); i++) {
            p2_rest[i] = P2Rest.get(i);
        }
        for (int i = 0; i < P1Miss.size(); i++) {
            p1_miss[i] = P1Miss.get(i);
        }
        for (int i = 0; i < P2Miss.size(); i++) {
            p2_miss[i] = P2Miss.get(i);
        }

        bundle.putIntArray("p1rest", p1_rest);
        bundle.putIntArray("p2rest", p2_rest);
        bundle.putIntArray("p1hit", p1_hit);
        bundle.putIntArray("p2hit", p2_hit);
        bundle.putIntArray("p1miss", p1_miss);
        bundle.putIntArray("p2miss", p2_miss);


        int boatNum = R.id.shipPieceView1;

        boolean[] flags = new boolean[48];
        for (int i = 0; i < 16; i++) {
            ShipPieceView my_view = (ShipPieceView) findViewById(boatNum);
            flags[i * 3] = my_view.isShowBoat();
            flags[i * 3 + 1] = my_view.isShowMiss();
            flags[i * 3 + 2] = my_view.isShowX();
            boatNum += 1;
        }
        bundle.putBooleanArray("allFlags", flags);

    }


    public void OnWin(View view) {


        if (CurrentPlayer.equals(Player1)) {
            if (prepare == true) {
                if (boatRest != 0) {
                    // Instantiate a dialog box builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(view.getContext());

                    // Parameterize the builder
                    builder.setTitle(R.string.Warning);
                    builder.setMessage(R.string.SetBoats);
                    builder.setPositiveButton(R.string.ok, null);

                    // Create the dialog box and show it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    int boatNum = R.id.shipPieceView1;
                    for (int i = 0; i < 16; i++) {
                        ShipPieceView my_view = (ShipPieceView) findViewById(boatNum);
                        if (my_view.getShowBoat()) {
                            P1Rest.add(i);
                            my_view.ClearAll();
                            my_view.invalidate();
                        }
                        boatNum += 1;
                    }
                    if (start == true) {
                        Button button = (Button) findViewById(R.id.PlaceShipDone);
                        button.setEnabled(false);
                        start = false;
                        SwitchPlayer();
                        boatRest = 4;

                        updateTV();
                    } else {
                        Button button = (Button) findViewById(R.id.PlaceShipDone);
                        button.setEnabled(false);
                        prepare = false;
                        fight = true;
                        start = true;
                        Random r = new Random();
                        int my_num = r.nextInt(200);
                        if (my_num >= 100) {
                            CurrentPlayer = Player1;
                        } else {
                            CurrentPlayer = Player2;
                        }

                        updateTV();

                    }
                }
            } else if (fight == true) {

//                if(CheckWhoWin(view))
//                {
//                    return;
//                }

                if (playerturn == false) {
                    playerturn = true;
                    Button button = (Button) findViewById(R.id.PlaceShipDone);
                    button.setEnabled(false);

                } else {
                    // Instantiate a dialog box builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(view.getContext());

                    // Parameterize the builder
                    builder.setTitle(R.string.WarningClick);
                    builder.setMessage(R.string.ClickBoat);
                    builder.setPositiveButton(R.string.ok, null);

                    // Create the dialog box and show it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }

                SwitchPlayer();


                updateTV();
                updatePlayerSituation();
            }
        } else if (CurrentPlayer.equals(Player2)) {
            if (prepare == true) {
                if (boatRest != 0) {
                    // Instantiate a dialog box builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(view.getContext());

                    // Parameterize the builder
                    builder.setTitle(R.string.Warning);
                    builder.setMessage(R.string.SetBoats);
                    builder.setPositiveButton(R.string.ok, null);

                    // Create the dialog box and show it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    int boatNum = R.id.shipPieceView1;
                    for (int i = 0; i < 16; i++) {
                        ShipPieceView my_view = (ShipPieceView) findViewById(boatNum);
                        if (my_view.getShowBoat()) {
                            P2Rest.add(i);
                            my_view.ClearAll();
                            my_view.invalidate();
                        }
                        boatNum += 1;
                    }
                    if (start == true) {
                        Button button = (Button) findViewById(R.id.PlaceShipDone);
                        button.setEnabled(false);
                        start = false;
                        CurrentPlayer = Player1;
                        boatRest = 4;
                        updateTV();
                    } else {
                        Button button = (Button) findViewById(R.id.PlaceShipDone);
                        button.setEnabled(false);

                        prepare = false;
                        fight = true;
                        start = true;
                        updateTV();
                    }
                }
            } else if (fight == true) {


//                if(CheckWhoWin(view))
//                {
//                    return;
//                }

                if (playerturn == false) {
                    playerturn = true;
                    Button button = (Button) findViewById(R.id.PlaceShipDone);
                    button.setEnabled(false);
                } else {
                    // Instantiate a dialog box builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(view.getContext());

                    // Parameterize the builder
                    builder.setTitle(R.string.WarningClick);
                    builder.setMessage(R.string.ClickBoat);
                    builder.setPositiveButton(R.string.ok, null);

                    // Create the dialog box and show it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }

                SwitchPlayer();


                updateTV();
                updatePlayerSituation();
            }

        }
//        // Instantiate a dialog box builder
//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(view.getContext());
//
//        // Parameterize the builder
//        builder.setTitle(R.string.End);
//        builder.setMessage(R.string.Win);
//        builder.setPositiveButton(R.string.ok, null);
//
//        // Create the dialog box and show it
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
    }

    public void OnLose(View view) {
        Intent intent = new Intent(this, EndActivity.class);

        message = CurrentPlayer + " give up the game.";
        intent.putExtra("EndMes", message);
        startActivity(intent);
    }

    public void UpdateBoatRest(int id) {
        ShipPieceView this_view = (ShipPieceView) findViewById(id);
        boolean showBoat = this_view.getShowBoat();
        if (prepare == true) {
            if (showBoat == true) {
                if (boatRest != 4) {
                    boatRest += 1;
                    updateTV();
                    this_view.setShowBoat(false);
                } else {
                    this_view.setShowBoat(true);
                }
            } else {
                if (boatRest != 0) {
                    boatRest -= 1;
                    updateTV();
                    this_view.setShowBoat(true);
                } else {
                    this_view.setShowBoat(false);
                }
            }
        }


    }


    public void updateTV() {
        if (prepare == true) {
            message = "Your turn:   " + CurrentPlayer;
            message += "\nPlace Your Boats: ";
            message += Integer.toString(boatRest);
            message += " remaining";
        } else if (fight == true) {
            String anotherPlayer;
            if (CurrentPlayer.equals(Player1)) {
                anotherPlayer = Player2;
            } else {
                anotherPlayer = Player1;
            }

            message = "Your turn:   " + anotherPlayer;
        }
        TextView tv1 = (TextView) findViewById(R.id.turn);

        tv1.setText(message);
    }

    public void updatePlayerSituation() {

        int boatNum = R.id.shipPieceView1;
        for (int i = 0; i < 16; i++) {
            ShipPieceView my_view = (ShipPieceView) findViewById(boatNum);

            my_view.ClearAll();
            my_view.invalidate();
            boatNum += 1;
        }

        if (CurrentPlayer.equals(Player1)) {
            for (int j = 0; j < P1Hit.size(); j++) {
                int this_view_id1 = P1Hit.get(j) + firstViewNum;
                ShipPieceView this_view1 = (ShipPieceView) findViewById(this_view_id1);
                this_view1.SetBoatX();
                this_view1.invalidate();

            }
            for (int m = 0; m < P1Miss.size(); m++) {
                int this_view_id2 = P1Miss.get(m) + firstViewNum;
                ShipPieceView this_view2 = (ShipPieceView) findViewById(this_view_id2);
                this_view2.SetMiss();
                this_view2.invalidate();
            }

        } else if (CurrentPlayer.equals(Player2)) {
            for (int j = 0; j < P2Hit.size(); j++) {
                int this_view_id1 = P2Hit.get(j) + firstViewNum;
                ShipPieceView this_view1 = (ShipPieceView) findViewById(this_view_id1);
                this_view1.SetBoatX();
                this_view1.invalidate();

            }
            for (int m = 0; m < P2Miss.size(); m++) {
                int this_view_id2 = P2Miss.get(m) + firstViewNum;
                ShipPieceView this_view2 = (ShipPieceView) findViewById(this_view_id2);
                this_view2.SetMiss();
                this_view2.invalidate();
            }
        }
    }

    private void SwitchPlayer() {
        if (CurrentPlayer.equals(Player1)) {
            CurrentPlayer = Player2;
        } else if (CurrentPlayer.equals(Player2)) {
            CurrentPlayer = Player1;
        }
    }


    public void CheckHitOrMiss(int id) {
        if (fight == true) {

            int this_view_id = id - firstViewNum;
            if (CurrentPlayer.equals(Player1)) {
                if (playerturn == true) {
                    for (int j = 0; j < P1Hit.size(); j++) {
                        if (this_view_id == P1Hit.get(j)) {
                            return;
                        }
                    }
                    for (int m = 0; m < P1Miss.size(); m++) {
                        if (this_view_id == P1Miss.get(m)) {
                            return;
                        }
                    }
                    int index_remove = -1;
                    for (int k = 0; k < P1Rest.size(); k++) {
                        if (this_view_id == P1Rest.get(k)) {
                            P1Hit.add(this_view_id);
                            index_remove = k;
                            ShipPieceView this_view = (ShipPieceView) findViewById(id);
                            this_view.SetBoatX();
                            this_view.invalidate();
                            break;
                        }

                    }
                    if (index_remove != -1) {
                        P1Rest.remove(index_remove);
                        playerturn = false;
                        return;
                    }
                    ShipPieceView this_view = (ShipPieceView) findViewById(id);
                    this_view.SetMiss();
                    P1Miss.add(this_view_id);
                    playerturn = false;
                    this_view.invalidate();

                } else {
                    return;
                }

            } else if (CurrentPlayer.equals(Player2)) {
                if (playerturn == true) {
                    for (int j = 0; j < P2Hit.size(); j++) {
                        if (this_view_id == P2Hit.get(j)) {
                            return;
                        }
                    }
                    for (int m = 0; m < P2Miss.size(); m++) {
                        if (this_view_id == P2Miss.get(m)) {
                            return;
                        }
                    }
                    int index_remove = -1;
                    for (int k = 0; k < P2Rest.size(); k++) {
                        if (this_view_id == P2Rest.get(k)) {
                            P2Hit.add(this_view_id);
                            index_remove = k;
                            ShipPieceView this_view = (ShipPieceView) findViewById(id);
                            this_view.SetBoatX();
                            this_view.invalidate();
                            break;
                        }

                    }
                    if (index_remove != -1) {
                        P2Rest.remove(index_remove);
                        playerturn = false;
                        return;
                    }
                    ShipPieceView this_view = (ShipPieceView) findViewById(id);
                    this_view.SetMiss();
                    P2Miss.add(this_view_id);
                    playerturn = false;
                    this_view.invalidate();
                } else {
                    return;
                }

            }
        }
    }

    public void CheckWhoWin() {
        if (fight == true) {
            if (P1Rest.size() == 0) {

                Intent intent = new Intent(this, EndActivity.class);
                message = Player2 + " win the game!";
                message += "\n" + Player1 + " lose the game.";
                intent.putExtra("EndMes", message);
                startActivity(intent);
                return;
            } else if (P2Rest.size() == 0) {
                Intent intent = new Intent(this, EndActivity.class);
                message = Player1 + " win the game!";
                message += "\n" + Player2 + " lose the game.";
                intent.putExtra("EndMes", message);
                startActivity(intent);
                return;
            }
            return;
        } else {
            return;
        }
    }

    private void Reset() {
        playerturn = true;

        boatRest = 4;
        start = true;
        prepare = true;
        fight = false;
        Random r = new Random();
        int my_num = r.nextInt(100);
        if (my_num >= 500) {
            CurrentPlayer = Player1;
        } else {
            CurrentPlayer = Player2;
        }

        P1Rest.clear();
        P2Rest.clear();
        P1Hit.clear();
        P2Hit.clear();
        P1Miss.clear();
        P2Miss.clear();
        int boatNum = R.id.shipPieceView1;
        for (int i = 0; i < 16; i++) {
            ShipPieceView my_view = (ShipPieceView) findViewById(boatNum);

            my_view.ClearAll();
            my_view.invalidate();
            boatNum += 1;
        }
        updateTV();

    }

    private class RestartGameListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Reset();
            Button button = (Button) findViewById(R.id.PlaceShipDone);
            button.setEnabled(false);
        }
    }

    private class EndGameListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Reset();
//            Intent intent = new Intent();
//            startActivity(intent);
            setContentView(R.layout.activity_main);
        }
    }

    public void CheckDoneButton() {
        Button button = (Button) findViewById(R.id.PlaceShipDone);

        if (boatRest == 0) {
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }


    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.2f, Math.min(scale, 2f)); //0.2f und 2f  //First: Zoom in __ Second: Zoom out
            //matrix.setScale(scale, scale);
            linearLayout.setScaleX(scale);
            linearLayout.setScaleY(scale);

            linearLayout.invalidate();

            return true;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                int x2 = (int) event.getRawX();
                int y2 = (int) event.getRawY();
                linearLayout.scrollBy(currentX - x2, currentY - y2);

                currentX = x2;
                currentY = y2;
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }

        return true;
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

//        invalidate();
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
}