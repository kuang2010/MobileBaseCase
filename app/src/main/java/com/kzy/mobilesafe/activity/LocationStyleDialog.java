package com.kzy.mobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.utils.SpUtil;
import com.kzy.mobilesafe.view.BottomDialog;

/**
 * author: kuangzeyu2019
 * date: 2020/4/10
 * time: 23:28
 * desc:
 */
public class LocationStyleDialog extends BottomDialog implements AdapterView.OnItemClickListener {

    public static final int[] locDrawable = new int[]{R.drawable.call_locate_blue,R.drawable.call_locate_gray,
            R.drawable.call_locate_green,R.drawable.call_locate_orange,R.drawable.call_locate_white};
    public static final String[] locDesc = new String[]{"卫士蓝","金属灰","苹果绿","活力橙","半透明"};
    private Context mContext;
    private ListView mLvLocationStyleSelect;
    private LocationStyleAdapter mAdapter;

    public LocationStyleDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_location_style_select, null);
        mLvLocationStyleSelect = view.findViewById(R.id.lv_location_style_select);
        setContentView(view);
        mAdapter = new LocationStyleAdapter();
        mLvLocationStyleSelect.setAdapter(mAdapter);
        mLvLocationStyleSelect.setOnItemClickListener(this);

        //一定要在setContentView之后调用super.onCreate
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SpUtil.putInt(mContext,MyConstants.LOCATIONSTYLESELECTPOS,position);
        mAdapter.notifyDataSetChanged();
        dismiss();
    }


    class LocationStyleAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return locDrawable.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = View.inflate(mContext,R.layout.item_dialog_location_select,null);
            }
            ImageView iv_style_location_dialog = convertView.findViewById(R.id.iv_style_location_dialog);
            TextView tv_style_location_dialog = convertView.findViewById(R.id.tv_style_location_dialog);
            ImageView iv_check_location_dialog = convertView.findViewById(R.id.iv_check_location_dialog);
            iv_style_location_dialog.setImageResource(locDrawable[position]);
            tv_style_location_dialog.setText(locDesc[position]);
            int selectIndex = SpUtil.getInt(mContext, MyConstants.LOCATIONSTYLESELECTPOS, MyConstants.LOCATIONDEFOULTINDX);
            if (position==selectIndex){
                iv_check_location_dialog.setVisibility(View.VISIBLE);
            }else {
                iv_check_location_dialog.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
}
