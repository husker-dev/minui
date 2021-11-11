#include "base.h"

jbyteArray nWideTextToMultiByte(JNIEnv* env, jbyteArray data) {
	auto source = env->GetByteArrayElements(data, nullptr);

	const int utf8_length = WideCharToMultiByte(CP_UTF8, 0, (LPCWCH)source, -1, nullptr, 0, nullptr, nullptr);

	char* utf8 = new char[utf8_length];
	WideCharToMultiByte(CP_UTF8, 0, (LPCWCH)source, -1, utf8, utf8_length, nullptr, nullptr);

	auto jkeys = env->NewByteArray((long)_msize(utf8) - 1);
	env->SetByteArrayRegion(jkeys, 0, (long)_msize(utf8) - 1, (jbyte*)utf8);
	delete[] utf8;
	return jkeys;
}

jbyteArray nMultiByteToWideText(JNIEnv* env, jbyteArray data, jint length) {
	auto source = env->GetByteArrayElements(data, nullptr);

	auto* wstr = new wchar_t[length];
	MultiByteToWideChar(CP_UTF8, 0, (LPCCH)source, -1, wstr, length);

	auto jkeys = env->NewByteArray((length + 1) * 2);
	env->SetByteArrayRegion(jkeys, 0, length * 2, (jbyte*)wstr);
	env->SetByteArrayRegion(jkeys, length * 2, 2, new jbyte[]{ 0, 0 });
	delete[] wstr;
	return jkeys;
}

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
