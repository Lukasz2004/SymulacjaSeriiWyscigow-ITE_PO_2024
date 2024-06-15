/** Interface zawiera metode wykorzystywana do ulepszen obiektow
 * Korzystaja z niego klasy::
 *       <ul>
 *             <li>{@link Kierowca}</li>
 *             <li>{@link Mechanik}</li>
 *             <li>{@link Pojazd}</li>
 *      </ul>
 */
public interface Ulepszenie {
    /** Metoda ulepsza jedna statystyke o podana wartosc
     * @param wartosc double zawieracy wartosc ulepszenia
     */
    public void ulepszStatystyki(double wartosc);
}
