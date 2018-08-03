package com.example.fyg.login;

public class Accepter {
    public String user;
    public String rece_time;
    public String srcplace;
    public String dstplace;
    public String item_id;
    public String price;
    public String size;
    public String rev_password;
    public String msg;
    public String pro_phone;
    public String gettime;
    public static int length;
    public static int num;
    public static Accepter[] accepters=new Accepter[1000];

    public void setUser(String user){
        this.user=user;
    }

    public void setRece_time(String rece_time){
        this.rece_time=rece_time;
    }

    public void setSrcplace(String srcplace){
        this.user=user;
    }

    public void setDstplace(String dstplace){
        this.dstplace=dstplace;
    }

    public void setItem_id(String item_id){
        this.item_id=item_id;
    }

    public void setMsg(String msg){
        this.msg=msg;
    }

    public void setPrice(String price){
        this.price=price;
    }

    public void setSize(String size){
        this.size=size;
    }

    public void setRev_password(String rev_password){
        this.rev_password=rev_password;
    }

    public void setPro_phone(String pro_phone){
        this.pro_phone=pro_phone;
    }

    public void setGettime(String gettime){
        this.gettime=gettime;
    }
}
