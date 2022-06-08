package rental;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import rental.model.Samochod;
import rental.model.SkrzyniaBiegow;
import rental.model.StatusSamochodu;
import rental.model.TypNadwozia;
import rental.service.Wypozyczalnia;

import java.util.List;

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
        Assert.assertEquals("lista powinna zawierać oba pojazdy", 2, wynikZwroconaLista.size());
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
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Lista zwrócona przez obiekt wypożyczalnia nie zawiera pojazdu dodanego, a powinna go zawierać", wynikZwroconaLista.contains(testowanySamochod));

        wypozyczalnia.usunSamochod("test1");
        wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.get(0).getStatus() == StatusSamochodu.NIEDOSTEPNY);

        Samochod samochodDoPorownania = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.NIEDOSTEPNY);
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.contains(samochodDoPorownania));

    }


    @Test
    public void test_uzytkownikNieZepsujeMetodyZmianyStatusuNaNiedostepnyPrzekazujacNieistniejacySamochod() {

    }

}
