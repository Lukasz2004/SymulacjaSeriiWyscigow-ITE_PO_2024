import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

/** Klasa glowna ktora wywoluje inicjalizacje danych startowych, dokonuje calego procesu wyscigow oraz wywoluje zapisy danych.
 */
public class Main {
    /**
     * Parametr ilosci dokonywanych okrazen w ciagu jednego wyscigu. Przyjmuje sie jego stala wartosc wynoszaca <code>50</code>
     * @see #uruchomWyscig(Tor) 
     */
    private static final int liczbaOkrazenNaTor = 50;
    /**
     * Parametr globalny uzywany do analizowania wplywu roznych czynnikow na wynik symulacji - Wplywa On na wynik interakcji-wyprzedzen pomiedzy kierowcami.
     * Nie mylic z {@link Kierowca#agresywnosc}.
     * Jako domyslna przyjmuje sie wartosc <code>0.03</code>
     * @see #wyprzedzanie(int)
     */
    private static final double globalnaAgresywnosc = 0.03;//Standardowa: 0.03
    /**
     * Parametr globalny uzywany do analizowania wplywu roznych czynnikow na wynik symulacji - Wplywa On na ilosc ulepszen statystyk {@link Kierowca Kierowcow},
     * {@link Mechanik Mechanikow} oraz {@link Pojazd Pojazdow} dokonywanych pomiedzy wyscigami.
     * Jako domyslna przyjmuje sie wartosc <code>0.25</code>
     * @see #ulepszenia()
     * @see Ulepszenie
     * @see Kierowca#ulepszStatystyki(double)
     * @see Mechanik#ulepszStatystyki(double)
     * @see Pojazd#ulepszStatystyki(double)
     */
    private static final double globalnaWartoscUlepszen = 0.25;//Standardowa: 0.25
    /**
     * Parametr globalny uzywany do analizowania wplywu roznych czynnikow na wynik symulacji - Ustala On startowy {@link Pojazd#stanPaliwa} dla kazdego z {@link Pojazd Pojazdow} resetowany co wyscig i w trakcie pitstopow.
     * Jako domyslna przyjmuje sie wartosc <code>50</code>
     *  @see Pojazd#stanPaliwa
     */
    private static final double wymaganaPojemnoscPaliwa = 10;//Standardowa: 50



    /**
     * Lista przechowujaca wszystkie obiekty typu {@link Kierowca} w kolejnosci ich pozycji w wyscigu, czyli na podstawie {@link Kierowca#czasPrzejazdu}.
     * @see Kierowca
     */
    private static ArrayList<Kierowca> listaKierowcow = new ArrayList<>();
    /**
     * Lista przechowujaca wszystkie obiekty typu {@link Tor} w kolejnosci wczytania ich z danych startowych. Na tych torach w kolejnosci ich wystepowania
     * w liscie odbywaja sie kolejno wyscigi.
     * @see Tor
     */
    private static ArrayList<Tor> listaTorow = new ArrayList<>();


    /**
     * Glowna metoda calego programu. Wywoluje caly proces zaladowania danych, odegrania serii wyscigow oraz zapisania danych.
     * @param args
     */
    public static void main(String[] args) {
        ObslugaPlikow.wczytajDane();
        for(int nrWyscigu=1; nrWyscigu<=listaTorow.size(); nrWyscigu++)
        {
            System.out.println("Wyscig nr: " + String.valueOf(nrWyscigu));
            uruchomWyscig(listaTorow.get(nrWyscigu - 1));
            ObslugaPlikow.zapiszWyniki(false,"Okrazenia"); //Zapisuje dane na temat i-go wyscigu
            if(nrWyscigu<listaTorow.size()) ulepszenia();//Dokonuje wszystkich ulepszen
            Collections.reverse(listaKierowcow); //Zamienia kierowcow kolejnoscia by ostatni zaczynali na przodzie itd.
        }
        ObslugaPlikow.zapiszWyniki(true);
    }

    //Przeprowadza jeden wyscig na podanym torze

    /**
     * Przeprowadza jeden kompletny wyscig skladajacy sie z {@link #liczbaOkrazenNaTor} okrazen na podanym torze.
     * <p>Przed rozpoczeciem inicjalizuje dane potrzebne do wyscigu.</p>
     * <p>Dla kazdego okrazenia przeprowadzany jest proces:</p>
     * <ul>
     * <li>Dla kazdego kierowcy kolejno z {@link #listaKierowcow} przeprowadza {@link #przejazdKierowcy(Kierowca, Tor, double)}.</li>
     * <li>Nastepnie dla kazdej kolejnej pary na liscie dokonuje {@link #wyprzedzanie(int)}</li>
     * </ul>
     * @param tor Obiekt typu {@link Tor} na ktorym przeprowadzony zostanie wyscig
     */
    private static void uruchomWyscig(Tor tor){
        System.out.println("TOR: "+tor.nazwa);
        if(tor.czyPada) System.out.println("Bedzie dzis padac");
        System.out.println("START !!!");
        //Ustawianie domyslnych stanow kierowcow
        for(Kierowca i:listaKierowcow)
        {
            i.czasPrzejazdu=0.0;
            i.pojazd.stanPaliwa=wymaganaPojemnoscPaliwa;
            i.pojazd.stanOpon=100;
            i.czyWPitstopie=false;
            i.czyEliminacja=false;
            i.statystykiOkrazenia.clear();
            i.statystykiWyprzedzenia.add(0);
        }

        //Przeprowadzie okrazenia po okrazeniu
        for(int okrazenie=1; okrazenie<=liczbaOkrazenNaTor; okrazenie++)
        {
            System.out.println("\nOKRĄŻENIE: " + okrazenie);
            //Przejazdy kazdego z kolei kierowcy
            for(int i=0; i<listaKierowcow.size();i++)
            {
                if(i==0)
                {
                    przejazdKierowcy(listaKierowcow.get(i), tor, 0.0);
                }
                else {
                   przejazdKierowcy(listaKierowcow.get(i), tor, listaKierowcow.get(i-1).czasPrzejazdu);
                }
            }
            //Wyprzedzanie kazdej kolejnej pary kierowcow
            for(int i=1; i<listaKierowcow.size();i++)
            {
                wyprzedzanie(i);
            }

        }
        System.out.println("KONIEC WYSCIGU");
        pokazWyniki();

        //Przyznawanie punktow za miejsce po wyscigu
        for(int i=0; i<listaKierowcow.size();i++)
        {
            Kierowca kierowca = listaKierowcow.get(i);
            int punktyZaPozycje = listaKierowcow.size()-i;
            if(i==0){punktyZaPozycje +=2;}
            if(listaKierowcow.get(i).czyEliminacja)
            {
                kierowca.statystykiWynikow.add("DNF");
                continue;
            }
            kierowca.punktyZaPozycje+=punktyZaPozycje;
            kierowca.statystykiWynikow.add(String.valueOf(i+1));
        }
    }

    /**
     * Metoda do obliczania i aktualizacji pola czasu przejazdu pojedynczego okrazenia dla podanego kierowcy na podanym torze.
     * <p>Uwzglednia Ona wartosc losowa, rozne parametry kierowcow oraz wywoluje {@link #pitstop(Kierowca)} by sprawdzic koniecznosc pitstopu.</p>
     * <p>Czas przejazdu jest zablokowany tak aby nie mogl wynosic mniej niz <code>CzasPoprzednika</code>. </p>
     * @param kierowca Kierowca ktorego czas przejazdu jest obliczany
     * @param tor Tor na ktorym odbywa sie obliczane okrazenie
     * @param CzasPoprzednika {@link Kierowca#czasPrzejazdu} kierowcy znajdujacego sie na wyzszej pozycji w wyscigu. Obliczony w tej
     * funkcji czas przejazdu bedzie zawsze wiekszy badz zrownany do tej wartosci.
     */
    private static void przejazdKierowcy(Kierowca kierowca, Tor tor, double CzasPoprzednika)
    {
        if(kierowca.czyEliminacja) return;


        kierowca.czyWPitstopie=false;
        double czasPrzejazdu = (kierowca.predkoscProsta*kierowca.pojazd.szybkosc/tor.procentProstych) + (kierowca.predkoscZakret*kierowca.pojazd.przyczepnosc/tor.procentZakretow);
        Random randTime = new Random();
        czasPrzejazdu+=randTime.nextDouble();
        if(tor.czyPada) czasPrzejazdu = czasPrzejazdu*kierowca.adaptacjaPogoda;
        czasPrzejazdu = tor.dlugosc/czasPrzejazdu;
        kierowca.pojazd.stanPaliwa = kierowca.pojazd.stanPaliwa - (czasPrzejazdu/kierowca.ekonomicznoscJazdy);
        kierowca.pojazd.stanOpon = kierowca.pojazd.stanOpon -(czasPrzejazdu/kierowca.ekonomicznoscJazdy*kierowca.pojazd.przyczepnosc);
        //Warunek konieczny na skorzystanie z pitstopu
        if(kierowca.pojazd.stanPaliwa < 5 || kierowca.pojazd.stanOpon < 10 ) {
            kierowca.czyWPitstopie=true;
            czasPrzejazdu += pitstop(kierowca);
        }
        kierowca.czasPrzejazdu = Math.max(kierowca.czasPrzejazdu + czasPrzejazdu, CzasPoprzednika);
        kierowca.statystykiOkrazenia.add(kierowca.czasPrzejazdu);
    }

    /** Metoda obliczajaca czas jaki kierowca spedzil w pitstopie
     * <p>Kiedy kierowca zjezdza do pitstopu sprawdzany jest {@link Pojazd#stanPaliwa} oraz {@link Pojazd#stanOpon}</p>
     * <p>Jezeli stan jest za maly to czas w pitstopie jest zwiekszany o zmienna losowa modyfikowana przez {@link Mechanik#szybkosc} oraz uzupelniany jest stan</p>
     * @param kierowca Kierowca, dla ktorego obliczany jest czas pistopu
     * @return Czas pitstopu, ktory bedzie dodany do czasu przejazdu
     */
    private static double pitstop(Kierowca kierowca){
        double czasPitstopu = 0;
        Random czas = new Random();
        if(kierowca.pojazd.stanPaliwa < 12.5)
        {
            czasPitstopu += (czas.nextDouble() * kierowca.pojazd.mechanik.szybkosc);
            kierowca.pojazd.stanPaliwa = wymaganaPojemnoscPaliwa;
        }
        if(kierowca.pojazd.stanOpon < 25)
        {
            czasPitstopu += (czas.nextDouble() * kierowca.pojazd.mechanik.szybkosc);
            kierowca.pojazd.stanOpon = 100;
        }
        System.out.println(kierowca.imie +  " zjezdza na PITSTOP ");
        return czasPitstopu;
    }

    //Oblicza mozliwosc i rezultat wyprzedzania miedzy sasiadujaca para kierowcow
    /** Metoda sprawdzajaca mozliwosc interakcji miedzy kierowcami, wyprzedzenia badz eliminacji, oraz dokonujaca zamiany kolejnosci kierowcow w liscie
     * <p>W tej metodzie sprawdzana i dokonywana jest interakcja miedzy dwoma kierowcami:</p>
     * <ul>
     *     <li>{@link #listaKierowcow listakierowcow[pozKierowcy-1]} zwany <code> kierowca1</code> czyli kierowca ktory moze zostac wyprzedzony</li>
     *     <li>{@link #listaKierowcow listakierowcow[pozKierowcy]} zwany <code> kierowca2</code> czyli kierowca ktory moze dokonac wyprzedzenia</li>
     * </ul>
     * <p>W celu kontroli mozliwosci wyprzedzenia sprawdzane sa kryteria niezbedne:</p>
     * <ul>
     *     <li>{@link Kierowca#czyEliminacja kierowca2.czyEliminacja} == false </li>
     *     <li>{@link Kierowca#czyWPitstopie} == false</li>
     *     <li>{@link Kierowca#czasPrzejazdu}</li>
     * </ul>
     *<p>W celu sprawdzenia czy wyprzedzenie sie udalo porownywane sa</p>
     * <ul>
     *           <li>{@link Kierowca#umiejetnoscWyprzedania}</li>
     *           <li>{@link Kierowca#agresywnosc}</li>
     *           <li>{@link Main#globalnaAgresywnosc}</li>
     *           <li>Losowa zmienna</li>
     * </ul>
     * oraz
     * <ul>
     *           <li>{@link Kierowca#umiejetnoscObrony}</li>
     *           <li>{@link Kierowca#agresywnosc}</li>
     *           <li>{@link Main#globalnaAgresywnosc}</li>
     *           <li>Losowa zmienna</li>
     * </ul>
     * <p>Kiedy wyprzedzanie sie udalo nastepuje zamiana pozycji kierowcy1 i kierowcy2 w liscie oraz ich {@link Kierowca#czasPrzejazdu}. Udane wyprzedzanie jest rowniez odnotowane w {@link Kierowca#statystykiWyprzedzenia kierowca2.statystykiWyprzedzenia} </p>
     * <p>Kierowca moze wtedy rozpoczac kolejne wyprzedzenie poprzez ponowne wywolanie metody</p>
     * <p>Jezeli wyprzedzanie sie nie udalo losowa wartosc jest porownywana z:
     * <ul>
     *     <li>{@link Kierowca#umiejetnoscWyprzedania} lub {@link Kierowca#umiejetnoscObrony}</li>
     *     <li>{@link Kierowca#agresywnosc}</li>
     *     <li>{@link Main#globalnaAgresywnosc}</li>
     * </ul>
     * <p>W celu sprawdzenia czy nastapila awaria wyprzedzajacego lub zderzenie obu pojazdow</p>
     * <p>Jezeli jedno z powyzszych nastapilo kierowca lub kierowcy zostaja wyeliminowani oraz trafiaja na koniec listy</p>
     * @param pozKierowcy Pozycja kierowcy, ktory zaczyna wyprzedzanie
     * @see #globalnaAgresywnosc
     */
    private static void wyprzedzanie(int pozKierowcy)
    {
        Kierowca kierowca1 = listaKierowcow.get(pozKierowcy-1);
        Kierowca kierowca2 = listaKierowcow.get(pozKierowcy);
        Random wartoscLosowaWyprzedzenia = new Random();
        //Warunki konieczne do zajscia wyprzedzenia:
        if(kierowca2.czyEliminacja || kierowca2.czyWPitstopie || !(kierowca2.czasPrzejazdu-kierowca1.czasPrzejazdu<0.25))
        {
            return;
        }
        //Jesli spelnione to podjeta jest proba wyprzedzania

        //Warunki natychmiastowego wyprzedzenia
        boolean czyNatychmiastWyprzedza = kierowca1.czyWPitstopie;

        //Warunki rezultatu: UDANE WYPRZEDZENIE
        if(czyNatychmiastWyprzedza||(kierowca2.umiejetnoscWyprzedania*kierowca2.agresywnosc*wartoscLosowaWyprzedzenia.nextDouble()*globalnaAgresywnosc)>(kierowca1.umiejetnoscObrony*kierowca1.agresywnosc*wartoscLosowaWyprzedzenia.nextDouble()))
        {
            double zamiana;
            zamiana = kierowca2.czasPrzejazdu;
            kierowca2.czasPrzejazdu=kierowca1.czasPrzejazdu;
            kierowca1.czasPrzejazdu=zamiana;
            kierowca2.statystykiWyprzedzenia.set(kierowca2.statystykiWyprzedzenia.size()-1,kierowca2.statystykiWyprzedzenia.get(kierowca2.statystykiWyprzedzenia.size()-1)+1);
            listaKierowcow.set(pozKierowcy-1,kierowca2);
            listaKierowcow.set(pozKierowcy, kierowca1);

            System.out.println(kierowca2.imie + " WYPRZEDZIŁ " + kierowca1.imie);

            if(kierowca2.czasPrzejazdu==kierowca1.czasPrzejazdu&&pozKierowcy>2) //Kierowca ktory wyprzedzil podejmuje sie wyprzedzenia kolejnego
            {
                wyprzedzanie(pozKierowcy-1);
            }

        }
        //Warunki rezultatu: AWARIA WYPRZEDZAJACEGO
        else if(((kierowca2.umiejetnoscWyprzedania*kierowca2.agresywnosc*globalnaAgresywnosc))/2 > wartoscLosowaWyprzedzenia.nextDouble())
        {
            kierowca2.czasPrzejazdu = -1;
            kierowca2.czyEliminacja = true;
            System.out.println(kierowca2.imie + " WYPADEK - KONIEC");
            for(int i = pozKierowcy;i<listaKierowcow.size()-1;i++)
            {
                listaKierowcow.set(i,listaKierowcow.get(i+1));
            }
            listaKierowcow.set(listaKierowcow.size()-1, kierowca2);

            //Warunki rezultatu: ZDERZENIE OBU POJAZDOW
            if(((kierowca1.umiejetnoscObrony*kierowca1.agresywnosc*globalnaAgresywnosc))/5 > wartoscLosowaWyprzedzenia.nextDouble() && !kierowca1.czyWPitstopie)
            {
                kierowca1.czasPrzejazdu = -1;
                kierowca1.czyEliminacja = true;
                System.out.println(kierowca1.imie + " WYPADEK - KONIEC");
                for(int i = pozKierowcy-1;i<listaKierowcow.size()-1;i++)
                {
                    listaKierowcow.set(i,listaKierowcow.get(i+1));
                }
                listaKierowcow.set(listaKierowcow.size()-1, kierowca1);
            }

        }

    }


    //Dokonuje wszystkich ulepszen
    /** Metoda wywolujaca ulepszenia mechanika, pojazdu oraz kierowcy
     * <p>Wartosc ulepszenia to zmodyfikowana {@link Main#globalnaWartoscUlepszen}</p>
     * <p>Ulepszony zostaje kazdy kierowca, jego pojazd oraz mechanik danego pojazdu</p>
     */
    private static void ulepszenia()
    {
        double ulepszenieMechanikow = globalnaWartoscUlepszen/20;
        double ulepszeniePojazdow = globalnaWartoscUlepszen/10;
        double ulepszenieKierowcow = globalnaWartoscUlepszen/5;

        for(Kierowca i:listaKierowcow)
        {
            i.ulepszStatystyki(ulepszenieKierowcow);
            i.pojazd.ulepszStatystyki(ulepszeniePojazdow);
            i.pojazd.mechanik.ulepszStatystyki(ulepszenieMechanikow);
        }
    }

    /**
     * Metoda wyswietlajaca imiona oraz czasy przejazdu 3 pierwszych w wyscigu kierowcow.
     * @see #listaKierowcow
     */
    private static void pokazWyniki() {
        System.out.print("Podium: ");
        for(int i=0; i<Math.min(3,listaKierowcow.size());i++)
        {
            System.out.print(String.valueOf(i+1)+":" + listaKierowcow.get(i).imie + ", ");
        }
        System.out.println("\n");
    }

    /**
     * Metoda wczytujaca dane i zapisujaca je w {@link #listaKierowcow} oraz {@link #listaTorow}. Konieczna do inicjalizacji danych startowych.
     * @param inKierowca Arraylist obiektow typu {@link Kierowca} zawierajacy wszystkich kierowcow i ich parametry startowe
     * @param inTor Arraylist obiektow typu {@link Tor} zawierajacy wszystkie tory do rozegrania wyscigow
     */
    public static void setterDanych(ArrayList<Kierowca> inKierowca, ArrayList<Tor> inTor)
    {
        listaKierowcow=inKierowca;
        listaTorow=inTor;
    }

    /**
     * Metoda udostepniajaca liste obiektow wszystkich kierowcow. Uzywana do przekazywania danych do zapisania.
     * @return {@link #listaKierowcow}
     * @see #listaKierowcow
     */
    public static ArrayList<Kierowca> getListaKierowcow(){
        return listaKierowcow;
    }

    /**
     * Metoda udostepniajaca laczna ilosc torow. Przydatna do wyznaczania lacznej ilosci wyscigow.
     * @return Rozmiar listy zawierajaca wszystkie obiekty Torow przechowywane w {@link #listaTorow}
     * @see #listaTorow
     */
    public static Integer getIloscTorow()
    {
        return listaTorow.size();
    }

}
