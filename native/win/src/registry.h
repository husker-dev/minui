#include <jni.h>
#include <windows.h>
#include <iostream>
#include <string>

jobjectArray nRegistryGetMap(JNIEnv*, jlong, jstring);

extern "C" {

	JNIEXPORT jobjectArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nRegistryGetMap(JNIEnv* env, jobject, jlong hkey, jstring path) {
		return nRegistryGetMap(env, hkey, path);
	}

}