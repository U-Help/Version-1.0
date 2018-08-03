package com.example.fyg.login;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public String rece_time;
    public String phone;
    public String price;
    public String size;
    public String item_id;
    public String dstplace;
    public String rev_password;
    public String is_available;
    public String prop_time;
    public String srcplace;
    public String username;
    public String msg;
    public String email;
    public void setRece_time(String rece_time){
        this.rece_time=rece_time;
    }

    public void setPhone(String phone){
        this.phone=phone;
    }

    public void setPrice(String price){
        this.price=price;
    }

    public void setSize(String size){
        this.size=size;
    }

    public void setItem_id(String item_id){
        this.item_id=item_id;
    }

    public void setDstplace(String dstplace){
        this.dstplace=dstplace;
    }

    public void setRev_password(String rev_password){
        this.rev_password=rev_password;
    }

    public void setIs_available(String is_available){
        this.is_available=is_available;
    }

    public void setProp_time(String prop_time){
        this.prop_time=prop_time;
    }

    public void setSrcplace(String srcplace){
        this.srcplace=srcplace;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setMsg(String msg){
        this.msg=msg;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public static Order[] orders=new Order[1000];

    public static int length;
}
