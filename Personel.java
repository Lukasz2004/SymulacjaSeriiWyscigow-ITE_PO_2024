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
    /** Przechowuje imie
     */
    public String imie;
    /** Przechowuje nazwisko
     */
    public String nazwisko;
    /** Przechowuje narodowosc
     */
    public String narodowosc;
    /** Przechowuje wiek
     */
    public int wiek;
}
