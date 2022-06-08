package rental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Samochod {
    // dla identyfikacji
    private String numerRejestracyjny;
    // dla filtrow
    private SkrzyniaBiegow skrzynia;
    private TypNadwozia typ;

    // dla sprawdzenia dostepnosci
    private StatusSamochodu status;


}
