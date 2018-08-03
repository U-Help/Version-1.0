package com.example.fyg.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpandReflectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpand_reflect);
        _CollectorActivity.addActivity(this);

        TextView text = (TextView) this.findViewById(R.id.text);
        String str = "注册登录\n" +
                "\n" +
                "Q:如何注册U-Help账号？\n" +
                "A:通过科大邮箱即可注册，而且为了确保您能收到快递信息，请填写常用手机号。\n" +
                "Q:如果我忘记密码，如何登录？\n" +
                "A:如果您忘记密码，在登陆页面中点击“忘记密码”选项或者侧滑界面中点击“修改密码”选项，即可重设密码。\n" +
                "Q:如何更改我的账户信息？\n" +
                "A:进入侧滑界面，点击头像，在“个人详情”页面进行修改。\n" +
                "Q:如何管理我的默认收货地址？\n" +
                "A:侧滑界面中点击“地址管理”选项，进行添加修改。\n" +
                "Q:我为什么需要将我的用户名设置成真实姓名？\n" +
                "A:真实姓名有利于快递的签收和方便发布代取时接单者更快找到。\n" +
                "\n" +
                "安全&隐私\n" +
                "\n" +
                "Q:如何阻止我的个人资料和快递信息被陌生人获取？\n" +
                "A:我们快递信息进行了严格的加密，只有接单人才能获取您的快递信息，其他非接单用户无法查看到您快递的详细信息。\n";
        text.setText(str);
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
