import java.util.List;
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
        if (deszcz.nextDouble() < szansaNaDeszcz) {
            System.out.println("Bedzie dzis padac");
            return true;
        }
        else
            return false;
    }
    public Tor(List<String> dane) {
        this.nazwa = dane.get(0);
        this.dlugosc = Double.parseDouble(dane.get(1));
        this.procentProstych = Double.parseDouble(dane.get(2));
        this.procentZakretow = Double.parseDouble(dane.get(3));
        this.szansaNaDeszcz = Double.parseDouble(dane.get(4));
        this.czyPada = warunkiPogodowe();
    }
}
