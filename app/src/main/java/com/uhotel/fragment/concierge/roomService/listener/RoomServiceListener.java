package com.uhotel.fragment.concierge.roomService.listener;

import com.uhotel.fragment.concierge.roomService.info.MasterItem;

import java.util.HashMap;

/**
 * Created by kiemhao on 8/29/16.
 */
public interface RoomServiceListener {

    String[] getArrMaster();

    HashMap<Integer, MasterItem> getHashMap();
}
