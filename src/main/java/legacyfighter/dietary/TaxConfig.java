package legacyfighter.dietary;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TaxConfig {
    final static int DEFAULT_MAX_RULES = 10;

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String countryReason;
    private CountryCode countryCode;
    private Instant lastModifiedDate;
    private int currentRulesCount;
    private int maxRulesCount;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TaxRule> taxRules = new ArrayList<>(); //usuwanieo z lastModifiedDate + liczniki,

    public TaxConfig() {
    }

    public TaxConfig(String countryCode, TaxRule taxRule) {
        this(countryCode, DEFAULT_MAX_RULES, taxRule);
    }

    public TaxConfig(String countryCode, int maxRulesCount, TaxRule taxRule) {
        this.countryCode = CountryCode.of(countryCode);
        this.maxRulesCount = maxRulesCount;
        this.add(taxRule);
    }

    void add(TaxRule taxRule) {
        if (maxRulesCount <= taxRules.size()) {
            throw new IllegalStateException("Too many rules");
        }
        taxRules.add(taxRule);
        currentRulesCount++;
        lastModifiedDate = Instant.now();
    }
    void remove(TaxRule taxRule) {
        if (taxRules.contains(taxRule) && taxRules.size() == 1) {
            throw new IllegalStateException("Last rule in country config");
        }
        taxRules.remove(taxRule);
        currentRulesCount--;
        lastModifiedDate = Instant.now();
    }

    public String getCountryCode() {
        return countryCode.asString();
    }

    public int getCurrentRulesCount() {
        return currentRulesCount;
    }

    public int getMaxRulesCount() {
        return maxRulesCount;
    }

    public List<TaxRule> getTaxRules() {
        return taxRules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxConfig taxConfig = (TaxConfig) o;
        return id.equals(taxConfig.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }
}

@Embeddable
class CountryCode {
    private String code;

    public CountryCode() {

    }

    CountryCode(String code) {
        this.code = code;
    }

    static CountryCode of(String code) {
        if (code == null || code.equals("") || code.length() == 1) {
            throw new IllegalStateException("Invalid country code");
        }
        return new CountryCode(code);
    }

    String asString() {
        return code;
    }


}
