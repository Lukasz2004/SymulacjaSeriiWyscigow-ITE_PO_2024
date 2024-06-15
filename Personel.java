/** Ta klasa abstrakcyjna zawiera informacje dotyczace podstawowych danych personelu.
 * <p>Klasa wchodzi w relacje z klasa {@link Druzyna}</p>
 * Rozszerzaja ja klasy:
 *     <ul>
 *           <li>{@link Kierowca}</li>
 *           <li>{@link Mechanik}</li>
 *    </ul>
 */
public abstract class Personel {
    /** Odwoluje sie do druzyny, do ktorej nalezy personel
     */
    public Druzyna druzyna;
    /** Przechowyje imie
     */
    public String imie;
    /** Przechowyje nazwisko
     */
    public String nazwisko;
    /** Przechowyje narodowosc
     */
    public String narodowosc;
    /** Przechowyje wiek
     */
    public int wiek;
}
