package com.example.testobj;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.panoramagl.PLIRenderer;
import com.panoramagl.hotspots.PLHotspot;

public class PLHotspotCube extends PLHotspot{

	private Cube mCube = new Cube();
	private float mAngle;
	private float mAngleWorld;
	
	public PLHotspotCube(long identifier, float atv, float ath)
	{
		super(identifier, atv, ath);
	}
		
	private void TranslateRotate(GL10 gl) {
		// Start position
		//float Tx = 4.f, Ty = 10.f, Tz = -10.f;
        float Tx = 0.f, Ty = 0.f, Tz = -10.f;

        gl.glRotatef(mAngleWorld, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(Tx, Ty, Tz);
		gl.glRotatef(mAngle, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(mAngle, 1.0f, 0.0f, 0.0f);
	}
	
	@Override
	protected void internalRender(GL10 gl, PLIRenderer renderer)
	{
		super.internalRender(gl, renderer);
		if (mCube == null) mCube = new Cube();
		gl.glPushMatrix();
        //gl.glMatrixMode(GL11.GL_MODELVIEW);
        //gl.glLoadIdentity();
        //gl.glTranslatef(0.0f,(float)Math.sin(mTransY), -7.0f + (float)Math.sin(mTransY));
		TranslateRotate(gl);
        
		gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL11.GL_COLOR_ARRAY);
        gl.glEnable(GL11.GL_CULL_FACE);
		mCube.draw(gl);
		gl.glDisable(GL11.GL_CULL_FACE);
		gl.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL11.GL_COLOR_ARRAY);
                
        mAngle +=.4;
        mAngleWorld += 0.1;
		gl.glPopMatrix();
	}
}
