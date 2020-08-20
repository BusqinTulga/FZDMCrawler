package com.fzdm.utils;

import java.io.*;

//操作txt文件
public class TextUtil {

    public static void main(String[] args) {

        String s = readTxt("C:\\Users\\Tulga\\Desktop\\BSAdmin改动部分.txt");
        System.out.println(s);

        writeTxt("C:\\Users\\Tulga\\Desktop\\哈哈.txt", "我丢勒楼某啊啊啊啊");
    }

    //读txt文件
    public static String readTxt(String txtPath) {

        File file = new File(txtPath);
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer stringBuffer = new StringBuffer();
                String text = null;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuffer.append(text);
                }

                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();

                return stringBuffer.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //写txt文件(可追加内容)
    public static void writeTxt(String txtPath,String content){

        try {
            FileWriter fileWriter = new FileWriter(txtPath, true);
            //写内容
            fileWriter.write(content);
            //换行
            fileWriter.write("\r\n");
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
