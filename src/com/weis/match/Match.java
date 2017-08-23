package com.weis.match;

import com.weis.match.po.ItemPo;
import com.weis.match.po.MatchPo;
import com.weis.match.po.OrderPo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangwei on 17/08/2017.
 */
public class Match {

    // 黄金分割比例
    private static final BigDecimal goldenCutNumber = new BigDecimal("0.618");
    // 当前项目下标
    public static int currIpIndex = 0;

    /**
     * 开始匹配
     * 为了便于测试，Item通过参数传递，与项目集成时需数据库动态获取
     *
     * @param opList
     * @param ipList
     * @return
     */
    public List<MatchPo> doMatch(List<OrderPo> opList, List<ItemPo> ipList) {
        if (null == opList || opList.isEmpty()) return null;
        if (null == ipList || ipList.isEmpty()) return null;

        // 1、排序
        OrderPo.sort(opList);

        List<MatchPo> mpList = new ArrayList();
        match:
        for (OrderPo op :
                opList) {

            ItemPo.sort(ipList);

            // 2、订单未匹配的金额是否小于项目中最小的每份金额？
            List<ItemPo> ipListCopy = new ArrayList(ipList);
            ItemPo.sortPerAmount(ipListCopy);
            if (op.getResidueMatchAmount().compareTo(ipListCopy.get(0).getCurrPerAmount()) < 0) {
                // 匹配失败，结束
                continue;
            }

            // 3、项目数量是否大于1？
            if (ipList.size() <= 1) {
                ItemPo ip = ipList.get(0);
                // 订单未匹配金额是否大于项目的剩余可匹配金额？
                if (op.getResidueMatchAmount().compareTo(ip.getResidueMatchAmount()) > 0) {
                    MatchPo mp = new MatchPo(op, ip, ip.getResidueMatchAmount());
                    mpList.add(mp);
                    // 部分匹配成功，结束
                    continue;
                } else {
                    MatchPo mp = new MatchPo(op, ip, op.getResidueMatchAmount());
                    mpList.add(mp);
                    // 匹配成功，结束
                    continue;
                }
            }

            // 份额A
            BigDecimal shareA = BigDecimal.valueOf(0);
            // 份额B
            BigDecimal shareB = BigDecimal.valueOf(0);
            // 份额C
            BigDecimal shareC = BigDecimal.valueOf(0);
            // 剩余份额
            BigDecimal shareResidue = BigDecimal.valueOf(0);

            // 4、当前订单是否已部分匹配
            if (op.getResidueMatchAmount().compareTo(BigDecimal.ZERO) > 0 && op.getBuyAmount().compareTo(op.getResidueMatchAmount()) > 0) {
                // 已部分匹配
                if (op.getMatchPoList().size() == 1) {
                    // 将当前订单剩余未匹配金额按照黄金分割比例分成A、B两份。
                    shareA = op.getResidueMatchAmount().multiply(goldenCutNumber);
                    shareB = op.getResidueMatchAmount().subtract(shareA);
                } else {
                    shareA = op.getResidueMatchAmount();
                }
            } else {
                // 将当前订单金额按照黄金分割比例分成A、B、C三份。
                shareA = op.getResidueMatchAmount().multiply(goldenCutNumber).setScale(6, BigDecimal.ROUND_UP);
                shareB = (op.getResidueMatchAmount().subtract(shareA)).multiply(goldenCutNumber).setScale(6, BigDecimal.ROUND_UP);
                shareC = op.getResidueMatchAmount().subtract(shareA).subtract(shareB);
            }

            System.out.println("订单" + op.getId() + "拆分结果：shareA:" + shareA + " | " + "shareB:" + shareB + " | " + "shareC:" + shareC);

            // B是否小于项目中最小的每份金额？
            if (shareA.compareTo(ipListCopy.get(0).getCurrPerAmount()) < 0 || shareB.compareTo(ipListCopy.get(0).getCurrPerAmount()) < 0) {
                shareC = BigDecimal.valueOf(0);
                shareB = BigDecimal.valueOf(0);
                shareA = op.getResidueMatchAmount();
            } else {
                // C是否小于项目中最小的每份金额？
                if (shareC.compareTo(ipListCopy.get(0).getCurrPerAmount()) < 0) {
                    shareB = shareB.add(shareC);
                    shareC = BigDecimal.valueOf(0);
                }
            }

            currIpIndex = 0;

            // 5、A开始匹配，选中第一个项目
            Map<String, Object> retMap = shareMatch(shareA, shareB, ipList, op, mpList);
            if (null == retMap) {
                // 匹配失败，结束
                continue;
            }
            ItemPo currIp = (ItemPo) retMap.get("currIp");
            shareA = (BigDecimal) retMap.get("matchShare");
            shareB = (BigDecimal) retMap.get("mergeShare");

            //B是否大于0？
            if (shareB.compareTo(BigDecimal.ZERO) <= 0) {
                // 匹配部分成功，结束
                continue;
            }
            // 6、B开始匹配
            // 是否存在下一个项目？
            currIp = currIp.selectIp(ipList);
            if (null == currIp) {
                // 匹配部分成功，结束
                continue;
            }

            retMap = shareMatch(shareB, shareC, ipList, op, mpList);
            if (null == retMap) {
                // 匹配部分成功，结束
                continue;
            }
            currIp = (ItemPo) retMap.get("currIp");
            shareB = (BigDecimal) retMap.get("matchShare");
            shareC = (BigDecimal) retMap.get("mergeShare");
            if (shareC.compareTo(BigDecimal.ZERO) <= 0) {
                // 匹配部分成功，结束
                continue;
            }

            // C开始匹配
            // 是否存在下一个项目？
            currIp = currIp.selectIp(ipList);
            if (null == currIp) {
                // 匹配部分成功，结束
                continue;
            }

            retMap = shareMatch(shareC, shareResidue, ipList, op, mpList);
            if (null == retMap) {
                // 匹配部分成功，结束
                continue;
            }
            currIp = (ItemPo) retMap.get("currIp");
            shareC = (BigDecimal) retMap.get("matchShare");
            shareResidue = (BigDecimal) retMap.get("mergeShare");
            if (shareResidue.compareTo(BigDecimal.ZERO) <= 0) {
                // 全部匹配成功，结束
                continue;
            }

            BigDecimal shareResidueTmp = BigDecimal.valueOf(0);
            do {
                // shareResidue开始匹配
                // 是否存在下一个项目？
                currIp = currIp.selectIp(ipList);
                if (null == currIp) {
                    // 匹配部分成功，结束
                    continue match;
                }
                if (shareResidue.compareTo(BigDecimal.ZERO) > 0 && shareResidueTmp.compareTo(BigDecimal.ZERO) == 0) {
                    retMap = shareMatch(shareResidue, shareResidueTmp, ipList, op, mpList);
                    if (null == retMap) {
                        // 匹配部分成功，结束
                        continue;
                    }
                    currIp = (ItemPo) retMap.get("currIp");
                    shareResidue = (BigDecimal) retMap.get("matchShare");
                    shareResidueTmp = (BigDecimal) retMap.get("mergeShare");
                } else if (shareResidue.compareTo(BigDecimal.ZERO) == 0 && shareResidueTmp.compareTo(BigDecimal.ZERO) > 0) {
                    retMap = shareMatch(shareResidueTmp, shareResidue, ipList, op, mpList);
                    if (null == retMap) {
                        // 匹配部分成功，结束
                        continue;
                    }
                    currIp = (ItemPo) retMap.get("currIp");
                    shareResidueTmp = (BigDecimal) retMap.get("matchShare");
                    shareResidue = (BigDecimal) retMap.get("mergeShare");
                }

                if (shareResidueTmp.compareTo(BigDecimal.ZERO) <= 0 && shareResidue.compareTo(BigDecimal.ZERO) <= 0) {
                    // 全部匹配成功，结束
                    continue match;
                }
            } while (true);

        }

        return mpList;
    }


    // 份额匹配
    public Map<String, Object> shareMatch(BigDecimal matchShare, BigDecimal mergeShare, List<ItemPo> ipList, OrderPo op, List<MatchPo> mpList) {
        ItemPo currIp = ipList.get(currIpIndex);
        // matchShare是否大于当前项目的每份金额？或当前项目剩余可匹配金额小于等于0
        do {
            if (matchShare.compareTo(currIp.getCurrPerAmount()) < 0 || currIp.getCurrPerAmount().compareTo(BigDecimal.ZERO) <= 0) {
                currIpIndex++;
                // 是否存在下一个项目？
                if (ipList.size() > currIpIndex) {
                    //选中下一个项目
                    ItemPo currIpTmp = ipList.get(currIpIndex);
                    if (currIpTmp.getResidueMatchAmount().compareTo(BigDecimal.ZERO) > 0) {
                        currIp = ipList.get(currIpIndex);
                    }
                } else {
                    // 匹配失败，结束
                    return null;
                }
            } else {
                break;
            }
        } while (true);

        if (matchShare.compareTo(currIp.getResidueMatchAmount()) > 0) {
            // 匹配项目的剩余可匹配额度，剩余未匹配上的matchShare份额并入mergeShare
            BigDecimal residueMatchAmount = currIp.getResidueMatchAmount();
            MatchPo mp = new MatchPo(op, currIp, residueMatchAmount);
            mpList.add(mp);
            mergeShare = mergeShare.add(matchShare.subtract(residueMatchAmount));
        } else {
            // matchShare全部匹配到当前项目
            MatchPo mp = new MatchPo(op, currIp, matchShare);
            mpList.add(mp);
        }
        matchShare = BigDecimal.valueOf(0);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("currIp", currIp);
        retMap.put("matchShare", matchShare);
        retMap.put("mergeShare", mergeShare);
        return retMap;
    }
}
