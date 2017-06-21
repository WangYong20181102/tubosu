package com.tobosu.mydecorate.database;

import java.util.List;
import java.util.Map;

import com.tobosu.mydecorate.entity.ChannelItem;
import com.tobosu.mydecorate.entity.DecorateTitleEntity;

import android.content.ContentValues;

public interface ChannelDaoInface {
	public boolean addCache(DecorateTitleEntity.ChannelItem item);

	public boolean deleteCache(String whereClause, String[] whereArgs);

	public boolean updateCache(ContentValues values, String whereClause, String[] whereArgs);

	public Map<String, String> viewCache(String selection, String[] selectionArgs);

	public List<Map<String, String>> listCache(String selection, String[] selectionArgs);

	public void clearFeedTable();
}
