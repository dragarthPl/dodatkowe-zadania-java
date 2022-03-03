package legacyfighter.dietary.integration;

import legacyfighter.dietary.TaxConfig;
import legacyfighter.dietary.TaxRule;
import legacyfighter.dietary.TaxRuleService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaxRuleIntegrationTest {
    @Autowired
    TaxRuleService taxRuleService;

    @Test
    void countryCodeIsAlwaysValid() {
        TaxRule rule = TaxRule.linearRule(10, 10, 2010, "taxCode");
        assertThrows(IllegalStateException.class, () -> createConfigWithInitialRule("", 2, rule));
        assertThrows(IllegalStateException.class, () -> createConfigWithInitialRule(null, 2, rule));
        assertThrows(IllegalStateException.class, () -> createConfigWithInitialRule("1", 2, rule));
    }

    @Test
    void aFactorIsNotZero() {
        //given
        createConfigWithInitialRule("HUN", 2, TaxRule.linearRule(10, 10, 2000, "taxCode"));

        //expect
        assertThrows(IllegalStateException.class, () -> taxRuleService.addTaxRuleToCountry("HUN", 0, 4, "taxRule2"));
        assertThrows(IllegalStateException.class, () -> taxRuleService.addTaxRuleToCountry("HUN", 0, 4, 5, "taxRule3"));
    }

    @Test
    void shouldNotHaveMoreThanMaximumNumberOfRules() {
        //given
        TaxRule rule = TaxRule.linearRule(10, 10, 2010, "taxCode");
        TaxConfig config = createConfigWithInitialRule("PL1", 2, rule);
        //and
        taxRuleService.addTaxRuleToCountry("PL1", 2, 4, "taxRule2");

        //expect
        assertThrows(IllegalStateException.class, () -> taxRuleService.addTaxRuleToCountry("PL1", 2, 4, "taxRule3"));
    }

    @Test
    void canAddARule() {
        //given
        TaxRule rule = TaxRule.linearRule(10, 10, 2012, "taxCode");
        TaxConfig config = createConfigWithInitialRule("PL2", 2, rule);

        //when
        taxRuleService.addTaxRuleToCountry("PL2", 2, 4, "taxRule2");

        //then
        assertThat(taxRuleService.rulesCount("PL2")).isEqualTo(2);
    }



    @Test
    void canDeleteARule() {
        //given
        TaxRule rule = TaxRule.linearRule(10, 10, 2020, "taxCode");
        TaxConfig config = createConfigWithInitialRule("PL3", 2, rule);
        //and
        taxRuleService.addTaxRuleToCountry("PL3", 2, 4, "taxRule2");

        //when
        taxRuleService.deleteRule(rule.getId(), config.getId());

        //expect
        assertThat(taxRuleService.rulesCount("PL3")).isEqualTo(1);
    }

    @Test
    void cannotDeleteARuleIfThatIsTheLastOne() {
        //given
        TaxRule rule = TaxRule.linearRule(10, 10, 2022, "taxCode");
        TaxConfig config = createConfigWithInitialRule("PL2", 2, rule);

        //expect
        assertThrows(IllegalStateException.class, () -> taxRuleService.deleteRule(rule.getId(), config.getId()));
    }

    TaxConfig createConfigWithInitialRule(String countryCode, int maxRules, TaxRule rule) {

        return taxRuleService.createTaxConfigWithRule(countryCode, maxRules, rule);
    }
}