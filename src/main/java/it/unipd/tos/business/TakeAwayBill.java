////////////////////////////////////////////////////////////////////
// Alberto Guarnieri 1187119
////////////////////////////////////////////////////////////////////

package it.unipd.tos.business;

import  java.util.List;

import it.unipd.tos.business.exception.TakeAwayBillException;
import it.unipd.tos.model.ItemType;
import it.unipd.tos.model.MenuItem;
import it.unipd.tos.model.User;

public class TakeAwayBill {

    void checkDomain(List<MenuItem> items)
            throws TakeAwayBillException {
        if(items.stream().anyMatch(i -> i.price < 0)) {
            throw new TakeAwayBillException();
        }
    }

    double getOrderPrice(List<MenuItem> itemsOrdered, User user)
            throws TakeAwayBillException {
        checkDomain(itemsOrdered);

        double orderPrice = 0;
        Double cheapestGelato = null;
        int numGelato = 0;
        for(MenuItem item : itemsOrdered) {
            orderPrice += item.price;

            if(item.itemType == ItemType.GELATO) {
                ++numGelato;
                if(cheapestGelato == null || cheapestGelato > item.price) {
                    cheapestGelato = item.price;
                }
            }
        }
        if(numGelato > 5) {
            orderPrice -= cheapestGelato / 2;
        }
        return orderPrice;
    }
}