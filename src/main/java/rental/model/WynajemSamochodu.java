package rental.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class WynajemSamochodu {

    private String identyfikator;
    private LocalDateTime dataWynajmu;
    private LocalDateTime dataZwrotu;
    private String imieINazwiskoKlienta;
    private Samochod wynajetySamochod;

    public WynajemSamochodu(String identyfikator, String imieINazwiskoKlienta, Samochod samochodWynajety) {
        this.identyfikator = identyfikator;
        this.imieINazwiskoKlienta = imieINazwiskoKlienta;
        this.wynajetySamochod = samochodWynajety;
        this.dataWynajmu = LocalDateTime.now();
    }
}
