package com.invisibi.opengles;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.util.Log;

public class LoadTextObjTask extends AsyncTask<String, Integer, Boolean>{

	private ArrayList<TextObj> mTextObjs;
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		if (arg0 == null) return false;
		String jsonData = MyUtils.ReadFile(arg0[0]);
		if (jsonData.length() < 1) {
			return false;
		}
		
		Type listType = new TypeToken<ArrayList<TextObj>>(){}.getType();
		Gson gson = new Gson();
		mTextObjs = gson.fromJson(jsonData, listType);
		
		float [] params = new float[6];
		for (Iterator<TextObj> iterator = mTextObjs.iterator(); iterator.hasNext();) {
			TextObj tobj = iterator.next();
			Log.i("JackTest","text ="+tobj.getText());
			Log.i("JackTest","colorR ="+tobj.getColorR());
            TextObjManager.AddTextObj(tobj);
		}
		
		return true;
	}
	
}
