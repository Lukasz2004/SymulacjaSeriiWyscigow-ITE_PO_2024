import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Kierowca extends Personel implements Ulepszenie {
    public Pojazd pojazd;
    public double predkoscProsta;
    public double predkoscZakret;
    public double umiejetnoscWyprzedania;
    public double umiejetnoscObrony;
    public double agresywnosc;
    public double adaptacjaPogoda;
    public double ekonomicznoscJazdy;
    public double czasPrzejazdu;
    public boolean czyWPitstopie;
    public boolean czyEliminacja;

    //Dane potrzebne do tworzenia statystyk
    public ArrayList<Double> statystykiOkrazenia=new ArrayList<>();
    public ArrayList<String> statystykiWynikow=new ArrayList<>();
    public ArrayList<Integer> statystykiWyprzedzenia=new ArrayList<>();
    public int punktyZaPozycje=0;

    @Override
    public void ulepszStatystyki(double wartosc) {
        Random ulepszenie = new Random();
        int wybor = ulepszenie.nextInt(7);
        if(wybor == 0) this.predkoscProsta += wartosc;
        if(wybor == 1) this.predkoscZakret += wartosc;
        if(wybor == 2) this.umiejetnoscWyprzedania += wartosc;
        if(wybor == 3) this.umiejetnoscObrony += wartosc;
        if(wybor == 4) this.agresywnosc += wartosc;
        if(wybor == 5) this.adaptacjaPogoda += wartosc;
        if(wybor == 6) this.ekonomicznoscJazdy += wartosc;
    }

    public Kierowca(Druzyna druzynaInput, Pojazd pojazdInput, List<String> dane) {
        this.druzyna = druzynaInput;
        this.imie = dane.get(1);
        this.nazwisko = dane.get(2);
        this.narodowosc = dane.get(3);
        this.wiek = Integer.parseInt(dane.get(4));
        this.pojazd = pojazdInput;
        this.predkoscProsta = Double.parseDouble(dane.get(6));
        this.predkoscZakret = Double.parseDouble(dane.get(7));
        this.umiejetnoscWyprzedania = Double.parseDouble(dane.get(8));
        this.umiejetnoscObrony = Double.parseDouble(dane.get(9));
        this.agresywnosc = Double.parseDouble(dane.get(10));
        this.adaptacjaPogoda = Double.parseDouble(dane.get(11));
        this.ekonomicznoscJazdy = Double.parseDouble(dane.get(12));
        this.czasPrzejazdu = 0;
        this.czyWPitstopie = false;
        this.czyEliminacja = false;
    }

}
