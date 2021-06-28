package cn.study.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    @GetMapping("{page}.html")
    public String page(@PathVariable("page")String page){
        return page;
    }

    @GetMapping("detail")
    public String detailPage(){
        return "detail";
    }


    @GetMapping("list")
    public String listPage(){
        return "list";
    }


    @GetMapping("confirm")
    public String confirmPage(){
        return "confirm";
    }


    @GetMapping("pay")
    public String payPage(){
        return "pay";
    }

}
