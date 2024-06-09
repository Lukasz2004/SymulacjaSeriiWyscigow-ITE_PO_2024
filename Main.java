import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static final int liczbaOkrazenNaTor = 50;
    private static final double globalnaAgresywnosc = 1;
    private static final double globalnaWartoscUlepszen = 0.25;
    private static final double wymaganaPojemnoscPaliwa = 50;
    private static ArrayList<Kierowca> listaKierowcow = new ArrayList<>();
    private static ArrayList<Tor> listaTorow = new ArrayList<>();
    private static ArrayList<Druzyna> listaDruzyn = new ArrayList<>();
    private static ArrayList<Pojazd> listaPojazdow = new ArrayList<>();
    private static ArrayList<Mechanik> listaMechanikow = new ArrayList<>();

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        ObslugaPlikow.wczytajDane();

        for(int nrWyscigu=1; nrWyscigu<=listaTorow.size(); nrWyscigu++)
        {
            uruchomWyscig(listaTorow.get(nrWyscigu - 1));
            ObslugaPlikow.zapiszWyniki(false,"Okrazenia");
            ulepszenia();

        }

        ObslugaPlikow.zapiszWyniki(true);

        /*
        if(Objects.equals(listaKierowcow.get(0).imie, "Max")||Objects.equals(listaKierowcow.get(0).imie, "Fernando ")||Objects.equals(listaKierowcow.get(0).imie, "Sergio")||Objects.equals(listaKierowcow.get(0).imie, "Lewis "))
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Dzwiek/Max.wav").getAbsoluteFile());;
            if(Objects.equals(listaKierowcow.get(0).imie, "Max")) audioInputStream = AudioSystem.getAudioInputStream(new File("Dzwiek/Max.wav").getAbsoluteFile());
            if(Objects.equals(listaKierowcow.get(0).imie, "Fernando ")) audioInputStream = AudioSystem.getAudioInputStream(new File("Dzwiek/Fernando.wav").getAbsoluteFile());
            if(Objects.equals(listaKierowcow.get(0).imie, "Lewis ")) audioInputStream = AudioSystem.getAudioInputStream(new File("Dzwiek/Lewis.wav").getAbsoluteFile());
            if(Objects.equals(listaKierowcow.get(0).imie, "Sergio")) audioInputStream = AudioSystem.getAudioInputStream(new File("Dzwiek/Sergio.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(10000);
        }
         */
    }
    private static void uruchomWyscig(Tor tor){
        System.out.println("TOR: "+tor.nazwa);
        if(tor.czyPada) System.out.println("Bedzie dzis padac");
        System.out.println("START !!!");
        for(Kierowca i:listaKierowcow)
        {
            i.czasPrzejazdu=0.0;
            i.pojazd.stanPaliwa=wymaganaPojemnoscPaliwa;
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

        //Przyznawanie punktow za miejsce w wyscigu
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
        if(kierowca.czasPrzejazdu < 0) return;


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

        if(kierowca2.czasPrzejazdu-kierowca1.czasPrzejazdu<0.25 && kierowca1.czyWPitstopie&&!kierowca2.czyWPitstopie&&kierowca2.czasPrzejazdu!=-1)
        {
            System.out.println(kierowca2.imie+" zaczyna wyprzedzac ");
            if((kierowca2.umiejetnoscWyprzedania*kierowca2.agresywnosc*wyprzedzanie.nextDouble()*globalnaAgresywnosc)>(kierowca1.umiejetnoscObrony*kierowca1.agresywnosc*wyprzedzanie.nextDouble()) && kierowca1.czyWPitstopie && !kierowca2.czyWPitstopie)
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
            else if(((kierowca2.umiejetnoscWyprzedania*kierowca2.agresywnosc*globalnaAgresywnosc))/2 > wyprzedzanie.nextDouble())
            {
                System.out.println(wyprzedzanie.nextDouble());
                kierowca2.czasPrzejazdu = -1;
                System.out.println(kierowca2.imie + " WYPADEK - KONIEC");
                for(int i = pozKierowcy;i<listaKierowcow.size()-1;i++)
                {
                    listaKierowcow.set(i,listaKierowcow.get(i+1));
                }
                listaKierowcow.set(listaKierowcow.size()-1, kierowca2);
                if(((kierowca1.umiejetnoscObrony*kierowca1.agresywnosc*globalnaAgresywnosc))/5 > wyprzedzanie.nextDouble() && !kierowca1.czyWPitstopie)
                {
                    kierowca1.czasPrzejazdu = -1;
                    System.out.println(kierowca1.imie + " WYPADEK - KONIEC");
                    for(int i = pozKierowcy+1;i<listaKierowcow.size()-1;i++)
                    {
                        listaKierowcow.set(i,listaKierowcow.get(i+1));
                    }
                    listaKierowcow.set(listaKierowcow.size()-1, kierowca1);
                }

            }
        }

    }


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
