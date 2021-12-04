#include "base.h"



jint nGetMousePositionX() {
	POINT p;
	GetCursorPos(&p);
	return p.x;
}

jint nGetMousePositionY() {
	POINT p;
	GetCursorPos(&p);
	return p.y;
}

jint nScreenToClientX(jlong hwnd, jint x) {
	POINT p{ x, 0 };
	ScreenToClient((HWND)hwnd, &p);
	return p.x;
}

jint nScreenToClientY(jlong hwnd, jint y) {
	POINT p{ 0, y };
	ScreenToClient((HWND)hwnd, &p);
	return p.y;
}

jlong nGetLCID(jbyte* localeBytes) {
	LCID lcid = 0;
	GetLocaleInfoEx((LPCWSTR)localeBytes, LOCALE_RETURN_NUMBER | LOCALE_ILANGUAGE, (LPWSTR)&lcid, sizeof(lcid));
	return (jlong)lcid;
}
