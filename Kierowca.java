import java.util.List;

public class Kierowca extends Personel {
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
    }
}
