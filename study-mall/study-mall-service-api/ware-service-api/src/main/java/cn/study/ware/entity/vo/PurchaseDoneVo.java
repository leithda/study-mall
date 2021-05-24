package cn.study.ware.entity.vo;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PurchaseDoneVo {

    @NotNull
    private Long id;//采购单id

    private List<PurchaseItemDoneVo> items;
}
