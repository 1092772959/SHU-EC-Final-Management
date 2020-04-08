package com.shu.icpc.utils;


import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    public static int extractFiles(ZipInputStream zipFile, Map<String, byte[]> res){
        byte[] buffer = new byte[1024];
        try{
            ZipEntry entry = null;
            String fileName = null;
            while((entry = zipFile.getNextEntry())!=null){
                fileName = entry.getName();
                String[] parts = fileName.split("/");
                //filter
                if(parts[parts.length-1].startsWith(".") || parts[0].startsWith("__MACOSX")
                        || parts[parts.length-1].equals("")){
                    continue;
                }

                //get bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = -1;
                while((len = zipFile.read(buffer)) !=-1){
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                byte[] bytes = baos.toByteArray();
                if(bytes.length == 0){
                    continue;
                }

                res.put(fileName, bytes);
            }
        }catch(Exception e){
            e.printStackTrace();
            return Constants.FAIL;
        }
        return 0;
    }
}
