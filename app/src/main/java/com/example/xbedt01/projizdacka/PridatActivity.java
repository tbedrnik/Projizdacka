package com.example.xbedt01.projizdacka;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PridatActivity extends AppCompatActivity {

    public String FILE = "/mnt/sdcard/jizdy.txt";
    public Integer a = 0;
    public Double stavtachometru = 0.0;
    public Double prumernaspotreba = 0.0;
    public Double prumernacenabenzinu = 0.0;
    public EditText datum, atr1, atr2, atr3, atr4, tach;
    public AutoCompleteTextView hint;
    public Boolean line;
    public String[] jizdahint, benzinhint, servishint;
    public RelativeLayout.LayoutParams params;
    public ArrayAdapter<String> jha, bha, sha;
    Button B;
    RadioButton RBJ;
    RadioButton RBB;
    RadioButton RBS;
    ViewFlipper VF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pridat);

        datum = (EditText) findViewById(R.id.udatum);
        atr1 = (EditText) findViewById(R.id.uatr1);
        atr2 = (EditText) findViewById(R.id.uatr2);
        tach = (EditText) findViewById(R.id.utach);
        atr3 = (EditText) findViewById(R.id.uatr3);
        atr4 = (EditText) findViewById(R.id.uatr4);

        Bundle b = getIntent().getExtras();
        try{
            line = b.getBoolean("line");
            stavtachometru = b.getDouble("stavtachometru");
            prumernaspotreba = b.getDouble("prumernaspotreba");
            prumernacenabenzinu = b.getDouble("prumernacenabenzinu");
            jizdahint = b.getStringArray("jh");
            benzinhint = b.getStringArray("bh");
            servishint = b.getStringArray("sh");
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        hint = (AutoCompleteTextView) findViewById(R.id.auto_misto);
        jha = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,jizdahint);
        bha = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,benzinhint);
        sha = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,servishint);
        hint.setAdapter(jha);

        B = (Button) findViewById(R.id.button);
        params = (RelativeLayout.LayoutParams) B.getLayoutParams();
        RBJ = (RadioButton) findViewById(R.id.rbj);
        RBB = (RadioButton) findViewById(R.id.rbb);
        RBS = (RadioButton) findViewById(R.id.rbs);
        VF = (ViewFlipper) findViewById(R.id.switcher);

        //datum do inputu
        Calendar c = Calendar.getInstance();
        SimpleDateFormat d = new SimpleDateFormat("d.M.yyyy");
        String dnes = d.format(c.getTime());
        datum.setText(dnes);

        //Když napíšu kilometry, navrhne stav tachometru
        atr2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String value = atr2.getText().toString();
                if(!hasFocus&&!value.equals("")) {
                    if(a==0) {
                        Double k = Double.parseDouble(value);
                        Double novy_stav = stavtachometru + k;
                        tach.setText(novy_stav.toString());
                    }
                }
            }
        });

        //0: Když napíšu spotřebu, navrhne teoretickou cenu
        //1: Když přepíšu litry, přepíše se cena (třeba když si koupim soušenku)
        atr3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String value = atr3.getText().toString();
                if(!hasFocus&&!value.equals("")) {
                    switch (a) {
                        case 0:
                            Double km = Double.parseDouble(atr2.getText().toString());
                            Double k = Double.parseDouble(value);
                            Double cena = k * km * prumernacenabenzinu + 0.5;
                            Integer c = cena.intValue();
                            cena = c.doubleValue()/100;
                            atr4.setText(cena.toString());
                            break;
                        case 1:
                            Double cena_litr = Double.parseDouble(tach.getText().toString());
                            Double pocet_litru = Double.parseDouble(value);
                            Double cena2 = cena_litr * pocet_litru * 100 + 0.5;
                            Integer k2 = cena2.intValue();
                            cena2 = k2.doubleValue() / 100;
                            atr2.setText(cena2.toString());
                            //teoretický dojezd = pocet_litru / pr_sp * 100
                            Double dojezd = pocet_litru / prumernaspotreba * 10000 + 0.5;
                            k2 = dojezd.intValue();
                            dojezd = k2.doubleValue() / 100;
                            atr4.setText(dojezd.toString());
                            break;
                    }
                }
            }
        });

        //když napíšu cenu za litr, doplní se počet litrů
        tach.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String value = tach.getText().toString();
                if(!hasFocus&&!value.equals("")) {
                    if (a==1) {
                        Double cena = 0.0;
                        Boolean yn = atr2.getText().toString().equals("");
                        if(!yn){
                            cena = Double.parseDouble(atr2.getText().toString());
                            Double cena_litr = Double.parseDouble(value);
                            Double pocet_litru = cena / cena_litr * 100 + 0.5;
                            Integer k = pocet_litru.intValue();
                            pocet_litru = k.doubleValue()/100;
                            atr3.setText(pocet_litru.toString());
                        }
                    }
                }
            }
        });
        
        RBJ.setOnClickListener(radio_listener);
        RBB.setOnClickListener(radio_listener);
        RBS.setOnClickListener(radio_listener);
    }

    hint.setOnFocusChangeListener(new View.OnFocusChangeListener() {

    });

    private OnClickListener radio_listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rbj:
                    VF.setDisplayedChild(0);
                    skryti(0);
                    a=0;
                    hint.setAdapter(jha);
                    break;
                case R.id.rbb:
                    VF.setDisplayedChild(1);
                    skryti(1);
                    a=1;
                    hint.setAdapter(bha);
                    break;
                case R.id.rbs:
                    VF.setDisplayedChild(2);
                    skryti(2);
                    a=2;
                    hint.setAdapter(sha);
            }
        }
    };

    public void skryti(Integer i) {
        switch(i) {
            case 0:
            case 1:
                tach.setVisibility(View.VISIBLE);
                atr3.setVisibility(View.VISIBLE);
                atr4.setVisibility(View.VISIBLE);
                params.addRule(RelativeLayout.BELOW, R.id.uatr4);
                break;
            case 2:
                tach.setVisibility(View.INVISIBLE);
                atr3.setVisibility(View.INVISIBLE);
                atr4.setVisibility(View.INVISIBLE);
                params.addRule(RelativeLayout.BELOW, R.id.uatr2);
                break;
        }
    }

    public void ulozit(View v) {

        String b = datum.getText().toString();
        String c;
        if(!atr1.getText().toString().isEmpty()) c = atr1.getText().toString();
        else c = hint.getText().toString();
        String d = atr2.getText().toString();
        String e="0",f="0",g="0";
        if(a!=2) {
            e = tach.getText().toString();
            f = atr3.getText().toString();
            g = atr4.getText().toString();
        }

        String zaznam = a.toString()+";"+b+";"+c+";"+d+";"+e+";"+f+";"+g;

        try {
            FileWriter fw = new FileWriter(FILE, true);
            BufferedWriter out = new BufferedWriter(fw);
            if(line) out.newLine();
            out.append(zaznam);
            out.close();
        }
        catch(IOException ex) {}
        finish();
    }
}
