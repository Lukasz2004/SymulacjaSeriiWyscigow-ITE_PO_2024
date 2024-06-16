import java.util.List;
import java.util.Random;
/** Ta klasa zawiera informacje dotyczace toru.
 * <p>Posiada konstrukor i metode losujaca warunki pogodowe</p>
 */
public class Tor {
    /** Przechowuje nazwe toru, uzywana w celach wizualnych i indentyfikacyjnych
     */
    public String nazwa;
    /** Przechowuje dlugosc toru w kilometrach, wykorzystywana do wyliczenia czasow przejazdu
     */
    public double dlugosc;
    /** Przechowuje procentowa wartosc odcinkow prostych na torze
     */
    public double procentProstych;
    /** Przechowuje procentowa wartosc zakretow na torze
     */
    public double procentZakretow;
    /** Przechowuje procentowa szanse na deszcz, ktory wplywa na osiagany czas przejadu
     */
    private double szansaNaDeszcz;
    /** Przechowuje informacje losowana w metodzie {@link #warunkiPogodowe()} czy na torze pada deszcz czy nie
     */
    public boolean czyPada;

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
     * <p>Wywolywana jest metoda {@link #warunkiPogodowe()}, ktora zwraca inforamcje czy na torze pada deszcz </p>
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
