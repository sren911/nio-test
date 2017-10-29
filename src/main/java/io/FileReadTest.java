package io;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 描述:
 * test
 *
 * @outhor sren
 * @create 2017-10-27 21:11
 */
public class FileReadTest {

    @Test
    public void test01() throws Exception {
        FileReader reader = new FileReader("1.txt");
        char[] buf = new char[1024];
        int num = 0;
        while ((num =reader.read(buf)) != -1) {
            System.out.println(new String(buf, 0, num));
        }

        reader.close();
    }


    @Test
    public void test02() throws Exception {
        FileReader reader = new FileReader("1.txt");
        BufferedReader bufferedReader = new BufferedReader(reader);

        String str = null;
        while ((str = bufferedReader.readLine())!=null) {
            System.out.println(str);
        }

        reader.close();
    }


    @Test
    public void test03() {
        File dir = new File("C:\\tools\\apache-tomcat-7.0.57");
        showDir(dir, 0);
    }

    public static void showDir(File dir, int leavel) {

        System.out.println(getLeavel(leavel)+dir);
        leavel++;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                showDir(file,leavel);
            }else
                    System.out.println(file);
        }
    }

    public static String getLeavel(int leavel) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<leavel; i++) {
            sb.append("|--");
        }

        return sb.toString();
    }

}