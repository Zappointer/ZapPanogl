package com.invisibi.zappanoramagl;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class CameraPreview extends Handler implements SurfaceHolder.Callback{

	final String TAG = "CameraPreview";
	Camera mCamera;
	Activity mActivity;

	final int CAMERA_RELEASED = 0;
	final int CAMERA_OPENED = 1;
	
	int mCameraState = CAMERA_RELEASED;
	
	final int MSG_SURFACE_CREATED = 100;
	final int MSG_SURFACE_CHANGED = 101;
	final int MSG_SURFACE_DESTROYED = 102;
	
	
	
	CameraPreview(HandlerThread hthread, Activity activity) {
		super(hthread.getLooper());
		mCamera = null;
		mActivity = activity;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
	    	case MSG_SURFACE_CREATED:
	    		Log.i(TAG, "MSG_SURFACE_CREATED");
	    		if (mCameraState == CAMERA_OPENED) break;
	    		mCamera = Camera.open();
	    		
				try {
					mCamera.setPreviewDisplay((SurfaceHolder) msg.obj);
					SetDesiredCameraParameter();
					mCamera.startPreview();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				Log.d(TAG, "Camera opened");
				mCameraState = CAMERA_OPENED;
	    		break;
	    	case MSG_SURFACE_CHANGED:
	    		Log.i(TAG, String.format("MSG_SURFACE_CHANGED  %d:%d", msg.arg1, msg.arg2));
	    		SetDisplayOrientation();
	    		break;
	    	case MSG_SURFACE_DESTROYED:
	    		Log.i(TAG, "MSG_SURFACE_DESTROYED");
	    		if (mCamera == null || mCameraState != CAMERA_OPENED) break;
	    		
	    		mCamera.stopPreview();
	    		mCamera.release();
	    		mCamera = null;
	    		mCameraState = CAMERA_RELEASED;
	    		Log.d(TAG, "Camera released");
	    		break;
	    	default:
	    		break;
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		Message msg = obtainMessage(MSG_SURFACE_CHANGED, width, height, holder);
		sendMessage(msg);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Message msg = obtainMessage(MSG_SURFACE_CREATED, holder);
		sendMessage(msg);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		sendEmptyMessage(MSG_SURFACE_DESTROYED);
	}

	private void SetDisplayOrientation() {
		if (mActivity == null || mCamera == null) return;
		
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(CameraInfo.CAMERA_FACING_BACK, info);
		int rotation = mActivity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		mCamera.setDisplayOrientation((info.orientation - degrees + 360) % 360);
	}
	
	private void SetDesiredCameraParameter() {
		if (mCamera == null) return;
		
		Camera.Parameters parameters = mCamera.getParameters();
		
		parameters.setPreviewFpsRange(30000, 30000);
		
		mCamera.setParameters(parameters);
	}
}
