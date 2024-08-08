package com.cyecize.app.web;

import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.models.ModelAndView;
import java.util.Map;

@Controller
public class TestController {

    @GetMapping("/test-javache")
    public ModelAndView testAction() {
        return new ModelAndView(
                "test.html.twig",
                Map.of("name", "Javache Angular Server")
        );
    }
}
