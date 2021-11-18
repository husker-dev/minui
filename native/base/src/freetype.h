#include <jni.h>
#include <iostream>

#include "ft2build.h"
#include FT_FREETYPE_H
#include FT_SFNT_NAMES_H

jlong nFreetypeInit();
jlong nFreetypeLoadFaceFile(jlong, jbyte*);
jlong nFreetypeDoneFace(jlong);
jlong nFreetypeLoadFace(jlong, jbyte*, jlong);
jlong nFreetypeSetFaceSize(jlong, jint);
jint nFreetypeGetFaceNameCount(jlong);
jobjectArray nFreetypeGetFaceName(JNIEnv*, jlong, jint);

jint nFreetypeGetCharIndex(jlong face, jint ch);
jlong nFreetypeLoadChar(jlong, jint);
jlong nFreetypeGetGlyphWidth(jlong);
jlong nFreetypeGetGlyphHeight(jlong);
jobject nFreetypeGetGlyphData(JNIEnv*, jlong);
jint nFreetypeGetBearingX(jlong);
jint nFreetypeGetBearingY(jlong);

extern "C" {

	/*
		nInitFreetype
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeInit(JNIEnv*, jobject) {
		return nFreetypeInit();
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeInit() {
		return nFreetypeInit();
	}

	/*
		nLoadFaceFile
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeLoadFaceFile(JNIEnv* env, jobject, jlong ft, jbyteArray path) {
		return nFreetypeLoadFaceFile(ft, env->GetByteArrayElements(path, nullptr));
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeLoadFaceFile(jlong ft, jbyte* path) {
		return nFreetypeLoadFaceFile(ft, path);
	}

	/*
		nDoneFace
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeDoneFace(JNIEnv* env, jobject, jlong face) {
		return nFreetypeDoneFace(face);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeDoneFace(jlong face) {
		return nFreetypeDoneFace(face);
	}

	/*
		nLoadFace
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeLoadFace(JNIEnv* env, jobject, jlong ft, jbyteArray data, jlong length) {
		return nFreetypeLoadFace(ft, env->GetByteArrayElements(data, nullptr), length);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeLoadFace(jlong ft, jbyte* data, jlong length) {
		return nFreetypeLoadFace(ft, data, length);
	}

	/*
		nSetFaceSize
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeSetFaceSize(JNIEnv* env, jobject, jlong face, jint size) {
		return nFreetypeSetFaceSize(face, size);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeSetFaceSize(jlong face, jint size) {
		return nFreetypeSetFaceSize(face, size);
	}

	/*
		nGetFaceNameCount
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetFaceNameCount(JNIEnv* env, jobject, jlong face) {
		return nFreetypeGetFaceNameCount(face);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetFaceNameCount(jlong face) {
		return nFreetypeGetFaceNameCount(face);
	}

	/*
		nGetFaceName
	*/
	JNIEXPORT jobjectArray JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetFaceName(JNIEnv* env, jobject, jlong face, jint id) {
		return nFreetypeGetFaceName(env, face, id);
	}

	/*
		nFreetypeGetCharIndex
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetCharIndex(JNIEnv* env, jobject, jlong face, jint ch) {
		return nFreetypeGetCharIndex(face, ch);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetCharIndex(jlong face, jint ch) {
		return nFreetypeGetCharIndex(face, ch);
	}

	/*
		nFtLoadChar
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeLoadChar(JNIEnv* env, jobject, jlong face, jint ch) {
		return nFreetypeLoadChar(face, ch);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeLoadChar(jlong face, jint ch) {
		return nFreetypeLoadChar(face, ch);
	}

	/*
		nFtGetGlyphWidth
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetGlyphWidth(JNIEnv* env, jobject, jlong face) {
		return nFreetypeGetGlyphWidth(face);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetGlyphWidth(jlong face) {
		return nFreetypeGetGlyphWidth(face);
	}

	/*
		nFtGetGlyphHeight
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetGlyphHeight(JNIEnv* env, jobject, jlong face) {
		return nFreetypeGetGlyphHeight(face);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetGlyphHeight(jlong face) {
		return nFreetypeGetGlyphHeight(face);
	}

	/*
		nFtGetGlyphData
	*/
	JNIEXPORT jobject JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetGlyphData(JNIEnv* env, jobject, jlong face) {
		return nFreetypeGetGlyphData(env, face);
	}

	/*
		nFreetypeGetBearingX
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetBearingX(JNIEnv* env, jobject, jlong face) {
		return nFreetypeGetBearingX(face);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetBearingX(jlong face) {
		return nFreetypeGetBearingX(face);
	}

	/*
		nFreetypeGetBearingY
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetBearingY(JNIEnv* env, jobject, jlong face) {
		return nFreetypeGetBearingY(face);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nFreetypeGetBearingY(jlong face) {
		return nFreetypeGetBearingY(face);
	}
}