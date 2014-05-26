/*
 * jni_textobj.cpp
 *
 *  Created on: 2014/5/19
 *      Author: jackf
 */

#include <vector>
#include "jni_TextObjManager.hpp"

//#include "logger.h"

std::vector<MyTextObj*> pMyTextObj;
#define LOG_TAG "jni_TextObjManager"

JNIEXPORT void JNICALL Java_com_invisibi_opengles_TextObjManager_nativeAddTextObj
	(JNIEnv* jenv, jobject obj, jstring jttfpath, jstring text, jfloatArray fParams)
{
	const char *nativeString = jenv->GetStringUTFChars(text, 0);
	const char *nativeTTFPath = jenv->GetStringUTFChars(jttfpath, 0);
	float* pfparams = (float*) jenv->GetFloatArrayElements(fParams, 0);

	LOG_INFO("AddTextObj %s - %s %.1f,%.1f,%.1f  %.1f,%.1f,%.1f",
			nativeTTFPath,
			nativeString,
			pfparams[0],pfparams[1],pfparams[2],
			pfparams[3],pfparams[4],pfparams[5]);

	MyTextObj* ptextObj = new MyTextObj(nativeString);
	ptextObj->OpenFont(nativeTTFPath);
	ptextObj->SetColor(pfparams[0],pfparams[1],pfparams[2]);
	ptextObj->SetPos(pfparams[3],pfparams[4],pfparams[5]);
	ptextObj->SetType((int)pfparams[6]);
	pMyTextObj.push_back(ptextObj);

	jenv->ReleaseStringUTFChars(text, nativeString);
	jenv->DeleteLocalRef(text);
	jenv->ReleaseStringUTFChars(jttfpath, nativeTTFPath);
	jenv->DeleteLocalRef(jttfpath);
}


JNIEXPORT int JNICALL Java_com_invisibi_opengles_TextObjManager_nativeDraw
	(JNIEnv* jenv, jobject obj, jint option)
{
    bool pause = option ? true : false;
    //LOG_INFO("nativeDraw pMyTestObj size=%d", pMyTextObj.size());
    for (unsigned int i = 0; i < pMyTextObj.size(); i++)
        {
        	pMyTextObj[i]->SetPause(pause);
        	pMyTextObj[i]->Draw(NULL);
        }

    return 0;
}

JNIEXPORT void JNICALL Java_com_invisibi_opengles_TextObjManager_nativeReleaseTextObj
	(JNIEnv* jenv, jobject obj)
{
    for (unsigned int i = 0; i < pMyTextObj.size(); i++)
        {
        	delete pMyTextObj[i];
        }
    pMyTextObj.clear();
}