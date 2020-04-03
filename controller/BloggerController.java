package com.ssm.controller;

import com.ssm.domain.Blogger;
import com.ssm.service.Impl.BloggerServiceImpl;
import com.ssm.util.CrytographyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/***
 * 博主表现层
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {
    
    @Autowired
    private BloggerServiceImpl bloggerService;

    /***
     * 注册账号
     * @param blogger
     * @param request
     * @return
     * @throws IOException
     */
    /*@RequestMapping("/register")
    public String register(Blogger blogger, HttpServletRequest request, MultipartFile imageFile) throws IOException {
        System.out.println("表现层：保存博主信息。");
        //获取要上传的文件目录
        String path = request.getSession().getServletContext().getRealPath("/uploads");
        //创建file对象
        File file = new File(path);
        if(! file.exists()){
            file.mkdirs();
        }
        //获取要上传的文件名称
        String fileName = imageFile.getOriginalFilename();
        //文件名称唯一化
        String uuid = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
        fileName = uuid+ "_"+fileName;
        //设置存入数据库的图像名称
        blogger.setImageName(fileName);
        //保存数据库
        bloggerService.saveBlogger(blogger);

        //上传文件
        imageFile.transferTo(new File(file,fileName));
        return "WEB-INF/pages/success";
    }
*/
    @RequestMapping("/login")
    public String login(Blogger blogger,HttpServletRequest request) {
        String username = blogger.getUsername();
        String password = blogger.getPassword();
        //加密的密码
        String pw = CrytographyUtil.md5(password,"java1234");
        Blogger blogger1 = bloggerService.getByUsername(username);
        if(pw.equals(blogger1.getPassword())) {//登录成功，跳转到后台管理页面
            return "WEB-INF/pages/success";
        }else {
            request.setAttribute("blogger",blogger);
            request.setAttribute("erroInfo","用户名或密码错误！");
        }
        return "login";
    }


    /*@RequestMapping("/login")
    public String login(Blogger blogger,HttpServletRequest request){
        String username = blogger.getUsername();
        String password = blogger.getPassword();
        //加密的密码
        String pw = CrytographyUtil.md5(password,"java1234");

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,pw);
        try {
            //传递token给shiro的realm
            subject.login(token);
            return "WEB-INF/pages/success";
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("blogger",blogger);
            request.setAttribute("erroInfo","用户名或密码错误！");
        }

        return "login";
    }*/

}
