public class Kierowca extends Personel {
    public Pojazd pojazd;
    public double predkoscProsta;
    public double predkoscZakret;
    public double umiejetnoscWyprzedania;
    public double agresywnosc;
    public double adaptacjaPogoda;
    public double ekonomicznoscJazdy;
    public double odlegloscOdPoprzednika;

    public Kierowca(Druzyna druzyna,String imie, String nazwisko, String narodowosc, int wiek, Pojazd pojazd,double predkoscProsta, double predkoscZakret, double umiejetnoscWyprzedania, double agresywnosc, double adaptacjaPogoda, double ekonomicznoscJazdy) {
        this.druzyna = druzyna;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.narodowosc = narodowosc;
        this.wiek = wiek;
        this.pojazd = pojazd;
        this.predkoscProsta = predkoscProsta;
        this.predkoscZakret = predkoscZakret;
        this.umiejetnoscWyprzedania = umiejetnoscWyprzedania;
        this.agresywnosc = agresywnosc;
        this.adaptacjaPogoda = adaptacjaPogoda;
        this.ekonomicznoscJazdy = ekonomicznoscJazdy;
    }
}
