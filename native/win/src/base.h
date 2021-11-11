
#include <jni.h>
#include <windows.h>
#include <vector>
#include <stringapiset.h>

jbyteArray nWideTextToMultiByte(JNIEnv*, jbyteArray);
jbyteArray nMultiByteToWideText(JNIEnv*, jbyteArray, jint);
jint nGetMousePositionX();
jint nGetMousePositionY();
jint nScreenToClientX(jlong, jint);
jint nScreenToClientY(jlong, jint);
jlong nGetLCID(jbyte*);

extern "C" {

	/*
		nWideTextToMultiByte
	*/
	JNIEXPORT jbyteArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nWideTextToMultiByte(JNIEnv* env, jobject, jbyteArray data) {
		return nWideTextToMultiByte(env, data);
	}

	/*
		nMultiByteToWideText
	*/
	JNIEXPORT jbyteArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nMultiByteToWideText(JNIEnv* env, jobject, jbyteArray data, jint length) {
		return nMultiByteToWideText(env, data, length);
	}

	/*
		nGetMousePositionX
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetMousePositionX(JNIEnv*, jobject) {
		return nGetMousePositionX();
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nGetMousePositionX() {
		return nGetMousePositionX();
	}

	/*
		nGetMousePositionY
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetMousePositionY(JNIEnv*, jobject) {
		return nGetMousePositionY();
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nGetMousePositionY() {
		return nGetMousePositionY();
	}

	/*
		nScreenToClientX
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nScreenToClientX(JNIEnv*, jobject, jlong hwnd, jint x) {
		return nScreenToClientX(hwnd, x);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nScreenToClientX(jlong hwnd, jint x) {
		return nScreenToClientX(hwnd, x);
	}

	/*
		nScreenToClientY
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nScreenToClientY(JNIEnv*, jobject, jlong hwnd, jint y) {
		return nScreenToClientY(hwnd, y);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nScreenToClientY(jlong hwnd, jint y) {
		return nScreenToClientY(hwnd, y);
	}

	/*
		nGetLCID
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetLCID(JNIEnv* env, jobject, jbyteArray localeBytes) {
		return nGetLCID(env->GetByteArrayElements(localeBytes, nullptr));
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nGetLCID(jbyte* localeBytes) {
		return nGetLCID(localeBytes);
	}
}
