#include <jni.h>
#include <iostream>

#include "ft2build.h"
#include FT_FREETYPE_H
#include FT_SFNT_NAMES_H

jlong nInitFreetype();
jlong nLoadFaceFile(jlong, jbyte*);
jlong nDoneFace(jlong);
jlong nLoadFace(jlong, jbyte*, jlong);
jlong nSetFaceSize(jlong, jint);
jint nGetFaceNameCount(jlong);
jobjectArray nGetFaceName(JNIEnv*, jlong, jint);

jlong nFtLoadChar(jlong, jint);
jlong nFtGetGlyphWidth(jlong);
jlong nFtGetGlyphHeight(jlong);
jobject nFtGetGlyphData(JNIEnv*, jlong);

jlong nHfCreateFont(jlong);

extern "C" {

	/*
		nInitFreetype
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nInitFreetype(JNIEnv*, jobject) {
		return nInitFreetype();
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nInitFreetype() {
		return nInitFreetype();
	}

	/*
		nLoadFaceFile
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nLoadFaceFile(JNIEnv* env, jobject, jlong ft, jbyteArray path) {
		return nLoadFaceFile(ft, env->GetByteArrayElements(path, nullptr));
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nLoadFaceFile(jlong ft, jbyte* path) {
		return nLoadFaceFile(ft, path);
	}

	/*
		nDoneFace
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nDoneFace(JNIEnv* env, jobject, jlong face) {
		return nDoneFace(face);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nDoneFace(jlong face) {
		return nDoneFace(face);
	}

	/*
		nLoadFace
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nLoadFace(JNIEnv* env, jobject, jlong ft, jbyteArray data, jlong length) {
		return nLoadFace(ft, env->GetByteArrayElements(data, nullptr), length);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nLoadFace(jlong ft, jbyte* data, jlong length) {
		return nLoadFace(ft, data, length);
	}

	/*
		nSetFaceSize
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nSetFaceSize(JNIEnv* env, jobject, jlong face, jint size) {
		return nSetFaceSize(face, size);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nSetFaceSize(jlong face, jint size) {
		return nSetFaceSize(face, size);
	}

	/*
		nGetFaceNameCount
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nGetFaceNameCount(JNIEnv* env, jobject, jlong face) {
		return nGetFaceNameCount(face);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nGetFaceNameCount(jlong face) {
		return nGetFaceNameCount(face);
	}

	/*
		nGetFaceName
	*/
	JNIEXPORT jobjectArray JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nGetFaceName(JNIEnv* env, jobject, jlong face, jint id) {
		return nGetFaceName(env, face, id);
	}

	/*
		nFtLoadChar
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFtLoadChar(JNIEnv* env, jobject, jlong face, jint ch) {
		return nFtLoadChar(face, ch);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFtLoadChar(jlong face, jint ch) {
		return nFtLoadChar(face, ch);
	}

	/*
		nFtGetGlyphWidth
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFtGetGlyphWidth(JNIEnv* env, jobject, jlong face) {
		return nFtGetGlyphWidth(face);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFtGetGlyphWidth(jlong face) {
		return nFtGetGlyphWidth(face);
	}

	/*
		nFtGetGlyphHeight
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFtGetGlyphHeight(JNIEnv* env, jobject, jlong face) {
		return nFtGetGlyphHeight(face);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFtGetGlyphHeight(jlong face) {
		return nFtGetGlyphHeight(face);
	}

	/*
		nFtGetGlyphData
	*/
	JNIEXPORT jobject JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFtGetGlyphData(JNIEnv* env, jobject, jlong face) {
		return nFtGetGlyphData(env, face);
	}
}