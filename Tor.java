import java.util.Random;

public class Tor {
    public String nazwa;
    public double dlugosc;
    public double procentProstych;
    public double procentZakretow;
    private double szansaNaDeszcz;
    public boolean czyPada;

    public boolean warunkiPogodowe(){
        Random deszcz = new Random();
        if(deszcz.nextDouble() < szansaNaDeszcz)
            return true;
        else
            return false;
    }


    public Tor(String nazwa, double dlugosc, double procentProstych, double procentZakretow, double szansaNaDeszcz) {
        this.nazwa = nazwa;
        this.dlugosc = dlugosc;
        this.procentProstych = procentProstych;
        this.procentZakretow = procentZakretow;
        this.szansaNaDeszcz = szansaNaDeszcz;
        this.czyPada = warunkiPogodowe();
    }
}
