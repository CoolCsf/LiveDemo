#include <jni.h>
#include <logger.h>
JavaVM *javaVm = nullptr;

extern "C"
JNIEXPORT void JNICALL
Java_com_coolcsf_livedemo_jni_LiveLibJni_sendCameraData(JNIEnv *env, jobject thiz,
                                                        jbyteArray byte_array) {
    int len = env->GetArrayLength(byte_array);
    auto *byte = new jbyte[len];
    // 拷贝出一份数据,这个方法会自动销毁内存，不用手动Release
    env->GetByteArrayRegion(byte_array, 0, len, byte);
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVm = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {

}