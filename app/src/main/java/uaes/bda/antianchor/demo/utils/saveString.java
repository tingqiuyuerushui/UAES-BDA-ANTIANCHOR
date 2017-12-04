package uaes.bda.antianchor.demo.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by gang.cao on 11/8/2017.
 */

public class saveString {
    // String file = "";
    //  String paths = Environment.getExternalStorageDirectory().getPath() + File.separator + "FtpPub/LogMsg/";

    //生成文件路径
    private static String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "FtpPub/CaoGang/";


    //文件路径+名称
    private static String filenameTemp;

    /**
     * 创建文件
     *
     * @param fileName    文件名称
     * @param filecontent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName, String filecontent) {
        Boolean bool = false;
        filenameTemp = path + fileName + ".txt";//文件路径+名称+文件类型
        try {
            File file = new File(filenameTemp);
            //如果文件不存在，则创建新的文件
            if (!file.exists()) {
                //   file.createNewFile();
                file.mkdirs();
                bool = true;
                System.out.println("success create file,the file is " + filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     *
     * @param filepath 文件路径与名称
     * @param newstr   写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath, String newstr) throws IOException {
        Boolean bool = false;
        String filein = newstr + "\r\n";//新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);
            new FileOutputStream(file);

            fos = new FileOutputStream(file);
            // fos.write();
            // pw = new PrintWriter(fos);
            // pw.write(buffer.toString().toCharArray());
            // pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    public static void createFile(String a) {
        try {
            long timestamp = System.currentTimeMillis();
            // String time = formatter.format(new Date());
            String fileName = "GPS01" + ".txt";
            // String string = a   + "\n";
            String string = a + "";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "CaoGang/caogang01/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(string.getBytes());
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    public static void createFiletwo(String a) {
        try {
            long timestamp = System.currentTimeMillis();
            // String time = formatter.format(new Date());
            String fileName = "GPS02" + ".txt";
            // String string = a   + "\n";
            String string = a + "";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "CaoGang/caogang02/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(string.getBytes());
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    public static void createFiletthree(String a) {
        try {
            long timestamp = System.currentTimeMillis();
            // String time = formatter.format(new Date());
            String fileName = "GPS03" + ".txt";
            // String string = a   + "\n";
            String string = a + "";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "CaoGang/caogang03/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(string.getBytes());
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    public static void createFilettfore(String a) {
        try {
            long timestamp = System.currentTimeMillis();
            // String time = formatter.format(new Date());
            String fileName = "GPS04" + ".txt";
            // String string = a   + "\n";
            String string = a + "";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "CaoGang/caogang04/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(string.getBytes());
                fos.close();
            }
        } catch (Exception e) {
        }
    }

    public static void readFilettfive() {
       /* String paths = Environment.getExternalStorageDirectory().getPath() + File.separator + "CaoGang/caogang04/";
        String line = "";
        try {
            String pathname = paths; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
           *//* 读入TXT文件 *//*
        return line;*/
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "CaoGang/caogang04/";
        String fileName = "GPS04" + ".txt";
        try{
            FileInputStream fis = new FileInputStream(path + fileName);
            int len = 0;
            byte[] bytes = new byte[1024 * 8];
            while ((len = fis.read(bytes)) != -1) {
                Log.e("134", "createFilettfore: " + new String(bytes, 0, len));
            }
            fis.close();
        } catch (Exception e) {
        }


    }
}


