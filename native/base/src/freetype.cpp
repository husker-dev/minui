#include "freetype.h"

jlong nInitFreetype() {
	FT_Library ft;
	if (int status = FT_Init_FreeType(&ft))
		return -status;
	return (jlong)ft;
}

jlong nLoadFaceFile(jlong ft, jbyte* path) {
	FT_Face face;

	if (int status = FT_New_Face((FT_Library)ft, (char*)path, 0, &face))
		return -status;
	return (jlong)face;
}

jlong nDoneFace(jlong face) {
	if (int status = FT_Done_Face((FT_Face)face))
		return -status;
	return 0;
}

jlong nLoadFace(jlong ft, jbyte* data, jlong length) {
	FT_Face face;
	if (int status = FT_New_Memory_Face((FT_Library)ft, (FT_Byte*)data, (FT_Long)length, 0, &face))
		return -status;
	FT_Select_Charmap(face, FT_ENCODING_UNICODE);
	return (jlong)face;
}

jlong nSetFaceSize(jlong face, jint size) {
	if (int status = FT_Set_Pixel_Sizes((FT_Face)face, 0, (FT_UInt)size)) 
		return -status;
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

jlong nFtLoadChar(jlong face, jint ch) {
	unsigned long c = FT_Get_Char_Index((FT_Face)face, (FT_Long)ch);
	if (int status = FT_Load_Glyph((FT_Face)face, c, FT_LOAD_RENDER | FT_LOAD_NO_HINTING))
		return -status;
	return 0;
}

jlong nFtGetGlyphWidth(jlong face) {
	return ((FT_Face)face)->glyph->bitmap.width;
}

jlong nFtGetGlyphHeight(jlong face) {
	return ((FT_Face)face)->glyph->bitmap.rows;
}

jobject nFtGetGlyphData(JNIEnv* env, jlong face) {
	auto data = ((FT_Face)face)->glyph->bitmap.buffer;
	return env->NewDirectByteBuffer(data, (jlong)((FT_Face)face)->glyph->bitmap.width * (jlong)((FT_Face)face)->glyph->bitmap.rows);
}