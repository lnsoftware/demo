package demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@Controller
public class HelloController {
    // @GetMapping(value = "/hello")
    @GetMapping(value = "/hello")
    @ResponseBody
    public String index(@RequestParam("abc") String abc) {
        System.out.println(this.getClass());
        // System.out.println(">>>>>>>>>>>>>>>>>>>>>login>>>>>>>>>>>>>>>>>>>>>>");
        return "Hello World";
    }
    @GetMapping(value = "/hello23")
    public String indexds() {
        System.out.println(">>login>>>>");
        // model.addAttribute("title","dsad");
        return "login";
    }
}
