package com.example.webviewtest;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class zhihuFragment extends Fragment{
    private ListView recyclerView;
    private boolean divpage;
    private MyBaseAdapter adapter;
    private List<StoriesData> list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int time=0;
    private FloatingActionButton fab;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.content_main,container,false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator=(Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE) ;
                vibrator.vibrate(10);
                recyclerView.setSelection(0);
            }
        });
        recyclerView=(ListView)view.findViewById(R.id.recyclerView);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list=getStories();
                adapter=new MyBaseAdapter(list,getContext(),getActivity());
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                list=getStories();
                adapter=new MyBaseAdapter(list,getContext(),getActivity());
                recyclerView.setAdapter(adapter);
            }
        });
        t.start();

        recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (divpage&&i== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    //Snackbar.make(recyclerView,"正在刷新...",Snackbar.LENGTH_SHORT).show();
                    addStoriesData();
                    //adapter.notifyDataSetInvalidated();
                    adapter.notifyDataSetChanged();
                    System.out.println("10101010");
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                divpage=(i+i1==i2);
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        return view;
    }
    public void addStoriesData(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String s=getResponseString("http://news.at.zhihu.com/api/4/news/before/"+time);
                    JSONObject object =new JSONObject(s);
                    String date=object.getString("date");
                    time--;
                    JSONArray jsonArray=object.getJSONArray("stories");
                    for (int j=0;j<jsonArray.length();j++){
                        JSONObject json1=jsonArray.getJSONObject(j);
                        StoriesData storiesData=new StoriesData();
                        storiesData.setDate(date);
                        storiesData.setImages(json1.getJSONArray("images").getString(0));
                        storiesData.setId(json1.getInt("id"));
                        storiesData.setTitle(json1.getString("title"));
                        list.add(storiesData);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    /*
     * get json data from server
     */
    public List<StoriesData> getStories(){
        final List<StoriesData> list=new ArrayList<>();
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String s=getResponseString("http://news-at.zhihu.com/api/4/news/latest");
                    JSONObject object =new JSONObject(s);
                    String date=object.getString("date");
                    if(time==0){
                        time=Integer.parseInt(date);
                    }
                    JSONArray jsonArray=object.getJSONArray("stories");
                    for (int j=0;j<jsonArray.length();j++){
                        JSONObject json1=jsonArray.getJSONObject(j);
                        StoriesData storiesData=new StoriesData();
                        storiesData.setDate(date);
                        storiesData.setImages(json1.getJSONArray("images").getString(0));
                        storiesData.setId(json1.getInt("id"));
                        storiesData.setTitle(json1.getString("title"));
                        list.add(storiesData);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }finally {
                }
            }
        });
        t.start();
        return list;
    }
    public String getResponseString(String sttr){
        String string="";
        try{
            URL url=new URL(sttr);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            String str="";
            InputStream inputStream=connection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            StringBuffer stringBuffer=new StringBuffer();
            while ((str=reader.readLine())!=null){
                stringBuffer.append(str);
            }
            string=stringBuffer.toString();
            inputStream.close();
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return string;
    }
}
