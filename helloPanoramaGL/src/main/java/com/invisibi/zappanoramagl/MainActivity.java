/*
 * PanoramaGL library
 * Version 0.2 beta
 * Copyright (c) 2010 Javier Baez <javbaezga@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.invisibi.zappanoramagl;

import com.invisibi.testobj.PLHotspotCube;
import com.invisibi.testobj.PLHotspotText;
import com.panoramagl.PLBlankPanorama;
import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLCylindricalPanorama;
import com.panoramagl.PLICamera;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLIView;
import com.panoramagl.PLImage;
import com.panoramagl.PLSpherical2Panorama;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLView;
import com.panoramagl.PLViewListener;
import com.panoramagl.enumerations.PLCubeFaceOrientation;
import com.panoramagl.hotspots.PLIHotspot;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.loaders.PLILoader;
import com.panoramagl.loaders.PLJSONLoader;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.transitions.PLITransition;
import com.panoramagl.transitions.PLTransitionBlend;
import com.panoramagl.utils.PLUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;

public class MainActivity extends PLView
{
	CameraPreview mCameraPreview;
	final String TAG = "MainActivity";
	final int PANORAMA_INDEX = 100;
	int mCurrentSpinnerPos = -1;
	final int CAMERA_VIEW_POS = 0;// Need to match string array
	SurfaceView mSurfaceView;
	
	/**member variables*/
	
	private Spinner mPanoramaTypeSpinner;
	private ZoomControls mZoomControls;
	private PLHotspotText mHotspotText;
	/**init methods*/
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        this.setListener(new PLViewListener()
        {
        	@Override
    		public void onDidClickHotspot(PLIView view, PLIHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
        	{
        		Toast.makeText(view.getActivity().getApplicationContext(), String.format("You select the hotspot with ID %d", hotspot.getIdentifier()), Toast.LENGTH_SHORT).show();
        	}
        	
        	@Override
        	public void onDidBeginLoader(PLIView view, PLILoader loader)
        	{
        		setControlsEnabled(false);
        	}
        	
        	@Override
        	public void onDidCompleteLoader(PLIView view, PLILoader loader)
        	{
        		setControlsEnabled(true);
        		Log.i("JackTest", "onDidCompleteLoader");
        		PLIPanorama panorama = null;
        		panorama = MainActivity.this.getPanorama();
        		panorama.addHotspot(new PLHotspotCube(2, 0, 0));
        	}
        	
        	@Override
        	public void onDidErrorLoader(PLIView view, PLILoader loader, String error)
        	{
        		setControlsEnabled(true);
        	}
        	
        	@Override
        	public void onDidBeginTransition(PLIView view, PLITransition transition)
        	{
        		setControlsEnabled(false);
        	}
        	
        	@Override
        	public void onDidStopTransition(PLIView view, PLITransition transition, int progressPercentage)
        	{
        		setControlsEnabled(true);
        	}
        	
        	@Override
        	public void onDidEndTransition(PLIView view, PLITransition transition)
        	{
        		setControlsEnabled(true);
        	}
        	
        	@Override
        	public void onDidResetCamera(PLIView view, Object sender, PLICamera camera)
        	{
        		if (mCurrentSpinnerPos == 0) {
        			loadPanoramaFromJSON(mCurrentSpinnerPos);
        		}
        	}
		});
        ZapApp.getInstance().SetContext(this);
	}
	
	/**
     * This event is fired when root content view is created
     * @param contentView current root content view
     * @return root content view that Activity will use
     */
	@Override
	protected View onContentViewCreated(View contentView)
	{
		//Load layout
		ViewGroup mainView = (ViewGroup)this.getLayoutInflater().inflate(R.layout.activity_main, null);
		//Add 360 view
    	mainView.addView(contentView, 0);
        
    	HandlerThread handlerThread = new HandlerThread("PreviewHandler");
	    handlerThread.start();
        mCameraPreview = new CameraPreview(handlerThread, this);
        mSurfaceView = (SurfaceView) mainView.findViewById(R.id.camera_preview);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(mCameraPreview);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    	
    	//Spinner control
        mPanoramaTypeSpinner = (Spinner)mainView.findViewById(R.id.spinner_panorama_type);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.panorama_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPanoramaTypeSpinner.setAdapter(adapter);
        mPanoramaTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				mCurrentSpinnerPos = position;
				loadPanoramaFromJSON(position);
				if (position == CAMERA_VIEW_POS) {
					mSurfaceView.setVisibility(View.VISIBLE);
					mZoomControls.setVisibility(View.GONE);
				}else{
					mSurfaceView.setVisibility(View.GONE);
					mZoomControls.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		});
        //Zoom controls
        mZoomControls = (ZoomControls)mainView.findViewById(R.id.zoom_controls);
        mZoomControls.setOnZoomInClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View view)
			{
				PLICamera camera = getCamera();
				if(camera != null)
					camera.zoomIn(true);
			}
		});
        mZoomControls.setOnZoomOutClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View view)
			{
				PLICamera camera = getCamera();
				if(camera != null)
					camera.zoomOut(true);
			}
		});
    	//Return root content view
		return super.onContentViewCreated(mainView);
	}
	
	/**utility methods*/
	
	private void setControlsEnabled(final boolean isEnabled)
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if(mPanoramaTypeSpinner != null)
				{
					mPanoramaTypeSpinner.setEnabled(isEnabled);
					mZoomControls.setIsZoomInEnabled(isEnabled);
					mZoomControls.setIsZoomOutEnabled(isEnabled);
				}
			}
		});
	}
    
    /**load methods*/
    
    /**
     * Load panorama image manually
     * @param index Spinner position where 0 = cubic, 1 = spherical2, 2 = spherical, 3 = cylindrical
     */
	private void loadPanorama(int index)
    {
		try
		{
	    	Context context = this.getApplicationContext();
	    	PLIPanorama panorama = null;
	    	//Lock panoramic view
	    	this.setLocked(true);
	    	//Create panorama
	    	switch(index)
	    	{
	    		//Cubic panorama (supports up 1024x1024 image per face)
		    	case 0:
		    		PLCubicPanorama cubicPanorama = new PLCubicPanorama();
		        	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_f), false), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
		        	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_b), false), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
		        	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_l), false), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
		        	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_r), false), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
		        	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_u), false), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
		        	cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_d), false), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
		            panorama = cubicPanorama;
		    		break;
		    	//Spherical2 panorama (supports up 2048x1024 image)
		    	case 1:
		    		panorama = new PLSpherical2Panorama();
		            ((PLSpherical2Panorama)panorama).setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_s2), false));
		            break;
            	//Spherical panorama (supports up 1024x512 image)
		    	case 2:
		    		panorama = new PLSphericalPanorama();
		            ((PLSphericalPanorama)panorama).setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_s), false));
		    		break;
		    	//Cylindrical panorama (supports up 1024x1024 image)
		    	case 3:
		    		PLCylindricalPanorama cylindricalPanorama = new PLCylindricalPanorama();
		        	cylindricalPanorama.setHeight(3.0f);
		        	cylindricalPanorama.getCamera().setPitchRange(0.0f, 0.0f);
		        	cylindricalPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.quito1_s), false));
		            panorama = cylindricalPanorama;
		    		break;
		    		
		    	case PANORAMA_INDEX:
		    		PLBlankPanorama blankPanorama = new PLBlankPanorama();
		    		panorama = blankPanorama;
		    		break;
		    	default:
		    		Log.e(TAG,"Wrong index for loadPanorama : " + index);
		    		break;
	    	}
	    	if(panorama != null)
	    	{
	    		PLICamera pliCamera = panorama.getCamera();
	    		//Set camera rotation
	    		boolean retLookAt = pliCamera.lookAt(0.0f, 170.0f);
	    		if (retLookAt == false) {
	    			Log.e("JackTest","Failed to set pliCamera lookAt");
	    		}
	    		pliCamera.setInitialFov(30);
	    		pliCamera.setFov(30);
		        //Add a hotspot
		        //panorama.addHotspot(new PLHotspot(1, new PLImage(PLUtils.getBitmap(context, R.raw.hotspot), false), 0.0f, 170.0f, 0.05f, 0.05f));
	    		panorama.addHotspot(new PLHotspotCube(2, 0, 0));

                if (mHotspotText!=null)  mHotspotText.release();
                mHotspotText = new PLHotspotText();
                panorama.addHotspot(mHotspotText);
	    		startSensorialRotation();
	    		
	    		setResetEnabled( true );
        		setNumberOfTouchesForReset( 3 );
			    setShakeResetEnabled(true);
			    setShakeThreshold(1300);
	    		
		        //Reset view
		        this.reset();
		        //Load panorama
		        this.startTransition(new PLTransitionBlend(2.0f), panorama); //or use this.setPanorama(panorama);
	    	}
	        //Unlock panoramic view
	        this.setLocked(false);
		}
    	catch(Throwable e)
    	{
    		Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * Load panorama image using JSON protocol
     * @param index Spinner position where 0 = cubic, 1 = spherical2, 2 = spherical, 3 = cylindrical
     */
    private void loadPanoramaFromJSON(int index)
    {
		try
    	{
    		PLILoader loader = null;
    		switch(index)
    		{
    			case CAMERA_VIEW_POS:
    				loadPanorama(PANORAMA_INDEX);
    				break;
	    		case 1:
	    			loader = new PLJSONLoader("res://raw/json_cubic");
	    			break;
	    		case 2:
	    			loader = new PLJSONLoader("res://raw/json_spherical2");
	    			break;
	    		case 3:
	    			loader = new PLJSONLoader("res://raw/json_spherical");
	    			break;
	    		case 4:
	    			loader = new PLJSONLoader("res://raw/json_cylindrical");
	    			break;
	    		default:
	    			break;
    		}
    		if(loader != null) {
    			this.load(loader, true, new PLTransitionBlend(2.0f));
    		}
    	}
    	catch(Throwable e)
    	{
    		Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
    	}
    }

    @Override
    protected void onDestroy() {
        ZapApp.getInstance().SetContext(null);
        super.onDestroy();

        if (mHotspotText!=null)  mHotspotText.release();
    }
}