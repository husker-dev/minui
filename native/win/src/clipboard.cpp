#include "clipboard.h"


jobjectArray nClipboardGetKeys(JNIEnv* env) {
	OpenClipboard(nullptr);

	std::vector<UINT> keys;

	unsigned int format = 0;
	do {
		format = EnumClipboardFormats(format);
		if (format)
			keys.emplace_back(format);
	} while (format != 0);

	auto jkeys = (jobjectArray)env->NewObjectArray((long)keys.size(), env->FindClass("java/lang/String"), env->NewStringUTF(""));
	for (int i = 0; i < (long)keys.size(); i++)
		env->SetObjectArrayElement(jkeys, i, env->NewStringUTF(getKeyName(keys[i])));

	CloseClipboard();
	return jkeys;
}

jbyteArray nClipboardGetData(JNIEnv* env, jstring jkey) {
	char* key = const_cast<char*>(env->GetStringUTFChars(jkey, nullptr));

	OpenClipboard(nullptr);
	HANDLE hData = GetClipboardData(getKeyId(key));
	auto data = GlobalLock(hData);

	auto jkeys = env->NewByteArray((long)_msize(data));
	env->SetByteArrayRegion(jkeys, 0, (long)_msize(data), reinterpret_cast<const jbyte*>(data));

	CloseClipboard();
	GlobalUnlock(hData);
	return jkeys;
}

void nClipboardEmpty() {
	OpenClipboard(nullptr);
	EmptyClipboard();
	CloseClipboard();
}

void nClipboardSetData(jbyte* key, jbyte* jdata, jint length) {
	OpenClipboard(nullptr);
	HGLOBAL data = GlobalAlloc(GMEM_MOVEABLE, length + 1);
	memcpy(GlobalLock(data), (char*)jdata, length);
	GlobalUnlock(data);

	SetClipboardData(getKeyId((char*)key), data);
	CloseClipboard();
	GlobalFree(data);
}

const char* getKeyName(UINT i) {
	if (i > sizeof(cfNames)) {
		static char buffer[256];
		GetClipboardFormatName(i, (LPWSTR)buffer, sizeof(buffer) / 2);
		return buffer;
	} else
		return cfNames[i - 1];
}

UINT getKeyId(const char* key) {
	int arraySize = sizeof(cfNames) / sizeof(*cfNames);
	for (int i = 0; i < arraySize; i++) {
		if (strcmp(cfNames[i], key) == 0)
			return i + 1;
	}
	return RegisterClipboardFormat((LPWSTR)key);
}

