package com.weis.match;

import com.weis.match.po.ItemPo;

import java.util.Comparator;

/**
 * Created by jiangwei on 17/08/2017.
 */
public class ItemCompartor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        return ((ItemPo) o1).getResidueMatchAmount().compareTo(((ItemPo) o2).getResidueMatchAmount());
    }
}
