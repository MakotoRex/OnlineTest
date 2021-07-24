package com.test.onlinetest;

import java.util.Date;

public class GoodsItem {

    private String mItemName;
    private int  mItemStatus;
    private Date mLastModifiedDateTime;
    private int mPrice;

    public GoodsItem(String itemName, int index){
        mItemName = itemName;
        mItemStatus = Consts.PASS_STATUS_EMPTY;  //default nothing bought
        mLastModifiedDateTime = new Date();
        setPriceByIndex(index);
    }

    public void setItemName(String itemName){
        mItemName = itemName;
    }

    public String getItemName(){
        return mItemName;
    }

    public void setItemStatus(int status){
        mItemStatus = status;
    }

    public int getItemStatus(){
        return mItemStatus;
    }

    public void setPrice(int price){
        mPrice = price;
    }

    public int getPrice(){
        return mPrice;
    }

    public void setLastMOdifiedDateTime(Date date){
        mLastModifiedDateTime = date;
    }

    public Date getLastMOdifiedDateTime(){
        return mLastModifiedDateTime;
    }

    public void setPriceByIndex(int index){
        switch(index){
            case Consts.GOODS_INDEX_SEVEN_DAYS :
                mPrice = Consts.PRICE_SEVEN_DAYS;
                break;
            case Consts.GOODS_INDEX_THREE_DAYS :
                mPrice = Consts.PRICE_THREE_DAYS;
                break;
            case Consts.GOODS_INDEX_ONE_DAYS :
                mPrice = Consts.PRICE_ONE_DAYS;
                break;
            case Consts.GOODS_INDEX_EIGHT_HOURS:
                mPrice = Consts.PRICE_EIGHT_HOURS;
                break;
            case Consts.GOODS_INDEX_FOUR_HOURS :
                mPrice = Consts.PRICE_FOUR_HOURS;
                break;
        }
    }
}