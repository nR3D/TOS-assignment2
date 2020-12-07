////////////////////////////////////////////////////////////////////
// Alberto Guarnieri 1187119
////////////////////////////////////////////////////////////////////

package it.unipd.tos.business;

import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.User;
import it.unipd.tos.model.MenuItem;
import it.unipd.tos.model.ItemType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class TakeAwayBillTest {

    private final TakeAwayBill bill = new TakeAwayBill();
    private final double DELTA_OP = 0.009;

    @Test(expected = TakeAwayBillException.class)
    public void checkDomain_NegativeItemPrice_ThrowsTakeAwayBillException()
            throws TakeAwayBillException {
        bill.checkDomain(Arrays.asList(
                new MenuItem(ItemType.GELATO, "Vaniglia", 3),
                new MenuItem(ItemType.BEVANDA, "atnaF", -2)
                )
        );
    }

    @Test
    public void getOrderPrice_OrderWithPositivePrices_CalculateSum()
            throws TakeAwayBillException {
        double[] results = {
                bill.getOrderPrice(Arrays.asList(
                        new MenuItem(ItemType.BEVANDA, "CocaCola", 2.5),
                        new MenuItem(ItemType.BUDINO, "CoppaNafta", 6.25),
                        new MenuItem(ItemType.GELATO, "Biancaneve", 2)
                        ),
                        new User("Mario", "Rossi", "mario@rossi.it",
                                LocalDate.of(1992, 4, 2))
                ),
                bill.getOrderPrice(Arrays.asList(
                        new MenuItem(ItemType.BUDINO, "BudinoSpecial", 10),
                        new MenuItem(ItemType.BEVANDA, "Acqua", 1.25)
                        ),
                        new User("Luca", "Bianchi", "luca@bianchi.it",
                                LocalDate.of(2001, 1, 30))
                ),
                bill.getOrderPrice(Collections.singletonList(
                        new MenuItem(ItemType.GELATO, "CoppaPerSingle", 2.75)
                        ),
                        new User("Genoveffa", "Berna", "g.berna@hotmail.it",
                                LocalDate.of(1987, 7, 15))
                ),
                bill.getOrderPrice(Collections.emptyList(),
                        new User("Massimo", "Bacone", "mbacon@gmail.com",
                                LocalDate.of(2003, 8, 11))
                )
        },
                expected = {10.75, 11.25, 2.75, 0};
        assertArrayEquals(results, expected, DELTA_OP);
    }

    @Test
    public void getOrderPrice_MoreThanFiveGelatoPerBill_CalculateSumWithSale()
            throws TakeAwayBillException {
        double[] results = {
                bill.getOrderPrice(Arrays.asList(
                        new MenuItem(ItemType.GELATO, "Fragola", 2),
                        new MenuItem(ItemType.BEVANDA, "Caffé", 1.2),
                        new MenuItem(ItemType.GELATO, "Amarena", 2.5),
                        new MenuItem(ItemType.GELATO, "CoppaVuota", 0.2),
                        new MenuItem(ItemType.GELATO, "Wasabi", 4),
                        new MenuItem(ItemType.BUDINO, "Cioccolato", 3.1),
                        new MenuItem(ItemType.GELATO, "Caviale", 20),
                        new MenuItem(ItemType.GELATO, "Mandarino", 2)
                        ),
                        new User("Luigi", "Mario", "itsame@luigi.jp",
                                LocalDate.of(1983, 3, 10))
                ),
                bill.getOrderPrice(Arrays.asList(
                        new MenuItem(ItemType.GELATO, "Panna", 3),
                        new MenuItem(ItemType.GELATO, "Lime", 2.25),
                        new MenuItem(ItemType.GELATO, "Pizzaiola", 1.5),
                        new MenuItem(ItemType.GELATO, "Cookie", 1),
                        new MenuItem(ItemType.GELATO, "Menta", 2),
                        new MenuItem(ItemType.GELATO, "Mentolo", 1),
                        new MenuItem(ItemType.GELATO, "Campari", 5)
                        ),
                        new User("Mario", "Mario", "itsame@mario.jp",
                                LocalDate.of(1983, 3, 10))
                ),
        },
                expected = {34.9, 15.25};
        assertArrayEquals(results, expected, DELTA_OP);
    }
}