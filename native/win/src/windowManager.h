#include <jni.h>
#include <windows.h>

void setWindowPosition(jlong, jint, jint);

extern "C" {

	/*=========================
	*	setWindowPosition
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_platform_win_WinWindowManager_setWindowPosition(JNIEnv*, jobject, jlong hwnd, jint x, jint y) {
		setWindowPosition(hwnd, x, y);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_platform_win_WinWindowManager_setWindowPosition(jlong hwnd, jint x, jint y) {
		setWindowPosition(hwnd, x, y);
	}
}
