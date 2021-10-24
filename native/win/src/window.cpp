#include <jni.h>
#include <windows.h>
#include <iostream>

extern "C"{

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_platform_Win_nMonitorFromWindow(JNIEnv *, jobject, jlong hwnd) {
        return (jlong) MonitorFromWindow(reinterpret_cast<HWND>(hwnd), MONITOR_DEFAULTTONEAREST);
    }

    JNIEXPORT jstring JNICALL Java_com_husker_minui_natives_platform_Win_nGetMonitorName(JNIEnv *env, jobject, jlong hmonitor) {
        MONITORINFOEX info;
        info.cbSize = sizeof(info);
        GetMonitorInfo((HMONITOR)hmonitor, (LPMONITORINFO)&info);
        return env->NewStringUTF(info.szDevice);
    }

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_platform_Win_nGetWindowExStyle(JNIEnv *, jobject, jlong hwnd) {
        return GetWindowLong(reinterpret_cast<HWND>(hwnd), GWL_EXSTYLE);
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_platform_Win_nSetWindowExStyle(JNIEnv *, jobject, jlong hwnd, jlong exstyle) {
        SetWindowLong(reinterpret_cast<HWND>(hwnd), GWL_EXSTYLE, exstyle);
    }
}
