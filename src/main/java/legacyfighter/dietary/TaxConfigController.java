package legacyfighter.dietary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TaxConfigController {

    @Autowired
    private TaxRuleService taxRuleService;

    @GetMapping("/config")
    public Map<String, List<TaxRule>> taxConfigs() {
        return taxRuleService.findAllConfigs();



    }


}
