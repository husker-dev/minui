#include <jni.h>


#ifdef _WIN32
#include "glPipelineWin.h"
#elif __linux__

#elif __APPLE__

#endif
#include <gl\gl.h>
#include <gl\glu.h>
#include <iostream>

extern "C" {

	/*=========================
	*	nInit
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLPipelineKt_nInit(JNIEnv*, jobject) {
		if (!gladLoadGL())
			std::cout << "Failed to load GLAD" << std::endl;
		nInit();
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLPipelineKt_nInit() {
		if (!gladLoadGL())
			std::cout << "Failed to load GLAD" << std::endl;
		nInit();
	}

	/*=========================
	*	createWindow
	* =========================
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minuicore_pipeline_gl_GLPipelineKt_nCreateWindow(JNIEnv* env, jobject, jlong shareWith) {
		return nCreateWindow(shareWith);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLPipelineKt_nCreateWindow(jlong shareWith) {
		return nCreateWindow(shareWith);
	}


	/*=========================
	*	nSwapBuffers
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLPipelineKt_nSwapBuffers(JNIEnv* env, jobject, jlong handle) {
		nSwapBuffers(handle);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLPipelineKt_nSwapBuffers(jlong handle) {
		nSwapBuffers(handle);
	}

	/*=========================
	*	nPollEvents
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLPipelineKt_nPollEvents(JNIEnv* env, jobject) {
		nPollEvents();
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLPipelineKt_nPollEvents() {
		nPollEvents();
	}

	/*=========================
	*	nMakeCurrent
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLPipelineKt_nMakeCurrent(JNIEnv* env, jobject, jlong handle) {
		nMakeCurrent(handle);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLPipelineKt_nMakeCurrent(jlong handle) {
		nMakeCurrent(handle);
	}

	/*=========================
	*	nSetVsync
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLPipelineKt_nSetVsync(JNIEnv* env, jobject, jlong handle, jboolean value) {
		nSetVsync(handle, value);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLPipelineKt_nSetVsync(jlong handle, jboolean value) {
		nSetVsync(handle, value);
	}

	/*=========================
	*	nGetVsync
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLPipelineKt_nGetVsync(JNIEnv* env, jobject, jlong handle) {
		return nGetVsync(handle);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLPipelineKt_nGetVsync(jlong handle) {
		return nGetVsync(handle);
	}

}