package legacyfighter.dietary;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TaxRule {

    public TaxRule() {

    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String taxCode;

    private boolean isLinear;
    private Integer aFactor;
    private Integer bFactor;

    private boolean isSquare;
    private Integer aSquareFactor;
    private Integer bSquareFactor;
    private Integer cSuqreFactor;

    private Integer year;

    @ManyToOne
    private TaxConfig taxConfig;

    TaxRule(String taxCode, int aFactor, int bFactor, int year) {
        isLinear = true;
        this.taxCode = taxCode;
        this.aFactor = aFactor;
        this.bSquareFactor = bFactor;
        this.year = year;
    }

    TaxRule(String taxCode, int aSquareFactor, int bSquareFactor, int cSquareFactor, int year) {
        this.taxCode = taxCode;
        this.aSquareFactor = aSquareFactor;
        this.bSquareFactor = bSquareFactor;
        this.cSuqreFactor = cSquareFactor;
        isSquare = true;
        this.year = year;
    }

    public static TaxRule linearRule(int aFactor, int bFactor, int year, String taxCode) {
        if (aFactor == 0) {
            throw new IllegalStateException("Invalid aFactor");
        }
        return new TaxRule(taxCode, aFactor, bFactor, year);
    }

    public static TaxRule squareRule(int aSquareFactor, int bSquareFactor, int cSquareFactor, int year, String taxCode) {
        if (aSquareFactor == 0) {
            throw new IllegalStateException("Invalid aFactor");
        }
        return new TaxRule(taxCode, aSquareFactor, bSquareFactor, cSquareFactor, year);
    }

    public boolean isLinear() {
        return isLinear;
    }

    public Integer getaFactor() {
        return aFactor;
    }

    public Integer getbFactor() {
        return bFactor;
    }

    public boolean isSquare() {
        return isSquare;
    }

    public Integer getaSquareFactor() {
        return aSquareFactor;
    }

    public Integer getbSquareFactor() {
        return bSquareFactor;
    }

    public Integer getcSuqreFactor() {
        return cSuqreFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxRule)) {
            return false;
        }
        TaxRule that = (TaxRule) o;
        return this.getTaxCode().equals(that.getTaxCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxCode);
    }


    public String getTaxCode() {
        return "A. 899. " + year + taxCode;
    }

    public Long getId() {
        return id;
    }
}
