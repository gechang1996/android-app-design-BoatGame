package edu.msu.gechang1.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class EndActivity extends AppCompatActivity {

    private String endmes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        endmes = getIntent().getStringExtra("EndMes");

        TextView EndMes = (TextView) findViewById(R.id.winner);
        EndMes.setText(endmes);
    }

    public void onEndGame(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
