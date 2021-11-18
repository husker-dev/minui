#include <jni.h>
#include <iostream>

#include "harfbuzz/hb.h"
#include "harfbuzz/hb-ft.h"


jlong nHarfBuzzCreateBuffer();
void nHarfBuzzSetBufferText(jlong, jbyte*);
jlong nHarfBuzzCreateFont(jlong);

void nHarfBuzzShape(jlong, jlong);

jint nHarfBuzzGetGlyphCount(jlong);
jlong nHarfBuzzGetGlyphInfo(jlong);
jlong nHarfBuzzGetGlyphPositions(jlong);

jint nHarfBuzzGetGlyphId(jlong, jint);
jint nHarfBuzzGetXOffset(jlong, jint);
jint nHarfBuzzGetYOffset(jlong, jint);
jint nHarfBuzzGetXAdvance(jlong, jint);
jint nHarfBuzzGetYAdvance(jlong, jint);

extern "C" {

	/*
		nHfCreateBuffer
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzCreateBuffer(JNIEnv* env, jobject) {
		return nHarfBuzzCreateBuffer();
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzCreateBuffer() {
		return nHarfBuzzCreateBuffer();
	}

	/*
		nHfSetBufferText
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzSetBufferText(JNIEnv* env, jobject, jlong buffer, jbyteArray text) {
		nHarfBuzzSetBufferText(buffer, env->GetByteArrayElements(text, nullptr));
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzSetBufferText(jlong buffer, jbyte* text) {
		nHarfBuzzSetBufferText(buffer, text);
	}

	/*
		nHfCreateFont
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzCreateFont(JNIEnv* env, jobject, jlong ftFace) {
		return nHarfBuzzCreateFont(ftFace);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzCreateFont(jlong ftFace) {
		return nHarfBuzzCreateFont(ftFace);
	}

	/*
		nHfShape
	*/
	JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzShape(JNIEnv* env, jobject, jlong font, jlong buffer) {
		nHarfBuzzShape(font, buffer);
	}

	JNIEXPORT void JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzShape(jlong font, jlong buffer) {
		nHarfBuzzShape(font, buffer);
	}

	/*
		nHfGetGlyphCount
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphCount(JNIEnv* env, jobject, jlong buffer) {
		return nHarfBuzzGetGlyphCount(buffer);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphCount(jlong buffer) {
		return nHarfBuzzGetGlyphCount(buffer);
	}

	/*
		nHfGetGlyphInfo
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphInfo(JNIEnv* env, jobject, jlong buffer) {
		return nHarfBuzzGetGlyphInfo(buffer);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphInfo(jlong buffer) {
		return nHarfBuzzGetGlyphInfo(buffer);
	}

	/*
		nHfGetGlyphPositions
	*/
	JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphPositions(JNIEnv* env, jobject, jlong buffer) {
		return nHarfBuzzGetGlyphPositions(buffer);
	}

	JNIEXPORT jlong JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphPositions(jlong buffer) {
		return nHarfBuzzGetGlyphPositions(buffer);
	}

	/*
		nHfGetGlyphId
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphId(JNIEnv* env, jobject, jlong info, jint index) {
		return nHarfBuzzGetGlyphId(info, index);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetGlyphId(jlong info, jint index) {
		return nHarfBuzzGetGlyphId(info, index);
	}

	/*
		nHfGetXOffset
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetXOffset(JNIEnv* env, jobject, jlong positions, jint index) {
		return nHarfBuzzGetXOffset(positions, index);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetXOffset(jlong positions, jint index) {
		return nHarfBuzzGetXOffset(positions, index);
	}

	/*
		nHfGetYOffset
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetYOffset(JNIEnv* env, jobject, jlong positions, jint index) {
		return nHarfBuzzGetYOffset(positions, index);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetYOffset(jlong positions, jint index) {
		return nHarfBuzzGetYOffset(positions, index);
	}

	/*
		nHfGetXAdvance
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetXAdvance(JNIEnv* env, jobject, jlong positions, jint index) {
		return nHarfBuzzGetXAdvance(positions, index);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetXAdvance(jlong positions, jint index) {
		return nHarfBuzzGetXAdvance(positions, index);
	}

	/*
		nHfGetYAdvance
	*/
	JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetYAdvance(JNIEnv* env, jobject, jlong positions, jint index) {
		return nHarfBuzzGetYAdvance(positions, index);
	}

	JNIEXPORT jint JNICALL JavaCritical_com_husker_minui_natives_impl_BaseLibrary_nHarfBuzzGetYAdvance(jlong positions, jint index) {
		return nHarfBuzzGetYAdvance(positions, index);
	}
}

