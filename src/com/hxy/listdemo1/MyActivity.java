package com.hxy.listdemo1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MyActivity extends Activity implements AbsListView.OnScrollListener {
    private static final String TAG="MainActivity";
    private ListView listView;
    private View moreView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String,String>> listData;

    private int lastItem;
    private int count;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView=(ListView)findViewById(R.id.listView);
        moreView=getLayoutInflater().inflate(R.layout.load,null);
        listData=new ArrayList<HashMap<String, String>>();
        prepareData();
        count=listData.size();
        adapter=new SimpleAdapter(this, listData,R.layout.item,new String[]{
                "itemText"
        },new int[]{R.id.itemText});
        listView.addFooterView(moreView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.i(TAG, "scrollState=" + scrollState);
        if(lastItem==count&&scrollState==this.SCROLL_STATE_IDLE){
            Log.i(TAG,"拉到最底部");
            moreView.setVisibility(view.VISIBLE);
            mHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i(TAG,"firstVisibleItem="+firstVisibleItem+"\nvisibleItemCount="+
        visibleItemCount+"\ntotalItemCount"+totalItemCount);
        lastItem=firstVisibleItem+visibleItemCount-1;

    }
    private void prepareData(){
        for(int i=0;i<10;i++){
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("itemText","测试数据"+i);
            listData.add(map);
        }
    }
    private void loadMoreData(){

        count=adapter.getCount();
        for(int i=count;i<count+5;i++){
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("itemText","测试数据"+i);
            listData.add(map);
        }
        count=listData.size();
    }

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                loadMoreData();
                    adapter.notifyDataSetChanged();
                    moreView.setVisibility(View.GONE);
                    if(count>30){
                        Toast.makeText(MyActivity.this, "没有更多数据！",Toast.LENGTH_LONG).show();
                        listView.removeFooterView(moreView);
                    }
                    Log.i(TAG,"加载更多数据...");
                case 1:
                    break;
                default:
                    break;
            }
        }
    };


}
