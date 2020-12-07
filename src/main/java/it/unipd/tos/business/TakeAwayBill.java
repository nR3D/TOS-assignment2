////////////////////////////////////////////////////////////////////
// Alberto Guarnieri 1187119
////////////////////////////////////////////////////////////////////

package it.unipd.tos.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.ItemType;
import it.unipd.tos.model.MenuItem;
import it.unipd.tos.model.User;

public class TakeAwayBill {

    List<String> ordersGifted = new ArrayList<>();

    double getRandomValue() {
        return Math.random();
    }

    int getCurrentHour() {
        return LocalTime.now().getHour();
    }

    void checkDomain(List<MenuItem> items)
            throws TakeAwayBillException {
        if(items.size() > 30) {
            throw TakeAwayBillException.productLimit();
        }
        if(items.size() == 0) {
            throw TakeAwayBillException.emptyOrder();
        }
        if(items.stream().anyMatch(i -> i.price < 0)) {
            throw TakeAwayBillException.negativePrice();
        }
    }

    boolean giftOrder(User user) {
        if(ordersGifted.size() < 10 && getCurrentHour() == 18) {
            boolean userIsMinor = user.birthday.plusYears(18)
                    .isAfter(LocalDate.now());
            if (userIsMinor && !ordersGifted.contains(user.email)
                    && getRandomValue() < 0.5) {
                ordersGifted.add(user.email);
                return true;
            }
        }
        return false;
    }

    double getOrderPrice(List<MenuItem> itemsOrdered, User user)
            throws TakeAwayBillException {
        checkDomain(itemsOrdered);

        if(giftOrder(user)) {
            return 0;
        }

        final Map<ItemType, Double> orderPrice =
                Arrays.stream(ItemType.values())
                        .collect(
                                Collectors.toMap(Function.identity(), i -> 0.0)
                        );

        Double cheapestGelato = null;
        int numGelato = 0;
        for(MenuItem item : itemsOrdered) {
            orderPrice.put(item.itemType,
                    orderPrice.get(item.itemType) + item.price);

            if(item.itemType == ItemType.GELATO) {
                ++numGelato;
                if(cheapestGelato == null || cheapestGelato > item.price) {
                    cheapestGelato = item.price;
                }
            }
        }

        if(numGelato > 5) {
            orderPrice.put(ItemType.GELATO,
                    orderPrice.get(ItemType.GELATO) - cheapestGelato / 2);
        }

        double totalPrice = orderPrice.values().stream()
                .mapToDouble(i -> i)
                .sum();

        if(orderPrice.get(ItemType.GELATO) +
                orderPrice.get(ItemType.BUDINO) > 50) {
            totalPrice *= 0.9;
        }

        if(totalPrice < 10) {
            totalPrice += 0.5;
        }

        return totalPrice;
    }
}