package com.example.xbedt01.projizdacka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShrnutiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shrnuti);

        TextView spoil1 = (TextView) findViewById(R.id.spoil1);
        TextView spoil2 = (TextView) findViewById(R.id.spoil2);
        TextView spopis2 = (TextView) findViewById(R.id.spopis2);
        TextView spoil3 = (TextView) findViewById(R.id.spoil3);
        TextView spoil4 = (TextView) findViewById(R.id.spoil4);
        TextView spopis5 = (TextView) findViewById(R.id.spopis5);
        TextView spoil5 = (TextView) findViewById(R.id.spoil5);

        Double data1 = 0.0, dat2, data3 = 0.0;
        Integer data2 = 0, data4 = 0, data5 = 0;
        String popis2 = "km za ", popis5="";

        Bundle b = getIntent().getExtras();
        try{
            data1 = b.getDouble("spoil1");
            dat2 = b.getDouble("spoil2");
            data2 = dat2.intValue();
            popis2 += b.getString("spopis2");
            data3 = b.getDouble("spoil3");
            data4 = b.getInt("spoil4");
            data5 = b.getInt("spoil5");
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        if(data5==1) popis5 = "a";
        if(data5==2||data5==3) popis5 = "y";

        spoil1.setText(data1.toString());
        spoil2.setText(data2.toString());
        spopis2.setText(popis2);
        spoil3.setText(data3.toString());
        spoil4.setText(data4.toString());
        spopis5.setText("návštěv" + popis5 + " servisu");
        spoil5.setText(data5.toString());
    }
}
