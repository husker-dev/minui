#include "WinPlatform.h"
#include <iostream>

jboolean nIsDarkTheme() {
	DWORD value = 0;
	DWORD size = sizeof(DWORD);

	auto status = RegGetValueA(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "AppsUseLightTheme", RRF_RT_DWORD, nullptr, &value, &size);
	if (status != 0)
		return false;
	else return value == 0;
}
