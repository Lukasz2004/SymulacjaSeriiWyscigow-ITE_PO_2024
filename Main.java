import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int liczbaOkrazenNaTor = 50;
    private static ArrayList<Kierowca> listaKierowcow = new ArrayList<Kierowca>();
    private static ArrayList<Tor> listaTorow = new ArrayList<Tor>();
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
        for(int okrazenie=0; okrazenie<liczbaOkrazenNaTor; okrazenie++)
        {
            for(int i=0; i<listaKierowcow.size();i++)
            {
                przejazdKierowcy(listaKierowcow.get(i));
            }
            for(int i=1; i<listaKierowcow.size();i++)
            {
                wyprzedzanie(i);
            }
        }
    }
    private static void przejazdKierowcy(Kierowca kierowca)
    {
        double czasPrzejazdu = 1;
        kierowca.odlegloscOdPoprzednika = Math.min(kierowca.odlegloscOdPoprzednika,kierowca.odlegloscOdPoprzednika-czasPrzejazdu);
    }
    private static void wyprzedzanie(int pozKierowcy)
    {
        Kierowca kierowca1 = listaKierowcow.get(pozKierowcy-1);
        Kierowca kierowca2 = listaKierowcow.get(pozKierowcy);
    }
    private static void zapiszWyniki() {

    }

    private static void pokazWyniki() {

    }


    private static List<String> liniaCSVnaDane(String linia) {
        List<String> wartosci = new ArrayList<String>();
        try (Scanner scanner = new Scanner(linia)) {
            scanner.useDelimiter(",");
            while (scanner.hasNext()) {
                wartosci.add(scanner.next());
            }
        }
        return wartosci;
    }
    private static void wczytajDane(){
        String sciezkaDaneKierowcow = "DaneStartowe/kierowcy.csv";

        List<List<String>> linieTekstu = new ArrayList<>();
        try (Scanner scannerKierowcow = new Scanner(new File(sciezkaDaneKierowcow))) {
            while (scannerKierowcow.hasNextLine()) {
                linieTekstu.add(liniaCSVnaDane(scannerKierowcow.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
