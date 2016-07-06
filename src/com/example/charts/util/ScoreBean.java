package com.example.charts.util;

public class ScoreBean {

	private int id;
	private String date;
	private float score;
//	public ScoreBean(float score) {
//		super();
//		this.score = score;
//	}
	public ScoreBean() {
		super();
	}
	public ScoreBean(String date, float score) {
		this.date = date;
		this.score = score;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
}
