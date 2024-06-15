import java.util.List;
import java.util.Random;
/** Ta klasa zawiera informacje dotyczace toru.
 * <p>Posiada konstrukor i metode losujaca warunki pogodowe</p>
 */
public class Tor {
    /** Przechowyje nazwe toru
     */
    public String nazwa;
    /** Przechowyje dlugosc toru w kilometrach
     */
    public double dlugosc;
    /** Przechowyje procentowa wartosc odcinkow prostych na torze
     */
    public double procentProstych;
    /** Przechowyje procentowa wartosc zakretow na torze
     */
    public double procentZakretow;
    /** Przechowyje procentowa szanse na deszcz
     */
    private double szansaNaDeszcz;
    /** Przechowyje informacje losowana w metodzie {@link #warunkiPogodowe()} czy na torze pada deszcz czy nie
     */
    public boolean czyPada;

    //Oblicza czy na danym torze spadnie deszcz
    /** Metoda losujaca warunki pogodowe na torze.
     * <p>Losowana wartosc porownywana jest ze zmienna {@link #szansaNaDeszcz}</p>
     * @return Informacje true/false czy na torze pada deszcz
     */
    private boolean warunkiPogodowe(){
        Random deszcz = new Random();
        if (deszcz.nextDouble() < szansaNaDeszcz) {
            return true;
        }
        else
            return false;
    }

    /** Konstruktor zwykly
     * <p>Wywolywana jest medota {@link #warunkiPogodowe()}, ktora zwraca inforamcje czy na torze pada deszcz </p>
     *  @param dane Lista String zawierajaca inforamcje dotyczace toru pobrane z pliku csv
     */
    public Tor(List<String> dane) {
        this.nazwa = dane.get(0);
        this.dlugosc = Double.parseDouble(dane.get(1));
        this.procentProstych = Double.parseDouble(dane.get(2));
        this.procentZakretow = Double.parseDouble(dane.get(3));
        this.szansaNaDeszcz = Double.parseDouble(dane.get(4));
        this.czyPada = warunkiPogodowe();
    }
}
