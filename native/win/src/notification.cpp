#include "notification.h"

void nShowNotification(JNIEnv* env, jlong hwnd) {

	NOTIFYICONDATA NID;

	memset(&NID, 0, sizeof(NID));

	//on main function:
	NID.cbSize = sizeof(NID);
	NID.hIcon = 0;

	NID.hWnd = (HWND)hwnd;
	NID.uID = WM_USER + 2;

	NID.szTip[0] = '2';
	//StrCpyW(NID.szTip, L"System Tray Icon: Hello World");
	//in a timer:

	NID.uFlags = NID.uFlags | NIF_ICON | NIF_TIP;
	Shell_NotifyIcon(NIM_ADD, &NID);


}
