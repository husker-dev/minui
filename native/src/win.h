#include <jni.h>

#ifndef _Included_com_husker_minui_natives_Win
#define _Included_com_husker_minui_natives_Win
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_Win_getWindowExStyle(JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_com_husker_minui_natives_Win_setWindowExStyle(JNIEnv *, jclass, jlong, jlong);
JNIEXPORT void JNICALL Java_com_husker_minui_natives_Win_updateWindow(JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
