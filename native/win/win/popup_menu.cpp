#include <jni.h>
#include <windows.h>

UINT showPopup(HMENU hmenu, int x, int y, HWND hwnd){
    UINT result = TrackPopupMenu(hmenu, TPM_VERTICAL | TPM_RETURNCMD, x, y, 0, hwnd, nullptr);
    DestroyMenu((HMENU) hmenu);
    return result;
}

extern "C"{

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_win_Win_nCreatePopupMenu(JNIEnv *env, jobject) {
        return reinterpret_cast<jlong>(CreatePopupMenu());
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nAddPopupString(JNIEnv *env, jobject, jlong hmenu, jint id, jbyteArray wideText) {
        auto bytes = env->GetByteArrayElements(wideText, nullptr);
        AppendMenuW(reinterpret_cast<HMENU>(hmenu), MF_STRING, id, (LPCWSTR) bytes);
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nAddPopupSeparator(JNIEnv *env, jobject, jlong hmenu) {
        AppendMenuW(reinterpret_cast<HMENU>(hmenu), MF_SEPARATOR, 0, nullptr);
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nAddPopupSubMenu(JNIEnv *env, jobject, jlong hmenu, jbyteArray wideText, jlong subMenu) {
        auto bytes = env->GetByteArrayElements(wideText, nullptr);
        AppendMenuW(reinterpret_cast<HMENU>(hmenu), MF_POPUP | MF_STRING, (UINT_PTR) subMenu, (LPCWSTR) bytes);
    }

    JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nShowPopup(JNIEnv *env, jobject, jlong hmenu, jint x, jint y) {
        // Create invisible window to do not block the main one
        WNDCLASS wc = { };
        wc.lpfnWndProc   = DefWindowProc;
        wc.hInstance     = GetModuleHandle(nullptr);
        wc.lpszClassName = L"TMP";
        RegisterClass(&wc);

        HWND hwnd = CreateWindowEx(
                0, L"TMP", L"TMP",
                WS_POPUP | WS_EX_NOACTIVATE,
                0, 0, 0, 0, nullptr, nullptr, GetModuleHandle(nullptr), nullptr);
        ShowWindow(hwnd, SW_SHOW);

        jint result = (jint) showPopup((HMENU) hmenu, x, y, (HWND) hwnd);
        DestroyWindow(hwnd);
        return result;
    }

    JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nShowPopupWnd(JNIEnv *env, jobject, jlong hmenu, jint x, jint y, jlong hwnd) {
         return (jint) showPopup((HMENU) hmenu, x, y, (HWND) hwnd);
    }
}
