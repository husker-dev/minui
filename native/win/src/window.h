#include <jni.h>
#include <windows.h>

jlong nMonitorFromWindow(jlong);
jstring nGetMonitorName(JNIEnv*, jlong);
jlong nGetWindowExStyle(jlong);
void nSetWindowExStyle(jlong hwnd, jlong exstyle);

extern "C" {

	/*
		nMonitorFromWindow
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_win_Win_nMonitorFromWindow(JNIEnv*, jobject, jlong hwnd) {
		return nMonitorFromWindow(hwnd);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nMonitorFromWindow(jlong hwnd) {
		return nMonitorFromWindow(hwnd);
	}

	/*
		nGetMonitorName
	*/
	JNIEXPORT jstring JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetMonitorName(JNIEnv* env, jobject, jlong hmonitor) {
		return nGetMonitorName(env, hmonitor);
	}

	/*
		nGetWindowExStyle
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetWindowExStyle(JNIEnv*, jobject, jlong hwnd) {
		return nGetWindowExStyle(hwnd);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nGetWindowExStyle(jlong hwnd) {
		return nGetWindowExStyle(hwnd);
	}

	/*
		nSetWindowExStyle
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nSetWindowExStyle(JNIEnv*, jobject, jlong hwnd, jlong exstyle) {
		nSetWindowExStyle(hwnd, exstyle);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nSetWindowExStyle(jlong hwnd, jlong exstyle) {
		nSetWindowExStyle(hwnd, exstyle);
	}
}
