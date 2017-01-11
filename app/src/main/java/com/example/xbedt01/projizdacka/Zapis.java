package com.example.xbedt01.projizdacka;

/**
 * Created by xbedt01 on 15. 12. 2016.
 */
public class Zapis {
    public Integer typ; // 0 jizda, 1 benzin, 2 servis
    public String datum; // datum, datum, datum
    public String atr1; // misto, misto, co
    public Double atr2; // ujeto km, cena, cena
    public Double tach; // stav tachometru, cena za litr, -
    public Double atr3; // prumerna spotreba, pocet litru, -
    public Double atr4; // teoreticka cena, teoreticky pocet km, -

    public Zapis(String typ, String datum, String atr1, String atr2, String tach, String atr3, String atr4) {
        this.typ = Integer.parseInt(typ);
        this.datum = datum;
        this.atr1 = atr1;
        this.atr2 = Double.parseDouble(atr2);
        this.tach = Double.parseDouble(tach);
        this.atr3 = Double.parseDouble(atr3);
        this.atr4 = Double.parseDouble(atr4);
    }
}
