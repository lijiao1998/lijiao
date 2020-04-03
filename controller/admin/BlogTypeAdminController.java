package com.ssm.controller.admin;


import com.ssm.domain.BlogType;
import com.ssm.domain.PageBean;
import com.ssm.service.BlogTypeService;
import com.ssm.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 博客类型管理
 */
@Controller
@RequestMapping("/admin/blogType")
public class BlogTypeAdminController {

    @Autowired
    private BlogTypeService blogTypeService;

    @RequestMapping("/deleteType")
    public String deleteType(@RequestParam("ids")String ids,HttpServletResponse response) throws Exception {
        String[] strIds = ids.split(",");
        for(int i=0;i<strIds.length;i++){
            blogTypeService.delete(Integer.valueOf(strIds[i]));
        }
        JSONObject result = new JSONObject();
        result.put("success",Boolean.valueOf(true));
        ResponseUtil.write(response,result);
        return null;
    }

    /***
     * 保存博客类别
     * @param blogType
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveType")
    public String addType(BlogType blogType,HttpServletResponse response) throws Exception {
        int resultTotal = 0;
        if(blogType.getId()==null){//添加
            resultTotal = blogTypeService.add(blogType);
        }else{//更新
            resultTotal = blogTypeService.update(blogType);
        }
        JSONObject result = new JSONObject();
        if(resultTotal>0){
            //Boolean.valueOf(true)返回true
            result.put("success",Boolean.valueOf(true));
        }else{
            result.put("success",Boolean.valueOf(false));
        }
        ResponseUtil.write(response,result);
        return null;
    }


    /***
     * 博客类型列表
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page",required = false) String page,
                       @RequestParam(value = "rows",required=false) String rows,
                       HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
        //查询博客类型列表
        Map<String,Object> map = new HashMap();
        map.put("start",pageBean.getStart());
        map.put("size",pageBean.getPageSize());
        List<BlogType> blogTypeList = blogTypeService.list(map);
        //查询总共有多少条数据
        Long total = blogTypeService.getTotal(map);
        //将数据写入response
        //创建JSONObject对象
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(blogTypeList);
        result.put("rows",jsonArray);
        result.put("total",total);
        //向客户端响应查询的数据
        ResponseUtil.write(response,result);
        return null;
    }

}
