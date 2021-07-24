package com.test.onlinetest.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import com.test.onlinetest.Consts;
import com.test.onlinetest.GoodsItem;
import com.test.onlinetest.MainActivity;
import com.test.onlinetest.R;
import com.test.onlinetest.utils.DateTimeUtils;


public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    private ArrayList<GoodsItem> mData;

    private boolean mIsDayPass = false;
    private Activity mActivity;

    public GoodsAdapter(Activity activity, boolean isDayPass) {
        mIsDayPass = isDayPass;
        mActivity = activity;
        mData = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private Button passBtn;
        private View rootView;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);
            passBtn = itemView.findViewById(R.id.btn_pass);
            rootView = itemView.findViewById(R.id.rl_item_block);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item;
        item = LayoutInflater.from(mActivity).inflate(R.layout.item_pass, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.title.setText( mData.get(position).getItemName() );
        final int status = mData.get(position).getItemStatus();
        String dateTime;
        int color;

        switch (status){
            case Consts.PASS_STATUS_EMPTY :
                holder.description.setText( "$" + mData.get(position).getPrice() );
                holder.passBtn.setText("BUY");

                color = mActivity.getResources().getColor(R.color.colorGray);
                holder.rootView.setBackgroundColor(color);
                break;
            case Consts.PASS_STATUS_BOUGHT :
                dateTime = DateTimeUtils.getFormattedDateTimeHour( mData.get(position).getLastMOdifiedDateTime() );
                holder.description.setText( "Purchased:" + dateTime );
                holder.passBtn.setText("ACTIVATE");

                color = mActivity.getResources().getColor(R.color.colorBlue);
                holder.rootView.setBackgroundColor(color);
                break;
            case Consts.PASS_STATUS_ACTIVATED :
                dateTime = DateTimeUtils.getFormattedDateTimeHour( mData.get(position).getLastMOdifiedDateTime() );
                Date expiredDateTime = ((MainActivity)mActivity).getExpireDateTime();
                String expiredateTime = DateTimeUtils.getFormattedDateTimeHour( expiredDateTime);

                holder.description.setText( "Activation:" + dateTime + "\nExpiration:" + expiredateTime);
                holder.passBtn.setText("ACTIVATING...");

                color = mActivity.getResources().getColor(R.color.colorGreen);
                holder.rootView.setBackgroundColor(color);
                break;
        }

        holder.passBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( status == Consts.PASS_STATUS_EMPTY ){
                    ((MainActivity)mActivity).buyItem( mData.get(position).getItemName() );
                } else if( status == Consts.PASS_STATUS_BOUGHT ){
                    if( ((MainActivity)mActivity).isActivating() ){
                        Toast.makeText(mActivity, "A pass is activating, please activate until expiration.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((MainActivity)mActivity).activateItem( mData.get(position).getItemName() );
                } else if( status == Consts.PASS_STATUS_ACTIVATED ){
                    if( ((MainActivity)mActivity).isActivating() ){
                        Toast.makeText(mActivity, "A pass is activating, please activate until expiration.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(ArrayList<GoodsItem> data){
        if( mData != null && data != null){
            mData.clear();
            mData = data;
            notifyDataSetChanged();
        }
    }
}