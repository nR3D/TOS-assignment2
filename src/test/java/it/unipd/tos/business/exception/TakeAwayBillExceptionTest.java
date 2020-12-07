package it.unipd.tos.business.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TakeAwayBillExceptionTest {

    @Test
    public void negativePrice_MsgCreation_ReturnSameMsgAsDataField() {
        assertEquals(TakeAwayBillException.negativePriceMsg,
                TakeAwayBillException.negativePrice().getMessage());
    }

    @Test
    public void productLimit_MsgCreation_ReturnSameMsgAsDataField() {
        assertEquals(TakeAwayBillException.productLimitMsg,
                TakeAwayBillException.productLimit().getMessage());
    }
}
