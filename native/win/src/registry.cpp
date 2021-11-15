#include "registry.h"

jobjectArray nRegistryGetMap(JNIEnv* env, jlong hkey, jbyteArray jpath) {
	HKEY hKey;
	auto status = RegOpenKeyEx((HKEY)hkey, (LPCWSTR)env->GetByteArrayElements(jpath, nullptr), 0, KEY_READ, &hKey);
	if (status != ERROR_SUCCESS)
		return env->NewObjectArray(1, env->FindClass("java/lang/Object"), env->NewStringUTF(std::to_string(status).c_str()));

	unsigned int size = 0;
	do {
		DWORD keySize = 255;
		char* keyBuffer[255];
		if (RegEnumValueA(hKey, size, (LPSTR)keyBuffer, &keySize, nullptr, nullptr, nullptr, nullptr) == ERROR_SUCCESS)
			size++;
		else
			break;
	} while (true);

	auto jkeys = (jobjectArray)env->NewObjectArray((jsize)size, env->FindClass("java/lang/String"), env->NewStringUTF(""));
	auto jvalues = (jobjectArray)env->NewObjectArray((jsize)size, env->FindClass("java/lang/String"), env->NewStringUTF(""));
	for (int i = 0; i < size; i++) {
		DWORD keySize = 255, valueSize = 255;
		char* keyBuffer[255];
		char* valueBuffer[255];

		RegEnumValueA(hKey, i, (LPSTR)keyBuffer, &keySize, nullptr, nullptr, (LPBYTE)valueBuffer, &valueSize);
		env->SetObjectArrayElement(jkeys, i, env->NewStringUTF((const char*)keyBuffer));
		env->SetObjectArrayElement(jvalues, i, env->NewStringUTF((const char*)valueBuffer));
	}
	RegCloseKey(hKey);

	auto result = (jobjectArray)env->NewObjectArray(2, env->FindClass("java/lang/Object"), nullptr);
	env->SetObjectArrayElement(result, 0, jkeys);
	env->SetObjectArrayElement(result, 1, jvalues);
	return result;
}