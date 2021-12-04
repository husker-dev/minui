#include <jni.h>
#include <windows.h>
#include <iostream>
#include <map>
#include "Callbacks.h"

static JavaVM* jvm;

void bind(JNIEnv*, jlong, jobject);
void unbind(HWND hwnd);
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
jboolean isAlwaysOnTop(jlong);
void setAlwaysOnTop(jlong, jboolean);
void tryCloseWindow(jlong);
void destroyWindow(jlong);

extern "C" {

	/*=========================
	*	install
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
	*	setWindowPosition
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowPosition(JNIEnv*, jobject, jlong hwnd, jint x, jint y) {
		setWindowPosition(hwnd, x, y);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowPosition(jlong hwnd, jint x, jint y) {
		setWindowPosition(hwnd, x, y);
	}

	/*=========================
	*	getWindowX
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowX(JNIEnv*, jobject, jlong hwnd) {
		return getWindowX(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowX(jlong hwnd) {
		return getWindowX(hwnd);
	}

	/*=========================
	*	getWindowY
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowY(JNIEnv*, jobject, jlong hwnd) {
		return getWindowY(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowY(jlong hwnd) {
		return getWindowY(hwnd);
	}

	/*=========================
	*	setWindowSize
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowSize(JNIEnv*, jobject, jlong hwnd, jint width, jint height) {
		setWindowSize(hwnd, width, height);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowSize(jlong hwnd, jint width, jint height) {
		setWindowSize(hwnd, width, height);
	}

	/*=========================
	*	getWindowWidth
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowWidth(JNIEnv*, jobject, jlong hwnd) {
		return getWindowWidth(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowWidth(jlong hwnd) {
		return getWindowWidth(hwnd);
	}

	/*=========================
	*	getWindowHeight
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowHeight(JNIEnv*, jobject, jlong hwnd) {
		return getWindowHeight(hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nGetWindowHeight(jlong hwnd) {
		return getWindowHeight(hwnd);
	}

	/*=========================
	*	setWindowTitle
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowTitle(JNIEnv* env, jobject, jlong hwnd, jbyteArray bytes) {
		setWindowTitle(hwnd, (char*)env->GetByteArrayElements(bytes, nullptr));
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowTitle(jlong hwnd, jbyte* bytes) {
		setWindowTitle(hwnd, (char*)bytes);
	}

	/*=========================
	*	getWindowTitle
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
	*	setWindowVisibility
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowVisibility(JNIEnv* env, jobject, jlong hwnd, jboolean visible) {
		setWindowVisibility(hwnd, visible);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetWindowVisibility(jlong hwnd, jboolean visible) {
		setWindowVisibility(hwnd, visible);
	}

	/*=========================
	*	isWindowVisible
	* =========================
	*/
	JNIEXPORT jboolean JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nIsWindowVisible(JNIEnv* env, jobject, jlong hwnd) {
		return isWindowVisible(hwnd);
	}

	JNIEXPORT jboolean JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nIsWindowVisible(jlong hwnd) {
		return isWindowVisible(hwnd);
	}

	/*=========================
	*	requestFocus
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nRequestFocus(JNIEnv* env, jobject, jlong hwnd) {
		requestFocus(hwnd);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nRequestFocus(jlong hwnd) {
		requestFocus(hwnd);
	}

	/*=========================
	*	isAlwaysOnTop
	* =========================
	*/
	JNIEXPORT jboolean JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nIsAlwaysOnTop(JNIEnv* env, jobject, jlong hwnd) {
		return isAlwaysOnTop(hwnd);
	}

	JNIEXPORT jboolean JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nIsAlwaysOnTop(jlong hwnd) {
		return isAlwaysOnTop(hwnd);
	}

	/*=========================
	*	setAlwaysOnTop
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_nSetAlwaysOnTop(JNIEnv* env, jobject, jlong hwnd, jboolean value) {
		setAlwaysOnTop(hwnd, value);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_nSetAlwaysOnTop(jlong hwnd, jboolean value) {
		setAlwaysOnTop(hwnd, value);
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
}
