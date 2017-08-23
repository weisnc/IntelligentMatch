package com.weis.match.po;

import java.math.BigDecimal;

/**
 * Created by jiangwei on 17/08/2017.
 */
public class MatchPo {
    // 订单po
    private OrderPo orderPo;
    // 项目po
    private ItemPo itemPo;
    // 匹配金额
    private BigDecimal matchAmount;

    public MatchPo() {
    }

    public MatchPo(OrderPo orderPo, ItemPo itemPo, BigDecimal matchAmount) {
        this.orderPo = orderPo;
        this.itemPo = itemPo;
        this.matchAmount = matchAmount;
        this.orderPo.setResidueMatchAmount(this.orderPo.getResidueMatchAmount().subtract(this.matchAmount));
        this.itemPo.setResidueMatchAmount(this.itemPo.getResidueMatchAmount().subtract(this.matchAmount));

        orderPo.getMatchPoList().add(this);
        itemPo.getMatchPoList().add(this);

        // 刷新可投资人数和每份金额
        itemPo.refreshPeopleNOAndPerAmount();
        System.out.println("匹配成功!订单编号:" + this.orderPo.getId() + " | 项目编号：" + this.itemPo.getId() + " | 匹配金额：" + this.matchAmount);
    }

    public OrderPo getOrderPo() {
        return orderPo;
    }

    public void setOrderPo(OrderPo orderPo) {
        this.orderPo = orderPo;
    }

    public ItemPo getItemPo() {
        return itemPo;
    }

    public void setItemPo(ItemPo itemPo) {
        this.itemPo = itemPo;
    }

    public BigDecimal getMatchAmount() {
        return matchAmount;
    }

    public void setMatchAmount(BigDecimal matchAmount) {
        this.matchAmount = matchAmount;
    }
}
