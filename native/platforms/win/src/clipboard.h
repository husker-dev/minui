#include <jni.h>
#include <windows.h>
#include <vector>
#include <iostream>
#include <stringapiset.h>
#include <cstring>

const char* cfNames[] = {
			"CF_TEXT",
			"CF_BITMAP",
			"CF_METAFILEPICT",
			"CF_SYLK",
			"CF_DIF",
			"CF_TIFF",
			"CF_OEMTEXT",
			"CF_DIB",
			"CF_PALETTE",
			"CF_PENDATA",
			"CF_RIFF",
			"CF_WAVE",
			"CF_UNICODETEXT",
			"CF_ENHMETAFILE",
			"CF_HDROP",
			"CF_LOCALE",
			"CF_DIBV5"
};

UINT getKeyId(const char* key);
const char* getKeyName(UINT i);

jobjectArray nClipboardGetKeys(JNIEnv*);
jbyteArray nClipboardGetData(JNIEnv*, jstring);
void nClipboardEmpty();
void nClipboardSetData(jbyte*, jbyte*, jint);

extern "C" {

	/*
		nGetClipboardKeys
	*/
	JNIEXPORT jobjectArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetClipboardKeys(JNIEnv* env, jobject) {
		return nClipboardGetKeys(env);
	}

	/*
		nGetClipboardKeys
	*/
	JNIEXPORT jbyteArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetClipboardData(JNIEnv* env, jobject, jstring jkey) {
		return nClipboardGetData(env, jkey);
	}

	/*
		nGetClipboardKeys
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nEmptyClipboard(JNIEnv* env, jobject) {
		nClipboardEmpty();
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nEmptyClipboard() {
		nClipboardEmpty();
	}

	/*
		nGetClipboardKeys
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nSetClipboardData(JNIEnv* env, jobject, jbyteArray key, jbyteArray data, jint length) {
		nClipboardSetData(env->GetByteArrayElements(key, nullptr), env->GetByteArrayElements(data, nullptr), length);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nSetClipboardData(jbyte* key, jbyte* data, jint length) {
		nClipboardSetData(key, data, length);
	}

}

