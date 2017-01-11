package com.example.xbedt01.projizdacka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xbedt01 on 15. 12. 2016.
 */
public class ZapisAdapter extends ArrayAdapter<Zapis> {
    public ZapisAdapter(Context context, ArrayList<Zapis> zapisy) {
        super(context, 0, zapisy);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Zapis zapis = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_zapis, parent, false);
        }
        // Lookup view for data population
        ImageView ico = (ImageView) convertView.findViewById(R.id.ikona);
        TextView zdate = (TextView) convertView.findViewById(R.id.zdatum);
        TextView zatr1 = (TextView) convertView.findViewById(R.id.zatr1);
        TextView zatr2 = (TextView) convertView.findViewById(R.id.zatr2);
        TextView ztach = (TextView) convertView.findViewById(R.id.ztach);
        TextView zatr3 = (TextView) convertView.findViewById(R.id.zatr3);
        TextView zatr4 = (TextView) convertView.findViewById(R.id.zatr4);
        // Populate the data into the template view using the data object
        zdate.setText(zapis.datum);
        zatr1.setText(zapis.atr1);

        switch(zapis.typ) {
            case 0:
                ico.setImageResource(R.mipmap.car);
                zatr2.setText(zapis.atr2.toString()+"km");
                ztach.setText(zapis.tach.toString()+"km");
                zatr3.setText(zapis.atr3.toString()+"l/100km");
                zatr4.setText(zapis.atr4.toString()+"Kč");
                break;
            case 1:
                ico.setImageResource(R.mipmap.gas);
                zatr2.setText(zapis.atr2.toString()+"Kč");
                ztach.setText(zapis.tach.toString()+"Kč/1l");
                zatr3.setText(zapis.atr3.toString()+"l");
                zatr4.setText(zapis.atr4.toString()+"km");
                break;
            case 2:
                ico.setImageResource(R.mipmap.key);
                zatr2.setText(zapis.atr2.toString()+"Kč");
                ztach.setText("");
                zatr3.setText("");
                zatr4.setText("");
                break;
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
