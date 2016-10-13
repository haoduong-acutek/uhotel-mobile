package com.uhotel.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiemhao on 9/23/16.
 */
public class CatInfo {

    public String id;
    public String title;
    public String tag;
    public String parentId;
    public boolean isLeaf;
    public List<String> purchaseGroupsIds = new ArrayList<String>();
    public String type;
    public Integer ageRating;
    public PurchaseInfo purchasedInfo;


    public int selectIndex;
    public List<VodInfo> listVod;
}
