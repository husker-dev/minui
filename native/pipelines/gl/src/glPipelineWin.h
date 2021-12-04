#include <jni.h>
#include <windows.h>
#include <map>
#include "wglext.h"

jlong nCreateWindow(jlong shareWith);
void nSwapBuffers(jlong handle);
void nPollEvents();
void nMakeCurrent(jlong handle);
void nSetVsync(jlong handle, jboolean value);
jboolean nGetVsync(jlong handle);