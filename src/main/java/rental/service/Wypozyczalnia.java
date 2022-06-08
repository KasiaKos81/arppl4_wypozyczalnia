package rental.service;

import rental.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wypozyczalnia {

    private Map<String, Samochod> pojazdy = new HashMap<>();
    private Map<String, WynajemSamochodu> wynajem = new HashMap<>();

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

}
