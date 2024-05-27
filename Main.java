import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static final int liczbaOkrazenNaTor = 50;
    private static ArrayList<Kierowca> listaKierowcow = new ArrayList<Kierowca>();
    private static ArrayList<Tor> listaTorow = new ArrayList<Tor>();
    private static ArrayList<Druzyna> listaDruzyn = new ArrayList<Druzyna>();
    private static ArrayList<Pojazd> listaPojazdow = new ArrayList<Pojazd>();
    private static ArrayList<Mechanik> listaMechanikow = new ArrayList<Mechanik>();
    public static void main(String[] args){
        wczytajDane();

        for(int nrWyscigu=1; nrWyscigu<=listaTorow.size(); nrWyscigu++)
        {
            uruchomWyscig(listaTorow.get(nrWyscigu - 1));
            zapiszWyniki();
        }
        pokazWyniki();
        zapiszWyniki();

    }

    private static void uruchomWyscig(Tor tor){
        System.out.println("TOR: "+tor.nazwa);
        System.out.println("START !!!");
        for(int okrazenie=1; okrazenie<=liczbaOkrazenNaTor; okrazenie++)
        {
            System.out.println("OKRĄŻENIE: " + okrazenie);
            for(int i=0; i<listaKierowcow.size();i++)
            {
                przejazdKierowcy(listaKierowcow.get(i), tor);
            }

            for(int i=1; i<listaKierowcow.size();i++)
            {
                wyprzedzanie(i);
            }


        }
        System.out.println("META !!!");
    }
    private static void przejazdKierowcy(Kierowca kierowca, Tor tor)
    {
        double czasPrzejazdu = (kierowca.predkoscProsta*kierowca.pojazd.szybkosc/tor.procentProstych) + (kierowca.predkoscZakret*kierowca.pojazd.przyczepnosc/tor.procentZakretow);
        if(tor.czyPada) czasPrzejazdu = czasPrzejazdu*kierowca.adaptacjaPogoda;
        czasPrzejazdu = tor.dlugosc/czasPrzejazdu;
        kierowca.pojazd.stanPaliwa = kierowca.pojazd.stanPaliwa - (czasPrzejazdu/kierowca.ekonomicznoscJazdy);
        kierowca.pojazd.stanOpon = kierowca.pojazd.stanOpon -(czasPrzejazdu/kierowca.ekonomicznoscJazdy*kierowca.pojazd.przyczepnosc);
        if(kierowca.pojazd.stanPaliwa < 5 || kierowca.pojazd.stanOpon < 10 )
        {
            czasPrzejazdu = czasPrzejazdu + pitstop(kierowca);
        }
        kierowca.czasPrzejazdu = kierowca.czasPrzejazdu + czasPrzejazdu;


        System.out.print("CZAS: "+kierowca.imie+" "+kierowca.czasPrzejazdu);
        System.out.println();
    }
    private static double pitstop(Kierowca kierowca){
    double czasPrzejazdu = 0;
    Random czas = new Random();
    if(kierowca.pojazd.stanPaliwa < 12.5)
    {
        czasPrzejazdu = czasPrzejazdu +(czas.nextDouble() * kierowca.pojazd.mechanik.szybkosc);
        kierowca.pojazd.stanPaliwa = 50;
    }
    if(kierowca.pojazd.stanOpon < 25)
    {
        czasPrzejazdu = czasPrzejazdu +(czas.nextDouble() * kierowca.pojazd.mechanik.szybkosc);
        kierowca.pojazd.stanOpon = 100;
    }
    System.out.print("PITSTOP ");
    return czasPrzejazdu;
    }

    private static void wyprzedzanie(int pozKierowcy)
    {
        Kierowca kierowca1 = listaKierowcow.get(pozKierowcy-1);
        Kierowca kierowca2 = listaKierowcow.get(pozKierowcy);
        Random wyprzedzanie = new Random();

        if(kierowca1.czasPrzejazdu-kierowca2.czasPrzejazdu<0.5 && kierowca1.czasPrzejazdu-kierowca2.czasPrzejazdu>0)
        {
            System.out.println(kierowca1.imie+" zaczyna wyprzedzac ");
            if((kierowca1.umiejetnoscWyprzedania*kierowca1.agresywnosc*wyprzedzanie.nextDouble())>(kierowca2.umiejetnoscObrony*kierowca2.agresywnosc*wyprzedzanie.nextDouble()))
            {
                kierowca1.czasPrzejazdu = kierowca1.czasPrzejazdu - 0.5;
                kierowca2.czasPrzejazdu = kierowca2.czasPrzejazdu + 0.5;

                System.out.println(kierowca1.imie + " WYPRZEDZIŁ " + kierowca2.imie);
            }
        }
        if(kierowca2.czasPrzejazdu-kierowca1.czasPrzejazdu<0.5 && kierowca2.czasPrzejazdu-kierowca1.czasPrzejazdu>0)
        {
            System.out.println(kierowca2.imie+" zaczyna wyprzedzac ");
            if((kierowca2.umiejetnoscWyprzedania*kierowca2.agresywnosc*wyprzedzanie.nextDouble())>(kierowca1.umiejetnoscObrony*kierowca1.agresywnosc* wyprzedzanie.nextDouble()))
            {
                kierowca2.czasPrzejazdu = kierowca2.czasPrzejazdu - 0.5;
                kierowca1.czasPrzejazdu = kierowca1.czasPrzejazdu + 0.5;

                System.out.println(kierowca2.imie + " WYPRZEDZIŁ " + kierowca1.imie);
            }
        }

    }
    private static void zapiszWyniki() {

    }

    private static void pokazWyniki() {

    }


    private static List<String> liniaCSVnaDane(String linia) {
        List<String> wartosci = new ArrayList<String>();
        try (Scanner scanner = new Scanner(linia)) {
            scanner.useDelimiter(";");
            while (scanner.hasNext()) {
                wartosci.add(scanner.next());
            }
        }
        return wartosci;
    }
    private static void wczytajDane(){
        String sciezkaDaneDruzyn = "DaneStartowe/Druzyna.csv";
        String sciezkaDaneMechanikow = "DaneStartowe/Mechanik.csv";
        String sciezkaDanePojazdow = "DaneStartowe/Pojazd.csv";
        String sciezkaDaneKierowcow = "DaneStartowe/Kierowca.csv";
        String sciezkaDaneTorow = "DaneStartowe/Tor.csv";

        List<List<String>> linieDruzyn = new ArrayList<>();
        try (Scanner scannerDruzyn = new Scanner(new File(sciezkaDaneDruzyn))) {
            while (scannerDruzyn.hasNextLine()) {
                linieDruzyn.add(liniaCSVnaDane(scannerDruzyn.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < linieDruzyn.size(); i++) {
            Druzyna Druzyna = new Druzyna(linieDruzyn.get(i).get(0));
            listaDruzyn.add(Druzyna);
        }

        List<List<String>> linieMechanikow = new ArrayList<>();
        try (Scanner scannerMechanikow = new Scanner(new File(sciezkaDaneMechanikow))) {
            while (scannerMechanikow.hasNextLine()) {
                linieMechanikow.add(liniaCSVnaDane(scannerMechanikow.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < linieMechanikow.size(); i++) {
            Mechanik Mechanik = new Mechanik(
                    listaDruzyn.get(Integer.parseInt(linieMechanikow.get(i).get(0))-1),
                    linieMechanikow.get(i).get(1),
                    linieMechanikow.get(i).get(2),
                    linieMechanikow.get(i).get(3),
                    Integer.parseInt(linieMechanikow.get(i).get(4)),
                    Double.parseDouble(linieMechanikow.get(i).get(5))
                    );
            listaMechanikow.add(Mechanik);
        }

        List<List<String>> liniePojazdow = new ArrayList<>();
        try (Scanner scannerPojazdow = new Scanner(new File(sciezkaDanePojazdow))) {
            while (scannerPojazdow.hasNextLine()) {
                liniePojazdow.add(liniaCSVnaDane(scannerPojazdow.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < liniePojazdow.size(); i++) {
            Pojazd Pojazd = new Pojazd(
                    liniePojazdow.get(i).get(0),
                    listaMechanikow.get(Integer.parseInt(liniePojazdow.get(i).get(1))-1),
                    Double.parseDouble(liniePojazdow.get(i).get(2)),
                    Double.parseDouble(liniePojazdow.get(i).get(3))
            );
            listaPojazdow.add(Pojazd);
        }

        List<List<String>> linieKierowcow = new ArrayList<>();
        try (Scanner scannerKierowcow = new Scanner(new File(sciezkaDaneKierowcow))) {
            while (scannerKierowcow.hasNextLine()) {
                linieKierowcow.add(liniaCSVnaDane(scannerKierowcow.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < linieKierowcow.size(); i++) {
            Kierowca Kierowca = new Kierowca(
                    listaDruzyn.get(Integer.parseInt(linieKierowcow.get(i).get(0))-1),
                    linieKierowcow.get(i).get(1),
                    linieKierowcow.get(i).get(2),
                    linieKierowcow.get(i).get(3),
                    Integer.parseInt(linieKierowcow.get(i).get(4)),
                    listaPojazdow.get(Integer.parseInt(linieKierowcow.get(i).get(5))-1),
                    Double.parseDouble(linieKierowcow.get(i).get(6)),
                    Double.parseDouble(linieKierowcow.get(i).get(7)),
                    Double.parseDouble(linieKierowcow.get(i).get(8)),
                    Double.parseDouble(linieKierowcow.get(i).get(9)),
                    Double.parseDouble(linieKierowcow.get(i).get(10)),
                    Double.parseDouble(linieKierowcow.get(i).get(11)),
                    Double.parseDouble(linieKierowcow.get(i).get(12))
            );
            listaKierowcow.add(Kierowca);
        }

        List<List<String>> linieTorow = new ArrayList<>();
        try (Scanner scannerTorow = new Scanner(new File(sciezkaDaneTorow))) {
            while (scannerTorow.hasNextLine()) {
                linieTorow.add(liniaCSVnaDane(scannerTorow.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < linieTorow.size(); i++) {
            Tor Tor = new Tor(
                    linieTorow.get(i).get(0),
                    Double.parseDouble(linieTorow.get(i).get(1)),
                    Double.parseDouble(linieTorow.get(i).get(2)),
                    Double.parseDouble(linieTorow.get(i).get(3)),
                    Double.parseDouble(linieTorow.get(i).get(4))
            );
            listaTorow.add(Tor);
        }


    }
}
