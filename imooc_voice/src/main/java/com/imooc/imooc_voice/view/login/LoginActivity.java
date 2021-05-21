package com.imooc.imooc_voice.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.api.RequestCenter;
import com.imooc.imooc_voice.model.login.LoginEvent;
import com.imooc.imooc_voice.model.user.User;
import com.imooc.imooc_voice.utils.UserManager;
import com.imooc.lib_common_ui.base.BaseActivity;
import com.imooc.lib_network.okhttp.listener.DisposeDataListener;

import org.greenrobot.eventbus.EventBus;

/*
 * @author zengx
 */
public class LoginActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestCenter.login(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        //处理正常逻辑
                        User user = (User) responseObj;
                        Log.d("data",user.data.name+user.data.mobile+user.data.photoUrl);
                        UserManager.getInstance().setUser(user);
                        EventBus.getDefault().post(new LoginEvent());
                        finish();
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        //处理失败逻辑
                    }
                });
//                mUserLoginPresenter.login(getUserName(), getPassword());
            }
        });
    }

}