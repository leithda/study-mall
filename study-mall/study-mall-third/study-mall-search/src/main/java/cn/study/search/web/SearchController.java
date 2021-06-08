package cn.study.search.web;


import cn.study.search.entity.vo.SearchParam;
import cn.study.search.entity.vo.SearchResult;
import cn.study.search.service.es.EsMallSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController {

    @Autowired
    EsMallSearchService mallSearchService;

    @RequestMapping("list.html")
    public String listPage(SearchParam param, Model model){

        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);

        return "list";
    }
}
