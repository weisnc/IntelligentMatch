import com.weis.match.Match;
import com.weis.match.po.ItemPo;
import com.weis.match.po.MatchPo;
import com.weis.match.po.OrderPo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Main main = new Main();
        Match match = new Match();

        List<OrderPo> opList = main.getOrderPoListMock5();
        List<ItemPo> ipList = main.getItemPoListMock5();

        BigDecimal totalOrderAmount = new BigDecimal(0);
        for (OrderPo op :
                opList) {
            totalOrderAmount = totalOrderAmount.add(op.getResidueMatchAmount());
        }
        BigDecimal totalItemAmount = new BigDecimal(0);
        for (ItemPo ip :
                ipList) {
            totalItemAmount = totalItemAmount.add(ip.getResidueMatchAmount());
        }


        List<MatchPo> mpList = match.doMatch(opList, ipList);


        System.out.println("\n");
        System.out.println("------------匹配结果-----------");
        Map<String, BigDecimal> matchedOrderMap = new HashMap<>();
        Map<String, BigDecimal> matchedItemMap = new HashMap<>();
        BigDecimal matchedAmount = new BigDecimal(0);
        System.out.println("订单编号 | 项目编号 | 匹配金额");
        for (MatchPo mp :
                mpList) {
            System.out.println(mp.getOrderPo().getId() + "       " + mp.getItemPo().getId() + "         " + mp.getMatchAmount());
            matchedAmount = matchedAmount.add(mp.getMatchAmount());

            String opId = String.valueOf(mp.getOrderPo().getId());
            BigDecimal matchOrderAmount = matchedOrderMap.get(opId);
            if (null == matchOrderAmount) {
                matchOrderAmount = new BigDecimal(0);
            }
            matchOrderAmount = matchOrderAmount.add(mp.getMatchAmount());
            matchedOrderMap.put(opId, matchOrderAmount);

            String ipId = String.valueOf(mp.getItemPo().getId());
            BigDecimal matchedItemAmount = matchedItemMap.get(ipId);
            if (null == matchedItemAmount) {
                matchedItemAmount = new BigDecimal(0);
            }
            matchedItemAmount = matchedItemAmount.add(mp.getMatchAmount());
            matchedItemMap.put(ipId, matchedItemAmount);
        }
        System.out.println("\n");
        System.out.println("需匹配订单笔数：" + opList.size());
        System.out.println("需匹配订单总额：" + totalOrderAmount);
        System.out.println("可匹配项目总数：" + ipList.size());
        System.out.println("可匹配项目总额：" + totalItemAmount);
        System.out.println("\n");
        System.out.println("成功匹配次数：" + mpList.size());
        System.out.println("已匹配订单笔数：" + matchedOrderMap.size());
        System.out.println("已匹配项目总数：" + matchedItemMap.size());
        System.out.println("已匹配总额：" + matchedAmount);

        OrderPo.sort(opList);
        ItemPo.sort(ipList);

    }

    public List<OrderPo> getOrderPoListMock1() {

        OrderPo op1 = new OrderPo(1, new BigDecimal("100"), new BigDecimal("100"));
        OrderPo op2 = new OrderPo(2, new BigDecimal("50"), new BigDecimal("50"));
        OrderPo op3 = new OrderPo(3, new BigDecimal("20"), new BigDecimal("20"));
        OrderPo op4 = new OrderPo(4, new BigDecimal("10"), new BigDecimal("10"));
        OrderPo op5 = new OrderPo(5, new BigDecimal("5"), new BigDecimal("5"));
        OrderPo op6 = new OrderPo(6, new BigDecimal("2"), new BigDecimal("2"));
        OrderPo op7 = new OrderPo(7, new BigDecimal("1"), new BigDecimal("1"));
        OrderPo op8 = new OrderPo(8, new BigDecimal("0.5"), new BigDecimal("0.5"));
        OrderPo op9 = new OrderPo(9, new BigDecimal("0.1"), new BigDecimal("0.1"));
        OrderPo op10 = new OrderPo(10, new BigDecimal("0.01"), new BigDecimal("0.01"));

        List<OrderPo> opList = new ArrayList();
        opList.add(op1);
        opList.add(op2);
        opList.add(op3);
        opList.add(op4);
        opList.add(op5);
        opList.add(op6);
        opList.add(op7);
        opList.add(op8);
        opList.add(op9);
        opList.add(op10);

        return opList;
    }

    public List<ItemPo> getItemPoListMock1() {

        ItemPo ip1 = new ItemPo(1, "项目1", new BigDecimal("1000"), 200, new BigDecimal("1000"));
        ItemPo ip2 = new ItemPo(2, "项目2", new BigDecimal("500"), 200, new BigDecimal("500"));
        ItemPo ip3 = new ItemPo(3, "项目3", new BigDecimal("200"), 200, new BigDecimal("200"));
        ItemPo ip4 = new ItemPo(4, "项目4", new BigDecimal("100"), 200, new BigDecimal("100"));
        ItemPo ip5 = new ItemPo(5, "项目5", new BigDecimal("50"), 200, new BigDecimal("50"));
        ItemPo ip6 = new ItemPo(6, "项目6", new BigDecimal("20"), 200, new BigDecimal("20"));
        ItemPo ip7 = new ItemPo(7, "项目7", new BigDecimal("10"), 200, new BigDecimal("10"));

        List<ItemPo> ipList = new ArrayList();
        ipList.add(ip1);
        ipList.add(ip2);
        ipList.add(ip3);
        ipList.add(ip4);
        ipList.add(ip5);
        ipList.add(ip6);
        ipList.add(ip7);
        return ipList;
    }

    public List<OrderPo> getOrderPoListMock2() {

        OrderPo op1 = new OrderPo(1, new BigDecimal("1001"), new BigDecimal("1001"));

        List<OrderPo> opList = new ArrayList();
        opList.add(op1);

        return opList;
    }

    public List<ItemPo> getItemPoListMock2() {

        ItemPo ip1 = new ItemPo(1, "项目1", new BigDecimal("1000"), 200, new BigDecimal("1000"));

        List<ItemPo> ipList = new ArrayList();
        ipList.add(ip1);
        return ipList;
    }

    public List<OrderPo> getOrderPoListMock3() {

        OrderPo op1 = new OrderPo(1, new BigDecimal("100"), new BigDecimal("100"));

        List<OrderPo> opList = new ArrayList();
        opList.add(op1);

        return opList;
    }

    public List<ItemPo> getItemPoListMock3() {

        ItemPo ip1 = new ItemPo(1, "项目1", new BigDecimal("1000"), 200, new BigDecimal("1000"));
        ItemPo ip2 = new ItemPo(2, "项目2", new BigDecimal("500"), 200, new BigDecimal("500"));

        List<ItemPo> ipList = new ArrayList();
        ipList.add(ip1);
        ipList.add(ip2);
        return ipList;
    }

    public List<OrderPo> getOrderPoListMock4() {

        OrderPo op1 = new OrderPo(1, new BigDecimal("100"), new BigDecimal("100"));
        OrderPo op2 = new OrderPo(2, new BigDecimal("50"), new BigDecimal("50"));
        OrderPo op3 = new OrderPo(3, new BigDecimal("20"), new BigDecimal("20"));
        OrderPo op4 = new OrderPo(4, new BigDecimal("10"), new BigDecimal("10"));
        OrderPo op5 = new OrderPo(5, new BigDecimal("5"), new BigDecimal("5"));
        OrderPo op6 = new OrderPo(6, new BigDecimal("2"), new BigDecimal("2"));
        OrderPo op7 = new OrderPo(7, new BigDecimal("1"), new BigDecimal("1"));
        OrderPo op8 = new OrderPo(8, new BigDecimal("0.5"), new BigDecimal("0.5"));
        OrderPo op9 = new OrderPo(9, new BigDecimal("0.1"), new BigDecimal("0.1"));
        OrderPo op10 = new OrderPo(10, new BigDecimal("0.01"), new BigDecimal("0.01"));

        List<OrderPo> opList = new ArrayList();
        opList.add(op1);
        opList.add(op2);
        opList.add(op3);
        opList.add(op4);
        opList.add(op5);
        opList.add(op6);
        opList.add(op7);
        opList.add(op8);
        opList.add(op9);
        opList.add(op10);

        return opList;
    }

    public List<ItemPo> getItemPoListMock4() {

//        ItemPo ip1 = new ItemPo(1, "项目1", new BigDecimal("1000"), 200, new BigDecimal("1000"));
//        ItemPo ip2 = new ItemPo(2, "项目2", new BigDecimal("500"), 200, new BigDecimal("500"));
//        ItemPo ip3 = new ItemPo(3, "项目3", new BigDecimal("200"), 200, new BigDecimal("200"));
        ItemPo ip4 = new ItemPo(4, "项目4", new BigDecimal("100"), 200, new BigDecimal("100"));
        ItemPo ip5 = new ItemPo(5, "项目5", new BigDecimal("50"), 200, new BigDecimal("50"));
        ItemPo ip6 = new ItemPo(6, "项目6", new BigDecimal("20"), 200, new BigDecimal("20"));
        ItemPo ip7 = new ItemPo(7, "项目7", new BigDecimal("10"), 200, new BigDecimal("10"));

        List<ItemPo> ipList = new ArrayList();
//        ipList.add(ip1);
//        ipList.add(ip2);
//        ipList.add(ip3);
        ipList.add(ip4);
        ipList.add(ip5);
        ipList.add(ip6);
        ipList.add(ip7);
        return ipList;
    }

    public List<OrderPo> getOrderPoListMock5() {

        List<OrderPo> opList = new ArrayList();
        int j = 0;
        for (int i = 0; i < 10; i++) {
            opList.add(new OrderPo(j++, new BigDecimal("100"), new BigDecimal("100")));
            opList.add(new OrderPo(j++, new BigDecimal("50"), new BigDecimal("50")));
            opList.add(new OrderPo(j++, new BigDecimal("20"), new BigDecimal("20")));
            opList.add(new OrderPo(j++, new BigDecimal("10"), new BigDecimal("10")));
            opList.add(new OrderPo(j++, new BigDecimal("5"), new BigDecimal("5")));
            opList.add(new OrderPo(j++, new BigDecimal("2"), new BigDecimal("2")));
            opList.add(new OrderPo(j++, new BigDecimal("1"), new BigDecimal("1")));
            opList.add(new OrderPo(j++, new BigDecimal("0.5"), new BigDecimal("0.5")));
            opList.add(new OrderPo(j++, new BigDecimal("0.1"), new BigDecimal("0.1")));
            opList.add(new OrderPo(j++, new BigDecimal("0.01"), new BigDecimal("0.01")));
        }
        opList.add(new OrderPo(j++, new BigDecimal("1"), new BigDecimal("1")));
        return opList;
    }

    public List<ItemPo> getItemPoListMock5() {

        ItemPo ip1 = new ItemPo(1, "项目1", new BigDecimal("1000"), 200, new BigDecimal("1000"));
        ItemPo ip2 = new ItemPo(2, "项目2", new BigDecimal("500"), 200, new BigDecimal("500"));
        ItemPo ip3 = new ItemPo(3, "项目3", new BigDecimal("200"), 200, new BigDecimal("200"));
        ItemPo ip4 = new ItemPo(4, "项目4", new BigDecimal("100"), 200, new BigDecimal("100"));
        ItemPo ip5 = new ItemPo(5, "项目5", new BigDecimal("50"), 200, new BigDecimal("50"));
        ItemPo ip6 = new ItemPo(6, "项目6", new BigDecimal("20"), 200, new BigDecimal("20"));
        ItemPo ip7 = new ItemPo(7, "项目7", new BigDecimal("10"), 200, new BigDecimal("10"));
        ItemPo ip8 = new ItemPo(8, "项目8", new BigDecimal("6.1"), 200, new BigDecimal("6.1"));

        List<ItemPo> ipList = new ArrayList();
        ipList.add(ip1);
        ipList.add(ip2);
        ipList.add(ip3);
        ipList.add(ip4);
        ipList.add(ip5);
        ipList.add(ip6);
        ipList.add(ip7);
        ipList.add(ip8);
        return ipList;
    }

    public List<OrderPo> getOrderPoListMock6() {

        List<OrderPo> opList = new ArrayList();
        for (int i = 1; i < 200; i++) {
            opList.add(new OrderPo(i, new BigDecimal("1"), new BigDecimal("1")));
        }
        opList.add(new OrderPo(200, new BigDecimal("0.6"), new BigDecimal("0.6")));
        return opList;
    }

    public List<ItemPo> getItemPoListMock6() {

        ItemPo ip1 = new ItemPo(1, "项目1", new BigDecimal("200"), 200, new BigDecimal("200"));

        List<ItemPo> ipList = new ArrayList();
        ipList.add(ip1);
        return ipList;
    }
}
