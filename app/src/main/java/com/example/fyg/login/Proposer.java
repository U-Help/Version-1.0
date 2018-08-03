package com.example.fyg.login;

public class Proposer {
    public int express_state;//是否有人接单

    public String user;//接单者姓名

    public String rece_time;//接单时间

    public String rephone;//接单者电话

    public String gettime;//收货时间

    public String item_id;

    public String price;

    public String size;

    public String dstplace;

    public String rev_password;

    public String srcplace;

    public String msg;

    public static int length;

    public  static  int num;

    public  static Proposer[] proposers=new Proposer[1000];

    public void setExpress_state(int express_state) {
        this.express_state = express_state;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRece_time(String rece_time) {
        this.rece_time = rece_time;
    }

    public void setRephone(String rephone){
        this.rephone=rephone;
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

    public void setSrcplace(String srcplace){
        this.srcplace=srcplace;
    }

    public void setMsg(String msg){
        this.msg=msg;
    }

    public void setGettime(String gettime){
        this.gettime=gettime;
    }
}
