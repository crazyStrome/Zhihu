package com.example.webviewtest;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by QDQ on 2016/8/26.
 */
public class MyBaseAdapter extends BaseAdapter{
    private List<StoriesData> list;
    private LayoutInflater inflater=null;
    private Context context;
    private ViewHolder holder;
    private Activity activity;
    public MyBaseAdapter(List<StoriesData> list, Context context,Activity activity){
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.context=context;
        this.activity=activity;
    }
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        holder=null;
        if (view==null){
            /*
             * avoid resource abuse
             */
            holder=new ViewHolder();
            view=inflater.inflate(R.layout.card,null);
            holder.imageView=(ImageView)view.findViewById(R.id.image);
            holder.time=(TextView)view.findViewById(R.id.time);
            holder.title=(TextView)view.findViewById(R.id.text);
            holder.cardView=(CardView)view.findViewById(R.id.card);
            view.setTag(holder);
        }else {
            holder=(ViewHolder)view.getTag();
        }
        Picasso.with(context).load(list.get(i).getImages()).into(holder.imageView);
        holder.title.setText(list.get(i).getTitle());
        holder.time.setText(list.get(i).getDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Vibrator vibrator=(Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE) ;
                        vibrator.vibrate(10);
                        (view.findViewById(R.id.text)).setAlpha(0.5f);
                        Intent intent=new Intent(activity,MainDataActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("data","http://daily.zhihu.com/story/"+list.get(i).getId());
                        intent.putExtras(bundle);
                        ActivityTransitionLauncher.with(activity).from(view).launch(intent);
                    }
                }).start();
                //context.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(activity,holder.cardView,"mybtn").toBundle());
            }
        });
        return view;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }
    static class ViewHolder{
        public CardView cardView;
        public ImageView imageView;
        public TextView title;
        public TextView time;
    }
}
