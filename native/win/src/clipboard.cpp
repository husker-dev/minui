#include <jni.h>
#include <windows.h>
#include <vector>
#include <iostream>
#include <stringapiset.h>
#include <cstring>

UINT getKeyId(const char *key);

const char *getKeyName(UINT i);

extern "C"{

    const char* cfNames[] = {
            "CF_TEXT",
            "CF_BITMAP",
            "CF_METAFILEPICT",
            "CF_SYLK",
            "CF_DIF",
            "CF_TIFF",
            "CF_OEMTEXT",
            "CF_DIB",
            "CF_PALETTE",
            "CF_PENDATA",
            "CF_RIFF",
            "CF_WAVE",
            "CF_UNICODETEXT",
            "CF_ENHMETAFILE",
            "CF_HDROP",
            "CF_LOCALE",
            "CF_DIBV5"
    };

    JNIEXPORT jobjectArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetClipboardKeys(JNIEnv *env, jobject) {
        OpenClipboard(nullptr);

        std::vector<UINT> keys;

        unsigned int format = 0;
        do {
            format = EnumClipboardFormats(format);
            if (format)
                keys.emplace_back(format);
        } while (format != 0);

        auto jkeys = (jobjectArray)env->NewObjectArray((long) keys.size(), env->FindClass("java/lang/String"), env->NewStringUTF(""));
        for(int i = 0; i < (long) keys.size(); i++)
            env->SetObjectArrayElement(jkeys, i, env->NewStringUTF(getKeyName(keys[i])));

        CloseClipboard();
        return jkeys;
    }

    JNIEXPORT jbyteArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetClipboardData(JNIEnv *env, jobject, jstring jkey) {
        char* key = const_cast<char *>(env->GetStringUTFChars(jkey, nullptr));

        OpenClipboard(nullptr);
        HANDLE hData = GetClipboardData(getKeyId(key));
        auto data = GlobalLock(hData);

        auto jkeys = env->NewByteArray((long)_msize(data));
        env->SetByteArrayRegion(jkeys, 0, (long)_msize(data), reinterpret_cast<const jbyte *>(data));

        CloseClipboard();
        GlobalUnlock(hData);
        return jkeys;
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nEmptyClipboard(JNIEnv *env, jobject) {
        OpenClipboard(nullptr);
        EmptyClipboard();
        CloseClipboard();
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nSetClipboardData(JNIEnv *env, jobject, jstring jkey, jbyteArray jbytes) {
        auto* bytes = env->GetByteArrayElements(jbytes, nullptr);
        int size = env->GetArrayLength(jbytes);
        auto* key = env->GetStringUTFChars(jkey, nullptr);

        OpenClipboard(nullptr);
        HGLOBAL data = GlobalAlloc(GMEM_MOVEABLE, size + 1);
        memcpy(GlobalLock(data), bytes, size);
        GlobalUnlock(data);

        SetClipboardData(getKeyId(key), data);
        CloseClipboard();
        GlobalFree(data);
    }

    JNIEXPORT jbyteArray JNICALL Java_com_husker_minui_natives_impl_win_Win_nGetLCID(JNIEnv *env, jobject, jbyteArray localeBytes) {
        LCID lcid = 0;
        GetLocaleInfoEx((LPCWSTR) env->GetByteArrayElements(localeBytes, nullptr), LOCALE_RETURN_NUMBER | LOCALE_ILANGUAGE, (LPWSTR)&lcid, sizeof(lcid));

        auto jkeys = env->NewByteArray(sizeof(lcid));
        env->SetByteArrayRegion(jkeys, 0, sizeof(lcid), reinterpret_cast<const jbyte *>(lcid));
        return jkeys;
    }

}

const char* getKeyName(UINT i) {
    if (i > sizeof(cfNames)) {
        static char buffer[256];
        GetClipboardFormatName(i, buffer, sizeof(buffer));
        return buffer;
    } else
        return cfNames[i - 1];
}

UINT getKeyId(const char *key) {
    int arraySize = sizeof(cfNames)/sizeof(*cfNames);
    for(int i = 0; i < arraySize; i++) {
        if (strcmp(cfNames[i], key) == 0)
            return i + 1;
    }
    return RegisterClipboardFormat(key);
}

