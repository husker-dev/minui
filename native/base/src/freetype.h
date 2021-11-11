#include <jni.h>
#include <iostream>

#include "ft2build.h"
#include FT_FREETYPE_H
#include FT_SFNT_NAMES_H

jlong nInitFreetype();
jlong nLoadFaceFile(jlong, jbyte*);
jlong nDoneFace(jlong);
jlong nLoadFace(jlong, jbyte*, jlong);
void nSetFaceSize(jlong, jint);
jint nGetFaceNameCount(jlong);
jobjectArray nGetFaceName(JNIEnv*, jlong, jint);
jlong nLoadGlyphTexture(JNIEnv*, jlong, jchar);

extern "C" {

	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nInitFreetype(JNIEnv*, jobject) {
		return nInitFreetype();
	}

	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nLoadFaceFile(JNIEnv* env, jobject, jlong ft, jbyteArray path) {
		return nLoadFaceFile(ft, env->GetByteArrayElements(path, nullptr));
	}

	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nDoneFace(JNIEnv* env, jobject, jlong face) {
		return nDoneFace(face);
	}

	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nLoadFace(JNIEnv* env, jobject, jlong ft, jbyteArray data, jlong length) {
		return nLoadFace(ft, env->GetByteArrayElements(data, nullptr), length);
	}

	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nSetFaceSize(JNIEnv* env, jobject, jlong face, jint size) {
		nSetFaceSize(face, size);
	}

	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nGetFaceNameCount(JNIEnv* env, jobject, jlong face) {
		return nGetFaceNameCount(face);
	}

	JNIEXPORT jobjectArray JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nGetFaceName(JNIEnv* env, jobject, jlong face, jint id) {
		return nGetFaceName(env, face, id);
	}

	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nLoadGlyphTexture(JNIEnv* env, jobject, jlong face, jchar ch) {
		return nLoadGlyphTexture(env, face, ch);
	}

}