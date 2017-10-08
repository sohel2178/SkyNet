package com.imatbd.skynet.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imatbd.skynet.AppUtility.DialogHelper;
import com.imatbd.skynet.Firebase.FdatabaseUtil;
import com.imatbd.skynet.MainActivity;
import com.imatbd.skynet.Model.User;
import com.imatbd.skynet.R;
import com.imatbd.skynet.Service.DatabaseCommunicationService;
import com.imatbd.skynet.Utility.Constant;
import com.imatbd.skynet.Utility.Method;

public class RegisterActivity extends BaseDetailActivity implements View.OnClickListener {

    private TextView tvTitle;

    private EditText etCode;
    private Button btnActivate,btnTest,btnQuery;

    //Database Util
    private FdatabaseUtil fdatabaseUtil;

    private DialogHelper dialogHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fdatabaseUtil = new FdatabaseUtil(getApplicationContext());

        dialogHelper = new DialogHelper(this);

        windowFadeAnimation();
        setupToolbar();

        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("User Registration");

        etCode = (EditText) findViewById(R.id.et_code);
        btnActivate = (Button) findViewById(R.id.btn_activate);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnQuery = (Button) findViewById(R.id.btn_query);
        btnActivate.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_activate:
                String code = etCode.getText().toString().trim();

                if(code.equals("sohel.ahmed2178@gmail.com")){

                    //Start service with Method Id
                    startService(1);

                    finish();


                }else{
                    // Get User from Online
                    startService(Method.FETCH_USER,code);
                    dialogHelper.showProgressDialog();
                }
                break;

            case R.id.btn_test:

                //startService(intent);

                break;

            case R.id.btn_query:
               //User user = fdatabaseUtil.getUserByID("-KuxBbka0-v9H-ppZuy0");

               // Log.d("SOHEl",user.getName());
                startService(2);
                break;
        }

    }

    private void startService(int method){
        Intent intent = getIntent(method);
        startService(intent);

    }

    private void startService(int method,String code){
        Intent intent = getIntent(method,code);
        startService(intent);

    }

    private void startService(int method,User user){
        Intent intent = getIntent(method,user);
        startService(intent);

    }

    private Intent getIntent(int method,String code){
        Intent intent = getIntent(method);
        intent.putExtra("code",code);
        return intent;

    }

    private Intent getIntent(int method,User user){
        Intent intent = getIntent(method);
        intent.putExtra(Constant.USER,user);
        return intent;

    }


    private Intent getIntent(int method){
        MyResultReceiver resultReceiver = new MyResultReceiver(null);
        Intent intent = new Intent(getApplicationContext(), DatabaseCommunicationService.class);
        intent.putExtra(Constant.RECEIVER,resultReceiver);
        intent.putExtra(Constant.METHOD_REQ,method);

        return intent;

    }

    private class MyResultReceiver extends ResultReceiver{

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode==Constant.SERVICE_REQ_CODE && resultData!=null){

                int method = resultData.getInt(Constant.METHOD_REQ);

                switch (method){
                    case Method.FETCH_USER:

                        User user = (User) resultData.getSerializable("user");
                        Log.d("SOHEL",user.getName()+" Hummmm");

                        if(user!=null){
                            startService(Method.UPDATE_TOKEN,user);
                        }

                        break;

                    case Method.UPDATE_TOKEN:

                        if(dialogHelper!=null){
                            dialogHelper.dismisDialog();
                        }

                        transitionTo(new Intent(getApplicationContext(), MainActivity.class));

                        break;
                }



            }



            Log.d("SOHEL","RESULT RECEIVE");
        }
    }



}
