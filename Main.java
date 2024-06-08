import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static final int liczbaOkrazenNaTor = 50;
    private static ArrayList<Kierowca> listaKierowcow = new ArrayList<>();
    private static ArrayList<Tor> listaTorow = new ArrayList<>();
    private static ArrayList<Druzyna> listaDruzyn = new ArrayList<>();
    private static ArrayList<Pojazd> listaPojazdow = new ArrayList<>();
    private static ArrayList<Mechanik> listaMechanikow = new ArrayList<>();

    public static void main(String[] args){
        ObslugaPlikow.wczytajDane();

        for(int nrWyscigu=1; nrWyscigu<=listaTorow.size(); nrWyscigu++)
        {
            uruchomWyscig(listaTorow.get(nrWyscigu - 1));
            ObslugaPlikow.zapiszWyniki(false,"Okrazenia");
        }
        ObslugaPlikow.zapiszWyniki(true);
    }
    private static void uruchomWyscig(Tor tor){
        System.out.println("TOR: "+tor.nazwa);
        if(tor.czyPada) System.out.println("Bedzie dzis padac");
        System.out.println("START !!!");
        for(Kierowca i:listaKierowcow)
        {
            i.czasPrzejazdu=0.0;
            i.pojazd.stanPaliwa=50;
            i.pojazd.stanOpon=100;
            i.statystykiOkrazenia.clear();
            i.statystykiWyprzedzenia.add(0);
        }

        for(int okrazenie=1; okrazenie<=liczbaOkrazenNaTor; okrazenie++)
        {
            System.out.println("\nOKRĄŻENIE: " + okrazenie);
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
            for(int i=1; i<listaKierowcow.size();i++)
            {
                wyprzedzanie(i);
            }
            pokazWyniki();

        }
        System.out.println("META !!!");
        for(int i=0; i<listaKierowcow.size();i++)
        {
            Kierowca kierowca = listaKierowcow.get(i);
            Integer punktyZaPozycje = listaKierowcow.size()-i;
            if(i==0){punktyZaPozycje +=2;}
            kierowca.punktyZaPozycje+=punktyZaPozycje;
            kierowca.statystykiWynikow.add(i+1);
        }
    }
    private static void przejazdKierowcy(Kierowca kierowca, Tor tor, double CzasPoprzednika)
    {
        kierowca.czyWPitstopie=false;
        double czasPrzejazdu = (kierowca.predkoscProsta*kierowca.pojazd.szybkosc/tor.procentProstych) + (kierowca.predkoscZakret*kierowca.pojazd.przyczepnosc/tor.procentZakretow);
        Random randTime = new Random();
        czasPrzejazdu+=randTime.nextDouble();
        if(tor.czyPada) czasPrzejazdu = czasPrzejazdu*kierowca.adaptacjaPogoda;
        czasPrzejazdu = tor.dlugosc/czasPrzejazdu;
        kierowca.pojazd.stanPaliwa = kierowca.pojazd.stanPaliwa - (czasPrzejazdu/kierowca.ekonomicznoscJazdy);
        kierowca.pojazd.stanOpon = kierowca.pojazd.stanOpon -(czasPrzejazdu/kierowca.ekonomicznoscJazdy*kierowca.pojazd.przyczepnosc);
        if(kierowca.pojazd.stanPaliwa < 5 || kierowca.pojazd.stanOpon < 10 ) {
            kierowca.czyWPitstopie=true;
            czasPrzejazdu += pitstop(kierowca);
        }
        kierowca.czasPrzejazdu = Math.max(kierowca.czasPrzejazdu + czasPrzejazdu, CzasPoprzednika);
        kierowca.statystykiOkrazenia.add(kierowca.czasPrzejazdu);
    }
    private static double pitstop(Kierowca kierowca){
        double czasPitstopu = 0;
        Random czas = new Random();
        if(kierowca.pojazd.stanPaliwa < 12.5)
        {
            czasPitstopu += (czas.nextDouble() * kierowca.pojazd.mechanik.szybkosc);
            kierowca.pojazd.stanPaliwa = 50;
        }
        if(kierowca.pojazd.stanOpon < 25)
        {
            czasPitstopu += (czas.nextDouble() * kierowca.pojazd.mechanik.szybkosc);
            kierowca.pojazd.stanOpon = 100;
        }
        System.out.println(kierowca.imie +  " zjezdza na PITSTOP ");
        return czasPitstopu;
    }

    private static void wyprzedzanie(int pozKierowcy)
    {
        Kierowca kierowca1 = listaKierowcow.get(pozKierowcy-1);
        Kierowca kierowca2 = listaKierowcow.get(pozKierowcy);
        double zamiana;
        Random wyprzedzanie = new Random();

        if(kierowca2.czasPrzejazdu-kierowca1.czasPrzejazdu<0.25||kierowca1.czyWPitstopie&&!kierowca2.czyWPitstopie)
        {
            System.out.println(kierowca2.imie+" zaczyna wyprzedzac ");
            if((kierowca2.umiejetnoscWyprzedania*kierowca2.agresywnosc*wyprzedzanie.nextDouble())>(kierowca1.umiejetnoscObrony*kierowca1.agresywnosc*wyprzedzanie.nextDouble())|| kierowca1.czyWPitstopie && !kierowca2.czyWPitstopie)
            {
                zamiana = kierowca2.czasPrzejazdu;
                kierowca2.czasPrzejazdu=kierowca1.czasPrzejazdu;
                kierowca1.czasPrzejazdu=zamiana;
                kierowca2.statystykiWyprzedzenia.set(kierowca2.statystykiWyprzedzenia.size()-1,kierowca2.statystykiWyprzedzenia.get(kierowca2.statystykiWyprzedzenia.size()-1)+1);
                listaKierowcow.set(pozKierowcy-1,kierowca2);
                listaKierowcow.set(pozKierowcy, kierowca1);

                System.out.println(kierowca2.imie + " WYPRZEDZIŁ " + kierowca1.imie);

                if(kierowca2.czasPrzejazdu==kierowca1.czasPrzejazdu&&pozKierowcy>2)
                {
                    wyprzedzanie(pozKierowcy-1);
                }

            }
        }

    }

    private static void pokazWyniki() {
        for (Kierowca kierowca : listaKierowcow) {
            System.out.print("CZAS: " + kierowca.imie + " " + kierowca.czasPrzejazdu);
            System.out.println();
        }
    }
    public static void setterDanych(ArrayList<Kierowca> inKierowca, ArrayList<Tor> inTor, ArrayList<Druzyna> inDruzyna, ArrayList<Pojazd> inPojazd, ArrayList<Mechanik> inMechanik)
    {
        listaKierowcow=inKierowca;
        listaTorow=inTor;
        listaDruzyn=inDruzyna;
        listaPojazdow=inPojazd;
        listaMechanikow=inMechanik;
    }
    public static ArrayList<Kierowca> getListaKierowcow(){
        return listaKierowcow;
    }
    public static Integer getIloscTorow()
    {
        return listaTorow.size();
    }

}
