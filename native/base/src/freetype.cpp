#include "freetype.h"

jlong nInitFreetype() {
	FT_Library ft;
	if (FT_Init_FreeType(&ft))
		std::cout << "ERROR::FREETYPE: Could not init FreeType Library" << std::endl;
	return (jlong)ft;
}

jlong nLoadFaceFile(jlong ft, jbyte* path) {
	FT_Face face;
	if (FT_New_Face((FT_Library)ft, (char*)path, 0, &face)) {
		std::cout << "ERROR::FREETYPE: Failed to load font" << std::endl;
		return -1;
	}
	return (jlong)face;
}

jlong nDoneFace(jlong face) {
	if (FT_Done_Face((FT_Face)face)) {
		std::cout << "ERROR::FREETYPE: Failed to load font" << std::endl;
		return -1;
	}
	return 0;
}

jlong nLoadFace(jlong ft, jbyte* data, jlong length) {
	FT_Face face;
	if (FT_New_Memory_Face((FT_Library)ft, (FT_Byte*)data, (FT_Long)length, 0, &face)) {
		std::cout << "ERROR::FREETYPE: Failed to load font" << std::endl;
		return -1;
	}
	return (jlong)face;
}

void nSetFaceSize(jlong face, jint size) {
	if (FT_Set_Pixel_Sizes((FT_Face)face, 0, (FT_UInt)size))
		std::cout << "ERROR::FREETYPE: Failed to set font size" << std::endl;
}

jint nGetFaceNameCount(jlong face) {
	return (jint)FT_Get_Sfnt_Name_Count((FT_Face)face);
}

jobjectArray nGetFaceName(JNIEnv* env, jlong face, jint id) {
	FT_SfntName name;
	FT_Get_Sfnt_Name((FT_Face)face, id, &name);

	auto byteArray = env->NewByteArray((jsize)name.string_len);
	env->SetByteArrayRegion(byteArray, 0, (jsize)name.string_len, (jbyte*)name.string);

	auto encodingArray = env->NewIntArray(3);
	int encoding[] = { name.platform_id, name.encoding_id, name.language_id };
	env->SetIntArrayRegion(encodingArray, 0, 3, (jint*)encoding);

	jobjectArray out = env->NewObjectArray(2, env->FindClass("java/lang/Object"), nullptr);
	env->SetObjectArrayElement(out, 0, byteArray);
	env->SetObjectArrayElement(out, 1, encodingArray);

	return out;
}

jlong nLoadGlyphTexture(JNIEnv* env, jlong face, jchar ch) {
	// load character glyph
	if (FT_Load_Char((FT_Face)face, (char)ch, FT_LOAD_RENDER)) {
		std::cout << "ERROR::FREETYTPE: Failed to load Glyph" << std::endl;
	}
	return 0;
}