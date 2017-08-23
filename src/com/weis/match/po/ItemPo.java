package com.weis.match.po;

import com.weis.match.ItemCompartor;
import com.weis.match.ItemPerAmountCompartor;
import com.weis.match.Match;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by jiangwei on 17/08/2017.
 */
public class ItemPo {
    // 项目编号
    private int id;
    // 项目名称
    private String name;
    // 项目总额
    private BigDecimal totalAmount;
    // 可匹配人数
    private int allowMatchPeopleNO;
    // 当前每份金额
    private BigDecimal currPerAmount;
    // 剩余可匹配金额
    private BigDecimal residueMatchAmount;
    // 已匹配的结果
    private List<MatchPo> matchPoList;

    public ItemPo() {
    }

    public ItemPo(int id, String name, BigDecimal totalAmount, int allowMatchPeopleNO, BigDecimal residueMatchAmount) {
        this.id = id;
        this.name = name;
        this.totalAmount = totalAmount;
        this.allowMatchPeopleNO = allowMatchPeopleNO;
        this.residueMatchAmount = residueMatchAmount;
        this.matchPoList = new ArrayList<>();
        this.currPerAmount = this.residueMatchAmount.divide(new BigDecimal(this.allowMatchPeopleNO), 6, BigDecimal.ROUND_HALF_EVEN);
    }

    public static void sort(List<ItemPo> ipList) {
        // 项目根据可匹配额度从大到小排序
        ItemCompartor ic = new ItemCompartor();
        Collections.sort(ipList, ic);
        Collections.reverse(ipList);

        System.out.println("---------------项目排序结果:---------------");
        System.out.println("项目编号 | 项目金额 | 项目可匹配人数 | 项目可匹配金额 | 当前项目每份金额");
        for (ItemPo itemPo :
                ipList) {
            System.out.println(itemPo.getId() + "         " + itemPo.getTotalAmount() + "          " + itemPo.getAllowMatchPeopleNO() + "          " + itemPo.getResidueMatchAmount() + "         " + itemPo.getCurrPerAmount());
        }
        System.out.println("\n");
    }

    public static void sortPerAmount(List<ItemPo> ipList) {
        // 项目根据每份金额从小到大排序
        ItemPerAmountCompartor iprc = new ItemPerAmountCompartor();
        Collections.sort(ipList, iprc);

        System.out.println("---------------项目根据每份金额从小到大排序结果:---------------");
        System.out.println("项目编号 | 当前每份金额");
        for (ItemPo itemPo :
                ipList) {
            System.out.println(itemPo.getId() + "         " + itemPo.getCurrPerAmount());
        }
        System.out.println("\n");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAllowMatchPeopleNO() {
        return allowMatchPeopleNO;
    }

    public void setAllowMatchPeopleNO(int allowMatchPeopleNO) {
        this.allowMatchPeopleNO = allowMatchPeopleNO;
    }

    public BigDecimal getResidueMatchAmount() {
        return residueMatchAmount;
    }

    public void setResidueMatchAmount(BigDecimal residueMatchAmount) {
        this.residueMatchAmount = residueMatchAmount;
    }

    public BigDecimal getCurrPerAmount() {
        return currPerAmount;
    }

    public void setCurrPerAmount(BigDecimal currPerAmount) {
        this.currPerAmount = currPerAmount;
    }

    public List<MatchPo> getMatchPoList() {
        return matchPoList;
    }

    public void setMatchPoList(List<MatchPo> matchPoList) {
        this.matchPoList = matchPoList;
    }

    /**
     * 刷新当前项目可投资人数和每份金额
     */
    public void refreshPeopleNOAndPerAmount() {
        Set<String> peopleSet = new HashSet<>();
        for (MatchPo mp :
                matchPoList) {
            // TODO 这里需通过订单所属的用户id区别项目人数，暂时用订单id代替
            peopleSet.add(String.valueOf(mp.getOrderPo().getId()));
        }
        this.setAllowMatchPeopleNO(200 - peopleSet.size());
        if (this.getAllowMatchPeopleNO() <= 0) {
            this.setCurrPerAmount(BigDecimal.ZERO);
        } else {
            this.setCurrPerAmount(this.getResidueMatchAmount().divide(new BigDecimal(this.getAllowMatchPeopleNO()), 6, BigDecimal.ROUND_HALF_EVEN));
        }

    }

    // 选择项目
    public ItemPo selectIp(List<ItemPo> ipList) {
        Match.currIpIndex++;
        ItemPo currIp = this;
        if (ipList.size() > Match.currIpIndex) {
            // 存在，选中下一个项目
            currIp = ipList.get(Match.currIpIndex);
        } else {
            // 不存在，继续选中当前项目
            if (currIp.getResidueMatchAmount().compareTo(BigDecimal.ZERO) <= 0) {
                // 匹配部分成功，结束
                return null;
            }
            if (Match.currIpIndex > 0) {
                Match.currIpIndex--;
            }
        }
        return currIp;
    }
}
