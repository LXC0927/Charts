package com.example.charts.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil {

	public DateUtil(){
		
	}
	
	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
	public String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
	
	public List<String> getLastNDays(int n,Date lastDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date curDate = null;
		if (lastDate == null) {
			curDate = new Date(System.currentTimeMillis());// 获取当前时间
		}else {
			curDate = lastDate;
		}
		String str = formatter.format(curDate);
		
		List<String> dateList = new ArrayList<String>();
		String[] date = str.split("/");
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);
		for (int i = 0; i < n; i++) {
			if (day - i > 0) {
				int tempDay = day - i;
				dateList.add(0,month + "/" + tempDay);
			} else {
				int tempMonth = month - 1;
				if (tempMonth == 0) {
					tempMonth = 12;
				}
				int tempDay = 0;
				switch (tempMonth) {
				case 2:
					if (isLeapYear(year)) {
						tempDay = 29 + day - i;
					} else {
						tempDay = 28 + day - i;
					}
					break;
				case 1:	case 3:	case 5:	case 7:	case 8:	case 10: case 12:
					tempDay = 31 + day - i;
					break;
				case 4: case 6: case 9: case 11:
					tempDay = 30 + day - i;
					break;
				}
				dateList.add(0,tempMonth + "/" + tempDay);
			}
		}
		return dateList;
	}
//	
//	public List<String> getLastNWeeks(int n) {
//		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//		for (int i = 0; i < n; i++) {
//			List<String> dateList = getLastNDays(7, curDate);
//			
//		}
//		
//		
//		
//		List<String> weekList = new ArrayList<>();
//		
//		return weekList;
//	}
	
	
	private boolean isLeapYear(int year) {
		return year % 400 == 0 || year % 100 != 0 && year % 4 == 0;
	}
}
