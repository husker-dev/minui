#include "Callbacks.h"
#include <iostream>

jweak getCallbackObject(JNIEnv* env, jobject object) {
	return env->NewWeakGlobalRef(object);
}

jmethodID getCallbackMethod(JNIEnv* env, jweak callbackObject, const char* name, const char* sig) {
	return env->GetMethodID(env->GetObjectClass(callbackObject), name, sig);
}

int _callbackBegin(JavaVM* jvm, JNIEnv **env) {
	int status = jvm->GetEnv((void**)env, JNI_VERSION_1_6);

	if (status == JNI_EDETACHED) {
		JavaVMAttachArgs args{ JNI_VERSION_1_6, NULL, NULL };
		jvm->AttachCurrentThread((void**)env, &args);
	}
	return status;
}

void _callbackEnd(JavaVM* jvm, int status) {
	if (status == JNI_EDETACHED)
		jvm->DetachCurrentThread();
}


void callback(JavaVM* jvm, jweak callbackObject, jmethodID method, ...){
	va_list args;
	va_start(args, method);

	JNIEnv* env;
	int status = _callbackBegin(jvm, &env);

	env->CallVoidMethod(callbackObject, method, args);

	_callbackEnd(jvm, status);
	va_end(args);
}


jboolean callbackBoolean(JavaVM* jvm, jweak callbackObject, jmethodID method, ...) {
	va_list args;
	va_start(args, method);

	JNIEnv* env = 0;
	int status = _callbackBegin(jvm, &env);

	auto result = env->CallBooleanMethod(callbackObject, method, args);
	_callbackEnd(jvm, status);
	va_end(args);
	return result;
}


jfloat callbackFloat(JavaVM* jvm, jweak callbackObject, jmethodID method, ...) {
	va_list args;
	va_start(args, method);

	JNIEnv* env;
	int status = _callbackBegin(jvm, &env);

	auto result = env->CallFloatMethod(callbackObject, method, args);

	_callbackEnd(jvm, status);
	va_end(args);
	return result;
}

jdouble callbackDouble(JavaVM* jvm, jweak callbackObject, jmethodID method, ...) {
	va_list args;
	va_start(args, method);

	JNIEnv* env;
	int status = _callbackBegin(jvm, &env);

	auto result = env->CallDoubleMethod(callbackObject, method, args);

	_callbackEnd(jvm, status);
	va_end(args);
	return result;
}

jint callbackInt(JavaVM* jvm, jweak callbackObject, jmethodID method, ...) {
	va_list args;
	va_start(args, method);

	JNIEnv* env;
	int status = _callbackBegin(jvm, &env);

	auto result = env->CallIntMethod(callbackObject, method, args);

	_callbackEnd(jvm, status);
	va_end(args);
	return result;
}
