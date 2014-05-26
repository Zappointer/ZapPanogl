LOCAL_PATH := $(call my-dir)
FTGLES_LOCATION := $(LOCAL_PATH)/../../../../../../ftgles/Android/jni/Android.mk

include $(CLEAR_VARS)

LOCAL_MODULE    := zappano
LOCAL_CFLAGS    := -Wall
LOCAL_SRC_FILES := MyFont.cpp MyTextObj.cpp jni_TextObjManager.cpp
LOCAL_LDLIBS    := -llog -landroid -lEGL -lGLESv1_CM

LOCAL_SHARED_LIBRARIES := ftgles

include $(BUILD_SHARED_LIBRARY)

#include $(CLEAR_VARS)
LOCAL_PATH := $(call my-dir)
include $(FTGLES_LOCATION)
