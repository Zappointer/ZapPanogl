/*
 * jni_textobj.hpp
 *
 *  Created on: 2014/5/19
 *      Author: jackf
 */

#ifndef JNI_TEXTOBJ_HPP_
#define JNI_TEXTOBJ_HPP_

#include <jni.h>
#include "MyTextObj.hpp"


extern "C" {
JNIEXPORT void JNICALL Java_com_invisibi_opengles_TextObjManager_nativeAddTextObj
	(JNIEnv* jenv, jobject obj, jstring jttfpath, jstring text, jfloatArray fParams);

JNIEXPORT int  JNICALL Java_com_invisibi_opengles_TextObjManager_nativeDraw
	(JNIEnv* jenv, jobject obj, jint option);

JNIEXPORT void JNICALL Java_com_invisibi_opengles_TextObjManager_nativeReleaseTextObj
	(JNIEnv* jenv, jobject obj);

};

#endif /* JNI_TEXTOBJ_HPP_ */
