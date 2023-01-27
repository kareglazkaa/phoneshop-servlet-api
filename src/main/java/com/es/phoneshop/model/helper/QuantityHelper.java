package com.es.phoneshop.model.helper;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;

public class QuantityHelper {

    public static int getQuantity(String quantity, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(request.getLocale());
        return numberFormat.parse(quantity).intValue();
    }


}
