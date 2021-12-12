#include <jni.h>
#include <windows.h>

jboolean nIsDarkTheme();

extern "C" {

	/*=========================
	*	nIsDarkTheme
	* =========================
	*/
	JNIEXPORT jboolean JNICALL Java_com_husker_minuicore_platform_win_WinPlatformKt_nIsDarkTheme(JNIEnv*, jobject) {
		return nIsDarkTheme();
	}

	JNIEXPORT jboolean JNICALL JavaCritical_com_husker_minuicore_platform_win_WinPlatformKt_nnIsDarkTheme() {
		return nIsDarkTheme();
	}


}