package com.example.journalnotebook;

public class Note {
    private long id;
    private String content;
    private String endpoint;
    private String price;
    private String text;
    private long fileid;
    private String filetag;
    private String time;
    private int tag;

    public Note() {
    }

    public Note(String content,String endpoint,String price,String text,
                long fileid,String filetag,String time, int tag) {
        this.content = content;
        this.endpoint = endpoint;
        this.price = price;
        this.text = text;
        this.fileid = fileid;
        this.filetag = filetag;

        this.time = time;
        this.tag = tag;
    }
    public long getId() {
        return id;
    }

    //返回笔记的内容
    public String getContent() {
        return content;
    }
    public String getEndpoint() {
        return endpoint;
    }
    public String getPrice() {
        return price;
    }
    public String getText() {
        return text;
    }
    public long getFileid() {
        return fileid;
    }
    public String getFiletag() {
        return filetag;
    }

    public String getTime() {
        return time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setFileid(long fileid) {
        this.fileid = fileid;
    }
    public void setFiletag(String filetag) {
        this.filetag = filetag;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return content + "\n" + time.substring(5,16) + " "+ id;
    }
}
