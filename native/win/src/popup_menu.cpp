#include <jni.h>
#include <windows.h>

extern "C"{

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_platform_Win_nCreatePopupMenu(JNIEnv *env, jobject) {
        return reinterpret_cast<jlong>(CreatePopupMenu());
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_platform_Win_nAddPopupString(JNIEnv *env, jobject, jlong hmenu, jint id, jbyteArray wideText) {
        auto bytes = env->GetByteArrayElements(wideText, nullptr);
        AppendMenuW(reinterpret_cast<HMENU>(hmenu), MF_STRING, id, (LPCWSTR) bytes);
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_platform_Win_nAddPopupSeparator(JNIEnv *env, jobject, jlong hmenu) {
        AppendMenuW(reinterpret_cast<HMENU>(hmenu), MF_SEPARATOR, 0, nullptr);
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_platform_Win_nAddPopupSubMenu(JNIEnv *env, jobject, jlong hmenu, jbyteArray wideText, jlong subMenu) {
        auto bytes = env->GetByteArrayElements(wideText, nullptr);
        AppendMenuW(reinterpret_cast<HMENU>(hmenu), MF_POPUP | MF_STRING, (UINT_PTR) subMenu, (LPCWSTR) bytes);
    }

    JNIEXPORT jint JNICALL Java_com_husker_minui_natives_platform_Win_nShowPopup(JNIEnv *env, jobject, jlong hmenu, jint x, jint y) {
        // Create  invisible window to do not block the main one
        WNDCLASS wc = { };
        wc.lpfnWndProc   = DefWindowProc;
        wc.hInstance     = GetModuleHandle(nullptr);
        wc.lpszClassName = reinterpret_cast<LPCSTR>(L"TMP");
        RegisterClass(&wc);

        HWND hwnd = CreateWindowEx(
                0, reinterpret_cast<LPCSTR>(L"TMP"), reinterpret_cast<LPCSTR>(L"TMP"),
                WS_POPUP | WS_EX_NOACTIVATE,
                0, 0, 0, 0, nullptr, nullptr, GetModuleHandle(nullptr), nullptr);

        //_AllowDarkModeForWindow(hwnd, true);
        ShowWindow(hwnd, SW_SHOW);

        UINT result = TrackPopupMenu((HMENU) hmenu, TPM_VERTICAL | TPM_RETURNCMD, x, y, 0, reinterpret_cast<HWND>(hwnd), nullptr);
        DestroyMenu((HMENU) hmenu);
        DestroyWindow(hwnd);
        return (jint) result;
    }


}
