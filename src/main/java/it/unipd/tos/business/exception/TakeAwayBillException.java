////////////////////////////////////////////////////////////////////
// Alberto Guarnieri 1187119
////////////////////////////////////////////////////////////////////

package it.unipd.tos.business.exception;

public class TakeAwayBillException extends Exception {
    static final String negativePriceMsg = "Prezzo prodotto negativo",
                        productLimitMsg = "Limite prodotti superato";

    private TakeAwayBillException(String msg) { super(msg); }

    public static TakeAwayBillException negativePrice() {
        return new TakeAwayBillException(negativePriceMsg);
    }

    public static TakeAwayBillException productLimit() {
        return new TakeAwayBillException(productLimitMsg);
    }
}
