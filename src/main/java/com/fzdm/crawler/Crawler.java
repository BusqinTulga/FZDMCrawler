package com.fzdm.crawler;

import com.fzdm.utils.TextUtil;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//爬取风之动漫漫画
public class Crawler {

    public static void main(String[] args) {
        //鬼灭之刃
        String url = "https://manhua.fzdm.com/153/";
        //保存目录
        String savePath = "C:\\Users\\Tulga\\Desktop\\鬼灭之刃\\";

        //爬取风之动漫漫画
        getFZDM(url, savePath);
    }

    //爬取风之动漫漫画
    public static void getFZDM(String url, String savePath) {
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if ("".equals(url) || url == null || "".equals(savePath) || savePath == null) {
            System.out.println("地址或路径不能为空！");
            return;
        }
        if (!savePath.endsWith("\\")) {
            savePath = savePath + "\\";
        }

        //已爬取的页面
        String txt1 = savePath + "已爬取的页面.txt";
        //未能爬取的页面
        String txt2 = savePath + "未能爬取的页面.txt";

        int count = 0;
        try {
            //创建文件夹
            File file2 = new File(savePath);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            //创建已爬取的页面.txt
            File txtFile1 = new File(txt1);
            if (!txtFile1.exists()) {
                TextUtil.writeTxt(txt1, "已爬取的页面：");
            }
            //创建未能爬取的页面.txt
            File txtFile2 = new File(txt2);
            if (!txtFile2.exists()) {
                TextUtil.writeTxt(txt2, "未能爬取的页面：");
            }

            Document document = Jsoup.connect(url).get();
            //详情列表
            Element listDiv = document.getElementsByAttributeValue("id", "content").get(0);
            Elements lis = listDiv.getElementsByTag("li");

            for (int i = lis.size() - 1; i >= 0 ; i--) {
                //循环进入详情页次数（第几话）
                count ++;

                Element li = lis.get(i);
                //标题
                String title = li.getElementsByTag("a").attr("title");
                System.out.println("标题：" + title);

                //详情页链接
                String href = li.getElementsByTag("a").attr("href");
                String listUrl = url + href;
                System.out.println("详情页链接：" + listUrl);

                //分页
                //循环分页次数（第几页）
                int page = 0;
                while (true) {
                    page ++;

                    //分页详情页链接
                    String navUrl = listUrl + "index_" + (page - 1) + ".html";

                    Document document1;
                    try {
                        //连接一下 看看行不行
                        document1 = Jsoup.connect(navUrl).get();
                    }
                    catch (HttpStatusException e) {
                        //不行 没这一页了（404了） 跳出
                        break;
                    }

                    System.out.println("第 " + page + " 页：" + navUrl);

                    //读取已经爬取过的页面.txt
                    String urls = TextUtil.readTxt(txt1);
                    if (urls.contains(navUrl)) {
                        //已经爬取过该页面 结束本次循环
                        System.out.println("已爬取过该页面。");
                        continue;
                    }

                    //获取图片url
                    String htmlText = String.valueOf(document1);
                    Matcher matcher = Pattern.compile("(?<=mhurl=\").*?(?=\")").matcher(htmlText);
                    while (matcher.find()) {
                        String group = matcher.group(0);
                        String picUrl = "https://p5.manhuapan.com/" + group;
                        System.out.println("图片url：" + picUrl);

                        //下载图片
                        String fileName = title + " p" + page;
                        System.out.println("图片名：" + fileName + ".jpg");
                        //文件夹
                        File file = new File(savePath + title);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        System.out.println("开始下载图片到：" + file.getAbsolutePath() + "\\");
                        //图片的绝对路径
                        File file1 = new File(file.getAbsolutePath() + "\\" + fileName + ".jpg");

                        //连接get请求
                        URL url1 = new URL(picUrl);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setRequestProperty("accept", "*/*");
                        httpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
                        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
                        httpURLConnection.setRequestProperty("Proxy-Connection", "Keep-Alive");
                        httpURLConnection.connect();
                        InputStream inputStream;

                        try {
                            inputStream = httpURLConnection.getInputStream();
                        }
                        catch (FileNotFoundException e) {
                            System.out.println("图片未能下载！");
                            e.printStackTrace();
                            //出错 写入未能爬取的页面.txt
                            String txt = TextUtil.readTxt(txt2);
                            if (!txt.contains(navUrl)) {
                                TextUtil.writeTxt(txt2, navUrl);
                            }
                            continue;
                        }

                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        FileOutputStream fileOutputStream = new FileOutputStream(file1);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        int len;
                        while ((len = bufferedInputStream.read()) != -1) {
                            bufferedOutputStream.write(len);
                        }
                        System.out.println("图片下载成功！");
                        //写入已爬取的页面.txt
                        TextUtil.writeTxt(txt1, navUrl);
                        //关闭流
                        bufferedOutputStream.close();
                        fileOutputStream.close();
                        bufferedInputStream.close();
                        inputStream.close();
                    }
                    System.out.println("=============================================================================\n");
                }

                //睡眠1到2秒
                int a = (int) (Math.random() * 2000) + 1000;
                Thread.sleep(a);
            }

            //记录结束时间
            long endTime = System.currentTimeMillis();
            System.out.println("执行完成！总耗时： " + (endTime - startTime) / 1000 + " 秒。");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}