package com.uhotel.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiemhao on 9/23/16.
 */
public class PurchaseInfo {

    public String purchaseGroupId;
    public String purchaseGroupTitle;
    public Object purchaseGroupTag;
    public String purchaseGroupPoster;
    public String type;
    public String groupType;
    public List<PriceList> priceList = new ArrayList<PriceList>();
    public List<PurchaseItem> purchaseItems = new ArrayList<PurchaseItem>();
    public PurchasePeriod purchasePeriod;
    public ConsumptionPeriod consumptionPeriod;
    public String subscriptionId;
    public Integer ageRating;
    public Object rating;
    public Boolean clientPurchaseEnabled;
    public String offerVersion;
    public DetailsInfo details;

    //custom
    public List<MediaInfo> mediaInfoList;
    public int selectIndex;

    class PurchasePeriod {

        public Integer from;
        public Integer to;
    }

    public class PurchaseItem{

        public String id;
        public String type;
    }


    class ConsumptionPeriod{

        public Integer from;
        public Integer to;
    }

    public class PriceList{

        public String id;
        public Price price;
        public String billingType;
        public String rentalPeriodUnit;
        public Integer rentalPeriodValue;
    }
    public class  Price{

        public double value;
        public String type;
        public String symbol;
    }

}

