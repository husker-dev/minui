#include "popup_menu.h"

UINT showPopup(HMENU hmenu, int x, int y, HWND hwnd) {
	UINT result = TrackPopupMenu(hmenu, TPM_VERTICAL | TPM_RETURNCMD, x, y, 0, hwnd, nullptr);
	DestroyMenu((HMENU)hmenu);
	return result;
}

jlong nPopupCreateMenu() {
	return (jlong)CreatePopupMenu();
}

void nPopupAddString(jlong hmenu, jint id, jbyte* wideText) {
	AppendMenuW((HMENU)hmenu, MF_STRING, id, (LPCWSTR)wideText);
}

void nPopupAddSeparator(jlong hmenu) {
	AppendMenuW((HMENU)hmenu, MF_SEPARATOR, 0, nullptr);
}

void nPopupAddSubMenu(jlong hmenu, jbyte* wideText, jlong subMenu) {
	AppendMenuW((HMENU)hmenu, MF_POPUP | MF_STRING, (UINT_PTR)subMenu, (LPCWSTR)wideText);
}

jint nPopupShow(jlong hmenu, jint x, jint y) {
	// Create invisible window to do not block the main one
	WNDCLASS wc = { };
	wc.lpfnWndProc = DefWindowProc;
	wc.hInstance = GetModuleHandle(nullptr);
	wc.lpszClassName = L"TMP";
	RegisterClass(&wc);

	HWND hwnd = CreateWindowEx(
		0, L"TMP", L"TMP",
		WS_POPUP | WS_EX_NOACTIVATE,
		0, 0, 0, 0, nullptr, nullptr, GetModuleHandle(nullptr), nullptr);
	ShowWindow(hwnd, SW_SHOW);

	jint result = (jint)showPopup((HMENU)hmenu, x, y, (HWND)hwnd);
	DestroyWindow(hwnd);
	return result;
}

jint nPopupShowWnd(jlong hmenu, jint x, jint y, jlong hwnd) {
	return (jint)showPopup((HMENU)hmenu, x, y, (HWND)hwnd);
}

