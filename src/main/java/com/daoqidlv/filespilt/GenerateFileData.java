package com.daoqidlv.filespilt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * decription: 用于生成压测用的测试文件
 * author: fang
 */
public class GenerateFileData {

    private String a = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@a@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String b = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@b@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String c = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@c@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdprdf@!@";
    private String d = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@d@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String e = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@e@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String f = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@f@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String g = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@g@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String h = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@h@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String i = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@i@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String j = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@j@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String k = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@k@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String l = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@l@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String m = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@m@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String n = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@n@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String o = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@o@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";
    private String p = "rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@p@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@rerwtwvds@!@fdfsafdsf@!@fdsfsfsdfsdf@!@";


    public void generateFile() {
        List<String> list = new ArrayList();
        list.add(a);
        list.add(b);
        list.add(c);
//        list.add(e);
//        list.add(f);
//        list.add(g);
//        list.add(h);
//        list.add(i);
//        list.add(j);
//        list.add(k);
//        list.add(l);
//        list.add(m);
//        list.add(n);
//        list.add(o);
//        list.add(p);

        File path = new File(this.getClass().getResource("/").getPath());
        String fileName = "test.txt";
        String fullPath =  path +"/" +  fileName;
        System.out.println("生成的测试文件的路径为：" + fullPath);
        try {
            writeFile(list, fullPath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeFile(List<String> list, String path) throws IOException {
        FileWriter fw= null;
        fw = new FileWriter(new File(path));

        //写入中文字符时会出现乱码
        BufferedWriter bw=new BufferedWriter(fw);
        int size = list.size();
        for (int q = 0; q < size; q++) {
            String temp = list.get(q);
            for (int r = 0; r < 1000000; r++) {
                bw.write(temp + "\t\n");

            }

        }
        bw.close();
        fw.close();

    }
}
