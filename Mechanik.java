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
}
