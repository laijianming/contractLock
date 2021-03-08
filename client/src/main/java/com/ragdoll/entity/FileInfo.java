package com.ragdoll.entity;

/**
 * @author jianming
 * @create 2021-03-08-22:09
 */
public class FileInfo {


    private String fileuuid = "";

    private String filename = "";

    private String randomkey = "";

    public FileInfo() {
    }

    public FileInfo(String fileuuid, String filename, String randomkey) {
        this.fileuuid = fileuuid;
        this.filename = filename;
        this.randomkey = randomkey;
    }

    public String getFileuuid() {
        return fileuuid;
    }

    public void setFileuuid(String fileuuid) {
        this.fileuuid = fileuuid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRandomkey() {
        return randomkey;
    }

    public void setRandomkey(String randomkey) {
        this.randomkey = randomkey;
    }
}
