package legacyfighter.dietary.newproducts;

public class OldProductDescription {
    private OldProduct oldProduct;

    public OldProductDescription(OldProduct oldProduct) {
        this.oldProduct = oldProduct;
    }

    void replaceCharFromDesc(char charToReplace, char replaceWith) {
        oldProduct.replaceCharFromDesc(charToReplace, replaceWith);
    }

    String formatDesc() {
        return oldProduct.formatDesc();
    }


}
