package com.weis.match.po;

import com.weis.match.OrderCompartor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jiangwei on 17/08/2017.
 */
public class OrderPo {
    // 订单编号
    private int id;
    // 购买金额
    private BigDecimal buyAmount;
    // 剩余待匹配金额
    private BigDecimal residueMatchAmount;
    // 已匹配的结果
    private List<MatchPo> matchPoList;

    public OrderPo() {

    }

    public OrderPo(int id, BigDecimal buyAmount, BigDecimal residueMatchAmount) {
        this.id = id;
        this.buyAmount = buyAmount;
        this.residueMatchAmount = residueMatchAmount;
        this.matchPoList = new ArrayList<>();
    }

    public static void sort(List<OrderPo> opList) {
        // 订单根据订单金额从大到小排序
        OrderCompartor oc = new OrderCompartor();
        Collections.sort(opList, oc);
        Collections.reverse(opList);
        System.out.println("---------------订单排序结果:---------------");
        System.out.println("订单编号 | 订单金额 | 剩余待匹配金额");
        for (OrderPo orderPo :
                opList) {
            System.out.println(orderPo.getId() + "        " + orderPo.getBuyAmount() + "        " + orderPo.getResidueMatchAmount());
        }
        System.out.println("\n");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public BigDecimal getResidueMatchAmount() {
        return residueMatchAmount;
    }

    public void setResidueMatchAmount(BigDecimal residueMatchAmount) {
        this.residueMatchAmount = residueMatchAmount;
    }

    public List<MatchPo> getMatchPoList() {
        return matchPoList;
    }

    public void setMatchPoList(List<MatchPo> matchPoList) {
        this.matchPoList = matchPoList;
    }
}
