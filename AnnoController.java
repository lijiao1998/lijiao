package com.mvc.controller;

import com.mvc.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(path = "/anno")
public class AnnoController {

    @RequestMapping(path = "/testReuqestParam")
    public String testReuqestParam(@RequestParam(name = "username") String name){
        System.out.println("执行了...");
        System.out.println(name);
        return "success";
    }

    @RequestMapping(path = "/testReuqestBody")
    public String testReuqestBody(@RequestBody String body){
        System.out.println("执行了...");
        System.out.println(body);
        return "success";
    }
    @RequestMapping(path = "/testPathValiable/{id}")
    public String testPathValiable(@PathVariable(name = "id") String id){
        System.out.println("执行了...");
        System.out.println(id);
        return "success";
    }

   /* @RequestMapping(path = "/testModelAttribute")
    public String testModelAttribute(User user){
        System.out.println("testModelAttribute执行了...");
        System.out.println(user);
        return "success";
    }*/

    @RequestMapping(path = "/testModelAttribute")
    public String testModelAttribute(@ModelAttribute(value = "aaa") User user){
        System.out.println("testModelAttribute执行了...");
        System.out.println(user);
        return "success";
    }

    /***
     * 作用在方法上
     * 有返回值
     * @param name
     * @return
     */
    @ModelAttribute
    public void updateUser(String name, Map<String,User> map){
        System.out.println("updateUser执行了....");
        User user = new User();
        user.setUname(name);
        user.setAge(10);
        user.setDate(new Date());
        map.put("aaa",user);

    }

    @RequestMapping(path = "/session")
    public String session(Model model){
        model.addAttribute("username","小小");
        return "success";
    }
}

