package com.example.charts.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * add persons
	 * 
	 * @param persons
	 */
	public void add(ScoreBean scoreBean) {
		db.beginTransaction(); // 开始事务
		try {
			db.execSQL("INSERT INTO sleep VALUES(null, date(?), ?)",
					new Object[] { scoreBean.getDate(), scoreBean.getScore() });
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void update(ScoreBean scoreBean) {
		db.beginTransaction(); // 开始事务
		try {

			db.execSQL("UPDATE sleep SET score = ? WHERE date = date(?)",
					new Object[] { scoreBean.getScore(), scoreBean.getDate() });

			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void updateTest() {
		db.beginTransaction(); // 开始事务
		try {

			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-25'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-24'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-23'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-22'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-21'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-20'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-19'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-18'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-17'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-16'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-15'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-14'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-13'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-12'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-11'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-10'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-09'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-08'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-07'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-06'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-05'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-04'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-03'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-02'), 89.2)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-04-01'), 34.5)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-03-31'), 67.8)");
			db.execSQL("INSERT INTO sleep VALUES(null, date('2016-03-30'), 89.2)");
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public List<ScoreBean> queryWeekDatas() {
		ArrayList<ScoreBean> scoreBeans = new ArrayList<>();
		Cursor c = queryTheCursor();
		int i = 0;
		while (c.moveToNext()) {
			if (i >= 7) {
				break;
			}

			ScoreBean scoreBean = new ScoreBean();
			scoreBean.setId(c.getInt(c.getColumnIndex("id")));
			scoreBean.setDate(c.getString(c.getColumnIndex("date")));
			scoreBean.setScore(c.getFloat(c.getColumnIndex("score")));
			scoreBeans.add(scoreBean);

			i++;
		}

		c.close();
		return scoreBeans;
	}

	public List<ScoreBean> queryMonthDatas() {
		ArrayList<ScoreBean> scoreBeans = new ArrayList<>();
		Cursor c = queryTheCursor();
		int i = 0;
		while (c.moveToNext()) {
			if (i >= 28) {
				break;
			}

			ScoreBean scoreBean = new ScoreBean();
			scoreBean.setId(c.getInt(c.getColumnIndex("id")));
			scoreBean.setDate(c.getString(c.getColumnIndex("date")));
			scoreBean.setScore(c.getFloat(c.getColumnIndex("score")));
			scoreBeans.add(scoreBean);

			i++;
		}
		c.close();
		int weekCount = scoreBeans.size() / 7;
		int weekModCount = scoreBeans.size() % 7;
		ArrayList<ScoreBean> monthScoreBeans = new ArrayList<>();
		for (int j = 0; j < weekCount; j++) {
			float sum = 0f;
			for (int j2 = j * 7; j2 < j * 7 + 7; j2++) {
				sum = sum + scoreBeans.get(j2).getScore();
			}
			float avg = sum / 7;
			ScoreBean scoreBean = new ScoreBean();
			scoreBean.setDate(scoreBeans.get(j * 7 + 6).getDate() + "~"
					+ scoreBeans.get(j * 7).getDate());
			scoreBean.setScore(avg);
			monthScoreBeans.add(scoreBean);
		}
		if (weekModCount == 0) {
			return monthScoreBeans;
		}
		float sum = 0f;

		for (int j = 0; j < weekModCount; j++) {
			sum = sum + scoreBeans.get(weekCount * 7 + j).getScore();
		}
		float avg = sum / weekModCount;
		ScoreBean scoreBean = new ScoreBean();
		if (weekModCount == 1) {
			scoreBean.setDate(scoreBeans.get(scoreBeans.size() - 1).getDate());
		} else {
			scoreBean.setDate(scoreBeans.get(scoreBeans.size() - 1).getDate()
					+ "~" + scoreBeans.get(weekCount * 7).getDate());
		}

		scoreBean.setScore(avg);
		monthScoreBeans.add(scoreBean);

		return monthScoreBeans;
	}

	public float getAverage() {
		Cursor c = db.rawQuery("SELECT avg(score) as acerage FROM sleep", null);
		if (c.moveToNext()) {
			float average = c.getFloat(c.getColumnIndex("acerage"));
			return average;
		}
		return 0f;
	}

	public int getCount() {
		Cursor c = db.rawQuery("SELECT count(*) as count FROM sleep", null);
		if (c.moveToNext()) {
			int count = c.getInt(c.getColumnIndex("count"));
			return count;
		}
		return 0;
	}

	/**
	 * query all persons, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor() {
		Cursor c = db.rawQuery("SELECT * FROM sleep ORDER BY id DESC", null);
		return c;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
