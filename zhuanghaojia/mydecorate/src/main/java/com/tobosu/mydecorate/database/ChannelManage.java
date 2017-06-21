package com.tobosu.mydecorate.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tobosu.mydecorate.entity.DecorateTitleEntity;
import com.tobosu.mydecorate.entity.DecorateTitleEntity.ChannelItem;
import com.tobosu.mydecorate.util.Util;

import android.database.SQLException;
import android.util.Log;

public class ChannelManage {
	public static ChannelManage channelManage;
	/**
	 * 默认的用户选择频道列表
	 * */
	public static ArrayList<DecorateTitleEntity.ChannelItem> defaultUserChannels = new ArrayList<DecorateTitleEntity.ChannelItem>();
	/**
	 * 默认的其他频道列表
	 * */
	public static List<DecorateTitleEntity.ChannelItem> defaultOtherChannels = new ArrayList<DecorateTitleEntity.ChannelItem>();
	private ChannelDao channelDao;

	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;
//	static {
//		defaultUserChannels = new ArrayList<DecorateTitleEntity.ChannelItem>();
//		defaultOtherChannels = new ArrayList<DecorateTitleEntity.ChannelItem>();
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(1, "推荐", 1, 1));
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(2, "热点", 2, 1));
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(3, "杭州", 3, 1));
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(4, "时尚", 4, 1));
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(5, "科技", 5, 1));
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(6, "体育", 6, 1));
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(7, "军事", 7, 1));
//
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(8, "财经", 1, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(9, "汽车", 2, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(10, "房产", 3, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(11, "社会", 4, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(12, "情感", 5, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(13, "女人", 6, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(14, "旅游", 7, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(15, "健康", 8, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(16, "美女", 9, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(17, "游戏", 10, 0));
//		defaultOtherChannels.add(new DecorateTitleEntity.ChannelItem(18, "数码", 11, 0));
//		defaultUserChannels.add(new DecorateTitleEntity.ChannelItem(19, "娱乐", 12, 0));
//	}

	public void setDefaultData(ArrayList<DecorateTitleEntity.ChannelItem> itemList){
		for(int i=0;i<itemList.size();i++){
			defaultUserChannels.add(itemList.get(i));
			Util.setErrorLog("ChannelManage  setDefaultData() ", itemList.get(i).getName());
		}
	}

	public void setOtherData(ArrayList<DecorateTitleEntity.ChannelItem> itemList){
		for(int i=0;i<itemList.size();i++){
			defaultOtherChannels.add(itemList.get(i));
//			Util.setErrorLog("ChannelManage  setOtherData() ", itemList.get(i).getName());
		}
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	/**
	 * 初始化频道管理类
	 * @param dbHelper
	 * @throws SQLException
	 */
	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}

	/**
	 * 清除所有的频道
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	public ArrayList<DecorateTitleEntity.ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			ArrayList<DecorateTitleEntity.ChannelItem> list = new ArrayList<DecorateTitleEntity.ChannelItem>();
			for (int i = 0; i < count; i++) {
				DecorateTitleEntity.ChannelItem navigate = new DecorateTitleEntity.ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}

	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	public ArrayList<DecorateTitleEntity.ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
		ArrayList<DecorateTitleEntity.ChannelItem> list = new ArrayList<DecorateTitleEntity.ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				DecorateTitleEntity.ChannelItem navigate= new DecorateTitleEntity.ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (ArrayList<DecorateTitleEntity.ChannelItem>) cacheList;
	}

	/**
	 * 保存用户频道到数据库
	 * @param userList
	 */
	public void saveUserChannel(List<DecorateTitleEntity.ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			DecorateTitleEntity.ChannelItem item = userList.get(i);
			item.setOrderId(i);
			item.setSelected(Integer.valueOf(1));
			channelDao.addCache(item);
		}
	}

	/**
	 * 保存其他频道到数据库
	 * @param otherList
	 */
	public void saveOtherChannel(List<DecorateTitleEntity.ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			DecorateTitleEntity.ChannelItem item = otherList.get(i);
			item.setOrderId(i);
			item.setSelected(Integer.valueOf(0));
			channelDao.addCache(item);
		}
	}

	/**
	 * 初始化数据库内的频道数据
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
