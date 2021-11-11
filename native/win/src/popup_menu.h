#include <jni.h>
#include <windows.h>

jlong nPopupCreateMenu();
void nPopupAddString(jlong, jint, jbyte*);
void nPopupAddSeparator(jlong);
void nPopupAddSubMenu(jlong, jbyte*, jlong);
jint nPopupShow(jlong, jint, jint);
jint nPopupShowWnd(jlong, jint, jint, jlong);

extern "C" {

	/*
		nPopupCreate
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_win_Win_nPopupCreate(JNIEnv*, jobject) {
		return nPopupCreateMenu();
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nPopupCreate() {
		return nPopupCreateMenu();
	}

	/*
		nPopupAddString
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nPopupAddString(JNIEnv* env, jobject, jlong hmenu, jint id, jbyteArray wideText) {
		return nPopupAddString(hmenu, id, env->GetByteArrayElements(wideText, nullptr));
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nPopupAddString(jlong hmenu, jint id, jbyte* wideText) {
		return nPopupAddString(hmenu, id, wideText);
	}

	/*
		nPopupAddSeparator
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nPopupAddSeparator(JNIEnv*, jobject, jlong hmenu) {
		nPopupAddSeparator(hmenu);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nPopupAddSeparator(jlong hmenu) {
		nPopupAddSeparator(hmenu);
	}

	/*
		nPopupAddSubMenu
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_win_Win_nPopupAddSubMenu(JNIEnv* env, jobject, jlong hmenu, jbyteArray wideText, jlong subMenu) {
		nPopupAddSubMenu(hmenu, env->GetByteArrayElements(wideText, nullptr), subMenu);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nPopupAddSubMenu(jlong hmenu, jbyte* wideText, jlong subMenu) {
		nPopupAddSubMenu(hmenu, wideText, subMenu);
	}

	/*
		nPopupShow
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nPopupShow(JNIEnv*, jobject, jlong hmenu, jint x, jint y) {
		return nPopupShow(hmenu, x, y);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nPopupShow(jlong hmenu, jint x, jint y) {
		return nPopupShow(hmenu, x, y);
	}

	/*
		nPopupShowWnd
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_win_Win_nPopupShowWnd(JNIEnv*, jobject, jlong hmenu, jint x, jint y, jlong hwnd) {
		return nPopupShowWnd(hmenu, x, y, hwnd);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_win_Win_nPopupShowWnd(jlong hmenu, jint x, jint y, jlong hwnd) {
		return nPopupShowWnd(hmenu, x, y, hwnd);
	}

}
