
#include <jni.h>
#include <windows.h>
#include <vector>
#include <stringapiset.h>


extern "C" {

    JNIEXPORT jbyteArray JNICALL Java_com_husker_minui_natives_platform_Win_nWideTextToMultiByte(JNIEnv *env, jobject, jbyteArray jbytes) {
        char* source = reinterpret_cast<char *>(env->GetByteArrayElements(jbytes, nullptr));

        const int utf8_length = WideCharToMultiByte(CP_UTF8, 0, reinterpret_cast<LPCWCH>(source), -1, nullptr, 0, nullptr, nullptr);

        char* utf8 = new char[utf8_length];
        WideCharToMultiByte(CP_UTF8, 0, reinterpret_cast<LPCWCH>(source), -1, utf8, utf8_length, nullptr, nullptr);

        auto jkeys = env->NewByteArray((long)_msize(utf8) - 1);
        env->SetByteArrayRegion(jkeys, 0, (long)_msize(utf8) - 1, reinterpret_cast<const jbyte *>(utf8));
        delete[] utf8;
        return jkeys;
    }

    JNIEXPORT jbyteArray JNICALL Java_com_husker_minui_natives_platform_Win_nMultiByteToWideText(JNIEnv *env, jobject, jbyteArray jbytes, jint chars) {
        auto source = env->GetByteArrayElements(jbytes, nullptr);

        auto* wstr = new wchar_t[chars];
        MultiByteToWideChar(CP_UTF8, 0, reinterpret_cast<LPCCH>(source), -1, wstr, chars);

        auto jkeys = env->NewByteArray((chars + 1) * 2);
        env->SetByteArrayRegion(jkeys, 0, chars * 2, reinterpret_cast<const jbyte *>(wstr));
        env->SetByteArrayRegion(jkeys, chars * 2, 2, new jbyte[]{0, 0});
        delete[] wstr;
        return jkeys;
    }

    JNIEXPORT jintArray JNICALL Java_com_husker_minui_natives_platform_Win_nGetMousePosition(JNIEnv *env, jobject) {
        POINT p;
        GetCursorPos(&p);

        auto jints = env->NewIntArray(2);
        LONG point[2] = {p.x, p.y};
        env->SetIntArrayRegion(jints, 0, 2, point);
        return jints;
    }

    JNIEXPORT jintArray JNICALL Java_com_husker_minui_natives_platform_Win_nScreenToClient(JNIEnv *env, jobject, jlong hwnd, jint x, jint y) {
        POINT p;
        p.x = x;
        p.y = y;
        ScreenToClient(reinterpret_cast<HWND>(hwnd), &p);

        auto jints = env->NewIntArray(2);
        LONG point[2] = {p.x, p.y};
        env->SetIntArrayRegion(jints, 0, 2, point);
        return jints;
    }

}
