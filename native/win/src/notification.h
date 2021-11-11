#include <jni.h>
#include <windows.h>
#include <shlwapi.h>

void nShowNotification(JNIEnv*, jlong);

extern "C" {

	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nShowNotification(JNIEnv* env, jobject, jlong hwnd) {
		nShowNotification(env, hwnd);
	}
}
