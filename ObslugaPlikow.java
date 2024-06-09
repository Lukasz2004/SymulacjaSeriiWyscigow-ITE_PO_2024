import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
                    new Pojazd(listaPojazdow.get(Integer.parseInt(kierowcaInput.get(5))-1)),
                    kierowcaInput
            );
            listaKierowcow.add(kierowca);
        }

        //Wczytuje tory
        for (List<String> torInput : odczytPliku(sciezkaDaneTorow)) {
            Tor tor = new Tor(torInput);
            listaTorow.add(tor);
        }

        Main.setterDanych(listaKierowcow,listaTorow);
    }

    //Otwiera plik CSV, przepuszcza go przez funkcje liniaCSVnaDane i zwraca dane w formie listy list stringow
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

    // Zamienia pojedyncze linie z pliku CSV na listy Stringow
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






    //Funkcja zapisz wyniki - w przypadku gdy czyWszystkieKoncowe == true
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
    //Funkcja zapisz wyniki - w przypadku gdy czyWszystkieKoncowe == false
    public static void zapiszWyniki(boolean czyWszystkieKoncowe, String typZapisu)
    {
        if (czyWszystkieKoncowe)
        {
            zapiszWyniki(true);
        }
        ArrayList<Kierowca> listaKierowcow = Main.getListaKierowcow();

        ArrayList<ArrayList<String>> arrayWynikowy = new ArrayList<>();
        ArrayList<String> arrayNaglowki = new ArrayList<>();

        //Typ zapisu Wyniki zapisuje koncowe wyniki wszystkich wyscigow oraz punktacje koncowa kierowcow
        if(typZapisu.equals("Wyniki"))
        {
            //Sortuje kierowcow na podstawie ich punktow koncowych
            listaKierowcow.sort((o1, o2) -> Integer.compare(o2.punktyZaPozycje, o1.punktyZaPozycje));

            //Tworzenie naglowkow
            arrayNaglowki.addAll(Arrays.asList("Imie","Nazwisko"));
            for(int i=0; i<Main.getIloscTorow();i++)
            {
                arrayNaglowki.add("Wyscig "+String.valueOf(i+1));
            }
            arrayNaglowki.add("Suma punktow");
            arrayWynikowy.add(arrayNaglowki);

            //Zapisywanie danych
            for(Kierowca kierowca : listaKierowcow)
            {
                ArrayList<String> arrayDanowy = new ArrayList<>();
                arrayDanowy.add(kierowca.imie);
                arrayDanowy.add(kierowca.nazwisko);
                arrayDanowy.addAll(kierowca.statystykiWynikow);
                arrayDanowy.add(String.valueOf(kierowca.punktyZaPozycje));
                arrayWynikowy.add(arrayDanowy);
            }
            zapisPliku(arrayWynikowy,"_PozycjeWSezonie");
        }
        //Typ zapisu Wyprzedzenia zapisuje ilosci wyprzedzen wykonanych przez danych kierowcow z podzialem na wyscigi
        if(typZapisu.equals("Wyprzedzenia"))
        {
            //Tworzenie naglowkow
            arrayNaglowki.addAll(Arrays.asList("Imie","Nazwisko"));
            for(int i=0; i<Main.getIloscTorow();i++)
            {
                arrayNaglowki.add("Wyscig "+String.valueOf(i+1));
            }
            arrayNaglowki.add("Suma");
            arrayWynikowy.add(arrayNaglowki);

            //Zapisywanie danych
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
        //Typ zapisu Parametry zapisuje stan wszystkich umiejetnosci kierowcow, ich pojazdow i ich mechanikow na moment ostatniego wyscigu
        if(typZapisu.equals("Parametry"))
        {
            //Tworzy naglowki
            arrayNaglowki.addAll(Arrays.asList("Imie","Nazwisko","Predkosc na prostych","Predkosc na zakretach",
                    "Umiejetnosc wyprzedzania","Umiejetnosc obrony","Agresywnosc","Adaptacja do pogody","Ekonomicznosc jazdy",
                    "Szybkosc pojazdu","Przyczepnosc pojazdu","Predkosc mechanika","Punktow w koncowym zestawieniu","Parametry personalne",
                    "Parametry wyprzedzania","Parametry wytrzymalosciowe","Parametry pojazdu"));
            arrayWynikowy.add(arrayNaglowki);

            //Zapisywanie danych
            for(Kierowca kierowca : listaKierowcow)
            {
                ArrayList<String> arrayDanowy = new ArrayList<>();
                arrayDanowy.add(kierowca.imie);
                arrayDanowy.add(kierowca.nazwisko);
                ArrayList<Double> arrayDoublowy = new ArrayList<>();
                arrayDoublowy.addAll(Arrays.asList(kierowca.predkoscProsta, kierowca.predkoscZakret, kierowca.umiejetnoscWyprzedania, kierowca.umiejetnoscObrony,
                                kierowca.agresywnosc, kierowca.adaptacjaPogoda,kierowca.ekonomicznoscJazdy,kierowca.pojazd.szybkosc,kierowca.pojazd.przyczepnosc,
                                kierowca.pojazd.mechanik.szybkosc,Double.valueOf(kierowca.punktyZaPozycje),
                                kierowca.predkoscProsta* kierowca.predkoscZakret*kierowca.adaptacjaPogoda, //Parametry personalne
                                kierowca.umiejetnoscWyprzedania* kierowca.umiejetnoscObrony* kierowca.agresywnosc, //Parametry wyprzedzania
                                kierowca.ekonomicznoscJazdy*kierowca.pojazd.mechanik.szybkosc, //Parametry wytrzymalosciowe
                                kierowca.pojazd.szybkosc*kierowca.pojazd.przyczepnosc //Parametry pojazdu
                        ));
                arrayDanowy.addAll(doubleArrayToStringArrayFormatter(arrayDoublowy));
                arrayWynikowy.add(arrayDanowy);
            }
            zapisPliku(arrayWynikowy,"_UmiejetnosciKierowcowPoSezonie");
        }
        //Typ zapisu Okrazenia zapisuje czasy poszczegolnych okrazen w podanym wyscigu
        if(typZapisu.equals("Okrazenia"))
        {
            //Tworzy naglowki
            arrayNaglowki.add("Imie");
            arrayNaglowki.add("Nazwisko");
            for(int i=0; i<listaKierowcow.get(0).statystykiOkrazenia.size();i++)
            {
                arrayNaglowki.add("Okrazenie "+String.valueOf(i+1));
            }
            arrayNaglowki.add("Pozycja koncowa");
            arrayWynikowy.add(arrayNaglowki);

            //Zapisywanie danych
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
                for(int ii=arrayDanowy.size()+1;ii<arrayNaglowki.size();ii++)
                {
                    arrayDanowy.add("DNF");
                }
                arrayDanowy.add(String.valueOf(i+1));
                arrayWynikowy.add(arrayDanowy);
            }
            zapisPliku(arrayWynikowy,"Wyscig" + listaKierowcow.get(0).statystykiWynikow.size());
        }

    }
    //Formatuje array typu Double do arrayu typu String w celu prostego zapisu do plik csv
    private static ArrayList<String> doubleArrayToStringArrayFormatter(ArrayList <Double> wartosci)
    {
        ArrayList<String> wynikowy = new ArrayList<>();
        for(int i=0; i<wartosci.size();i++)
        {
            wynikowy.add(String.format("%.2f",wartosci.get(i)));
        }
        return wynikowy;
    }

    //Zapisuje dane zadane w formacie ArrayList<Arraylist<String>> do podanego pliku formatujac je jako plik csv
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
