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
        this.stanPaliwa = 1;
        this.stanOpon = 1;

    }
}
