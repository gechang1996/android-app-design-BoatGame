package edu.msu.gechang1.project1;


import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText ed1=(EditText)findViewById(R.id.User1);
        final String U1=ed1.getText().toString();
        EditText ed2=(EditText)findViewById(R.id.User2);
        final String U2=ed2.getText().toString();
        final Button my_botton=(Button)findViewById(R.id.button2);
        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText ed1=(EditText)findViewById(R.id.User1);
                final String U1=ed1.getText().toString();
                EditText ed2=(EditText)findViewById(R.id.User2);
                final String U2=ed2.getText().toString();
                if(U1.isEmpty() || U2.isEmpty())
                {
                    my_botton.setEnabled(false);
                }
                else
                {
                    my_botton.setEnabled(true);
                }
            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText ed1=(EditText)findViewById(R.id.User1);
                final String U1=ed1.getText().toString();
                EditText ed2=(EditText)findViewById(R.id.User2);
                final String U2=ed2.getText().toString();
                if(U1.isEmpty() || U2.isEmpty())
                {
                    my_botton.setEnabled(false);
                }
                else
                {
                    my_botton.setEnabled(true);
                }
            }
        });


    }

    public void onGameStart(View view){
        Intent intent = new Intent(this, PlaceShip1.class);

        EditText ed1=(EditText)findViewById(R.id.User1);
        String U1=ed1.getText().toString();
        EditText ed2=(EditText)findViewById(R.id.User2);
        String U2=ed2.getText().toString();
        intent.putExtra("player1",U1);
        intent.putExtra("player2",U2);

        Random r = new Random();
        int my_num = r.nextInt(100);
        if(my_num>=50) {
            intent.putExtra("CurrentPlayer", U1);
        }
        else
        {
            intent.putExtra("CurrentPlayer", U2);
        }

        startActivity(intent);


    }
    public void onHelpShow(View view)
    {
        // Instantiate a dialog box builder
        AlertDialog.Builder builder =
                new AlertDialog.Builder(view.getContext());

        // Parameterize the builder
        builder.setTitle(R.string.GameDescription);
        builder.setMessage(R.string.Detail);
        builder.setPositiveButton(R.string.ok, null);

        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }






}
