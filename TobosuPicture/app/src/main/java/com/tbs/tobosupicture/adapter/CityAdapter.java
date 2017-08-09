package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.tbs.tobosupicture.bean.City;
import com.tbs.tobosupicture.view.PinnedHeaderListView;
import com.tbs.tobosupicture.R;
import java.util.Arrays;
import java.util.List;
import java.util.Map;




public class CityAdapter extends BaseAdapter implements SectionIndexer, PinnedHeaderListView.PinnedHeaderAdapter, OnScrollListener {

	private List<City> mCities;
	private Map<String, List<City>> mMap;
	private List<String> mSections;
	private List<Integer> mPositions;
	private LayoutInflater inflater;
	private Context context;

	public CityAdapter(Context context, List<City> cities,
                       Map<String, List<City>> map, List<String> sections,
                       List<Integer> positions) {
		inflater = LayoutInflater.from(context);
		mCities = cities;
		mMap = map;
		mSections = sections;
		mPositions = positions;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mCities.size();
	}

	@Override
	public City getItem(int position) {
		int section = getSectionForPosition(position);
		return mMap.get(mSections.get(section)).get(
				position - getPositionForSection(section));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int section = getSectionForPosition(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.select_city_item, null);
		}
		TextView group = (TextView) convertView.findViewById(R.id.group_title);
		TextView city = (TextView) convertView.findViewById(R.id.column_title);
		if (getPositionForSection(section) == position) {
			group.setVisibility(View.VISIBLE);
			group.setText(mSections.get(section));
		} else {
			group.setVisibility(View.GONE);
		}
		City item = mMap.get(mSections.get(section)).get(
				position - getPositionForSection(section));
		city.setText(item.getNm());
		return convertView;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}

	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0 || position >= getCount()) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
	}

	@Override
	public Object[] getSections() {
		return mSections.toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= mPositions.size()) {
			return -1;
		}
		return mPositions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}
}
