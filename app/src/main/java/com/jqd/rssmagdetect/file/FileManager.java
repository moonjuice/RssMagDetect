package com.jqd.rssmagdetect.file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import com.jqd.rssmagdetect.model.SensorsDataManager;
import com.jqd.rssmagdetect.model.WiFiDataManager;
import com.jqd.rssmagdetect.ui.MainActivity;
import com.jqd.rssmagdetect.util.GlobalPara;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-28 下午3:51:19
 * @description 采集好的数据的存储
 */
public class FileManager {
	private Context mContext;
	public FileManager(Activity activity) {
		mContext = activity.getApplicationContext();
	}

	/**
	 * 这个函数每次存两个文件，"dataRssi_at_1" 和 "dataBssid.txt"
	 * dataRssi_at_1存的是rssi和传感器数据，每个时刻的一组数据包括n个AP的rssi和15个传感器的数值，依次添加进去。
	 * dataBssid存的是Wifi热点一些信息，顺序和上面的对应 注意：如果已存在该文件，这个函数创建的新的文件会覆盖之前的。（
	 * APP第一次开启获取的BSSID顺序和关闭APP再开启进行采集得到的BSSID顺序是不一样的）
	 * 但是app的逻辑是只有改变位置后，存储在内存的数据才清零，所以同一位置的多次存储并无影响。
	 */
	public void saveData() {
		saveRssiAndSensors(); // 存数据
		saveWifiBssids(); // 存wifi的bssid
	}

	private void saveRssiAndSensors() {
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath()
					+ "/CIPS-DataCollect");
			directory.mkdirs();
			File file = new File(directory, "dataRssi_at_"
					+ GlobalPara.getInstance().position_index + ".txt");
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStream fos = fOut;
			DataOutputStream dos = new DataOutputStream(fos);
			JSONArray mResult = new JSONArray();
			if (WiFiDataManager.getInstance().dataCount != 0) {
				for (int i = 0; i < WiFiDataManager.getInstance().dataCount; i++) {
					// 存wifi的Rssi数据
					JSONObject object = new JSONObject();
					object.put("SSID", WiFiDataManager.getInstance().dataWifiNames.get(i));
					for (String bssid : WiFiDataManager.getInstance().dataBssid
							.keySet()) {
						object.put("BSSID", bssid);
						int j = WiFiDataManager.getInstance().dataBssid.get(bssid);
						if (WiFiDataManager.getInstance().dataRssi.get(j)
								.containsKey(i)) {
							object.put("RSSI", WiFiDataManager.getInstance().dataRssi
									.get(j).get(i));
						/*dos.write((WiFiDataManager.getInstance().dataRssi
								.get(j).get(i) + "\t").getBytes());*/
						}/* else {
						dos.write((0 + "\t").getBytes()); // 没有的话就存0
					}*/
					}
					mResult.put(object);
					dos.write(mResult.toString().getBytes());
					// 存传感器数据，rss后面增加15个int
				/*SensorsDataManager sdm = SensorsDataManager.getInstance();
				String outString = sdm.dataMagnetic.get(0).get(i) + "\t"
						+ sdm.dataMagnetic.get(1).get(i) + "\t"
						+ sdm.dataMagnetic.get(2).get(i) + "\t"
						+ sdm.dataOrientation.get(0).get(i) + "\t"
						+ sdm.dataOrientation.get(1).get(i) + "\t"
						+ sdm.dataOrientation.get(2).get(i) + "\t"
						+ sdm.dataAccelerate.get(0).get(i) + "\t"
						+ sdm.dataAccelerate.get(1).get(i) + "\t"
						+ sdm.dataAccelerate.get(2).get(i) + "\t"
						+ sdm.dataGyroscope.get(0).get(i) + "\t"
						+ sdm.dataGyroscope.get(1).get(i) + "\t"
						+ sdm.dataGyroscope.get(2).get(i) + "\t"
						+ sdm.dataGravity.get(0).get(i) + "\t"
						+ sdm.dataGravity.get(1).get(i) + "\t"
						+ sdm.dataGravity.get(2).get(i) + "\n";
				System.out.println(outString);
				dos.write(outString.getBytes());*/
				}
			}
			dos.close();

			Toast toast = Toast.makeText(
					WiFiDataManager.getInstance().activity,
					"save to “/CIPS-DataCollect”", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			MediaScannerConnection.scanFile(mContext, new String[] { file.getAbsolutePath() }, null, null);
		} catch (FileNotFoundException e) {
			Toast.makeText(WiFiDataManager.getInstance().activity, "存储失败。",
					Toast.LENGTH_SHORT).show();
			return;
		} catch (IOException e) {
			Toast.makeText(WiFiDataManager.getInstance().activity, "存储失败。",
					Toast.LENGTH_SHORT).show();
			return;
		} catch (JSONException e) {
			Toast.makeText(WiFiDataManager.getInstance().activity, "存储失败。",
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	private void saveWifiBssids() {
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath()
					+ "/CIPS-DataCollect");
			directory.mkdirs();
			File file = new File(directory, "dataBssid.txt");
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStream fos = fOut;
			DataOutputStream dos = new DataOutputStream(fos);

			String[] tmpOutString = new String[WiFiDataManager.getInstance().dataBssid
					.size()];
			for (String bssid : WiFiDataManager.getInstance().dataBssid
					.keySet()) {
				int j = WiFiDataManager.getInstance().dataBssid.get(bssid);
				String jString = j + 1 + "\tBSSID:\t" + bssid + "\tSSID:\t"
						+ WiFiDataManager.getInstance().dataWifiNames.get(j)
						+ "\n";
				tmpOutString[j] = jString;
			}
			for (int i = 0; i < tmpOutString.length; i++) {
				dos.write(tmpOutString[i].getBytes());
			}
			dos.close();
			MediaScannerConnection.scanFile(mContext, new String[] { file.getAbsolutePath() }, null, null);
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}
}
