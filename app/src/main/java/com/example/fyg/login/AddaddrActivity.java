package com.example.fyg.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.Toast;

public class AddaddrActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private Button btnSave;
    private EditText editname;
    private EditText editcall;
    private EditText editaddr;
    private String name,call,addr;
    private Switch choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddr);
        _CollectorActivity.addActivity(this);

        sp=this.getSharedPreferences("userAddr", Context.MODE_PRIVATE);
        editname = findViewById(R.id.autoCompleteTextView);
        editcall = findViewById(R.id.autoCompleteTextView2);
        editaddr = findViewById(R.id.autoCompleteTextView3);
        final Switch choice=findViewById(R.id.switch1);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name = editname.getText().toString();
                call = editcall.getText().toString();
                addr = editaddr.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("NAME", name);
                editor.putString("CALL",call);
                editor.putString("ADDR",addr);
                if(choice.isChecked()==true){
                    editor.putString("state","1");
                    editor.commit();
                }
                else{
                    editor.putString("state","0");
                }
                Toast.makeText(AddaddrActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
