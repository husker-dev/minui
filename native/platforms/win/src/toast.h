#include <jni.h>
#include <windows.h>

#include <winrt/Windows.Data.Xml.Dom.h>
#include <winrt/Windows.UI.Notifications.h>
#include <iostream>
#include "DesktopNotificationManagerCompat.h"

void nToastInit(JNIEnv*, jobject, jbyteArray, jbyteArray, jbyteArray);
void nToastShow(jbyte*);
void nToastUninstall();
void nToastClearAll();

extern "C" {

	/*
		nToastInit
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nToastInit(JNIEnv* env, jobject obj, jbyteArray id, jbyteArray displayName, jbyteArray iconPath) {
		nToastInit(env, obj, id, displayName, iconPath);
	}

	/*
		nToastShow
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nToastShow(JNIEnv* env, jobject, jbyteArray xml) {
		nToastShow(env->GetByteArrayElements(xml, nullptr));
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nToastShow(jbyte* xml) {
		nToastShow(xml);
	}

	/*
		nToastUninstall
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nToastUninstall(JNIEnv* env, jobject) {
		nToastUninstall();
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nToastUninstall() {
		nToastUninstall();
	}

	/*
		nToastClearAll
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nToastClearAll(JNIEnv* env, jobject) {
		nToastClearAll();
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nToastClearAll() {
		nToastClearAll();
	}
}