package rental;

import org.junit.Assert;

import org.junit.Test;
import rental.exceptions.SamochodNieIstniejeException;
import rental.model.*;
import rental.service.Wypozyczalnia;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class WypozyczalniaTest {

    @Test
    public void test_mozliweJestDodawanieSamochodu() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(testowanySamochod.getNumerRejestracyjny(), testowanySamochod.getSkrzynia(), testowanySamochod.getTyp(), testowanySamochod.getStatus());
        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        //contains rowniez porownuje metoda equals
        Assert.assertTrue("lista zwrocona przez obiekt wypozyczalnia nie zawiera pojazdu dodanego a powinna", wynikZwroconaLista.contains(testowanySamochod));
    }

    @Test
    public void test_mozliweJestDodawanieSamochoduAleNieJegoNadpisanie() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod testowanySamochodDrugi = new Samochod("test1", SkrzyniaBiegow.MANUAL, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(testowanySamochod.getNumerRejestracyjny(), testowanySamochod.getSkrzynia(), testowanySamochod.getTyp(), testowanySamochod.getStatus());
        wypozyczalnia.dodajSamochod(testowanySamochodDrugi.getNumerRejestracyjny(), testowanySamochodDrugi.getSkrzynia(), testowanySamochodDrugi.getTyp(), testowanySamochodDrugi.getStatus());
        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        Assert.assertEquals("lista powinna zawierac dokladnie jeden pojazd ", 1, wynikZwroconaLista.size());
        Assert.assertTrue("lista zwrocona przez obiekt wypozyczalnia nie zawiera pojazdu dodanego a powinna", wynikZwroconaLista.contains(testowanySamochod));

    }

    @Test
    public void test_mozemyPobracListeSamochodowDostepnych() {
        // pobieramy liste ktora zawiera tylko dostepne samochody
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod testowanySamochodDrugi = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.CABRIO, StatusSamochodu.WYNAJETY);
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(testowanySamochod.getNumerRejestracyjny(), testowanySamochod.getSkrzynia(), testowanySamochod.getTyp(), testowanySamochod.getStatus());
        wypozyczalnia.dodajSamochod(testowanySamochodDrugi.getNumerRejestracyjny(), testowanySamochodDrugi.getSkrzynia(), testowanySamochodDrugi.getTyp(), testowanySamochodDrugi.getStatus());
        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        Assert.assertEquals("lista powinna zawiera?? oba pojazdy", 2, wynikZwroconaLista.size());
        List<Samochod> dostepne = wypozyczalnia.zwrocListeDostepnych();
        Assert.assertEquals("lista powinna zawierac jeden pojazd dostepny", 1, dostepne.size());
        Assert.assertTrue("lista powinna zawierac tylko dostepny samochod ", dostepne.contains(testowanySamochod));
    }

    @Test
    public void test_mozemyZmienicStatusSamochoduNaNiedostepny() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains r??wnie?? por??wnuje metod?? equals
        Assert.assertEquals("Lista powinna zawiera?? dok??adnie jeden pojazd, bo tylko tyle ich dodali??my", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Lista zwr??cona przez obiekt wypo??yczalnia nie zawiera pojazdu dodanego, a powinna go zawiera??", wynikZwroconaLista.contains(testowanySamochod));

        wypozyczalnia.usunSamochod("test1");
        wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains r??wnie?? por??wnuje metod?? equals
        Assert.assertEquals("Lista powinna zawiera?? dok??adnie jeden pojazd, bo tylko tyle ich dodali??my", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.get(0).getStatus() == StatusSamochodu.NIEDOSTEPNY);

        Samochod samochodDoPorownania = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.NIEDOSTEPNY);
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.contains(samochodDoPorownania));

    }

    // 1. Chc?? wiedzie?? ??e mog?? pobra?? list?? wynaj??tych samochod??w
    //    //  - Stw??rz nowy obiekt Wypozyczalnia
    //    //  - Nie posiada samochod??w wi??c zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi by?? pusta
    //    //  - Nie posiada samochod??w wi??c zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi by?? pusta
    //    //  - Dodajemy dwa samochody
    //    //  - zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi zawiera?? dwa samochody podane wy??ej
    //    //  - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi by?? pusta
    //    //  - wynajmuj?? samoch??d 1
    //    //  - zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi zawiera?? dwa samochody podane wy??ej
    //    //  - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi zwr??ci?? samoch??d 1
    //    //  - wynajmuj?? samoch??d 2
    //    //  - zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi zawiera?? dwa samochody podane wy??ej
    //    //  - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi zwr??ci?? samoch??d 1 i 2

    @Test
    public void test_zwrocListeWynajetych() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod sam2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe();

        Assert.assertEquals("Lista powinna by?? pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna by?? pusta", 0, wszystkie1.size());

        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());

        wypozyczalnia.dodajSamochod(
                sam2.getNumerRejestracyjny(),
                sam2.getSkrzynia(),
                sam2.getTyp(),
                sam2.getStatus());

        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe();

        Assert.assertEquals("Lista powinna byc pusta", 0, wynajete2.size());
        Assert.assertEquals("Lista powinna mie?? 2 auta", 2, wszystkie2.size());

        // opcjonalnie
        Assert.assertTrue("powinien zawierac sam 1", wszystkie2.contains(sam1));
        Assert.assertTrue("powinien zawierac sam 1", wszystkie2.contains(sam2));

        wypozyczalnia.wynajmij("test1", "Mis Koala", 10);
        List<Samochod> wynajete3 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie3 = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista powinna miec 1 samochod", 1, wynajete3.size());
        Assert.assertEquals("Lista powinna mie?? 2 auta", 2, wszystkie3.size());

        // opcjonalnie
        // tak robimy jak nie dopiszenie w klasie samochod tego exclude equals i hashcode
        Samochod sam1Wynajety = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.WYNAJETY);
        Assert.assertTrue("wynajete 3 powinny zwierac samochod 1 ze zmienionym statusem", wynajete3.contains(sam1Wynajety));
// to zadzia??a, bo w samochodzie ustawilismy to exclude equals i hashcode
        Assert.assertTrue("Wynajete 3 po wynajeciu powinien zawiera?? samoch??d 1", wynajete3.contains(sam1));

        wypozyczalnia.wynajmij("test2", "Mis Panda", 20);
        List<Samochod> wynajete4 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie4 = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista wynajetych powinna miec 2 samochody", 2, wynajete4.size());
        Assert.assertEquals("Lista wszystkich powinna mie?? 2 samochody", 2, wszystkie4.size());

        // opcjonalnie
        Assert.assertTrue("Wynajete 4 po wynajeciu powinien zawiera?? samoch??d 1", wynajete4.contains(sam1));
        Assert.assertTrue("Wynajete 4 po wynajeciu powinien zawiera?? samoch??d 2", wynajete4.contains(sam2));

        // dzia???? metoda sprawdz cene samochodu
        // stworz nowy obielt wypozyczalnia
        // sprawdzCeneSamochodu dla test1 nie dziala bo samochod nie istnieje
        // dodaj samochod test1
        // cena dziala dla samochodu test1
        // przeliczenie ceny dziala dla ilosci dni 1, 5, 10
        // sprawdzCeneSamochodu dla samochodu test2 nie dziala bo samochod nie istnieje

    }

    @Test
    public void test_czyDzialaMetodaSprawdzCene() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        Samochod samTest = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Optional<Double> cena = wypozyczalnia.sprawdzCeneSamochodu("test1", 1);
        Assert.assertFalse("Cena nie powinna istnie??", cena.isPresent());

        wypozyczalnia.dodajSamochod(samTest.getNumerRejestracyjny(), samTest.getSkrzynia(), samTest.getTyp(), samTest.getStatus());
        Optional<Double> cena2 = wypozyczalnia.sprawdzCeneSamochodu("test1", 1);
        Assert.assertTrue(cena2.isPresent());
        double cenaZa1Dzien = cena2.get();
        Optional<Double> cenaOptional3_dla5 = wypozyczalnia.sprawdzCeneSamochodu("test1", 5);
        Assert.assertTrue(cenaOptional3_dla5.isPresent());
        double cenaZa5Dni = cenaOptional3_dla5.get();
        Assert.assertTrue("cena za 1 dzien razy 5 == cena za 5 dni", cenaZa5Dni == (cenaZa1Dzien * 5));
        Optional<Double> cenaOptional3_dla10 = wypozyczalnia.sprawdzCeneSamochodu("test1", 10);
        Assert.assertTrue(cenaOptional3_dla10.isPresent());
        double cenaZa10Dni = cenaOptional3_dla10.get();
        Assert.assertTrue("cena za 1 dzien razy 10 == cena za 10 dni", cenaZa10Dni == (cenaZa1Dzien * 10));
        Optional<Double> cena4 = wypozyczalnia.sprawdzCeneSamochodu("test2", 1);
        Assert.assertFalse("Cena nie powinna istnie??", cena4.isPresent());
    }


    // 3. Dziala metoda Wynajmij i ListaAktywnychWynajm??w
    // - Stw??rz nowy obiekt Wypozyczalnia
    //  - (Na pocz??tku wypozyczalnia nie posiada samochod??w) - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi by?? pusta
    // - (Na pocz??tku wypozyczalnia nie posiada samochod??w) -  zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi by?? pusta
    // - (Na pocz??tku wypozyczalnia nie posiada wynajm??w) -  zwr???? (wynik metody ma zwr??ci??) list?? aktywnych wynajm??w - lista musi by?? pusta
    //  - dodaj samochod 'test1'
    // - dodaj samochod 'test2'
    // - list?? wynaj??tych - lista musi by?? pusta
    // - list?? wszystkich - 2 samochody
    // - list?? aktywnych wynajm??w - lista musi by?? pusta
    // - wynajmij samochod 'test1'
    //  - list?? wszystkich - 2 samochody
    //  - list?? wynaj??tych - 1 samoch??d
    //  - list?? aktywnych wynajm??w - lista musi zawiera?? 1 wynajem
    // - obiekt na pozycji 0 (wynaj??ty samoch??d) musi zawiera?? samoch??d z listy wynaj??tych

    @Test
    public void test_wynajmijIListaAktywnychWynajmow() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        //  - krok 1:
        //  - (Na pocz??tku wypozyczalnia nie posiada samochod??w) - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi by?? pusta
        //  - (Na pocz??tku wypozyczalnia nie posiada samochod??w) -  zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi by?? pusta
        //  - (Na pocz??tku wypozyczalnia nie posiada wynajm??w) -  zwr???? (wynik metody ma zwr??ci??) list?? aktywnych wynajm??w - lista musi by?? pusta
        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajm??w(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna by?? pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna by?? pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista powinna by?? pusta", 0, wynajmy1.size());

        //  - dodaj samochod 'test1'
        //  - dodaj samochod 'test2'
        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod sam2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());
        wypozyczalnia.dodajSamochod(
                sam2.getNumerRejestracyjny(),
                sam2.getSkrzynia(),
                sam2.getTyp(),
                sam2.getStatus());

        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy2 = wypozyczalnia.listaAktywnychWynajm??w(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna by?? pusta", 0, wynajete2.size());
        Assert.assertEquals("Lista powinna mie?? 2 elementy", 2, wszystkie2.size());
        Assert.assertEquals("Lista powinna by?? pusta", 0, wynajmy2.size());

        wypozyczalnia.wynajmij("test1", "Pawel Test Gawel", 10);

        List<Samochod> wynajete3 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie3 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy3 = wypozyczalnia.listaAktywnychWynajm??w(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna mie?? jeden samochod", 1, wynajete3.size());          // samoch??d "maluch"
        Assert.assertEquals("Lista powinna mie?? 2 elementy", 2, wszystkie3.size());
        Assert.assertEquals("Lista powinna mie?? jeden aktywny wynajem", 1, wynajmy3.size());    // wynajem musi by?? na samoch??d "maluch"

        //  - obiekt na pozycji 0 (wynaj??ty samoch??d) musi zawiera?? samoch??d z listy wynaj??tych
        WynajemSamochodu sprawdzanyWynajem = wynajmy3.get(0);
        Samochod sprawdzanySamochod = wynajete3.get(0);
        Assert.assertEquals("Wynajem posiada samoch??d i tym samochodem jest jedyny wynaj??ty samoch??d", sprawdzanyWynajem.getWynajetySamochod(), sprawdzanySamochod);
    }

    // / 4. Wynajmij rzuca exception je??li nie ma szukanego samochodu
    ////  - Stw??rz nowy obiekt Wypozyczalnia
    ////  - (Na pocz??tku wypozyczalnia nie posiada samochod??w wynajetych) - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi by?? pusta
    ////  - (Na pocz??tku wypozyczalnia nie posiada samochod??w [wszystkich]) -  zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi by?? pusta
    ////  - (Na pocz??tku wypozyczalnia nie posiada wynajm??w) -  zwr???? (wynik metody ma zwr??ci??) list?? aktywnych wynajm??w - lista musi by?? pusta
    ////  - wywo??uj?? metod?? wynajmij samochodu 'test1' i oczekuj?? ??e otrzymam exception

    @Test(expected = SamochodNieIstniejeException.class)
    public void test_wynajmijNieDzialaJesliSamochodNieIstnieje() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        //  - krok 1:
        //  - (Na pocz??tku wypozyczalnia nie posiada samochod??w wynajetych) - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi by?? pusta
        //  - (Na pocz??tku wypozyczalnia nie posiada samochod??w [wszystkich]) -  zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi by?? pusta
        //  - (Na pocz??tku wypozyczalnia nie posiada wynajm??w) -  zwr???? (wynik metody ma zwr??ci??) list?? aktywnych wynajm??w - lista musi by?? pusta
        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajm??w(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna by?? pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna by?? pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista powinna by?? pusta", 0, wynajmy1.size());

        //  - wywo??uj?? metod?? wynajmij samochodu 'test1' i oczekuj?? ??e otrzymam exception
        wypozyczalnia.wynajmij("test1", "Pawel Test Gawel", 10);
    }

    // 5. Wynajmij i zwr???? samochod dzia??a
    // Krok 1:
    //  - Stw??rz nowy obiekt Wypozyczalnia
    //  - (Na pocz??tku wypozyczalnia nie posiada samochod??w) - zwr???? (wynik metody ma zwr??ci??) list?? wynaj??tych - lista musi by?? pusta
    //  - (Na pocz??tku wypozyczalnia nie posiada samochod??w) -  zwr???? (wynik metody ma zwr??ci??) list?? wszystkich - lista musi by?? pusta
    //  - (Na pocz??tku wypozyczalnia nie posiada wynajm??w) -  zwr???? (wynik metody ma zwr??ci??) list?? aktywnych wynajm??w - lista musi by?? pusta
    //
    //
    // Krok 2:
    //  - dodaj samochod 1 i 2
    //  - wywo??uj?? metod?? wynajmij samochodu 'test1' powinno dzialac
    //  - list?? wszystkich - 2 samochody
    //  - list?? wynaj??tych - 1 samoch??d
    //  - list?? aktywnych wynajm??w - lista musi zawiera?? 1 wynajem
    //  - list?? zakonczonych wynajm??w - lista musi zawiera?? 0 wynajm??w
    //  - obiekt na pozycji 0 (wynaj??ty samoch??d) musi zawiera?? samoch??d z listy wynaj??tych
    //
    // Krok 3:
    //  - wywo??uj?? metod?? zwrocSamochod 'test1` - powinno dzialac i nie rzuci?? exception (wszystko ok)
    //  - list?? wszystkich - 2 samochody
    //  - list?? wynaj??tych - 0 samoch??d
    //  - list?? aktywnych wynajm??w - lista musi zawiera?? 0 wynajem
    //  - list?? zakonczonych wynajm??w - lista musi zawiera?? 1 wynajem
    //
    // Krok 4:
    //  - wywo??uj?? metod?? zwrocSamochod (drugi raz) 'test1` - powinno rzuci?? exception

    @Test(expected = SamochodNieIstniejeException.class)
    public void test_wynajmijIZwroc() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajm??w();
        Assert.assertEquals("Lista wynaj??tych powinna by?? pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista wszystkich powinna by?? pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista aktywnych wynajmow powinna by?? pusta", 0, wynajmy1.size());

        Samochod auto1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod auto2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod(
                auto1.getNumerRejestracyjny(),
                auto1.getSkrzynia(),
                auto1.getTyp(),
                auto1.getStatus());
        wypozyczalnia.dodajSamochod(
                auto2.getNumerRejestracyjny(),
                auto2.getSkrzynia(),
                auto2.getTyp(),
                auto2.getStatus());
        wypozyczalnia.wynajmij("test1", "Mis Koala", 10);
        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe();
        List<WynajemSamochodu> wynajmy2 = wypozyczalnia.listaAktywnychWynajm??w();
        List<WynajemSamochodu> wynajmyZakonczone = wypozyczalnia.listaZakonczonychWynajm??w();
        Assert.assertEquals("Lista wynaj??tych powinna mie?? 1 samochod", 1, wynajete2.size());
        Assert.assertEquals("Lista wszystkich powinna mie?? 2 samochody", 2, wszystkie2.size());
        Assert.assertEquals("Lista aktywnych wynajmow powinna miec 1 samochod", 1, wynajmy2.size());
        Assert.assertEquals("Lista zakonczonych wynajmow powinna miec 0 samochodow", 0, wynajmyZakonczone.size());
        WynajemSamochodu sprawdzanyWynajem = wynajmy2.get(0);
        Samochod sprawdzanySamochod = wynajete2.get(0);
        Assert.assertEquals("Wynajem posiada samoch??d i tym samochodem jest jedyny wynaj??ty samoch??d", sprawdzanyWynajem.getWynajetySamochod(), sprawdzanySamochod);
        wypozyczalnia.zwrocSamochod("test1","WYNAJEM-1");
        List<Samochod> wynajete3 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie3 = wypozyczalnia.zwrocListe();
        List<WynajemSamochodu> wynajmy3 = wypozyczalnia.listaAktywnychWynajm??w();
        List<WynajemSamochodu> wynajmyZakonczone3 = wypozyczalnia.listaZakonczonychWynajm??w();
        Assert.assertEquals("Lista wynaj??tych powinna byc pusta", 0, wynajete3.size());
        Assert.assertEquals("Lista wszystkich powinna mie?? 2 samochody", 2, wszystkie3.size());
        Assert.assertEquals("Lista aktywnych wynajmow powinna byc pusta", 0, wynajmy3.size());
        Assert.assertEquals("Lista zakonczonych wynajmow powinna miec 1 samochod", 1, wynajmyZakonczone3.size());
        wypozyczalnia.zwrocSamochod("test1", "WYNAJEM-1");
        // zwroc samochod zwraca exception bo samochod nie istnieje
    }
     // Znajd?? samoch??d zwraca KA??DY SAMOCH??D (musisz potwierdzi??, ??e niezale??nie od tego czy samoch??d jest:
            //      dost??pny,
            //      wynaj??ty czy
            //      niedost??pny
            //  - mo??liwe jest znalezienie samochodu metod?? znajdzSamochod
@Test
public void test_znajdzSamochod(){
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
    Samochod autoTest1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
    Samochod autoTest2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.WYNAJETY);
    Samochod autoTest3 = new Samochod("test3", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.SEDAN, StatusSamochodu.NIEDOSTEPNY);
    wypozyczalnia.dodajSamochod(
            autoTest1.getNumerRejestracyjny(),
            autoTest1.getSkrzynia(),
            autoTest1.getTyp(),
            autoTest1.getStatus());
    wypozyczalnia.dodajSamochod(
            autoTest2.getNumerRejestracyjny(),
            autoTest2.getSkrzynia(),
            autoTest2.getTyp(),
            autoTest2.getStatus());
    wypozyczalnia.dodajSamochod(
            autoTest3.getNumerRejestracyjny(),
            autoTest3.getSkrzynia(),
            autoTest3.getTyp(),
            autoTest3.getStatus());
    List<Samochod> wszystkieSamochody = wypozyczalnia.zwrocListe();
    Assert.assertEquals("Lista powinna zwierac 3 auta", 3, wszystkieSamochody.size());

    // nie jest skonczony ten test wyzej

}
// przetestowac:
 // Mo??liwe jest wynaj??cie dw??ch samochod??w (tutaj mamy bug'a, trzeba poprawi?? kod)
            // stw??rz samoch??d sam1, sam2
            // dodaj oba samochody
            // wynajmij samochod sam1 (sprawdz dlugosci poszczegolnych list)
            // wynajmij samochod sam2 (sprawdz dlugosci poszczegolnych list)

    @Test
    public void test_mozliweJestWynajecieDwochSamochodow(){
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        Samochod carTest1 = new Samochod("GD624CR", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.SEDAN, StatusSamochodu.DOSTEPNY);
        Samochod carTest2 = new Samochod("NEL37349", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod(
                carTest1.getNumerRejestracyjny(),
                carTest1.getSkrzynia(),
                carTest1.getTyp(),
                carTest1.getStatus());
        wypozyczalnia.dodajSamochod(
                carTest2.getNumerRejestracyjny(),
                carTest2.getSkrzynia(),
                carTest2.getTyp(),
                carTest2.getStatus());
        List<Samochod> wszystkieCary = wypozyczalnia.zwrocListe();
        Assert.assertEquals("lista zawiera 2 cary", 2, wszystkieCary.size());
        List<Samochod> wynajeteCary = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista wynajetych nie ma zadnych car'ow", 0, wynajeteCary.size());
        wypozyczalnia.wynajmij("NEL37349", "Kot Lorek", 5);
        List<Samochod> wynajeteCary2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkieCary2 = wypozyczalnia.zwrocListe();
        List<WynajemSamochodu> wynajmyCarow2 = wypozyczalnia.listaAktywnychWynajm??w();
        Assert.assertEquals("Lista wynajetych car'ow powinna zwierac 1 car", 1, wynajeteCary2.size());
        Assert.assertEquals("Lista wszystkich car'won powinna zawierac 2 car'y", 2, wszystkieCary2.size());
        Assert.assertEquals("Lista wynajmow powinna zawierac 1 car", 1, wynajmyCarow2.size());
        wypozyczalnia.wynajmij("GD624CR", "Kotka Bella", 3);
        List<Samochod> wynajeteCary3 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkieCary3 = wypozyczalnia.zwrocListe();
        List<WynajemSamochodu> wynajmyCarow3 = wypozyczalnia.listaAktywnychWynajm??w();
        Assert.assertEquals("Lista wynajetych car'ow powinna zwierac 1 car", 2, wynajeteCary3.size());
        Assert.assertEquals("Lista wszystkich car'won powinna zawierac 2 car'y", 2, wszystkieCary3.size());
        Assert.assertEquals("Lista wynajmow powinna zawierac 1 car", 2, wynajmyCarow3.size());

    }


    @Test
    public void test_uzytkownikNieZepsujeMetodyZmianyStatusuNaNiedostepnyPrzekazujacNieistniejacySamochod() {

    }

}
