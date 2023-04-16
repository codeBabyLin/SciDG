package cn.DynamicGraph;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileProcess {
    String fileName;
    public FileProcess(String fileName){
        this.fileName = fileName;
    }
    public void writeBytesToFile(byte [] data){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(data);
            fileOutputStream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public byte[] readBytesFromFile(){
        byte data[] = new byte[0];
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            int size = fileInputStream.available();
            data = new byte[size];
            fileInputStream.read(data);
            fileInputStream.close();
            return data;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }



}
