package us.rzweb.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import us.rzweb.taotao.search.bean.SearchResult;
import us.rzweb.taotao.search.service.SearchService;

/**
 * Created by RZ on 9/12/16.
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keywords,
                               @RequestParam(value="page", defaultValue ="1") Integer page) {
        ModelAndView mv = new ModelAndView("search");
        try {
            keywords = new String(keywords.getBytes("ISO-8859-1"),"UTF-8");
            SearchResult searchResult = this.searchService.search(keywords, page);
            mv.addObject("query", keywords);
            mv.addObject("itemList", searchResult.getData());
            mv.addObject("page", page);
            int total = searchResult.getTotal().intValue();
            int pages = total % SearchService.ROWS == 0 ? total / SearchService.ROWS : total / SearchService.ROWS + 1;
            mv.addObject("pages", pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
}
