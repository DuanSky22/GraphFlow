package com.duansky.hazelcast.graphflow.util;

import java.io.*;

/**
 * Created by SkyDream on 2016/11/1.
 */
public class Files {

    /**
     * get the {@link PrintWriter} of this path.
     * @param path the PrintWriter you want to create to.
     * @return the PrintWriter of your given path.
     */
    public static PrintWriter asPrintWriter(String path){
        return asPrintWriter(new File(path),false);
    }

    public static PrintWriter asPrintWriter(String path,boolean isAppend){
        return asPrintWriter(new File(path),true);
    }

    public static PrintWriter asPrintWriter(File file,boolean isAppend){
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            return new PrintWriter(new FileOutputStream(file,isAppend));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkNotNull(String path){
        File file = new File(path);
        if(!file.exists()) {
            throw new IllegalArgumentException("the path is not a file path!");
        }

    }

    public static String getFolderPath(String path){
        checkNotNull(path);
        File file = new File(path);
        return file.getParent();
    }


    public static int getLineNumber(String path){
        checkNotNull(path);
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(path));
            reader.skip(new File(path).length());
            return reader.getLineNumber();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void writeAsTxt(String path,String data,boolean isAppend){
        PrintWriter writer;
        if(isAppend) writer = asPrintWriter(path,isAppend);
        else writer = asPrintWriter(path);
        writer.write(data);
        writer.close();
    }

    public static BufferedReader asBufferedReader(File file){
        try {
            if(!file.exists()) {
                throw new IllegalArgumentException("the "+ file + " is not exists!");
            }
            return new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedReader asBufferedReader(String path){
        return asBufferedReader(new File(path));
    }

    /**
     * check this folder exists. if not create a new folder.
     * @param folder the folder that will be checked.
     * @return true if this folder alreay exists, otherwise false.
     */
    public static boolean checkAndCreateFolder(String folder){
        File file = new File(folder);
        if(!file.exists()){
            file.mkdir();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\SkyDream\\Desktop\\duan\\target\\8.txt";
        System.out.println(getLineNumber(path));
    }
}
