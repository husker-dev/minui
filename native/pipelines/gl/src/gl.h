#include <jni.h>

#include "glad/glad.h"
#ifdef _WIN32
#include <windows.h>
#elif __linux__

#elif __APPLE__

#endif


#include <iostream>

extern "C" {

	/*=========================
	*	glBlendFunc
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glBlendFunc(JNIEnv* env, jobject, jint sfactor, jint dfactor) {
		glBlendFunc(sfactor, dfactor);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glBlendFunc(jint sfactor, jint dfactor) {
		glBlendFunc(sfactor, dfactor);
	}

	/*=========================
	*	glMatrixMode
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glMatrixMode(JNIEnv* env, jobject, jint mode) {
		glMatrixMode(mode);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glMatrixMode(jint mode) {
		glMatrixMode(mode);
	}

	/*=========================
	*	glOrtho
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glOrtho(JNIEnv* env, jobject, jdouble l, jdouble r, jdouble b, jdouble t, jdouble n, jdouble f) {
		glOrtho(l, r, b, t, n, f);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glOrtho(jdouble l, jdouble r, jdouble b, jdouble t, jdouble n, jdouble f) {
		glOrtho(l, r, b, t, n, f);
	}

	/*=========================
	*	glViewport
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glViewport(JNIEnv* env, jobject, jint x, jint y, jint w, jint h) {
		glViewport(x, y, w, h);
	}

	/*=========================
	*	glBegin
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glBegin(JNIEnv* env, jobject, jint a) {
		glBegin(a);
	}

	/*=========================
	*	glEnd
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glEnd(JNIEnv* env, jobject) {
		glEnd();
	}

	/*=========================
	*	glVertex2d
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glVertex2d(JNIEnv* env, jobject, jdouble x, jdouble y) {
		glVertex2d(x, y);
	}

	/*=========================
	*	glColor3d
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glColor3d(JNIEnv* env, jobject, jdouble r, jdouble g, jdouble b) {
		glColor3d(r, g, b);
	}

	/*=========================
	*	glLoadIdentity
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glLoadIdentity(JNIEnv* env, jobject) {
		glLoadIdentity();
	}

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

	/*=========================
	*	glEnable
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glEnable(JNIEnv* env, jobject, jint target) {
		glEnable(target);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glEnable(jint target) {
		glEnable(target);
	}

	/*=========================
	*	glFlush
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glFlush(JNIEnv* env, jobject) {
		glFlush();
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glFlush() {
		glFlush();
	}

	/*=========================
	*	glGenTextures
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGenTextures(JNIEnv* env, jobject) {
		unsigned int texture;
		glGenTextures(1, &texture);
		return texture;
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glGenTextures() {
		unsigned int texture;
		glGenTextures(1, &texture);
		return texture;
	}

	/*=========================
	*	glBindTexture
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glBindTexture(JNIEnv* env, jobject, jint target, jint texture) {
		glBindTexture(target, texture);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glBindTexture(jint target, jint texture) {
		glBindTexture(target, texture);
	}

	/*=========================
	*	glTexParameteri
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glTexParameteri(JNIEnv* env, jobject, jint target, jint pname, jint param) {
		glTexParameteri(target, pname, param);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glTexParameteri(jint target, jint pname, jint param) {
		glTexParameteri(target, pname, param);
	}

	/*=========================
	*	glTexImage2D
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glTexImage2D(JNIEnv* env, jobject, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jlong address) {
		glTexImage2D(target, level, internalformat, width, height, border, format, type, (GLvoid*)address);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glTexImage2D(jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jlong address) {
		glTexImage2D(target, level, internalformat, width, height, border, format, type, (GLvoid*)address);
	}

	/*=========================
	*	glGetTexLevelParameteri
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGetTexLevelParameteri(JNIEnv* env, jobject, jint target, jint level, jint pname) {
		int result;
		glGetTexLevelParameteriv(target, level, pname, &result);
		return result;
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glGetTexLevelParameteri(jint target, jint level, jint pname) {
		int result;
		glGetTexLevelParameteriv(target, level, pname, &result);
		return result;
	}
	
	/*=========================
	*	glUseProgram
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glUseProgram(JNIEnv* env, jobject, jint program) {
		glUseProgram(program);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glUseProgram(jint target, jint program) {
		glUseProgram(program);
	}

	/*=========================
	*	glCreateShader
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glCreateShader(JNIEnv* env, jobject, jint type) {
		return glCreateShader(type);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glCreateShader(jint target, jint type) {
		return glCreateShader(type);
	}

	/*=========================
	*	glShaderSource
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glShaderSource(JNIEnv* env, jobject, jint shader, jbyteArray source) {
		char* text = (char*)env->GetByteArrayElements(source, nullptr);
		glShaderSource(shader, 1, &text, NULL);
	}

	/*=========================
	*	glCompileShader
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glCompileShader(JNIEnv* env, jobject, jint shader) {
		glCompileShader(shader);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glCompileShader(jint shader) {
		glCompileShader(shader);
	}

	/*=========================
	*	glGetShaderi
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGetShaderi(JNIEnv* env, jobject, jint shader, jint pname) {
		int result;
		glGetShaderiv(shader, pname, &result);
		return result;
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glGetShaderi(jint shader, jint pname) {
		int result;
		glGetShaderiv(shader, pname, &result);
		return result;
	}

	/*=========================
	*	glAttachShader
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glAttachShader(JNIEnv* env, jobject, jint program, jint shader) {
		glAttachShader(program, shader);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glAttachShader(jint program, jint shader) {
		glAttachShader(program, shader);
	}

	/*=========================
	*	glLinkProgram
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glLinkProgram(JNIEnv* env, jobject, jint program) {
		glLinkProgram(program);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glLinkProgram(jint program, jint shader) {
		glLinkProgram(program);
	}

	/*=========================
	*	glGetProgrami
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGetProgrami(JNIEnv* env, jobject, jint program, jint pname) {
		int result;
		glGetProgramiv(program, pname, &result);
		return result;
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glGetProgrami(jint program, jint pname) {
		int result;
		glGetProgramiv(program, pname, &result);
		return result;
	}

	/*=========================
	*	glValidateProgram
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glValidateProgram(JNIEnv* env, jobject, jint program) {
		glValidateProgram(program);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glValidateProgram(jint program) {
		glValidateProgram(program);
	}

	/*=========================
	*	glDeleteShader
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glDeleteShader(JNIEnv* env, jobject, jint shader) {
		glDeleteShader(shader);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glDeleteShader(jint shader) {
		glDeleteShader(shader);
	}

	/*=========================
	*	glGetUniformLocation
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGetUniformLocation(JNIEnv* env, jobject, jint program, jstring name) {
		return glGetUniformLocation(program, (GLchar*)env->GetStringUTFChars(name, nullptr));
	}

	/*=========================
	*	glUniform1f
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glUniform1f(JNIEnv* env, jobject, jint location, jfloat v1) {
		glUniform1f(location, v1);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glUniform1f(jint location, jfloat v1) {
		glUniform1f(location, v1);
	}
	
	/*=========================
	*	glUniform2f
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glUniform2f(JNIEnv* env, jobject, jint location, jfloat v1, jfloat v2) {
		glUniform2f(location, v1, v2);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glUniform2f(jint location, jfloat v1, jfloat v2) {
		glUniform2f(location, v1, v2);
	}

	/*=========================
	*	glUniform3f
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glUniform3f(JNIEnv* env, jobject, jint location, jfloat v1, jfloat v2, jfloat v3) {
		glUniform3f(location, v1, v2, v3);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glUniform3f(jint location, jfloat v1, jfloat v2, jfloat v3) {
		glUniform3f(location, v1, v2, v3);
	}

	/*=========================
	*	glUniform4f
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glUniform4f(JNIEnv* env, jobject, jint location, jfloat v1, jfloat v2, jfloat v3, jfloat v4) {
		glUniform4f(location, v1, v2, v3, v4);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glUniform4f(jint location, jfloat v1, jfloat v2, jfloat v3, jfloat v4) {
		glUniform4f(location, v1, v2, v3, v4);
	}

	/*=========================
	*	glCreateProgram
	* =========================
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glCreateProgram(JNIEnv* env, jobject) {
		return glCreateProgram();
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glCreateProgram() {
		return glCreateProgram();
	}

	/*=========================
	*	glGetShaderInfoLog
	* =========================
	*/
	JNIEXPORT jstring JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGetShaderInfoLog(JNIEnv* env, jobject, jint shader) {
		GLsizei logLength;
		GLchar  log[1024];
		glGetShaderInfoLog(shader, sizeof(log), &logLength, log);
		return env->NewString((jchar*)log, logLength);
	}

	/*=========================
	*	glGetProgramInfoLog
	* =========================
	*/
	JNIEXPORT jstring JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGetProgramInfoLog(JNIEnv* env, jobject, jint shader) {
		GLsizei logLength;
		GLchar  log[1024];
		glGetProgramInfoLog(shader, sizeof(log), &logLength, log);
		return env->NewString((jchar*)log, logLength);
	}
}