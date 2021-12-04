#include "windowManager.h"

/*
jlong nMonitorFromWindow(jlong hwnd) {
	return (jlong)MonitorFromWindow((HWND)hwnd, MONITOR_DEFAULTTONEAREST);
}

jstring nGetMonitorName(JNIEnv* env, jlong hmonitor) {
	MONITORINFOEX info;
	info.cbSize = sizeof(info);
	GetMonitorInfo((HMONITOR)hmonitor, (LPMONITORINFO)&info);
	return env->NewStringUTF((char*)info.szDevice);
}

jlong nGetWindowExStyle(jlong hwnd) {
	return GetWindowLong((HWND)hwnd, GWL_EXSTYLE);
}

void nSetWindowExStyle(jlong hwnd, jlong exstyle) {
	SetWindowLong((HWND)hwnd, GWL_EXSTYLE, exstyle);
}

*/

void setWindowPosition(jlong hwnd, jint x, jint y) {
	SetWindowPos((HWND)hwnd, 0, x, y, 0, 0, SWP_NOSIZE | SWP_NOZORDER | SWP_NOACTIVATE);
}

int getWindowX(jlong hwnd) {

}

int getWindowY(jlong hwnd) {

}

void setWindowSize(jlong hwnd, jint width, jint height) {

}

int getWindowWidth(jlong hwnd) {

}

int getWindowHeight(jlong hwnd) {

}

void setWindowTitle(jlong hwnd, char* title) {

}

char* getWindowTitle(jlong hwnd) {

}