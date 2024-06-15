import java.util.List;
/** Ta klasa zawiera informacje dotyczace mechanika.
 * <p>Rozszerza klase abstrakyjna {@link Personel} </p>
 * <p>Klasa wchodzi w relacje z klasa {@link Pojazd}</p>
 * <p>Posiada konstrukor i metode implementowana z interface {@link Ulepszenie}</p>
 */
public class Mechanik extends Personel implements Ulepszenie{
    /** Przechowyje wartosc, ktora definiuje szybkosc wykonywania serwisu pitstop
     */
    public double szybkosc;

    //Dokonuje ulepszen parametrow pomiedzy wyscigami
    /** Metoda ulepsza jedna statystyke o podana wartosc
     * @param wartosc double zawieracy wartosc ulepszenia
     */
    @Override
    public void ulepszStatystyki(double wartosc) {
        this.szybkosc += wartosc;
    }
    /** Konstruktor zwykly
     *  @param druzyna Druzyna do ktorej nalezy mechanik
     *  @param dane Lista String zawierajaca inforamcje dotyczace mechanika pobrane z pliku csv
     */
    public Mechanik(Druzyna druzyna, List<String> dane) {
        this.druzyna = druzyna;
        this.imie = dane.get(1);
        this.nazwisko = dane.get(2);
        this.narodowosc = dane.get(3);
        this.wiek = Integer.parseInt(dane.get(4));
        this.szybkosc = Double.parseDouble(dane.get(5));
    }
}
