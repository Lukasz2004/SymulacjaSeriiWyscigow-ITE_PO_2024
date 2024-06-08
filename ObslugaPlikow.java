import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ObslugaPlikow {
    public static void wczytajDane(){
        String sciezkaDaneDruzyn = "DaneStartowe/Druzyna.csv";
        String sciezkaDaneMechanikow = "DaneStartowe/Mechanik.csv";
        String sciezkaDanePojazdow = "DaneStartowe/Pojazd.csv";
        String sciezkaDaneKierowcow = "DaneStartowe/Kierowca.csv";
        String sciezkaDaneTorow = "DaneStartowe/Tor.csv";

        ArrayList<Kierowca> listaKierowcow = new ArrayList<>();
        ArrayList<Tor> listaTorow = new ArrayList<>();
        ArrayList<Druzyna> listaDruzyn = new ArrayList<>();
        ArrayList<Pojazd> listaPojazdow = new ArrayList<>();
        ArrayList<Mechanik> listaMechanikow = new ArrayList<>();

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

        Main.setterDanych(listaKierowcow,listaTorow,listaDruzyn,listaPojazdow,listaMechanikow);
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







    public static void zapiszWyniki(boolean czyWszystkieKoncowe)
    {
        if(czyWszystkieKoncowe)
        {
            zapiszWyniki(false,"Wyniki");
            zapiszWyniki(false,"Wyprzedzenia");
            zapiszWyniki(false,"Parametry");
        }
        else
        {
            throw new RuntimeException("Nie sprecyzowano typu zapisu wynikow.");
        }
    }
    public static void zapiszWyniki(boolean czyWszystkieKoncowe, String typZapisu)
    {
        if (czyWszystkieKoncowe)
        {
            zapiszWyniki(true);
        }
        ArrayList<Kierowca> listaKierowcow = Main.getListaKierowcow();

        ArrayList<ArrayList<String>> arrayWynikowy = new ArrayList<>();
        ArrayList<String> arrayNaglowki = new ArrayList<>();
        if(typZapisu.equals("Wyniki"))
        {
            listaKierowcow.sort(new Comparator<Kierowca>() {
                @Override
                public int compare(Kierowca o1, Kierowca o2) {
                    return o1.punktyZaPozycje > o2.punktyZaPozycje ? -1 : (o1.punktyZaPozycje < o2.punktyZaPozycje) ? 1 : 0;
                }
            });
            arrayNaglowki.add("Imie");
            arrayNaglowki.add("Nazwisko");
            for(int i=0; i<Main.getIloscTorow();i++)
            {
                arrayNaglowki.add("Wyscig "+String.valueOf(i+1));
            }
            arrayNaglowki.add("Suma punktow");
            arrayWynikowy.add(arrayNaglowki);

            for(Kierowca kierowca : listaKierowcow)
            {
                ArrayList<String> arrayDanowy = new ArrayList<>();
                arrayDanowy.add(kierowca.imie);
                arrayDanowy.add(kierowca.nazwisko);
                for(Integer i:kierowca.statystykiWynikow)
                {
                    arrayDanowy.add(String.valueOf(i));
                }
                arrayDanowy.add(String.valueOf(kierowca.punktyZaPozycje));
                arrayWynikowy.add(arrayDanowy);
            }
            zapisPliku(arrayWynikowy,"_PozycjeWSezonie");
        }
        if(typZapisu.equals("Wyprzedzenia"))
        {
            arrayNaglowki.add("Imie");
            arrayNaglowki.add("Nazwisko");
            for(int i=0; i<Main.getIloscTorow();i++)
            {
                arrayNaglowki.add("Wyscig "+String.valueOf(i+1));
            }
            arrayNaglowki.add("Suma");
            arrayWynikowy.add(arrayNaglowki);

            for(Kierowca kierowca : listaKierowcow)
            {
                ArrayList<String> arrayDanowy = new ArrayList<>();
                arrayDanowy.add(kierowca.imie);
                arrayDanowy.add(kierowca.nazwisko);
                Integer sumaWyprzedzenKierowcy = 0;
                for(Integer i:kierowca.statystykiWyprzedzenia)
                {
                    arrayDanowy.add(String.valueOf(i));
                    sumaWyprzedzenKierowcy+=i;
                }
                arrayDanowy.add(String.valueOf(sumaWyprzedzenKierowcy));
                arrayWynikowy.add(arrayDanowy);
            }
            zapisPliku(arrayWynikowy,"_Wyprzedzenia");
        }
        if(typZapisu.equals("Parametry"))
        {
            arrayNaglowki.add("Imie");
            arrayNaglowki.add("Nazwisko");
            arrayNaglowki.add("Predkosc na prostych");
            arrayNaglowki.add("Predkosc na zakretach");
            arrayNaglowki.add("Umiejetnosc wyprzedzania");
            arrayNaglowki.add("Umiejetnosc obrony");
            arrayNaglowki.add("Agresywnosc");
            arrayNaglowki.add("Adaptacja do pogody");
            arrayNaglowki.add("Ekonomicznosc jazdy");
            arrayNaglowki.add("Szybkosc pojazdu");
            arrayNaglowki.add("Przyczepnosc pojazdu");
            arrayNaglowki.add("Predkosc mechanika");
            arrayWynikowy.add(arrayNaglowki);

            for(Kierowca kierowca : listaKierowcow)
            {
                ArrayList<String> arrayDanowy = new ArrayList<>();
                arrayDanowy.add(kierowca.imie);
                arrayDanowy.add(kierowca.nazwisko);
                arrayDanowy.add(String.valueOf(kierowca.predkoscProsta));
                arrayDanowy.add(String.valueOf(kierowca.predkoscZakret));
                arrayDanowy.add(String.valueOf(kierowca.umiejetnoscWyprzedania));
                arrayDanowy.add(String.valueOf(kierowca.umiejetnoscObrony));
                arrayDanowy.add(String.valueOf(kierowca.agresywnosc));
                arrayDanowy.add(String.valueOf(kierowca.adaptacjaPogoda));
                arrayDanowy.add(String.valueOf(kierowca.ekonomicznoscJazdy));

                arrayDanowy.add(String.valueOf(kierowca.pojazd.szybkosc));
                arrayDanowy.add(String.valueOf(kierowca.pojazd.przyczepnosc));

                arrayDanowy.add(String.valueOf(kierowca.pojazd.mechanik.szybkosc));
                arrayWynikowy.add(arrayDanowy);
            }
            zapisPliku(arrayWynikowy,"_UmiejetnosciKierowcowPoSezonie");
        }
        if(typZapisu.equals("Okrazenia"))
        {
            arrayNaglowki.add("Imie");
            arrayNaglowki.add("Nazwisko");
            for(int i=0; i<listaKierowcow.get(0).statystykiOkrazenia.size();i++)
            {
                arrayNaglowki.add("Okrazenie "+String.valueOf(i+1));
            }
            arrayNaglowki.add("Pozycja koncowa");
            arrayWynikowy.add(arrayNaglowki);

            for(int i=0; i<listaKierowcow.size();i++)
            {
                Kierowca kierowca = listaKierowcow.get(i);
                ArrayList<String> arrayDanowy = new ArrayList<>();
                arrayDanowy.add(kierowca.imie);
                arrayDanowy.add(kierowca.nazwisko);
                for(Double ii:kierowca.statystykiOkrazenia)
                {
                    arrayDanowy.add(String.valueOf(ii));
                }
                arrayDanowy.add(String.valueOf(i+1));
                arrayWynikowy.add(arrayDanowy);
            }
            zapisPliku(arrayWynikowy,"Wyscig" + listaKierowcow.get(0).statystykiWynikow.size());
        }

    }
    private static void zapisPliku(ArrayList<ArrayList<String>> dane, String nazwaPliku) {
        File plik = new File("Wyniki/"+nazwaPliku+".csv");
        try (PrintWriter printWriter = new PrintWriter(plik)) {
            for(ArrayList <String> i:dane)
            {
                String stringWynikowy="";
                for(String str:i)
                {
                    if(!stringWynikowy.isEmpty())
                        stringWynikowy+=";";
                    stringWynikowy+=str;
                }
                printWriter.println(stringWynikowy);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
