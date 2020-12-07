////////////////////////////////////////////////////////////////////
// Alberto Guarnieri 1187119
////////////////////////////////////////////////////////////////////

package it.unipd.tos.business;

import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.User;
import it.unipd.tos.model.MenuItem;
import it.unipd.tos.model.ItemType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TakeAwayBillTest {

    private final TakeAwayBill bill = new TakeAwayBill();
    private final double DELTA_OP = 0.009;

    @Test
    public void checkDomain_NegativeItemPrice_ThrowsTakeAwayBillException() {
        TakeAwayBillException thrown = assertThrows(
                TakeAwayBillException.class,
                () -> bill.checkDomain(Arrays.asList(
                new MenuItem(ItemType.GELATO, "Vaniglia", 3),
                new MenuItem(ItemType.BEVANDA, "atnaF", -2)
                )
        ));
        assertEquals(TakeAwayBillException.negativePrice().getMessage(),
                thrown.getMessage());
    }

    @Test
    public void checkDomain_MoreThan30Orders_ThrowsTakeAwayBillException() {
        List<MenuItem> items = new ArrayList<>();
        for(int i = 0; i < 35; ++i) {
            items.add(new MenuItem(ItemType.GELATO, "Vaniglia", 1));
        }
        TakeAwayBillException thrown = assertThrows(
                TakeAwayBillException.class,
                () -> bill.checkDomain(items));
        assertEquals(TakeAwayBillException.productLimit().getMessage(),
                thrown.getMessage());
    }

    @Test
    public void checkDomain_Exactly30Orders_ExceptionNotThrown()
            throws TakeAwayBillException {
        List<MenuItem> items = new ArrayList<>();
        for(int i = 0; i < 30; ++i) {
            items.add(new MenuItem(ItemType.GELATO, "Vaniglia", 1));
        }
        bill.checkDomain(items);
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

    @Test
    public void getOrderPrice_MoreThan50EuroGelatoBudino_CalculateSumWithSale()
            throws TakeAwayBillException {
        double[] results = {
                bill.getOrderPrice(Arrays.asList(
                        new MenuItem(ItemType.GELATO, "ExtraPanna", 8),
                        new MenuItem(ItemType.BUDINO, "ExtraCioccolato", 12),
                        new MenuItem(ItemType.GELATO, "ExtraCosto", 31)
                        ),
                        new User("Filippa", "Calla", "filcal@gmail.com",
                                LocalDate.of(1999, 11, 11))
                ),
                bill.getOrderPrice(Arrays.asList(
                        new MenuItem(ItemType.GELATO, "Pistacchio", 37.25),
                        new MenuItem(ItemType.BUDINO, "Arachidi", 20.5),
                        new MenuItem(ItemType.BEVANDA, "Sprite", 2.25)
                        ),
                        new User("Salvo", "Silvestri", "sal@sil.it",
                                LocalDate.of(1975, 12, 25))
                ),
        },
                expected = {45.9, 54};
        assertArrayEquals(results, expected, DELTA_OP);
    }

    @Test
    public void getOrderPrice_Exactly50EuroGelatoBudino_CalculateFullPrice()
            throws TakeAwayBillException {
        double result = bill.getOrderPrice(Arrays.asList(
                new MenuItem(ItemType.GELATO, "Nettare", 22),
                new MenuItem(ItemType.BEVANDA, "Champagne", 17.5),
                new MenuItem(ItemType.BUDINO, "Budisì", 10.5)
                ),
                new User("Franco", "Vero", "franco50@yahoo.com",
                        LocalDate.of(1950, 2, 12))
        );
        assertEquals(result, 50, DELTA_OP);
    }
}