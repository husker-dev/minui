#include <jni.h>
#include "glad/glad.h"
#include "glad/wgl/glad_wgl.h"
#include <windows.h>
#include <dwmapi.h>
#include <map>


void nInit();
jlong nCreateWindow(jlong shareWith);
void nSwapBuffers(jlong handle);
void nPollEvents();
void nMakeCurrent(jlong handle);
void nSetVsync(jlong handle, jboolean value);
jboolean nGetVsync(jlong handle);