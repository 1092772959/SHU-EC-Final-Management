package com.shu.icpc.utils;

import java.io.*;

public class FileUtil {

    public static void copyFile(String inputFileSrc, String outputFilesrc) throws FileNotFoundException {
        InputStream in = new FileInputStream(inputFileSrc);
        OutputStream out = new FileOutputStream(outputFilesrc);
        copyStream(in,out);
        close(in,out);      //关闭流
    }

    public static void copyStream(InputStream in, OutputStream out){
        byte[] bytes = InputStreamToBytes(in);
        bytesToOutputStream(out,bytes);
    }


    public static byte[] InputStreamToBytes(InputStream in){
        byte[] dest = null;
        ByteArrayOutputStream baos = null;
        in = new BufferedInputStream(in);
        try {
            baos  = new ByteArrayOutputStream();
            dest = new byte[1024*10];
            int len = -1;
            while(-1 != (len = in.read(dest))){
                baos.write(dest,0,len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(baos);
        }
        return null;
    }

    /**
     * 使用ByteArrayInputStream(不用也行) 和 FileOutputStream
     * @param bytes

     */
    public static void bytesToOutputStream(OutputStream out, byte[] bytes){
        out = new BufferedOutputStream(out);
        try {
            out.write(bytes,0,bytes.length);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable ...ios){
        for(Closeable io: ios){
            if(io!=null){
                try {
                    io.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * stream to byte array
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}