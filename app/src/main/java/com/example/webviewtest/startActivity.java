package com.example.webviewtest;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class startActivity extends Activity{
    private ImageView imageView1;
    private Handler handler;
    private String result="";
    private JSONObject jsonObject;
    private String string="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW);
        setContentView(R.layout.start);
        imageView1=(ImageView)findViewById(R.id.imageView);
        /*
         *cause android does not allow modify UI in other threads
         * so wo need Handler to modify UI in main thread
         */
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:Picasso.with(getBaseContext()).load(getURL(result)).into(imageView1);
                }
            }
        };
        /*
         *this thread is used to get the picture in the start interface from server
         */
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url=new URL("http://news-at.zhihu.com/api/4/start-image/480*728");
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode()==200){
                        String str="";
                        InputStream inputStream=connection.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
                        StringBuffer stringBuffer=new StringBuffer();
                        while ((str=reader.readLine())!=null){
                            stringBuffer.append(str).append("\n");
                        }
                        result=stringBuffer.toString();

                        inputStream.close();
                        reader.close();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally{
                }
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        });
        t.start();
        initImage();
    }
    private String getURL(String result){

        try{
            jsonObject=new JSONObject(result);
            string=jsonObject.getString("img");
        }catch (Exception e1){
            e1.printStackTrace();
        }
        return string;
    }
    private void initImage(){

        final ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f, 1.005f, 1.0f, 1.005f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView1.startAnimation(scaleAnimation);
    }
    private void startActivity(){
        Intent intent=new Intent(startActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
}
