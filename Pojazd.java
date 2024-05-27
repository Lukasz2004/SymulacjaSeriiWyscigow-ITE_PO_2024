import java.util.List;

public class Pojazd {
    public String nazwa;
    public Mechanik mechanik;
    public double szybkosc;
    public double przyczepnosc;
    public double stanPaliwa;
    public double stanOpon;

    public Pojazd (String nazwa, Mechanik mechanik, double szybkosc, double przyczepnosc)
    {
        this.nazwa = nazwa;
        this.mechanik = mechanik;
        this.szybkosc = szybkosc;
        this.przyczepnosc = przyczepnosc;
        this.stanPaliwa = 50;
        this.stanOpon = 100;

    }
    public Pojazd (Mechanik mechanik, List<String> dane)
    {
        this.nazwa = dane.get(0);
        this.mechanik = mechanik;
        this.szybkosc = Double.parseDouble(dane.get(2));
        this.przyczepnosc = Double.parseDouble(dane.get(3));
        this.stanPaliwa = 50;
        this.stanOpon = 100;

    }
}
