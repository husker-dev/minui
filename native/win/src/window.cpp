#include "window.h"

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

