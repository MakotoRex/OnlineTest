package com.test.onlinetest;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;

import com.test.onlinetest.adapters.GoodsAdapter;
import com.test.onlinetest.models.MainActivityModel;
import com.test.onlinetest.utils.NetworkUtils;

import com.test.onlinetest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private MainActivityModel mMainActivityModel;

    private static final String PUBLIC_API  = "https://code-test.migoinc-dev.com/status";
    private static final String PRIVATE_API = "http://192.168.2.2/status";

    private Activity mActivity;
    private boolean mIsPrivateConnect;
    private String mApiResponse = "";
    private OkHttpClient mClient;

    private GoodsAdapter mDayPassAdapter;
    private GoodsAdapter mHourPassAdapter;
    private PassWallet mPassWallet;

    private final int UI_HANDLER_RATE = 1000; // per second
    private Handler mUIHandler = new Handler();
    private Runnable updateTimer = new Runnable() {
        public void run() {
            //update UI when PASS is expired
            if(mPassWallet != null){
                mPassWallet.checkAndUpdateExpire();
                updateWalletUI();
            }
            mUIHandler.postDelayed(this, UI_HANDLER_RATE);
        }
    };

    private Callback mPrivateCallback = new Callback() {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mApiResponse = "Private Api : ";
            String body = response.body().string();
            mApiResponse = mApiResponse + body;
            mIsPrivateConnect = true;
            Log.d("MIGO", mApiResponse);

            mMainActivityModel.setStatus( mApiResponse);
            mMainActivityModel.setIsReady(true);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("MIGO", e.toString());

            //No private server, simply "bindProcessToNetwork" with "NetworkCapabilities.TRANSPORT_CELLULAR" for mobile network
            NetworkUtils.bindNetworkToMobile(mActivity);

            Request publicRequest = new Request.Builder()
                    .url(PUBLIC_API)
                    .build();

            Call privateCall = mClient.newCall(publicRequest);
            privateCall.enqueue(mPublicCallback);
        }
    };

    private Callback mPublicCallback = new Callback() {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mApiResponse = "Public Api : ";
            String body = response.body().string();
            mApiResponse = mApiResponse + body;

            mMainActivityModel.setStatus( mApiResponse);
            mMainActivityModel.setIsReady(true);
            Log.d("MIGO", mApiResponse);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("MIGO", e.toString());
            mMainActivityModel.setStatus( e.toString() );
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(mUIHandler != null){
            mUIHandler.postDelayed(updateTimer, UI_HANDLER_RATE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mUIHandler != null){
            mUIHandler.removeCallbacks(updateTimer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        initViews();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            //Before Android Q, assume that the private WiFi is connected,
            //simply do "bindProcessToNetwork" with "NetworkCapabilities.TRANSPORT_WIFI"
            NetworkUtils.bindNetworkToWifi(mActivity);
        }/* else {
            //For Android Q and the newer version, you have to connect to WIFI by using "NetworkRequest"
            //and do "bindProcessToNetwork" in NetworkCallback.onAvailable when WIFI is connected.
            //Assume that user do the WIFI connection before starting this Activity.
            //See the code of "connectAndBinding" for connection details.

            NetworkUtils.connectAndBinding(mActivity,"ssid", "pass");
        }*/

        mClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();

        Request privateRequest = new Request.Builder()
                .url(PRIVATE_API)
                .build();

        Call privateCall = mClient.newCall(privateRequest);
        privateCall.enqueue(mPrivateCallback);

        //Wallet data Loaded
        mPassWallet = new PassWallet();
        updateWalletUI();
    }

    private void initViews(){
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mActivityMainBinding.rvDayList.setLayoutManager(new LinearLayoutManager(this));
        mDayPassAdapter = new GoodsAdapter(this, true);
        mActivityMainBinding.rvDayList.setAdapter(mDayPassAdapter);

        mActivityMainBinding.rvHourList.setLayoutManager(new LinearLayoutManager(this));
        mHourPassAdapter = new GoodsAdapter(this,false);
        mActivityMainBinding.rvHourList.setAdapter(mHourPassAdapter);

        mMainActivityModel = new MainActivityModel(this);
        mMainActivityModel.setStatus("Connecting...");
        mMainActivityModel.setIsReady(false);
        mActivityMainBinding.setMainActivityModel(mMainActivityModel);
    }

    public void buyItem(String key){
        mPassWallet.buyItem(key);
        updateWalletUI();
    }

    public void activateItem(String key){
        mPassWallet.activateItem(key);
        updateWalletUI();
    }

    public Date getExpireDateTime(){
        return mPassWallet.getExpireDateTime();
    }

    public boolean isActivating(){
        return mPassWallet.isActivating();
    }

    public void updateWalletUI(){
        if( mPassWallet != null){
            if( mDayPassAdapter != null){
                ArrayList dayList = new ArrayList();
                dayList.add( mPassWallet.getGoodsItemByKey(Consts.GOODS_NAME_SEVEN_DAYS) );
                dayList.add( mPassWallet.getGoodsItemByKey(Consts.GOODS_NAME_THREE_DAYS) );
                dayList.add( mPassWallet.getGoodsItemByKey(Consts.GOODS_NAME_ONE_DAYS) );
                mDayPassAdapter.updateData(dayList);
            }

            if(mHourPassAdapter != null){
                ArrayList hourList = new ArrayList();
                hourList.add( mPassWallet.getGoodsItemByKey(Consts.GOODS_NAME_EIGHT_HOURS) );
                hourList.add( mPassWallet.getGoodsItemByKey(Consts.GOODS_NAME_FOUR_HOURS) );
                mHourPassAdapter.updateData(hourList);
            }
        }
    }
}