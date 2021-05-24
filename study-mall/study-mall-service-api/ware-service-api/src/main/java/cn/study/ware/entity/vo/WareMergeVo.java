package cn.study.ware.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class WareMergeVo {
    /**
     * 采购单ID
     */
    private Long purchaseId;

    /**
     * 采购需求集合
     */
    private List<Long> items;
}
