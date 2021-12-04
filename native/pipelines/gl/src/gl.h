#include <jni.h>

#ifdef _WIN32
#include <windows.h>
#elif __linux__

#elif __APPLE__

#endif

#include <gl\gl.h>
#include <gl\glu.h>
#include <iostream>

extern "C" {

	/*=========================
	*	glClear
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glClear(JNIEnv* env, jobject, jint mask) {
		glClear((GLbitfield)mask);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glClear(jint mask) {
		glClear((GLbitfield)mask);
	}

	/*=========================
	*	glClearColor
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glClearColor(JNIEnv* env, jobject, jfloat r, jfloat g, jfloat b, jfloat a) {
		glClearColor(r, g, b, a);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glClearColor(jfloat r, jfloat g, jfloat b, jfloat a) {
		glClearColor(r, g, b, a);
	}

}