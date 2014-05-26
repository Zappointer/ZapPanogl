package com.invisibi.opengles;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.invisibi.testobj.PLHotspotText;
import com.invisibi.zappanoramagl.R;
import com.invisibi.zappanoramagl.ZapApp;
import com.panoramagl.hotspots.PLHotspot;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadTextObjTask extends AsyncTask<String, Integer, Boolean>{

    final String TAG = "LoadTextObjTask";
	private ArrayList<TextObj> mTextObjs;

    public final static String DEFAULT_FONT_PATH = "/tesseract/Vera.ttf";
    public final static String DEFAULT_JSON_PATH = "/tesseract/textobj.json";

    public LoadTextObjTask() {
    }
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		if (arg0 == null) return false;
		String jsonData = MyUtils.ReadFile(arg0[0]);
		if (jsonData.length() < 1) {
            if (CopyDefaultFont()) {
                MyUtils.WriteFile(MyUtils.SDCARD_PATH + DEFAULT_JSON_PATH, defaultJSON);
                jsonData = defaultJSON;
                Log.i(TAG, "Created default ttf and json file");
            }
            else return false;
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

    private boolean CopyDefaultFont() {
        Context context = ZapApp.getInstance().GetContext();
        if (context==null) return false;

        MyUtils.makeDirs(MyUtils.SDCARD_PATH + DEFAULT_FONT_PATH);
        InputStream inputStream = context.getResources().openRawResource(R.raw.vera);
        byte[] reader = null;
        try {
            reader = new byte[inputStream.available()];
            while (inputStream.read(reader) != -1) {}

            OutputStream outputStream = new FileOutputStream(MyUtils.SDCARD_PATH + DEFAULT_FONT_PATH);
            outputStream.write(reader);
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // This use package name to access res/raw/*.ttf
    final String defaultJSON =   "[\n" +
            "{\n" +
            "    \"fontpath\"=\"/tesseract/Vera.ttf\",\n" +
            "    \"text\"=\"Zappoint\",\n" +
            "    \"colorR\"=123,\n" +
            "    \"colorG\"=230,\n" +
            "    \"colorB\"=45,\n" +
            "    \"posX\"=1,\n" +
            "    \"posY\"=-5,\n" +
            "    \"posZ\"=-13,\n" +
            "    \"type\"=3\n" +
            "  },\n" +
            "  {\n" +
            "    \"fontpath\"=\"/tesseract/Vera.ttf\",\n" +
            "    \"text\"=\"Good\",\n" +
            "    \"colorR\"=200,\n" +
            "    \"colorG\"=200,\n" +
            "    \"colorB\"=0,\n" +
            "    \"posX\"=-10,\n" +
            "    \"posY\"=1,\n" +
            "    \"posZ\"=-12,\n" +
            "    \"type\"=3\n" +
            "  }" +
            " ]";
}
