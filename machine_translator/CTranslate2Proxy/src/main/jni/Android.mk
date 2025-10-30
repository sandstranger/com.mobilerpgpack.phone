LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := CTranslate2Proxy

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)

LOCAL_SRC_FILES := \
	$(LOCAL_PATH)/src/OpusMtTranslator.cpp \
	$(LOCAL_PATH)/src/M2M100Translator.cpp \
	$(LOCAL_PATH)/src/Small100Translator.cpp \
	$(LOCAL_PATH)/src/NLLB200Translator.cpp \
	$(LOCAL_PATH)/src/Utils.cpp

LOCAL_CPPFLAGS += -O3 -flto=thin -std=c++20 -fexceptions -frtti
LOCAL_LDFLAGS += -flto=thin -Wl,-plugin-opt=-emulated-tls -fuse-ld=lld
LOCAL_LDLIBS += -llog
LOCAL_SHARED_LIBRARIES += ctranslate2
LOCAL_SHARED_LIBRARIES += sentencepiece
include $(BUILD_SHARED_LIBRARY)

ifneq ($(call ndk-major-at-least,21),true)
    $(call import-add-path,$(NDK_GRADLE_INJECTED_IMPORT_PATH))
endif
$(call import-module,prefab/CTranslate2)
$(call import-module,prefab/sentencepiece)