package com.invisibi.opengles;

/**
 * Created by jackf on 2014/5/26.
 */
public class TextObjManager {

    private static Object TObject = new Object();

    public static void AddTextObj(TextObj tobj) {
        float [] params = new float[7];
        params[0] = tobj.getColorR() / 255.f;
        params[1] = tobj.getColorG() / 255.f;
        params[2] = tobj.getColorB() / 255.f;
        params[3] = tobj.getPosX();
        params[4] = tobj.getPosY();
        params[5] = tobj.getPosZ();
        params[6] = tobj.getType();

        String fontPath = MyUtils.SDCARD_PATH + tobj.getFontPath();

        synchronized (TObject) {
            nativeAddTextObj(fontPath, tobj.getText(), params);
        }
    }

    public static int Draw() {
        int option = 0; //0: Not Pause
        int ret;
        synchronized (TObject) {
            ret = nativeDraw(option);
        }
        return ret;
    }

    public static void Release() {
        synchronized (TObject) {
            nativeReleaseTextObj();
        }
        return;
    }

    public static native void nativeAddTextObj(String fontPath, String text, float[] params);
    private static native int nativeDraw(int option);
    private static native void nativeReleaseTextObj();

    static {
        System.loadLibrary("zappano");
    }

}
