package com.example.xbedt01.projizdacka;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static java.util.Calendar.MONTH;

public class Dashboard extends AppCompatActivity {

    public ArrayList<Zapis> list = new ArrayList<Zapis>();
    public String FILE = "/mnt/sdcard/jizdy.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        load();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        load();
    }

    public void load() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(FILE),"UTF-8"));
            ZapisAdapter adapter = new ZapisAdapter(this, list);
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);
            adapter.clear();
            String radek;
            try {
                while((radek=in.readLine())!=null) {
                    StringTokenizer rv = new StringTokenizer(radek, ";");
                    Zapis novyzapis = new Zapis(rv.nextToken(), rv.nextToken(), rv.nextToken(), rv.nextToken(), rv.nextToken(), rv.nextToken(), rv.nextToken());
                    adapter.insert(novyzapis,0);
                }
            }
            catch (IOException ex) {}
        }
        catch (FileNotFoundException e) { novy(); }
        catch (UnsupportedEncodingException e) {}
    }

    public void pridat(View v) {
        //Stav tachometru
        Double stavtachometru = 0.0;
        for(int i = 0;i < list.size(); i++){
            if(list.get(i).typ==0) {
                stavtachometru = list.get(i).tach;
                break;
            }
        }

        //Průměrná spotřeba
        Double prumernaspotreba = 0.0;
        Integer k = 0;
        for(int i = 0;i < list.size(); i++) {
            if(list.get(i).typ==0) {
                prumernaspotreba += list.get(i).atr3;
                k++;
            }
        }
        prumernaspotreba /= k;

        //Průměrná cena za litr
        k = 0;
        Double prumernacenabenzinu = 0.0;
        for(int i = 0;i < list.size(); i++) {
            if(list.get(i).typ==1) {
                prumernacenabenzinu += list.get(i).tach;
                k++;
            }
        }
        prumernacenabenzinu /= k;

        ArrayList<String> jha = new ArrayList<String>();
        ArrayList<String> bha = new ArrayList<String>();
        ArrayList<String> sha = new ArrayList<String>();
        //jizda hint words
        for(int i = 0;i < list.size(); i++) {
            switch(list.get(i).typ) {
                case 0:
                    jha.add(list.get(i).atr1);
                    break;
                case 1:
                    bha.add(list.get(i).atr1);
                    break;
                case 2:
                    sha.add(list.get(i).atr1);
                    break;
            }
        }

        //odstranění duplikátů
        Set<String> jhs = new HashSet<>();
        jhs.addAll(jha);
        jha.clear();
        jha.addAll(jhs);

        Set<String> bhs = new HashSet<>();
        bhs.addAll(bha);
        bha.clear();
        bha.addAll(bhs);

        Set<String> shs = new HashSet<>();
        shs.addAll(sha);
        sha.clear();
        sha.addAll(shs);

        String[] jh, bh, sh;
        jh = jha.toArray(new String[0]);
        bh = bha.toArray(new String[0]);
        sh = sha.toArray(new String[0]);

        Intent intent = new Intent(this, PridatActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("line",true);
        b.putDouble("stavtachometru", stavtachometru);
        b.putDouble("prumernaspotreba", prumernaspotreba);
        b.putDouble("prumernacenabenzinu", prumernacenabenzinu);
        b.putStringArray("jh",jh);
        b.putStringArray("bh",bh);
        b.putStringArray("sh",sh);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void novy() {
        Intent intent = new Intent(this, PridatActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("line",false);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void shrnuti(View v) {
        Double data1, data2, data3;
        Integer data4 = 0, data5 = 0;

        //data1 Cena za 1 km
        Double km = 0.0, koef = 0.0, cena = 0.0, cenazavse = 0.0;
        for (int i = 0;i<list.size();i++) {
            switch(list.get(i).typ)
            {
                case 0:
                    km += list.get(i).atr2;
                    koef += list.get(i).atr3*list.get(i).atr2;
                    break;
                case 1:
                    cena += list.get(i).tach;
                    cenazavse += list.get(i).atr2;
                    data4++;
                    break;
                case 2:
                    cenazavse += list.get(i).atr2;
                    data5++;
                    break;
            }
        }

        //zaokrouhlování na 2 desetinná místa
        Integer k = 0;
        data1 = (koef/km)*(cena/data4)+0.5;
        k = data1.intValue();
        data1 = k.doubleValue()/100;
        data3 = cenazavse*100+0.5;
        k = data3.intValue();
        data3 = k.doubleValue()/100;

        //data2 km/aktuální měsíc + měsíc string;
        String[] mesice = {"leden","únor","březen","duben","květen","červen","červenec","srpen","září","říjen","listopad","prosinec"};
        Calendar c = Calendar.getInstance();
        Integer mesic = c.get(MONTH);
        String popis2 = mesice[mesic];
        data2 = 0.0;
        Integer i = 0;
        mesic++;
        while(Integer.parseInt(list.get(i).datum.split("[.]")[1])==mesic){
            if(list.get(i).typ==0) data2 += list.get(i).atr2;
            i++;
            if(i==list.size())break;
        }

        Intent intent = new Intent(this, ShrnutiActivity.class);
        Bundle b = new Bundle();
        b.putDouble("spoil1",data1);
        b.putDouble("spoil2",data2);
        b.putString("spopis2",popis2);
        b.putDouble("spoil3",data3);
        b.putInt("spoil4",data4);
        b.putInt("spoil5",data5);
        intent.putExtras(b);
        startActivity(intent);
    }
}
