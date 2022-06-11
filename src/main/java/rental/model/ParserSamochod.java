package rental.model;

import rental.service.Wypozyczalnia;

import java.util.List;
import java.util.Scanner;

public class ParserSamochod {

    private Wypozyczalnia wypozyczalnia;
    private Scanner scanner;

    public ParserSamochod(Wypozyczalnia wypozyczalnia, Scanner scanner) {
        this.wypozyczalnia = wypozyczalnia;
        this.scanner = scanner;
    }

    public void ogarnij() {
        String polecenie;

        do {
            System.out.println("podaj polecenie: dostepneLista, wynajeteLista, usun, lub podaj 'quit' by zakonczyc");
            polecenie = scanner.next();

            switch (polecenie) {
                case "dostepneLista":
                    obslugaPoleceniaDostepne();
                    break;
                case "wynajeteLista":
                    obslugaPoleceniaWynajete();
                    break;
                case "podajCene":
                    obslugaPoleceniaPodajCene();
                    break;
                case "usun":
                    obslugaPoleceniaUsun();
                    break;
            }
        } while (!polecenie.equalsIgnoreCase("quit"));
    }

    private void obslugaPoleceniaPodajCene() {
        System.out.println("Podaj nr rejestracyjny:");
        String numer = scanner.next();

        System.out.println("Podaj ilość dni:");
        int iloscDni = scanner.nextInt();

    }

    public void obslugaPoleceniaDostepne(){
        List<Samochod> listaDostepnych = wypozyczalnia.zwrocListeDostepnych();
        System.out.println("Dostępne samochody:");
        for (Samochod dostepny : listaDostepnych) {
            System.out.println(dostepny);
        }
    }

    public void obslugaPoleceniaWynajete(){
        List<Samochod> listaWynajetych = wypozyczalnia.zwrocListeWynajetych();
        System.out.println("Wynajete samochody");
        for (Samochod wynajete : listaWynajetych) {
            System.out.println(wynajete);
        }
    }
    public void obslugaPoleceniaUsun(){
        System.out.println("Podaj numer rejestracyjny");
        String numerRejestracyjny = scanner.next();
        wypozyczalnia.usunSamochod(numerRejestracyjny);
    }



    }



