package com.weis.match;

import com.weis.match.po.OrderPo;

import java.util.Comparator;

/**
 * Created by jiangwei on 17/08/2017.
 */
public class OrderCompartor implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        return ((OrderPo) o1).getBuyAmount().compareTo(((OrderPo) o2).getBuyAmount());
    }
}
