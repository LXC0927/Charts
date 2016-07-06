package com.example.charts.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

	private String fileName;
	private int mode;
	private Context context;

	public SharedPreferencesUtil(Context context, String fileName, int mode) {
		this.context = context;
		this.fileName = fileName;
		this.mode = mode;
	}

	public void saveData(List<String> keys, List<String> lst) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("count", lst.size());
		for (int i = 0; i < lst.size(); i++) {
			editor.putString(keys.get(i), lst.get(i));
		}
		editor.commit();
	}
	public void saveData(String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);		
		editor.commit();
	}
	public void saveData(String key, Float value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat(key, value);		
		editor.commit();
	}
	public void saveData(String key, Integer value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);		
		editor.commit();
	}
	public String getData(String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		String string = sp.getString(key, "");
		return string;
	}

	public int getDataCount() {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("count", 0);
		return count;
	}
	public int getRecordCount() {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("recordcount", 0);
		return count;
	}
	public float getLastScore() {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		float lastScore = sp.getFloat("recordcount", 0f);
		return lastScore;
	}
	
	public HashMap<String, Float> getSleepMap() {
		HashMap<String, Float> sleepMap = new HashMap<>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("sleepCount", 0);
		for (int i = 0; i < count; i++) {
			sleepMap.put("sleep"+i, sp.getFloat("sleep"+i, 0f));
		}
		return sleepMap;
	}
	
	public HashMap<String, Float> getRateMap() {
		HashMap<String, Float> rateMap = new HashMap<>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("rateCount", 0);
		for (int i = 0; i < count; i++) {
			rateMap.put("rate"+i, sp.getFloat("rate"+i, 0f));
		}
		return rateMap;
	}
	
	public HashMap<String, Float> getTempMap() {
		HashMap<String, Float> tempMap = new HashMap<>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("tempCount", 0);
		for (int i = 0; i < count; i++) {
			tempMap.put("temp"+i, sp.getFloat("temp"+i, 0f));
		}
		return tempMap;
	}

	public HashMap<String, Float> getWaterMap() {
		HashMap<String, Float> waterMap = new HashMap<>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("waterCount", 0);
		for (int i = 0; i < count; i++) {
			waterMap.put("water"+i, sp.getFloat("water"+i, 0f));
		}
		return waterMap;
	}
	
	public HashMap<String, Float> getLightMap() {
		HashMap<String, Float> lightMap = new HashMap<>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("lightCount", 0);
		for (int i = 0; i < count; i++) {
			lightMap.put("light"+i, sp.getFloat("light"+i, 0f));
		}
		return lightMap;
	}
	
	public HashMap<String, Float> getNoiseMap() {
		HashMap<String, Float> noiseMap = new HashMap<>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("noiseCount", 0);
		for (int i = 0; i < count; i++) {
			noiseMap.put("noise"+i, sp.getFloat("noise"+i, 0f));
		}
		return noiseMap;
	}
	
	public HashMap<String, Float> getShakeMap() {
		HashMap<String, Float> shakeMap = new HashMap<>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		int count = sp.getInt("shakeCount", 0);
		for (int i = 0; i < count; i++) {
			shakeMap.put("shake"+i, sp.getFloat("shake"+i, 0f));
		}
		return shakeMap;
	}

	public List<String> getData(List<String> keys) {
		List<String> lst = new ArrayList<String>();
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		for (int i = 0; i < keys.size(); i++) {
			String data = sp.getString(keys.get(i), "");
			lst.add(data);
		}
		return lst;
	}
}
