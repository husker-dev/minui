#include <jni.h>
#include <windows.h>
#include <iostream>
#include <map>
#include "Callbacks.h"

static JavaVM* jvm;

void bind(JNIEnv*, jlong, jobject);
void setWindowPosition(jlong, jint, jint);
jint getWindowX(jlong);
jint getWindowY(jlong);
void setWindowSize(jlong, jint, jint);
jint getWindowWidth(jlong);
jint getWindowHeight(jlong);
void setWindowTitle(jlong, char*);
jbyte* getWindowTitle(jlong, int*);
void setWindowVisibility(jlong, jboolean);
jboolean isWindowVisible(jlong);
void requestFocus(jlong);
void tryCloseWindow(jlong);
void destroyWindow(jlong);
void nUpdateExStyle(jlong, jboolean, jboolean);
void nSetResizable(jlong, jboolean);
jboolean nIsResizable(jlong);
void nSetWindowStyleId(jlong, jint);
void nSetMinimumSize(jlong, jint, jint);
void nSetMaximumSize(jlong, jint, jint);

extern "C" {

	/*=========================
	*	nInstall
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManagerKt_nInstall(JNIEnv* env, jobject, jlong hwnd, jobject callback) {
		env->GetJavaVM(&jvm);
	}

	/*=========================
	*	nBind
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nBind(JNIEnv* env, jobject, jlong hwnd, jobject callback) {
		bind(env, hwnd, callback);
	}

	/*=========================
	*	nSetWindowPosition
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowPosition(JNIEnv*, jobject, jlong hwnd, jint x, jint y) {
		setWindowPosition(hwnd, x, y);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowPosition(jlong hwnd, jint x, jint y) {
		setWindowPosition(hwnd, x, y);
	}

	/*=========================
	*	nGetWindowX
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowX(JNIEnv*, jobject, jlong hwnd) {
		return getWindowX(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowX(jlong hwnd) {
		return getWindowX(hwnd);
	}

	/*=========================
	*	nGetWindowY
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowY(JNIEnv*, jobject, jlong hwnd) {
		return getWindowY(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowY(jlong hwnd) {
		return getWindowY(hwnd);
	}

	/*=========================
	*	nSetWindowSize
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowSize(JNIEnv*, jobject, jlong hwnd, jint width, jint height) {
		setWindowSize(hwnd, width, height);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowSize(jlong hwnd, jint width, jint height) {
		setWindowSize(hwnd, width, height);
	}

	/*=========================
	*	nGetWindowWidth
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowWidth(JNIEnv*, jobject, jlong hwnd) {
		return getWindowWidth(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowWidth(jlong hwnd) {
		return getWindowWidth(hwnd);
	}

	/*=========================
	*	nGetWindowHeight
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowHeight(JNIEnv*, jobject, jlong hwnd) {
		return getWindowHeight(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowHeight(jlong hwnd) {
		return getWindowHeight(hwnd);
	}

	/*=========================
	*	nSetWindowTitle
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowTitle(JNIEnv* env, jobject, jlong hwnd, jbyteArray bytes) {
		setWindowTitle(hwnd, (char*)env->GetByteArrayElements(bytes, nullptr));
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowTitle(jlong hwnd, jbyte* bytes) {
		setWindowTitle(hwnd, (char*)bytes);
	}

	/*=========================
	*	nGetWindowTitle
	* =========================
	*/
	JNIEXPORT jbyteArray JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowTitle(JNIEnv* env, jobject, jlong hwnd) {
		int length = 0;
		auto title = getWindowTitle(hwnd, &length);

		auto byteArray = env->NewByteArray(length);
		env->SetByteArrayRegion(byteArray, 0, length, title);
		return byteArray;
	}

	/*=========================
	*	nSetWindowVisibility
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowVisibility(JNIEnv* env, jobject, jlong hwnd, jboolean visible) {
		setWindowVisibility(hwnd, visible);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowVisibility(jlong hwnd, jboolean visible) {
		setWindowVisibility(hwnd, visible);
	}

	/*=========================
	*	nIsWindowVisible
	* =========================
	*/
	JNIEXPORT jboolean JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nIsWindowVisible(JNIEnv* env, jobject, jlong hwnd) {
		return isWindowVisible(hwnd);
	}

	JNIEXPORT jboolean JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nIsWindowVisible(jlong hwnd) {
		return isWindowVisible(hwnd);
	}

	/*=========================
	*	nRequestFocus
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nRequestFocus(JNIEnv* env, jobject, jlong hwnd) {
		requestFocus(hwnd);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nRequestFocus(jlong hwnd) {
		requestFocus(hwnd);
	}

	/*=========================
	*	nTryCloseWindow
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nTryCloseWindow(JNIEnv* env, jobject, jlong hwnd) {
		tryCloseWindow(hwnd);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nTryCloseWindow(jlong hwnd) {
		tryCloseWindow(hwnd);
	}

	/*=========================
	*	nDestroyWindow
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nDestroyWindow(JNIEnv* env, jobject, jlong hwnd) {
		destroyWindow(hwnd);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nDestroyWindow(jlong hwnd) {
		destroyWindow(hwnd);
	}

	/*=========================
	*	nUpdateExStyle
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nUpdateExStyle(JNIEnv* env, jobject, jlong hwnd, jboolean taskbar, jboolean topMost) {
		nUpdateExStyle(hwnd, taskbar, topMost);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nUpdateExStyle(jlong hwnd, jboolean taskbar, jboolean topMost) {
		nUpdateExStyle(hwnd, taskbar, topMost);
	}

	/*=========================
	*	nSetResizable
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetResizable(JNIEnv* env, jobject, jlong hwnd, jboolean value) {
		nSetResizable(hwnd, value);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetResizable(jlong hwnd, jboolean value) {
		nSetResizable(hwnd, value);
	}

	/*=========================
	*	nIsResizable
	* =========================
	*/
	JNIEXPORT jboolean JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nIsResizable(JNIEnv* env, jobject, jlong hwnd) {
		return nIsResizable(hwnd);
	}

	JNIEXPORT jboolean JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nIsResizable(jlong hwnd) {
		return nIsResizable(hwnd);
	}

	/*=========================
	*	nSetWindowStyleId
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowStyleId(JNIEnv* env, jobject, jlong hwnd, jint id) {
		nSetWindowStyleId(hwnd, id);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowStyleId(jlong hwnd, jint id) {
		nSetWindowStyleId(hwnd, id);
	}

	/*=========================
	*	nSetMinimumSize
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetMinimumSize(JNIEnv* env, jobject, jlong hwnd, jint width, jint height) {
		nSetMinimumSize(hwnd, width, height);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetMinimumSize(jlong hwnd, jint width, jint height) {
		nSetMinimumSize(hwnd, width, height);
	}

	/*=========================
	*	nSetMaximumSize
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetMaximumSize(JNIEnv* env, jobject, jlong hwnd, jint width, jint height) {
		nSetMaximumSize(hwnd, width, height);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetMaximumSize(jlong hwnd, jint width, jint height) {
		nSetMaximumSize(hwnd, width, height);
	}
}
