#include <jni.h>
#include <windows.h>


extern "C"{
    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_platform_Win_nGetWindowExStyle(JNIEnv *, jobject, jlong hwnd) {
        return GetWindowLong(reinterpret_cast<HWND>(hwnd), GWL_EXSTYLE);
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_platform_Win_nSetWindowExStyle(JNIEnv *, jobject, jlong hwnd, jlong exstyle) {
        SetWindowLong(reinterpret_cast<HWND>(hwnd), GWL_EXSTYLE, exstyle);
    }
}
