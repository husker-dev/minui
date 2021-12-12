#include <jni.h>

#include <glad/glad.h>
#ifdef _WIN32
#include <windows.h>
#elif __linux__

#elif __APPLE__

#endif

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include <iostream>

extern "C" {

	/*=========================
	*	glDrawArrays
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glDrawArrays(JNIEnv* env, jobject, jint mode, jint first, jint last) {
		glDrawArrays(mode, first, last);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glDrawArrays(jint mode, jint first, jint last) {
		glDrawArrays(mode, first, last);
	}

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
	*	glViewport
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glViewport(JNIEnv* env, jobject, jint x, jint y, jint w, jint h) {
		glViewport(x, y, w, h);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glViewport(jint x, jint y, jint w, jint h) {
		glViewport(x, y, w, h);
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
		glTexImage2D(target, level, internalformat, width, height, border, format, type, (char*)address);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glTexImage2D(jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jlong address) {
		glTexImage2D(target, level, internalformat, width, height, border, format, type, (char*)address);
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
		env->ReleaseByteArrayElements(source, (jbyte*)text, 0);
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
		const char* data = env->GetStringUTFChars(name, nullptr);
		jint result = glGetUniformLocation(program, (GLchar*)data);
		env->ReleaseStringUTFChars(name, data);
		return result;
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
		return env->NewStringUTF(log);
	}

	/*=========================
	*	glGetProgramInfoLog
	* =========================
	*/
	JNIEXPORT jstring JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glGetProgramInfoLog(JNIEnv* env, jobject, jint shader) {
		GLsizei logLength;
		GLchar  log[1024];
		glGetProgramInfoLog(shader, sizeof(log), &logLength, log);
		return env->NewStringUTF(log);
	}

	/*=========================
	*	glUniformMatrix4f
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glUniformMatrix4f(JNIEnv* env, jobject, jint location, jfloatArray matrix) {
		jfloat* data = env->GetFloatArrayElements(matrix, nullptr);
		glUniformMatrix4fv(location, 1, GL_FALSE, data);
		env->ReleaseFloatArrayElements(matrix, data, 0);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glUniformMatrix4f(jint location, jfloat* matrix, jint size) {
		glUniformMatrix4fv(location, 1, GL_FALSE, matrix);
	}

	/*=========================
	*	glPixelStorei
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_glPixelStorei(JNIEnv* env, jobject, jint pname, jint param) {
		glPixelStorei(pname, param);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_glPixelStorei(jint pname, jint param) {
		glPixelStorei(pname, param);
	}

	// ============================================================================================================================================================================
	//		Custom
	// ============================================================================================================================================================================

	/*=========================
	*	applyMatrix
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_applyMatrix(JNIEnv* env, jobject, jfloatArray matrix) {
		jfloat* data = env->GetFloatArrayElements(matrix, nullptr);
		GLint id;
		glGetIntegerv(GL_CURRENT_PROGRAM, &id);
		auto transformLoc = glGetUniformLocation(id, "a_Matrix");
		glUniformMatrix4fv(transformLoc, 1, GL_TRUE, data);
		env->ReleaseFloatArrayElements(matrix, data, 0);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_applyMatrix(jfloat* matrix, jint size) {
		GLint id;
		glGetIntegerv(GL_CURRENT_PROGRAM, &id);
		auto transformLoc = glGetUniformLocation(id, "a_Matrix");
		glUniformMatrix4fv(transformLoc, 1, GL_TRUE, matrix);
	}

	/*=========================
	*	setAttribute
	* =========================
	*/
	JNIEXPORT void JNICALL Java_com_husker_minuicore_pipeline_gl_GLKt_setAttribute(JNIEnv* env, jobject, jint index, jint length, jdoubleArray array) {
		jdouble* data = env->GetDoubleArrayElements(array, nullptr);
		glVertexAttribPointer(index, length, GL_DOUBLE, GL_FALSE, 0, data);
		glEnableVertexAttribArray(index);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minuicore_pipeline_gl_GLKt_setAttribute(jint index, jint length, jdouble* array, jint size) {
		glVertexAttribPointer(index, length, GL_DOUBLE, GL_FALSE, 0, array);
		glEnableVertexAttribArray(index);
	}
}