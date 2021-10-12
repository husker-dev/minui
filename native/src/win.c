#include "win.h"
#include "windows.h"

JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_Win_getWindowExStyle(JNIEnv *env, jobject obj, jlong hwnd){
    return GetWindowLong(hwnd, GWL_EXSTYLE);
}

JNIEXPORT void JNICALL Java_com_husker_minui_natives_Win_setWindowExStyle(JNIEnv *env, jobject obj, jlong hwnd, jlong exstyle){
    SetWindowLong(hwnd, GWL_EXSTYLE, exstyle);
}

JNIEXPORT void JNICALL Java_com_husker_minui_natives_Win_updateWindow(JNIEnv *env, jobject obj, jlong hwnd){
    ShowWindow(hwnd, SW_HIDE);
    ShowWindow(hwnd, SW_SHOW);
}

