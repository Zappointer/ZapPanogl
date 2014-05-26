package com.invisibi.testobj;

import com.invisibi.opengles.LoadTextObjTask;
import com.invisibi.opengles.MyUtils;
import com.invisibi.opengles.TextObj;
import com.invisibi.opengles.TextObjManager;
import com.panoramagl.PLIRenderer;
import com.panoramagl.hotspots.PLHotspot;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by jackf on 2014/5/23.
 */
public class PLHotspotText extends PLHotspot{

    private final String JSON_PATH = "/tesseract/textobj.json";

    public PLHotspotText(long identifier, float atv, float ath) {
        super(identifier, atv, ath);
    }

    @Override
    protected void initializeValues()
    {
        super.initializeValues();
        new LoadTextObjTask().execute(MyUtils.SDCARD_PATH + JSON_PATH);
    }

    @Override
    protected void internalRender(GL10 gl, PLIRenderer renderer)
    {
        super.internalRender(gl, renderer);

        TextObjManager.Draw();
    }

    public void release() {

        TextObjManager.Release();
    }
}
