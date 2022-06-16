package rental.service;

import rental.exceptions.SamochodNieIstniejeException;
import rental.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Wypozyczalnia {

    private Map<String, Samochod> pojazdy = new HashMap<>();
    private Map<String, WynajemSamochodu> wynajmy = new HashMap<>();

    private static Integer LICZBIK_WYNAJMOW = 1;

    Scanner scanner = new Scanner(System.in);

    // możemy dodac samochod
    // Co ta funkcja powinna robić:
    // -dodawac samochod - jesli dodam samochod (XYZ) to ten samochod musi byc w wypozyczalni
    // -dodawac samochody - jesli dodam 5 roznych to powinno byc 5 roznych w wypozyczalni
    // Czego funkcja ma nie robic
    // - dodawanie pojazdu o istniejacym numerze rejestracyjnym
    // Zasada  TDD - zasada dobrego testera
    // Tresc testu powinna mowic jakie sa warunki dzialania / zakres funkcji
    // Test mowi jak funkcja powinna sie zachowywac
    // użytkownik ma mozliwosc:
    //- wyswietlic liste samochodow
    //- lista dostepnych samochodow
    //- sprawdzic cene samochodu
    //- wynajac samochod
    //- zwrocic samochod

    public void dodajSamochod(String numerRejestracyjny, SkrzyniaBiegow skrzyniaBiegow, TypNadwozia typNadwozia, StatusSamochodu statusSamochodu){
       if(!pojazdy.containsKey(numerRejestracyjny)){
            pojazdy.put(numerRejestracyjny, new Samochod(numerRejestracyjny, skrzyniaBiegow,typNadwozia, statusSamochodu));
            // udalo sie dodac samochod
        }
    }

    public List<Samochod> zwrocListe(){
        return new ArrayList<>(pojazdy.values());
    }
    public List<Samochod> zwrocListeDostepnych(){
        List<Samochod> listaDostepnych = new ArrayList<>();
        for (Samochod samochod : pojazdy.values()) {
            if (samochod.getStatus() == StatusSamochodu.DOSTEPNY){
                listaDostepnych.add(samochod);
            }
        }
        return listaDostepnych;
    }
    public List<Samochod> zwrocListeWynajetych(){
        List<Samochod> listaWynajetych = new ArrayList<>();
        for (Samochod samochod : pojazdy.values()) {
            if (samochod.getStatus() == StatusSamochodu.WYNAJETY){
                listaWynajetych.add(samochod);
            }
        }
        return listaWynajetych;
}
public void usunSamochod(String numerRejestracyjny){
        pojazdy.get(numerRejestracyjny).setStatus(StatusSamochodu.NIEDOSTEPNY);
}

    public Optional<Samochod> znajdzSamochod(String rejestracja) {
        return Optional.ofNullable(pojazdy.get(rejestracja));
    }

    public Optional<Double> sprawdzCeneSamochodu(String rejestracja, int liczbaDni) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();

            double cenaZaIloscDni = samochod.getTyp().getCenaBazowa() * liczbaDni;
            return Optional.of(cenaZaIloscDni);
        }
        return Optional.empty();
    }
    public void wynajmij(String rejestracja, String imieINazwiskoKlienta, int liczbaDni) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();
            if(samochod.getStatus() != StatusSamochodu.DOSTEPNY){
                throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
            }
            samochod.setStatus(StatusSamochodu.WYNAJETY);

            String generowanyIdentyfikator = "WYNAJEM-" + LICZBIK_WYNAJMOW;
            wynajmy.put(generowanyIdentyfikator,
                    new WynajemSamochodu(
                            generowanyIdentyfikator,
                            imieINazwiskoKlienta,
                            samochod));
            return;
        }

        // Note: rzucamy exception ponieważ nikt nie prosi nas o wynik (nie ma typu zwracanego)
        //  ale chcemy zwrócić uwagę że metoda się "nie udała"
        throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
    }

    public void zwrocSamochod(String rejestracja, String identyfikatorWynajmu) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();

            if (samochod.getStatus() != StatusSamochodu.WYNAJETY) {
                throw new SamochodNieIstniejeException("Nie można zwrócić samochodu który nie jest wynajety");
            }
            samochod.setStatus(StatusSamochodu.DOSTEPNY);
            WynajemSamochodu wynajemSamochodu = wynajmy.get(identyfikatorWynajmu);
            wynajemSamochodu.setDataZwrotu(LocalDateTime.now());
            return;
        }

        // Note: rzucamy exception ponieważ nikt nie prosi nas o wynik (nie ma typu zwracanego)
        //  ale chcemy zwrócić uwagę że metoda się "nie udała"
        throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
    }

    public List<WynajemSamochodu> listaAktywnychWynajmów() {
        List<WynajemSamochodu> wynajmyAktywne = new ArrayList<>();
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() == null) {
                wynajmyAktywne.add(wynajem);
            }
        }
        return wynajmyAktywne;
    }

    public List<WynajemSamochodu> listaZakonczonychWynajmów() {
        List<WynajemSamochodu> wynajmyAktywne = new ArrayList<>();
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() != null) {
                wynajmyAktywne.add(wynajem);
            }
        }
        return wynajmyAktywne;
    }

    public double łącznyZysk() {
        double zysk = 0.0;
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() != null) {

                // tutaj pierwsza wersja jest w "dniach"
                // obliczanie ile minęło dni (tutaj zawsze będzie 0, chyba że zostawicie aplikacje na 1 dzień i będą wynajmy) :)
//                Period period = Period.between(wynajem.getDataWynajmu().toLocalDate(), wynajem.getDataZwrotu().toLocalDate());
//                zysk += period.getDays() * wynajem.getWynajetySamochod().getTyp().getCenaBazowa();

                // Druga wersja jest w minutach
                Duration duration = Duration.between(wynajem.getDataWynajmu(), wynajem.getDataZwrotu());
                zysk += (duration.getSeconds()/60) * wynajem.getWynajetySamochod().getTyp().getCenaBazowa();
            }
        }
        return zysk;
    }
}





