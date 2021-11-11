#include "toast.h"

using namespace winrt;
using namespace Windows::Data::Xml::Dom;
using namespace Windows::UI::Notifications;

static JavaVM* jvm;

static jweak callbackObj;
static jmethodID callbackMethod;


void nToastInit(JNIEnv* env, jobject _obj, jbyteArray id, jbyteArray displayName, jbyteArray iconPath) {
	env->GetJavaVM(&jvm);
	callbackObj = env->NewWeakGlobalRef(_obj);
	jclass callbackClass = env->GetObjectClass(callbackObj);
	callbackMethod = env->GetMethodID(callbackClass, "onToastCallback", "(Ljava/lang/String;)V");

	auto _id = (LPCWSTR)env->GetByteArrayElements(id, nullptr);
	auto _displayName = (LPCWSTR)env->GetByteArrayElements(displayName, nullptr);
	auto _iconPath = (LPCWSTR)env->GetByteArrayElements(iconPath, nullptr);

	DesktopNotificationManagerCompat::Register(_id, _displayName, _iconPath);
	DesktopNotificationManagerCompat::OnActivated([](DesktopNotificationActivatedEventArgsCompat e) {
		std::wstring b = e.Argument();

		auto argument = new char[b.length() + 1];
		for (int i = 0; i < b.length(); i++)
			argument[i] = (char)b.at(i);
		argument[b.length()] = '\0';

		// Getting JVM Env and attach thread
		JNIEnv* g_env;
		JavaVMAttachArgs args{ JNI_VERSION_1_6, NULL, NULL };

		int getEnvStat = jvm->GetEnv((void**)&g_env, JNI_VERSION_1_6);

		if (getEnvStat == JNI_EDETACHED)
			jvm->AttachCurrentThread((void**)&g_env, &args);

		// Callback
		g_env->CallVoidMethod(callbackObj, callbackMethod, g_env->NewStringUTF(argument));

		// Detach thread
		if (getEnvStat == JNI_EDETACHED)
			jvm->DetachCurrentThread();

		delete[] argument;
		});
}

void nToastShow(jbyte* xml) {
	XmlDocument doc;
	doc.LoadXml((LPCWSTR)xml);

	ToastNotification notif{ doc };
	DesktopNotificationManagerCompat::CreateToastNotifier().Show(notif);
}

void nToastUninstall() {
	DesktopNotificationManagerCompat::Uninstall();
}

void nToastClearAll() {
	DesktopNotificationManagerCompat::History().Clear();
}
