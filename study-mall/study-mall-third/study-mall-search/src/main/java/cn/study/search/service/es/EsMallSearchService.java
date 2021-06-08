package cn.study.search.service.es;

import cn.study.search.entity.vo.SearchParam;
import cn.study.search.entity.vo.SearchResult;

public interface EsMallSearchService {

    /**
     * 检索商品
     * @param param 检索参数
     * @return 检索结果
     */
    SearchResult search(SearchParam param);
}
