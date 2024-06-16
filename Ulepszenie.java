/** Interface zawiera metode wykorzystywana do ulepszen obiektow pomiedzy wyscigami. Wywolywany jest przez {@link Main#ulepszenia()}.
 * Korzystaja z niego klasy::
 *       <ul>
 *             <li>{@link Kierowca}</li>
 *             <li>{@link Mechanik}</li>
 *             <li>{@link Pojazd}</li>
 *      </ul>
 */
public interface Ulepszenie {
    /** Metoda ulepsza jedna statystyke z dostepnych o podana wartosc
     * @param wartosc double zawieracy wartosc ulepszenia
     */
    public void ulepszStatystyki(double wartosc);
}
