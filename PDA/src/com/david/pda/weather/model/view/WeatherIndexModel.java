package com.david.pda.weather.model.view;

import android.widget.ImageButton;

import com.david.pda.weather.model.Index;

public class WeatherIndexModel {

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ImageButton getView() {
		return view;
	}

	public void setView(ImageButton view) {
		this.view = view;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	private int id;
	private ImageButton view;
	private Index index;

	public WeatherIndexModel(int id, ImageButton view, Index index) {
		super();
		this.id = id;
		this.view = view;
		this.index = index;
	}

	public WeatherIndexModel() {
		super();
	}

}
