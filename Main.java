import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    private static final int liczbaOkrazenNaTor = 50;
    private static ArrayList<Kierowca> listaKierowcow = new ArrayList<>();
    private static ArrayList<Tor> listaTorow = new ArrayList<>();
    private static ArrayList<Druzyna> listaDruzyn = new ArrayList<>();
    private static ArrayList<Pojazd> listaPojazdow = new ArrayList<>();
    private static ArrayList<Mechanik> listaMechanikow = new ArrayList<>();

    private static ArrayList<Integer> statystykiLiczbaWyprzedzen = new ArrayList<>();
    public static void main(String[] args){
        wczytajDane();

        for(int nrWyscigu=1; nrWyscigu<=listaTorow.size(); nrWyscigu++)
        {
            uruchomWyscig(listaTorow.get(nrWyscigu - 1));
            zapiszWyniki(listaKierowcow,"Kierowcy - Wyscig " + nrWyscigu);
        }
        zapiszWyniki(listaKierowcow,"Kierowcy - _Koncowe");
    }

    private static void uruchomWyscig(Tor tor){
        System.out.println("TOR: "+tor.nazwa);
        System.out.println("START !!!");
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
    }
    private static void przejazdKierowcy(Kierowca kierowca, Tor tor, Double CzasPoprzednika)
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
        Random wyprzedzanie = new Random();
        if(kierowca2.czasPrzejazdu-kierowca1.czasPrzejazdu<0.25||kierowca1.czyWPitstopie&&!kierowca2.czyWPitstopie)
        {
            System.out.println(kierowca2.imie+" zaczyna wyprzedzac ");
            if((kierowca2.umiejetnoscWyprzedania*kierowca2.agresywnosc*wyprzedzanie.nextDouble())>(kierowca1.umiejetnoscObrony*kierowca1.agresywnosc*wyprzedzanie.nextDouble())|| kierowca1.czyWPitstopie && !kierowca2.czyWPitstopie)
            {
                kierowca2.czasPrzejazdu=kierowca1.czasPrzejazdu;
                listaKierowcow.set(pozKierowcy-1,kierowca2);
                listaKierowcow.set(pozKierowcy, kierowca1);

                System.out.println(kierowca2.imie + " WYPRZEDZIŁ " + kierowca1.imie);
            }
        }

    }

    private static void pokazWyniki() {
        for (Kierowca kierowca : listaKierowcow) {
            System.out.print("CZAS: " + kierowca.imie + " " + kierowca.czasPrzejazdu);
            System.out.println();
        }
    }
    private static void zapiszWyniki(ArrayList <Kierowca> dane, String nazwaPliku) {
        File plik = new File("Wyniki/"+nazwaPliku+".csv");
        try (PrintWriter printWriter = new PrintWriter(plik)) {
            printWriter.println("Imie;Nazwisko;Czas");
            for(Kierowca i:dane)
            {
                String[] danee = new String[3];
                danee[0]=i.imie;
                danee[1]=i.nazwisko;
                danee[2]= String.valueOf(i.czasPrzejazdu);
                printWriter.println(daneNaLinieCSV(danee));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        //assertTrue(plik.exists());
    }

    private static List<String> liniaCSVnaDane(String linia) {
        List<String> wartosci = new ArrayList<>();
        try (Scanner scanner = new Scanner(linia)) {
            scanner.useDelimiter(";");
            while (scanner.hasNext()) {
                wartosci.add(scanner.next());
            }
        }
        return wartosci;
    }
    private static String daneNaLinieCSV(String[] dane) {
        return String.join(";", dane);
    }
    private static List<List<String>> odczytPliku(String sciezka)
    {
        List<List<String>> zwrotDanych = new ArrayList<>();
        try (Scanner scannerDruzyn = new Scanner(new File(sciezka))) {
            while (scannerDruzyn.hasNextLine()) {
                zwrotDanych.add(liniaCSVnaDane(scannerDruzyn.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        zwrotDanych.remove(0); //Usuwa naglowki
        return zwrotDanych;
    }
    private static void wczytajDane(){
        String sciezkaDaneDruzyn = "DaneStartowe/Druzyna.csv";
        String sciezkaDaneMechanikow = "DaneStartowe/Mechanik.csv";
        String sciezkaDanePojazdow = "DaneStartowe/Pojazd.csv";
        String sciezkaDaneKierowcow = "DaneStartowe/Kierowca.csv";
        String sciezkaDaneTorow = "DaneStartowe/Tor.csv";

        //Wczytuje druzyny
        for (List<String> druzynaInput : odczytPliku(sciezkaDaneDruzyn)) {
            Druzyna druzyna = new Druzyna(druzynaInput.get(0));
            listaDruzyn.add(druzyna);
        }

        //Wczytuje mechanikow
        for (List<String> mechanikInput : odczytPliku(sciezkaDaneMechanikow)) {
            Mechanik mechanik = new Mechanik(
                    listaDruzyn.get(Integer.parseInt(mechanikInput.get(0))-1),
                    mechanikInput
            );
            listaMechanikow.add(mechanik);
        }

        //Wczytuje pojazdy
        for (List<String> pojazdInput : odczytPliku(sciezkaDanePojazdow)) {
            Pojazd pojazd = new Pojazd(
                    listaMechanikow.get(Integer.parseInt(pojazdInput.get(1))-1),
                    pojazdInput
            );
            listaPojazdow.add(pojazd);
        }

        //Wczytuje kierowcow
        for (List<String> kierowcaInput : odczytPliku(sciezkaDaneKierowcow)) {
            Kierowca kierowca = new Kierowca(
                    listaDruzyn.get(Integer.parseInt(kierowcaInput.get(0))-1),
                    listaPojazdow.get(Integer.parseInt(kierowcaInput.get(5))-1),
                    kierowcaInput
            );
            listaKierowcow.add(kierowca);
        }

        //Wczytuje tory
        for (List<String> torInput : odczytPliku(sciezkaDaneTorow)) {
            Tor tor = new Tor(torInput);
            listaTorow.add(tor);
        }
    }
}
