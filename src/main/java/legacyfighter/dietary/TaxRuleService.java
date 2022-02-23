package legacyfighter.dietary;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.Year;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaxRuleService {

    @Autowired
    private TaxRuleRepository taxRuleRepository;

    @Autowired
    private TaxConfigRepository taxConfigRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void addTaxRuleToCountry(String countryCode, int aFactor, int bFactor, String taxCode) {
        TaxRule taxRule = TaxRule.linearRule(aFactor, bFactor, Year.now().getValue(), taxCode);
        TaxConfig taxConfig = taxConfigRepository.findByCountryCode(countryCode);
        if (taxConfig == null) {
            taxConfig = createTaxConfigWithRule(countryCode, taxRule);
            return;
        }
        taxConfig.add(taxRule);

        List<Order> byOrderState = orderRepository.findByOrderState(Order.OrderState.Initial);

        byOrderState.forEach(order -> {
            if (order.getCustomerOrderGroup().getCustomer().getType().equals(Customer.Type.Person)) {
                order.getTaxRules().add(taxRule);
                orderRepository.save(order);
            }

        });
    }

    @Transactional
    public TaxConfig createTaxConfigWithRule(String countryCode, TaxRule taxRule) {
        TaxConfig taxConfig = new TaxConfig(countryCode, taxRule);
        taxConfigRepository.save(taxConfig);
        return taxConfig;
    }

    @Transactional
    public TaxConfig createTaxConfigWithRule(String countryCode, int maxRulesCount, TaxRule taxRule) {
        TaxConfig taxConfig = new TaxConfig(countryCode, maxRulesCount, taxRule);
        taxConfigRepository.save(taxConfig);
        return taxConfig;
    }

    @Transactional
    public void addTaxRuleToCountry(String countryCode, int aFactor, int bFactor, int cFactor, String taxCode) {
        TaxRule taxRule = TaxRule.squareRule(aFactor, bFactor, cFactor, Year.now().getValue(), taxCode);
        TaxConfig taxConfig = taxConfigRepository.findByCountryCode(countryCode);
        if (taxConfig == null) {
            taxConfig = createTaxConfigWithRule(countryCode, taxRule);
        }
        taxConfig.add(taxRule);
    }

    @Transactional
    public void deleteRule(Long taxRuleId, Long configId) {
        TaxRule taxRule = taxRuleRepository.getOne(taxRuleId);
        TaxConfig taxConfig = taxConfigRepository.getOne(configId);
        taxConfig.remove(taxRule);
    }

    @Transactional
    public List<TaxRule> findRules(String countryCode) {
        return taxConfigRepository.findByCountryCode(countryCode).getTaxRules();
    }

    @Transactional
    public int rulesCount(String countryCode) {
        return taxConfigRepository.findByCountryCode(countryCode).getCurrentRulesCount();
    }

    public Map<String, List<TaxRule>> findAllConfigs() {
        List<TaxConfig> taxConfigs = taxConfigRepository.findAll();

        Map<String, List<TaxRule>> map = new HashMap<>();
        for (TaxConfig tax : taxConfigs) {
            if (map.get(tax.getCountryCode()) == null) {
                map.put(tax.getCountryCode(), tax.getTaxRules());
            } else {
                map.get(tax.getCountryCode()).addAll(tax.getTaxRules());
            }
        }
        Map<String, List<TaxRule>> newRuleMap= new HashMap<>();
        for (Map.Entry<String, List<TaxRule>> taxList : map.entrySet()) {
            Collection<TaxRule> values = taxList.getValue();
            List<TaxRule> newList = values
                    .stream()
                    .filter(Utils.distinctByKey(TaxRule::getTaxCode))
                    .collect(Collectors.toList());
            newRuleMap.put(taxList.getKey(), newList);
        }

        return newRuleMap;
    }
}
