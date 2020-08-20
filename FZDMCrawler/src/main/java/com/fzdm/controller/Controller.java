package com.fzdm.controller;

import com.fzdm.crawler.Crawler;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@org.springframework.stereotype.Controller
public class Controller {

    //调用爬虫
    @RequestMapping("/startCrawler")
    public void startCrawler(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        //获取数据
        String url = request.getParameter("url");
        String savePath = request.getParameter("savePath");

        //提示信息
        //设置编码 为统一response和jsp的编码
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print("<script>alert('程序已开始运行，详情请见命令提示符。此页面可以关闭。');</script>");
        writer.close();

        //调用爬虫
        Crawler.getFZDM(url, savePath);
    }
}
