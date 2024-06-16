/** Ta klasa zawiera informacje dotyczaca druzyny.
 * <p>Klasa wchodzi w relacje z klasa {@link Personel}</p>
 * <p>Posiada konstruktor tworzacy obiekt </p>
 */
public class Druzyna {
    /** Przechowuje nazwe druzyny
     */
    String NazwaDruzyny;
    /** Konstruktor zwykly
     *  @param NazwaDruzyny String zawierajacy nazwe druzyny pobrany z pliku csv
     */
    public Druzyna(String NazwaDruzyny)
    {
        this.NazwaDruzyny = NazwaDruzyny;
    }
}