package com.invisibi.opengles;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

/**
 * Class containing some static utility methods.
 */
public class MyUtils {
	public interface AfterHTCSearch {
		public void AfterSearchAction(boolean result, String searchRstPath);
	}

	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

	private MyUtils() {
	};

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean PathAtLocal(String urlString) {
		if (urlString.startsWith("http")) {
			return false;
		} else {
			return true;
		}
	}

	public static String GetFileMainNameByTime() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MMdd-kk-mmss");
		return df.format(date);
	}

	public static String[] GetSeriesFileNameByTime(int nCnt) {
		String[] fList = new String[nCnt];
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ssZ");
		long GMTTime = date.getTime();
		for (int i = 0; i < nCnt; ++i) {

			fList[i] = df.format(date) + ".JPG";
			GMTTime += 1000;
			date.setTime(GMTTime);
		}

		return fList;
	}

	public static String[] GetSeriesFileNameByTime(String root, int nCnt) {
		String[] fList = new String[nCnt];
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ssZ");
		long GMTTime = date.getTime();
		for (int i = 0; i < nCnt; ++i) {

			fList[i] = root + df.format(date) + ".JPG";
			GMTTime += 1000;
			date.setTime(GMTTime);
		}

		return fList;
	}

	public static void makeDirs(String pathName) {
		File tempf = new File(pathName);
		File tempfdir = tempf.getParentFile();
		tempfdir.mkdirs();
	}

	public static void showToast(Context context, String msg) {
		Toast error = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		error.show();
	}

	public static String ReadFile(String filepath) {

		String ret = "";
		try {
			InputStream streamtext;
			streamtext = new FileInputStream(filepath);
			if (streamtext != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						streamtext);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				streamtext.close();
				bufferedReader.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("Jack", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("Jack", "Can not read file: " + e.toString());
		}

		return ret;
	}

	public static boolean WriteFile(String path, String data) {
		makeDirs(path);
		OutputStream streamtext;
		try {
			streamtext = new FileOutputStream(path);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					streamtext);
			outputStreamWriter.write(data);
			outputStreamWriter.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean WriteBitmapToFile(Bitmap bmp, String pathname) {
	  if (bmp == null || pathname == null) {	    
	    return false;
	  }
	  FileOutputStream out = null;
	  boolean ret = true;
	  try {
	         out = new FileOutputStream(pathname);
	         bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
	  } catch (Exception e) {
	      ret = false;
	      e.printStackTrace();
	  } finally {
	         try{
	           if (out != null)
	             out.close();
	         } catch(Throwable ignore) {}
	  }
	  return ret;
	}
	
	public static long GetFacebookOwnerId(JSONObject jobj) {
		long userId = 0;
		try {
			Object obj = jobj.get("ownerid");
			userId = Long.valueOf(obj.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("Jack", "Fail to get userID");
			// e.printStackTrace();
		}
		return userId;
	}

	public static Bitmap GetBitmapFromBase64(String base64Encoded) {
		if (base64Encoded == null)
			return null;
		byte[] rawbyte;
		rawbyte = Base64.decode(base64Encoded, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(rawbyte, 0, rawbyte.length);
	}

	public static boolean isValidDate(String input) {
		String formatString = "MM/dd/yyyy";

		SimpleDateFormat format = new SimpleDateFormat(formatString);
		format.setLenient(false);
		try {
			format.parse(input);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	public static String GetSearchResultJSONPath() {
		String SearchResultJSONPath = Environment.getExternalStorageDirectory()
				.toString() + "/PhotoPortal/Local/SearchResult.json";
		return SearchResultJSONPath;
	}

	public static String GetLocalJSONPath() {
		String SearchResultJSONPath = Environment.getExternalStorageDirectory()
				.toString() + "/PhotoPortal/Local/local.json";
		return SearchResultJSONPath;
	}

	public static String GetLocalImagePath() {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/DCIM/100MEDIA/";
		return path;
	}

	public static int GetMaxSerial(String prefixPath) {
		int maxSerial = -1;

		final int MAX_SERIAL = 999;
		for (int i = 0; i < MAX_SERIAL; ++i) {
			String path = String.format("%s%03d.json", prefixPath, i);
			File f = new File(path);
			if (f.exists() == false) {
				maxSerial = i;
				break;
			}
		}

		return maxSerial;
	}

	public static String GetAlbumSerialPath(String category) {
		String SearchResultJSONPath = "";
		String prefix = Environment.getExternalStorageDirectory().toString()
				+ "/PhotoPortal/Album/Album_" + category;
		int serial = GetMaxSerial(prefix);
		if (serial >= 0) {
			SearchResultJSONPath = String.format("%s%03d.json", prefix, serial);
		}

		return SearchResultJSONPath;
	}

	public static String GetAlbumPath(String name) {
		String SearchResultJSONPath = "";
		String prefix = Environment.getExternalStorageDirectory().toString()
				+ "/PhotoPortal/Album/Album_" + name;
		SearchResultJSONPath = String.format("%s.json", prefix);
		return SearchResultJSONPath;
	}

	public static ArrayList<String> GetFileList(String Foldername, String[] filter ) {
		// Foldername can be "/Pictures/"
		String targetFolder = SDCARD_PATH + Foldername;
		Log.d("Files", "Target Folder: " + targetFolder);
		ArrayList<String> ret = new ArrayList<String>();
		File f = new File(targetFolder);
		File[] files = f.listFiles();
		if (files != null) {
			Log.d("Files", "Size: " + files.length);
		} else {
			Log.d("Files", "GetFileList get null");
			return ret;
		}
		
		
		if (filter == null) {
		  for ( File file : files) {
		    ret.add(file.getPath());
		  }
		} else {
		  for ( File file : files) {
		    for ( String ext: filter ) {
		      if (file.getName().endsWith(ext)) {
		        ret.add(file.getPath());
		        break;
		      }
		    }
      }
		}
		return ret;
	}

    public static final void logThread(String title) {
        Thread t = Thread.currentThread();
        Log.d("MyUtils", title + 
                "  <" + t.getName() + ">id: " + t.getId() + ", Priority: "
                        + t.getPriority() + ", Group: "
                        + t.getThreadGroup().getName());
    }
	
    public static String StringArrayToString(String[] input) {
      if (input == null) return "";
      
      String ret = "";
      for( int i = 0; i< input.length; i++) {
        ret += input[i];
        if (i < input.length - 1) {
          ret += "\n";
        }
      }
      return ret;
    }
}
