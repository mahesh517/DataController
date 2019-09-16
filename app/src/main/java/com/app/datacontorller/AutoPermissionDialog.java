package com.app.datacontorller;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

public class AutoPermissionDialog extends Dialog {

    Context context;

    TextView add_member, cancel;

    List<String> names;


    MemberInterface memberInterface;

    public AutoPermissionDialog(Context context, int appTheme, MemberInterface memberInterface) {
        super(context, appTheme);
        this.context = context;
        this.memberInterface = memberInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.dialog_traspe)));
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.memeber_add_dailog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        super.onCreate(savedInstanceState);


        add_member = findViewById(R.id.add_member);

        cancel = findViewById(R.id.btn_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberInterface != null) {
                    memberInterface.onNameAdded(0, "");
                }
            }
        });
        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    public interface MemberInterface {
        void onNameAdded(int position, String name);
    }
}
