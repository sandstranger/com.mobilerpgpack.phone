LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := Translator

LOCAL_C_INCLUDES :=	$(LOCAL_PATH)/include

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)

LOCAL_SRC_FILES := \
	$(LOCAL_PATH)/src/Translator.cpp

LOCAL_CPPFLAGS += -O3 -flto=thin -std=c++20 -fexceptions -frtti
LOCAL_LDFLAGS += -flto=thin -Wl,-plugin-opt=-emulated-tls -fuse-ld=lld
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)
