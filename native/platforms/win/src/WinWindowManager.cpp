#include "WinWindowManager.h"

const auto DWMWA_BORDER_COLOR = DWORD(34);
const auto DWMWA_CAPTION_COLOR = DWORD(35);
const auto DWMWA_TEXT_COLOR = DWORD(36);
const auto DWMWA_MICA_EFFECT = DWORD(1029);
const auto DWMWA_USE_IMMERSIVE_DARK_MODE = DWORD(20);

static jmethodID onClosingCallback;
static jmethodID onCloseCallback;
static jmethodID onResizeCallback;
static jmethodID onMoveCallback;

static std::map<HWND, WNDPROC> baseProcs;
static std::map<HWND, jweak> callbackObjects;

static std::map<HWND, POINT> minimumSizes;
static std::map<HWND, POINT> maximumSizes;

static std::map<HWND, DWORD*> titleColors;
static std::map<HWND, jint*> textColors;
static std::map<HWND, jint*> borderColors;

int to_int(char* str) {
	int res = 0;
	for (int i = 0; str[i] != '\0'; ++i)
		res = res * 10 + str[i] - '0';
	return res;
}

bool IsWindows11OrGreater() {
	char minorVersion[64];
	DWORD majorVersion = 0;

	DWORD size1 = (DWORD)(64 * sizeof(char));
	DWORD size2 = sizeof(DWORD);
	RegGetValueA(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "CurrentBuild", RRF_RT_REG_SZ, nullptr, &minorVersion, &size1);
	RegGetValueA(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "CurrentMajorVersionNumber", RRF_RT_REG_DWORD, nullptr, &majorVersion, &size2);

	return to_int(minorVersion) > 10 || majorVersion >= 22000;
}

void printExStyles(HWND hwnd) {
	auto style = GetWindowLongPtr(hwnd, GWL_EXSTYLE);

	std::cout << std::endl;
	if ((style & WS_EX_DLGMODALFRAME) != 0) std::cout << "WS_EX_DLGMODALFRAME" << std::endl;
	if ((style & WS_EX_NOPARENTNOTIFY) != 0) std::cout << "WS_EX_NOPARENTNOTIFY" << std::endl;
	if ((style & WS_EX_TOPMOST) != 0) std::cout << "WS_EX_TOPMOST" << std::endl;
	if ((style & WS_EX_ACCEPTFILES) != 0) std::cout << "WS_EX_ACCEPTFILES" << std::endl;
	if ((style & WS_EX_TRANSPARENT) != 0) std::cout << "WS_EX_TRANSPARENT" << std::endl;
	if ((style & WS_EX_MDICHILD) != 0) std::cout << "WS_EX_MDICHILD" << std::endl;
	if ((style & WS_EX_TOOLWINDOW) != 0) std::cout << "WS_EX_TOOLWINDOW" << std::endl;
	if ((style & WS_EX_WINDOWEDGE) != 0) std::cout << "WS_EX_WINDOWEDGE" << std::endl;
	if ((style & WS_EX_CLIENTEDGE) != 0) std::cout << "WS_EX_CLIENTEDGE" << std::endl;
	if ((style & WS_EX_CONTEXTHELP) != 0) std::cout << "WS_EX_CONTEXTHELP" << std::endl;
	if ((style & WS_EX_RIGHT) != 0) std::cout << "WS_EX_RIGHT" << std::endl;
	if ((style & WS_EX_LEFT) != 0) std::cout << "WS_EX_LEFT" << std::endl;
	if ((style & WS_EX_RTLREADING) != 0) std::cout << "WS_EX_RTLREADING" << std::endl;
	if ((style & WS_EX_LTRREADING) != 0) std::cout << "WS_EX_LTRREADING" << std::endl;
	if ((style & WS_EX_LEFTSCROLLBAR) != 0) std::cout << "WS_EX_LEFTSCROLLBAR" << std::endl;
	if ((style & WS_EX_RIGHTSCROLLBAR) != 0) std::cout << "WS_EX_RIGHTSCROLLBAR" << std::endl;
	if ((style & WS_EX_CONTROLPARENT) != 0) std::cout << "WS_EX_CONTROLPARENT" << std::endl;
	if ((style & WS_EX_STATICEDGE) != 0) std::cout << "WS_EX_STATICEDGE" << std::endl;
	if ((style & WS_EX_APPWINDOW) != 0) std::cout << "WS_EX_APPWINDOW" << std::endl;
	if ((style & WS_EX_LAYERED) != 0) std::cout << "WS_EX_LAYERED" << std::endl;
	if ((style & WS_EX_NOINHERITLAYOUT) != 0) std::cout << "WS_EX_NOINHERITLAYOUT" << std::endl;
	if ((style & WS_EX_NOREDIRECTIONBITMAP) != 0) std::cout << "WS_EX_NOREDIRECTIONBITMAP" << std::endl;
	if ((style & WS_EX_LAYOUTRTL) != 0) std::cout << "WS_EX_LAYOUTRTL" << std::endl;
	if ((style & WS_EX_COMPOSITED) != 0) std::cout << "WS_EX_COMPOSITED" << std::endl;
	if ((style & WS_EX_NOACTIVATE) != 0) std::cout << "WS_EX_NOACTIVATE" << std::endl;
}

int getResizeHandleHeight(HWND hwnd) {
	int dpi = GetDpiForWindow(hwnd);

	// there isn't a SM_CYPADDEDBORDER for the Y axis
	return GetSystemMetricsForDpi(SM_CXPADDEDBORDER, dpi)
		+ GetSystemMetricsForDpi(SM_CYSIZEFRAME, dpi);
}

bool hasAutohideTaskbar(UINT edge, RECT rcMonitor) {
	APPBARDATA data{ 0 };
	data.cbSize = sizeof(data);
	data.uEdge = edge;
	data.rc = rcMonitor;
	HWND hTaskbar = (HWND) ::SHAppBarMessage(ABM_GETAUTOHIDEBAREX, &data);
	return hTaskbar != nullptr;
}


void sendMessageToClientArea(HWND hwnd, int uMsg, LPARAM lParam) {
	// get mouse x/y in window coordinates
	//LRESULT xy = screen2windowCoordinates(hwnd, lParam);

	// send message
	::SendMessage(hwnd, uMsg, 0, lParam);
}

static LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
	switch (msg) {
	case WM_CLOSE:
	{
		if (!callbackBoolean(jvm, callbackObjects[hwnd], onClosingCallback))
			return 0;
		break;
	}
	case WM_DESTROY:
	{
		callback(jvm, callbackObjects[hwnd], onCloseCallback);
		SetWindowLongPtr((HWND)hwnd, GWLP_WNDPROC, (LONG_PTR)baseProcs[hwnd]);

		// Clear all maps
		baseProcs.erase(baseProcs.find(hwnd));
		callbackObjects.erase(callbackObjects.find(hwnd));

		return 0;
	}
	case WM_SIZE:
	{
		callback(jvm, callbackObjects[hwnd], onResizeCallback);
		break;
	}
	case WM_MOVE:
	{
		callback(jvm, callbackObjects[hwnd], onMoveCallback);
		break;
	}
	case WM_GETMINMAXINFO:
	{
		LPMINMAXINFO info = (LPMINMAXINFO)lParam;
		if (minimumSizes.find(hwnd) != minimumSizes.end())
			info->ptMinTrackSize = minimumSizes[hwnd];
		if (maximumSizes.find(hwnd) != maximumSizes.end())
			info->ptMaxTrackSize = maximumSizes[hwnd];
		return 0;
	}

	// Titless
	/*
	case WM_ERASEBKGND:
		return FALSE;

	case WM_NCHITTEST:
		return HTCAPTION;

	case WM_NCCALCSIZE:
	{
		return 0;
	}

	case WM_NCMOUSEMOVE:
		// if mouse is moved over some non-client areas,
		// send it also to the client area to allow Swing to process it
		// (required for Windows 11 maximize button)
		if (wParam == HTMINBUTTON || wParam == HTMAXBUTTON || wParam == HTCLOSE ||
			wParam == HTCAPTION || wParam == HTSYSMENU)
			sendMessageToClientArea(hwnd, WM_MOUSEMOVE, lParam);
		break;

	case WM_NCLBUTTONDOWN:
	case WM_NCLBUTTONUP:
		// if left mouse was pressed/released over minimize/maximize/close button,
		// send it also to the client area to allow Swing to process it
		// (required for Windows 11 maximize button)
		if (wParam == HTMINBUTTON || wParam == HTMAXBUTTON || wParam == HTCLOSE) {
			int uClientMsg = (msg == WM_NCLBUTTONDOWN) ? WM_LBUTTONDOWN : WM_LBUTTONUP;
			sendMessageToClientArea(hwnd, uClientMsg, lParam);
			return 0;
		}
		break;


	//case WM_NCHITTEST:
	//	return WmNcHitTest(hwnd, uMsg, wParam, lParam);
	//}

	*/
	
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
	callbackObjects[(HWND)hwnd] = object;
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
	if ((GetWindowLong((HWND)hwnd, GWL_EXSTYLE) & WS_EX_TOPMOST) == 0) {
		SetWindowPos((HWND)hwnd, HWND_TOPMOST, 0, 0, 0, 0, SWP_NOSIZE | SWP_NOMOVE);
		SetWindowPos((HWND)hwnd, HWND_NOTOPMOST, 0, 0, 0, 0, SWP_SHOWWINDOW | SWP_NOSIZE | SWP_NOMOVE);
	}
	SetForegroundWindow((HWND)hwnd);
	SetFocus((HWND)hwnd);
	SetActiveWindow((HWND)hwnd);
	AttachThreadInput(dwCurID, dwMyID, FALSE);
}

void tryCloseWindow(jlong hwnd) {
	SendMessage((HWND)hwnd, WM_CLOSE, NULL, NULL);
}

void destroyWindow(jlong hwnd) {
	DestroyWindow((HWND)hwnd);
}

// Do not modify this, please
void nUpdateExStyle(jlong hwnd, jboolean isShowTaskbar, jboolean isTopMost) {
	SetWindowPos((HWND)hwnd, isTopMost ? HWND_TOPMOST : HWND_NOTOPMOST, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOACTIVATE);

	long topMost = isTopMost ? WS_EX_TOPMOST : 0;
	long taskbar = isShowTaskbar ? WS_EX_APPWINDOW : WS_EX_NOACTIVATE;
	SetWindowLong((HWND)hwnd, GWL_EXSTYLE, topMost | taskbar);
	SetWindowPos((HWND)hwnd, 0, 0, 0, 0, 0, SWP_SHOWWINDOW | SWP_NOSIZE | SWP_NOMOVE | SWP_NOZORDER);
}

void nSetResizable(jlong hwnd, jboolean value) {
	auto style = GetWindowLong((HWND)hwnd, GWL_STYLE);

	if (value)
		SetWindowLong((HWND)hwnd, GWL_STYLE, style | WS_SIZEBOX | WS_MAXIMIZEBOX);
	else
		SetWindowLong((HWND)hwnd, GWL_STYLE, style ^ WS_SIZEBOX ^ WS_MAXIMIZEBOX);
}

jboolean nIsResizable(jlong hwnd) {
	return (GetWindowLong((HWND)hwnd, GWL_STYLE) & WS_SIZEBOX) != 0;
}

void nSetWindowColors(jlong hwnd, jint title, jint text, jint border, jboolean defTitle, jboolean defText, jboolean defBorder) {
	DwmSetWindowAttribute((HWND)hwnd, DWMWA_CAPTION_COLOR, defTitle ? 0 : &title, sizeof(COLORREF));
	DwmSetWindowAttribute((HWND)hwnd, DWMWA_TEXT_COLOR, defText ? 0 : &text, sizeof(COLORREF));
	DwmSetWindowAttribute((HWND)hwnd, DWMWA_BORDER_COLOR, defBorder ? 0 : &border, sizeof(COLORREF));
}

void applyWindowSetting(HWND hwnd, int darkMode, boolean mica) {
	int trueValue = 1;
	int falseValue = 0;
	int* darkModeValue = 0;
	if (darkMode == 1) darkModeValue = &trueValue;
	if (darkMode == 0) darkModeValue = &falseValue;


	DwmSetWindowAttribute(hwnd, DWMWA_USE_IMMERSIVE_DARK_MODE, darkModeValue, sizeof(int));
	if (IsWindows11OrGreater()) {
		DwmSetWindowAttribute(hwnd, DWMWA_MICA_EFFECT, mica ? &trueValue : &falseValue, sizeof(int));
	}
}

void nSetWindowStyleId(jlong hwnd, jint id) {
	if (id == 0) {			// Default
		applyWindowSetting((HWND)hwnd, 2, false);
	} else if (id == 1) {	// Titless
		applyWindowSetting((HWND)hwnd, 2, false);
	} else if (id == 2) {	// Borderless
		applyWindowSetting((HWND)hwnd, 2, false);
	} else if (id == 3) {	// Colorized
		applyWindowSetting((HWND)hwnd, 2, false);
	} else if (id == 4) {	// Dark
		applyWindowSetting((HWND)hwnd, 1, false);
	} else if (id == 5) {	// Light
		applyWindowSetting((HWND)hwnd, 0, false);
	} else if (id == 6) {	// Mica dark
		applyWindowSetting((HWND)hwnd, 1, true);
	} else if (id == 7) {	// Mica light
		applyWindowSetting((HWND)hwnd, 0, true);
	}
}

void nSetMinimumSize(jlong hwnd, jint width, jint height) {
	if (width == -1 && height == -1)
		minimumSizes.erase(minimumSizes.find((HWND)hwnd));
	else 
		minimumSizes[(HWND)hwnd] = POINT{ width, height };
}

void nSetMaximumSize(jlong hwnd, jint width, jint height) {
	if (width == -1 && height == -1)
		maximumSizes.erase(minimumSizes.find((HWND)hwnd));
	else
		maximumSizes[(HWND)hwnd] = POINT{ width, height };
}

jint nGetClientWidth(jlong hwnd) {
	RECT r = {};
	GetClientRect((HWND)hwnd, &r);
	return r.right - r.left;
}

jint nGetClientHeight(jlong hwnd) {
	RECT r = {};
	GetClientRect((HWND)hwnd, &r);
	return r.bottom - r.top;
}




/*
jboolean isAlwaysOnTop(jlong hwnd) {
	return (GetWindowLong((HWND)hwnd, GWL_EXSTYLE) & WS_EX_TOPMOST) != 0;
}


void setAlwaysOnTop(jlong hwnd, jboolean value) {
	auto style = GetWindowLongPtr((HWND)hwnd, GWL_EXSTYLE);

	SetWindowLong((HWND)hwnd, GWL_EXSTYLE, value ?
		style | WS_EX_TOPMOST :
		style ^ WS_EX_TOPMOST);
	SetWindowPos((HWND)hwnd, HWND_TOPMOST, 0, 0, 0, 0, SWP_NOSIZE | SWP_NOMOVE);
}


*/



/*
void nSetUndecorated(jlong hwnd, boolean value) {
	auto style = GetWindowLongPtr((HWND)hwnd, GWL_STYLE);

	SetWindowLong((HWND)hwnd, GWL_STYLE, value? 
		WS_POPUP | WS_SYSMENU | WS_MINIMIZEBOX | WS_MAXIMIZEBOX:
		WS_OVERLAPPEDWINDOW);
	SetWindowPos((HWND)hwnd, nullptr, 0, 0, 0, 0, SWP_FRAMECHANGED | SWP_NOMOVE | SWP_NOSIZE);
	ShowWindow((HWND)hwnd, SW_SHOW);
}

void nSetResizable(jlong hwnd, boolean value) {
	auto style = GetWindowLongPtr((HWND)hwnd, GWL_STYLE);

	SetWindowLong((HWND)hwnd, GWL_STYLE, value ? 
		style | WS_SIZEBOX | WS_MAXIMIZEBOX : 
		style ^ WS_SIZEBOX ^ WS_MAXIMIZEBOX);
	SetWindowPos((HWND)hwnd, nullptr, 0, 0, 0, 0, SWP_FRAMECHANGED | SWP_NOMOVE | SWP_NOSIZE);
	ShowWindow((HWND)hwnd, SW_SHOW);
}


void nSetShowTaskbarIcon(jlong hwnd, boolean value) {
	auto style = GetWindowLongPtr((HWND)hwnd, GWL_EXSTYLE);

	if ((style & WS_EX_DLGMODALFRAME) != 0) std::cout << "WS_EX_DLGMODALFRAME" << std::endl;
	if ((style & WS_EX_NOPARENTNOTIFY) != 0) std::cout << "WS_EX_NOPARENTNOTIFY" << std::endl;
	if ((style & WS_EX_TOPMOST) != 0) std::cout << "WS_EX_TOPMOST" << std::endl;
	if ((style & WS_EX_ACCEPTFILES) != 0) std::cout << "WS_EX_ACCEPTFILES" << std::endl;
	if ((style & WS_EX_TRANSPARENT) != 0) std::cout << "WS_EX_TRANSPARENT" << std::endl;
	if ((style & WS_EX_MDICHILD) != 0) std::cout << "WS_EX_MDICHILD" << std::endl;
	if ((style & WS_EX_TOOLWINDOW) != 0) std::cout << "WS_EX_TOOLWINDOW" << std::endl;
	if ((style & WS_EX_WINDOWEDGE) != 0) std::cout << "WS_EX_WINDOWEDGE" << std::endl;
	if ((style & WS_EX_CLIENTEDGE) != 0) std::cout << "WS_EX_CLIENTEDGE" << std::endl;
	if ((style & WS_EX_CONTEXTHELP) != 0) std::cout << "WS_EX_CONTEXTHELP" << std::endl;
	if ((style & WS_EX_RIGHT) != 0) std::cout << "WS_EX_RIGHT" << std::endl;
	if ((style & WS_EX_LEFT) != 0) std::cout << "WS_EX_LEFT" << std::endl;
	if ((style & WS_EX_RTLREADING) != 0) std::cout << "WS_EX_RTLREADING" << std::endl;
	if ((style & WS_EX_LTRREADING) != 0) std::cout << "WS_EX_LTRREADING" << std::endl;
	if ((style & WS_EX_LEFTSCROLLBAR) != 0) std::cout << "WS_EX_LEFTSCROLLBAR" << std::endl;
	if ((style & WS_EX_RIGHTSCROLLBAR) != 0) std::cout << "WS_EX_RIGHTSCROLLBAR" << std::endl;
	if ((style & WS_EX_CONTROLPARENT) != 0) std::cout << "WS_EX_CONTROLPARENT" << std::endl;
	if ((style & WS_EX_STATICEDGE) != 0) std::cout << "WS_EX_STATICEDGE" << std::endl;
	if ((style & WS_EX_APPWINDOW) != 0) std::cout << "WS_EX_APPWINDOW" << std::endl;
	if ((style & WS_EX_LAYERED) != 0) std::cout << "WS_EX_LAYERED" << std::endl;
	if ((style & WS_EX_NOINHERITLAYOUT) != 0) std::cout << "WS_EX_NOINHERITLAYOUT" << std::endl;
	if ((style & WS_EX_NOREDIRECTIONBITMAP) != 0) std::cout << "WS_EX_NOREDIRECTIONBITMAP" << std::endl;
	if ((style & WS_EX_LAYOUTRTL) != 0) std::cout << "WS_EX_LAYOUTRTL" << std::endl;
	if ((style & WS_EX_COMPOSITED) != 0) std::cout << "WS_EX_COMPOSITED" << std::endl;
	if ((style & WS_EX_NOACTIVATE) != 0) std::cout << "WS_EX_NOACTIVATE" << std::endl;

	jboolean lastState = nGetShowTaskbarIcon(hwnd);
	SetWindowLong((HWND)hwnd, GWL_EXSTYLE, value ?
		WS_EX_APPWINDOW | WS_EX_WINDOWEDGE :
		WS_EX_NOACTIVATE);
	SetWindowPos((HWND)hwnd, nullptr, 0, 0, 0, 0, SWP_FRAMECHANGED | SWP_NOMOVE | SWP_NOSIZE);
	if(!lastState && value)
		ShowWindow((HWND)hwnd, SW_HIDE);
	ShowWindow((HWND)hwnd, SW_SHOW);
}

jboolean nGetShowTaskbarIcon(jlong hwnd) {
	return (GetWindowLong((HWND)hwnd, GWL_EXSTYLE) & WS_EX_NOACTIVATE) != 0;
}
*/
