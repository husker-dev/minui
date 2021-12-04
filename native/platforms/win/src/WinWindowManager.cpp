#include "WinWindowManager.h"


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

static jmethodID onClosingCallback;
static jmethodID onCloseCallback;
static jmethodID onResizeCallback;
static jmethodID onMoveCallback;

static std::map<HWND, WNDPROC> baseProcs;
static std::map<HWND, jweak> objects;

static LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
	switch (msg) {
	case WM_CLOSE:
	{
		if (!callbackBoolean(jvm, objects[hwnd], onClosingCallback))
			return 0;
		break;
	}
	case WM_DESTROY:
	{
		callback(jvm, objects[hwnd], onCloseCallback);
		SetWindowLongPtr((HWND)hwnd, GWLP_WNDPROC, (LONG_PTR)baseProcs[hwnd]);
		baseProcs.erase(baseProcs.find(hwnd));
		objects.erase(objects.find(hwnd));
		return 0;
	}
	case WM_SIZE:
	{
		callback(jvm, objects[hwnd], onResizeCallback);
		break;
	}
	case WM_MOVE:
	{
		callback(jvm, objects[hwnd], onMoveCallback);
		break;
	}
	}
	return CallWindowProc(baseProcs[hwnd], hwnd, msg, wParam, lParam);
}

void bind(JNIEnv* env, jlong hwnd, jobject _object) {
	auto object = getCallbackObject(env, _object);

	onClosingCallback = getCallbackMethod(env, _object, "onClosingCallback", "()Z");
	onCloseCallback = getCallbackMethod(env, _object, "onCloseCallback", "()V");
	onResizeCallback = getCallbackMethod(env, _object, "onResizeCallback", "()V");
	onMoveCallback = getCallbackMethod(env, _object, "onMoveCallback", "()V");

	baseProcs[(HWND)hwnd] = (WNDPROC)SetWindowLongPtr((HWND)hwnd, GWLP_WNDPROC, (LONG_PTR)&WndProc);
	objects[(HWND)hwnd] = object;
}

void setWindowPosition(jlong hwnd, jint x, jint y) {
	SetWindowPos((HWND)hwnd, 0, x, y, 0, 0, SWP_NOSIZE | SWP_NOZORDER | SWP_NOACTIVATE);
}

jint getWindowX(jlong hwnd) {
	RECT rect;
	GetWindowRect((HWND)hwnd, &rect);
	return rect.left;
}

jint getWindowY(jlong hwnd) {
	RECT rect;
	GetWindowRect((HWND)hwnd, &rect);
	return rect.top;
}

void setWindowSize(jlong hwnd, jint width, jint height) {
	SetWindowPos((HWND)hwnd, 0, 0, 0, width, height, SWP_NOMOVE | SWP_NOZORDER | SWP_NOACTIVATE);
}

jint getWindowWidth(jlong hwnd) {
	RECT rect;
	GetWindowRect((HWND)hwnd, &rect);
	return rect.right - rect.left;
}

jint getWindowHeight(jlong hwnd) {
	RECT rect;
	GetWindowRect((HWND)hwnd, &rect);
	return rect.bottom - rect.top;
}

void setWindowTitle(jlong hwnd, char* title) {
	SetWindowTextW((HWND)hwnd, (LPWSTR)title);
}

jbyte* getWindowTitle(jlong hwnd, int* length) {
	*length = GetWindowTextLengthW((HWND)hwnd) * 2;

	auto buffer = new char[*length];
	GetWindowTextW((HWND)hwnd, (LPWSTR)buffer, *length);
	return (jbyte*)buffer;
}

void setWindowVisibility(jlong hwnd, jboolean visible) {
	ShowWindow((HWND)hwnd, visible ? SW_SHOW : SW_HIDE);
	if (visible) {
		SetForegroundWindow((HWND)hwnd);
		SetFocus((HWND)hwnd);
	}
}

jboolean isWindowVisible(jlong hwnd) {
	return IsWindowVisible((HWND)hwnd);
}

void requestFocus(jlong hwnd) {
	if (GetActiveWindow() == (HWND)hwnd)
		return;
	HWND hCurWnd = GetForegroundWindow();
	DWORD dwMyID = GetCurrentThreadId();
	DWORD dwCurID = GetWindowThreadProcessId(hCurWnd, NULL);
	AttachThreadInput(dwCurID, dwMyID, TRUE);
	if (!isAlwaysOnTop(hwnd)) {
		SetWindowPos((HWND)hwnd, HWND_TOPMOST, 0, 0, 0, 0, SWP_NOSIZE | SWP_NOMOVE);
		SetWindowPos((HWND)hwnd, HWND_NOTOPMOST, 0, 0, 0, 0, SWP_SHOWWINDOW | SWP_NOSIZE | SWP_NOMOVE);
	}
	SetForegroundWindow((HWND)hwnd);
	SetFocus((HWND)hwnd);
	SetActiveWindow((HWND)hwnd);
	AttachThreadInput(dwCurID, dwMyID, FALSE);
}

jboolean isAlwaysOnTop(jlong hwnd) {
	return (GetWindowLong((HWND)hwnd, GWL_EXSTYLE) & WS_EX_TOPMOST) != 0;
}

void setAlwaysOnTop(jlong hwnd, jboolean value) {
	SetWindowPos((HWND)hwnd, HWND_TOPMOST, 0, 0, 0, 0, SWP_NOSIZE | SWP_NOMOVE);
}

void tryCloseWindow(jlong hwnd) {
	SendMessage((HWND)hwnd, WM_CLOSE, NULL, NULL);
}

void destroyWindow(jlong hwnd) {
	DestroyWindow((HWND)hwnd);
}
