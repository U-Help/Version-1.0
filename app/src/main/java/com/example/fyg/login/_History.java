package com.example.fyg.login;

public class _History {
    public String rece_time;
    public  String accept_time;
    public  String item_id;
    public  String username;

    public   String phone;
    public  String price;
    public  String size;
    public  String dstplace;
    public  String rev_password;
    public String is_available;
    public  String prop_time;
    public String srcplace;
    public  String msg;
    public  String email;
    public String acc_phone;
    public static int num;
    public static boolean flag;

    public void setRece_time(String rece_time){
        this.rece_time=rece_time;
    }
    public void setAccept_time(String accept_time){this.accept_time = accept_time;}
    public void setItem_id(String item_id){ this.item_id=item_id; }
    public void setUsername(String username){
        this.username=username;
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

    public void setMsg(String msg){
        this.msg=msg;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setAcc_phone(String acc_phone){ this.acc_phone = acc_phone; }

    public static _History[] _histories = new _History[1000];

    public static int length;
}
