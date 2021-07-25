//
// Created by coolcsf on 7/25/21.
//
#include <android/log.h>

#ifndef LIVEDEMO_LOGUTILS_H
#define LIVEDEMO_LOGUTILS_H

#define LOGI(TAG, ...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define LOGD(TAG, ...) __android_log_print(ANDROID_LOG_DEBUG,TAG, __VA_ARGS__)
#define LOGE(TAG, ...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

#endif //LIVEDEMO_LOGUTILS_H

