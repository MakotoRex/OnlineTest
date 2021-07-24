package com.test.onlinetest;

import org.jetbrains.annotations.NotNull;
import java.util.Date;
import java.util.HashMap;

import com.test.onlinetest.utils.DateTimeUtils;

public class PassWallet {

    private HashMap<String, GoodsItem> mGoodsList;
    private Date mExpireDateTime;

    public PassWallet(){
        mGoodsList = new HashMap<>();

        mGoodsList.put(Consts.GOODS_NAME_SEVEN_DAYS, new GoodsItem(Consts.GOODS_NAME_SEVEN_DAYS, Consts.GOODS_INDEX_SEVEN_DAYS) );
        mGoodsList.put(Consts.GOODS_NAME_THREE_DAYS, new GoodsItem(Consts.GOODS_NAME_THREE_DAYS, Consts.GOODS_INDEX_THREE_DAYS) );
        mGoodsList.put(Consts.GOODS_NAME_ONE_DAYS, new GoodsItem(Consts.GOODS_NAME_ONE_DAYS, Consts.GOODS_INDEX_ONE_DAYS) );
        mGoodsList.put(Consts.GOODS_NAME_EIGHT_HOURS, new GoodsItem(Consts.GOODS_NAME_EIGHT_HOURS, Consts.GOODS_INDEX_EIGHT_HOURS) );
        mGoodsList.put(Consts.GOODS_NAME_FOUR_HOURS, new GoodsItem(Consts.GOODS_NAME_FOUR_HOURS, Consts.GOODS_INDEX_FOUR_HOURS) );

        mExpireDateTime = new Date();
    }

    public void buyItem(@NotNull String key){
        GoodsItem item = mGoodsList.get(key);

        if( item.getItemStatus() != Consts.PASS_STATUS_EMPTY ){
            return;
        }

        //Item bought
        item.setItemStatus(Consts.PASS_STATUS_BOUGHT);
        item.setLastMOdifiedDateTime( DateTimeUtils.getCurrentDateTime() );

        mGoodsList.put(key, item);
    }

    public void activateItem(@NotNull String key){
        GoodsItem item = mGoodsList.get(key);

        if( item.getItemStatus() != Consts.PASS_STATUS_BOUGHT ){
            return;
        }

        Date date = DateTimeUtils.getCurrentDateTime();

        //Item activated
        item.setItemStatus(Consts.PASS_STATUS_ACTIVATED);
        item.setLastMOdifiedDateTime( date );

        setExpireDateTime(date, key);
        mGoodsList.put(key, item);
    }

    public void checkAndUpdateExpire(){

        Date lastDateTime = DateTimeUtils.getCurrentDateTime();

        if( lastDateTime.after(mExpireDateTime) ){
            for (String key : mGoodsList.keySet()) {
                GoodsItem item = mGoodsList.get(key);
                if( item.getItemStatus() == Consts.PASS_STATUS_ACTIVATED ){
                    item.setItemStatus(Consts.PASS_STATUS_EMPTY);
                }
            }
        }
    }

    public boolean isActivating(){
        for (String key : mGoodsList.keySet()) {
            GoodsItem item = mGoodsList.get(key);
            if( item.getItemStatus() == Consts.PASS_STATUS_ACTIVATED ){
                return true;
            }
        }
        return false;
    }

    public int getItemListSize(){
        return mGoodsList.size();
    }

    public GoodsItem getGoodsItemByKey(@NotNull String key){
        if( mGoodsList != null){
            return mGoodsList.get(key);
        }
        return null;
    }

    public Date getExpireDateTime(){
        return mExpireDateTime;
    }

    private void setExpireDateTime(Date date, String key){
        switch(key){
            case Consts.GOODS_NAME_SEVEN_DAYS :
                mExpireDateTime = DateTimeUtils.plusSevenDays(date);
                break;
            case Consts.GOODS_NAME_THREE_DAYS :
                mExpireDateTime = DateTimeUtils.plusThreeDays(date);
                break;
            case Consts.GOODS_NAME_ONE_DAYS :
                mExpireDateTime = DateTimeUtils.plusOneDay(date);
                break;
            case Consts.GOODS_NAME_EIGHT_HOURS:
                mExpireDateTime = DateTimeUtils.plusEightHours(date);
                break;
            case Consts.GOODS_NAME_FOUR_HOURS :
                mExpireDateTime = DateTimeUtils.plusFourHours(date);
                break;
        }
    }
}