package com.jqd.rssmagdetect.ui;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-28 下午7:20:18
 * @description 配置显示WiFi的listView的显示数据和格式
 */
public class WiFiListAdapter extends BaseAdapter {

	private Context context;
	private List<ScanResult> scanResults;

	public WiFiListAdapter(MainActivity context, List<ScanResult> scanResults) {
		super();
		this.context = context;
		this.scanResults = scanResults;
	}

	// getcount 获取数据的个数
	@Override
	public int getCount() {
		return scanResults.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	// getView 需要构建一个View对象来显示数据源中的数据
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ScanResult scanResult = scanResults.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup group = (ViewGroup) inflater.inflate(R.layout.wifi_msg, null);
		TextView textView1 = (TextView) group.findViewById(R.id.textView1);
		TextView textView2 = (TextView) group.findViewById(R.id.textView2);
		textView1.setText(scanResult.SSID);
		textView2.setText(scanResult.level + " dBm");

		return group;
	}

}