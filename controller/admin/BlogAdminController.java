package com.ssm.controller.admin;

import com.ssm.domain.Blog;
import com.ssm.domain.PageBean;
import com.ssm.service.BlogService;
import com.ssm.util.DateJsonValueProcessor;
import com.ssm.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {

    @Autowired
    private BlogService blogService;

    /***
     * 查询所有博文信息
     * @return
     */
    @RequestMapping("/blogList")
    public String blogList(@RequestParam(value = "page",required = false) String page,
                         @RequestParam(value = "rows",required=false) String rows,
                         HttpServletResponse response) throws Exception{
        PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
        Map<String,Object> map = new HashMap<>();
        map.put("start",pageBean.getStart());
        map.put("size",pageBean.getPageSize());
        //查询博文信息列表
        List<Blog> blogList = blogService.list(map);
        //查询总共有多少条数据
        Long total = blogService.getTotal(map);
        //将数据写入response
        //创建JSONObject对象,{key:value}结构
        JSONObject result = new JSONObject();
        JsonConfig config = new JsonConfig();
        //转换日期类型为指定格式的字符串
        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        //JSONArray是[{key:value},{key:value},{}]结构
        JSONArray jsonArray = JSONArray.fromObject(blogList,config);
        result.put("rows",jsonArray);
        result.put("total",total);
        //向客户端响应查询的数据
        ResponseUtil.write(response,result);
        return null;
    }

    /***
     * 保存一条博客信息
     * @param blog
     * @return
     */
    @RequestMapping("/saveBlog")
    public String saveBlog(Blog blog, HttpServletResponse response) throws Exception {
        int flag = blogService.add(blog);
        if(flag != 0 ){//发表成功
            return "WEB-INF/pages/publicSuccess";
        }
           return null;
    }

    /***
     * 图片上传
     * @return
     * @throws Exception
     */
    @RequestMapping("/fileUpload")
    public String upload(@RequestParam("fileName")MultipartFile[] files,HttpServletRequest request,HttpServletResponse response) throws Exception {
        List list = new ArrayList();
        for(MultipartFile imageFile : files){
            //获取要上传的文件目录
            String filePath = request.getSession().getServletContext().getRealPath("/images/uploads");
            //创建file对象
            File file = new File(filePath);
            if(! file.exists()){//如果要上传的目录不存在，则创建该目录
                file.mkdirs();
            }
            //获取要上传的文件名称
            String fileName = imageFile.getOriginalFilename();
            //获取随机字符串，用于图片命名
            String uuid = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
            // 新名称
            String newFileName = uuid + "_" + fileName;
            //上传文件
            imageFile.transferTo(new File(file,newFileName));
            //返回图片访问路径
            String url = request.getScheme()+"://"+ request.getServerName()+":"+request.getServerPort() +request.getContextPath()+"/images/uploads/"+ newFileName;
            list.add(url);
            System.out.print(url);
        }
        JSONObject result = new JSONObject();
        //必须传给前台两个参数，一个是errno，一个是data，data即使只有一个图片的地址，也必须是数组，，json格式的，否则会报错
        result.put("errno",0);
        result.put("data",list.toArray());
        ResponseUtil.write(response,result);
        return null;
    }

    /***
     * 删除博客
     * @return
     */
    @RequestMapping("/deleteBlog")
    public String deleteBlog(@RequestParam("ids") String ids,HttpServletResponse response) throws Exception{
        String[] strIds = ids.split(",");
        for(int i=0;i<strIds.length;i++){
            blogService.delete(Integer.parseInt(strIds[i]));
        }
        JSONObject result = new JSONObject();
        result.put("success",Boolean.valueOf(true));
        ResponseUtil.write(response,result);
        return null;
    }

    /***
     * 更新博客信息
     * @param id
     * @return
     */
    @RequestMapping("updateBlog")
    public String updateBlog(@RequestParam("id") Integer id){
        return null;
    }
}
