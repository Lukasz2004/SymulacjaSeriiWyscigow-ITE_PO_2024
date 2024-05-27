import java.util.List;

public class Mechanik extends Personel {
    public double szybkosc;

    public Mechanik(Druzyna druzyna,String imie, String nazwisko, String narodowosc, int wiek, double szybkosc) {
        this.druzyna = druzyna;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.narodowosc = narodowosc;
        this.wiek = wiek;
        this.szybkosc = szybkosc;
    }
    public Mechanik(Druzyna druzyna, List<String> dane) {
        this.druzyna = druzyna;
        this.imie = dane.get(1);
        this.nazwisko = dane.get(2);
        this.narodowosc = dane.get(3);
        this.wiek = Integer.parseInt(dane.get(4));
        this.szybkosc = Double.parseDouble(dane.get(5));
    }
}
