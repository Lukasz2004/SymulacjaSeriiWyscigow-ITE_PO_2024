import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/** Ta klasa zawiera informacje dotyczace kierowcy.
 * <p>Rozszerza klase abstrakyjna {@link Personel} </p>
 * <p>Klasa wchodzi w relacje z klasa {@link Pojazd}</p>
 * <p>Posiada konstrukor i metode implementowana z interface {@link Ulepszenie}</p>
 */
public class Kierowca extends Personel implements Ulepszenie {
    /** Przechowyje pojazd z ktorego korzysta kierowca
     */
    public Pojazd pojazd;
    /** Przechowyje wartosc, ktora wplywa na czas osiagany na odcinkach prostych
     */
    public double predkoscProsta;
    /** Przechowyje wartosc, ktora wplywa na czas osiagany na zakretach
     */
    public double predkoscZakret;
    /** Przechowyje wartosc, ktora wplywa na wyprzedzenia przeciwnikow
     */
    public double umiejetnoscWyprzedania;
    /** Przechowyje wartosc, ktora wplywa na obrone przed wyprzedzeniem przez przeciwnika
     */
    public double umiejetnoscObrony;
    /** Przechowyje wartosc, ktora wplywa na wyprzedzenia, obrone, możliwość zniszenia pojazdu i zakonczenia wyscigu
     */
    public double agresywnosc;
    /** Przechowyje wartosc, ktora wplywa odpornosc na warunki pogodowe panujace na torze
     */
    public double adaptacjaPogoda;
    /** Przechowyje wartosc, ktora wplywa na ilosc zuzywanego paliwa oraz opon podczas wycigu
     */
    public double ekonomicznoscJazdy;
    /** Przechowyje czas przejazdu podczas pojedynczego wyscigu
     */
    public double czasPrzejazdu;
    /** Przechowyje informacje czy kierowca zjechal do pitstopu
     */
    public boolean czyWPitstopie;
    /** Przechowyje informacje czy kierowca zostal wyelininowany podczas pojedynczego wyscigu
     */
    public boolean czyEliminacja;

    //Dane potrzebne do tworzenia statystyk
    /** Przechowyje statystyki okrazen w pojedynczym wyscigu
     */
    public ArrayList<Double> statystykiOkrazenia=new ArrayList<>();
    /** Przechowyje statystyki wynikow w sezonie
     */
    public ArrayList<String> statystykiWynikow=new ArrayList<>();
    /** Przechowyje statystyki wyprzedzen w sezonie
     */
    public ArrayList<Integer> statystykiWyprzedzenia=new ArrayList<>();
    /** Przechowyje ilosc punktow w sezonie
     */
    public int punktyZaPozycje=0;

    //Dokonuje ulepszen parametrow pomiedzy wyscigami
    /** Metoda ulepsza jedna statystyke o podana wartosc
     * <p>Parametr, ktory zostanie ulepszony jest losowany </p>
     * @param wartosc double zawieracy wartosc ulepszenia
     */
    @Override
    public void ulepszStatystyki(double wartosc) {
        Random ulepszenie = new Random();
        int wybor = ulepszenie.nextInt(7);
        if(wybor == 0) this.predkoscProsta += wartosc;
        if(wybor == 1) this.predkoscZakret += wartosc;
        if(wybor == 2) this.umiejetnoscWyprzedania += wartosc;
        if(wybor == 3) this.umiejetnoscObrony += wartosc;
        if(wybor == 4) this.agresywnosc += wartosc;
        if(wybor == 5) this.adaptacjaPogoda += wartosc;
        if(wybor == 6) this.ekonomicznoscJazdy += wartosc;
    }
    /** Konstruktor zwykly
     *  @param druzynaInput Druzyna do ktorej nalezy kierowca
     *  @param pojazdInput Pojazd, z ktorego korzysta kierowca
     *  @param dane Lista String zawierajaca inforamcje dotyczace kierowcy pobrane z pliku csv
     */
    public Kierowca(Druzyna druzynaInput, Pojazd pojazdInput, List<String> dane) {
        this.druzyna = druzynaInput;
        this.imie = dane.get(1);
        this.nazwisko = dane.get(2);
        this.narodowosc = dane.get(3);
        this.wiek = Integer.parseInt(dane.get(4));
        this.pojazd = pojazdInput;
        this.predkoscProsta = Double.parseDouble(dane.get(6));
        this.predkoscZakret = Double.parseDouble(dane.get(7));
        this.umiejetnoscWyprzedania = Double.parseDouble(dane.get(8));
        this.umiejetnoscObrony = Double.parseDouble(dane.get(9));
        this.agresywnosc = Double.parseDouble(dane.get(10));
        this.adaptacjaPogoda = Double.parseDouble(dane.get(11));
        this.ekonomicznoscJazdy = Double.parseDouble(dane.get(12));
        this.czasPrzejazdu = 0;
        this.czyWPitstopie = false;
        this.czyEliminacja = false;
    }

}
