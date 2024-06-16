import java.util.List;
import java.util.Random;
/** Ta klasa zawiera informacje dotyczace pojazdu.
 * <p>Klasa wchodzi w relacje z klasami {@link Kierowca} oraz {@link Mechanik}</p>
 * <p>Posiada konstrukory i metode implementowana z interface {@link Ulepszenie}</p>
 */
public class Pojazd implements Ulepszenie {
    /** Przechowuje nazwe pojazdu
     */
    public String nazwa;
    /** Przechowuje mechanika, ktory wykonuje serwis pojazdu podczas pitstopu
     */
    public Mechanik mechanik;
    /** Przechowuje wartosc, ktora wplywa na czas osiagany na odcinkach prostych
     */
    public double szybkosc;
    /** Przechowuje wartosc, ktora wplywa na czas osiagany na zakretach oraz na szybkosc zuzuwania opon
     */
    public double przyczepnosc;
    /** Przechowuje ilosc paliwa podczas wyscigu
     */
    public double stanPaliwa;
    /** Przechowuje wartosc, ktora okresla stopien zuzycia opon podczas wyscigu
     */
    public double stanOpon;

    //Dokonuje ulepszen parametrow pomiedzy wyscigami
    /** Metoda ulepsza jedna statystyke o podana wartosc
     * <p>Parametr, ktory zostanie ulepszony jest losowany z ponizszych: </p>
     * <ul>
     *            <li>{@link #szybkosc}</li>
     *            <li>{@link #przyczepnosc}</li>
     *  </ul>
     * @param wartosc double zawieracy wartosc ulepszenia
     */
    @Override
    public void ulepszStatystyki(double wartosc) {
        Random ulepszenie = new Random();
        int wybor;
        wybor = ulepszenie.nextInt(2);
        if(wybor == 0) this.szybkosc += wartosc;
        if(wybor == 1) this.przyczepnosc += wartosc;
    }

    /** Konstruktor zwykly
     *  @param mechanik Mechanik, ktory wykonuje serwis pojazdu podczas pitstopu
     *  @param dane Lista String zawierajaca inforamcje dotyczace pojazdu pobrane z pliku csv
     */
    public Pojazd (Mechanik mechanik, List<String> dane)
    {
        this.nazwa = dane.get(0);
        this.mechanik = mechanik;
        this.szybkosc = Double.parseDouble(dane.get(2));
        this.przyczepnosc = Double.parseDouble(dane.get(3));
        this.stanPaliwa = 50;
        this.stanOpon = 100;

    }
    /** Konstruktor kopiujacy
     *  @param p Pojazd, od ktorego kopiowane sa wartosci dla tworzonego nowego pojazdu
     */
    public Pojazd (Pojazd p)
    {
        this.nazwa = p.nazwa;
        this.mechanik = p.mechanik;
        this.szybkosc = p.szybkosc;
        this.przyczepnosc = p.przyczepnosc;
        this.stanPaliwa = p.stanPaliwa;
        this.stanOpon = p.stanOpon;

    }

}
