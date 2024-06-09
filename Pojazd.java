import java.util.List;

public class Pojazd {
    public String nazwa;
    public Mechanik mechanik;
    public double szybkosc;
    public double przyczepnosc;
    public double stanPaliwa;
    public double stanOpon;

    public Pojazd (Mechanik mechanik, List<String> dane)
    {
        this.nazwa = dane.get(0);
        this.mechanik = mechanik;
        this.szybkosc = Double.parseDouble(dane.get(2));
        this.przyczepnosc = Double.parseDouble(dane.get(3));
        this.stanPaliwa = 50;
        this.stanOpon = 100;

    }
    public Pojazd (Pojazd p)
    {
        this.nazwa = p.nazwa;
        this.mechanik = mechanik;
        this.szybkosc = p.szybkosc;
        this.przyczepnosc = p.przyczepnosc;
        this.stanPaliwa = p.stanPaliwa;
        this.stanOpon = p.stanOpon;

    }
}
