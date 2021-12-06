#include "freetype.h"

jlong nFreetypeInit() {
	FT_Library ft;
	if (int status = FT_Init_FreeType(&ft))
		return -status;
	return (jlong)ft;
}

jlong nFreetypeLoadFaceFile(jlong ft, jbyte* path) {
	FT_Face face;
	if (int status = FT_New_Face((FT_Library)ft, (char*)path, 0, &face))
		return -status;
	return (jlong)face;
}

jlong nFreetypeDoneFace(jlong face) {
	if (int status = FT_Done_Face((FT_Face)face))
		return -status;
	return 0;
}

jlong nFreetypeLoadFace(jlong ft, jbyte* data, jlong length) {
	FT_Face face;
	if (int status = FT_New_Memory_Face((FT_Library)ft, (FT_Byte*)data, (FT_Long)length, 0, &face))
		return -status;
	FT_Select_Charmap(face, FT_ENCODING_UNICODE);
	return (jlong)face;
}

jlong nFreetypeSetFaceSize(jlong face, jint size) {
	//FT_Set_Char_Size((FT_Face)face, size << 6, size << 6, 96, 96);
	if (int status = FT_Set_Pixel_Sizes((FT_Face)face, 0, (FT_UInt)size))
		return -status;
	return 0;
}

jint nFreetypeGetFaceNameCount(jlong face) {
	return (jint)FT_Get_Sfnt_Name_Count((FT_Face)face);
}

jobjectArray nFreetypeGetFaceName(JNIEnv* env, jlong face, jint id) {
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

jint nFreetypeGetCharIndex(jlong face, jint ch) {
	return (jlong)FT_Get_Char_Index((FT_Face)face, (FT_Long)ch);
}

jlong nFreetypeLoadChar(jlong face, jint ch) {
	if (int status = FT_Load_Glyph((FT_Face)face, ch, FT_LOAD_DEFAULT))
		return -status;
	FT_Render_Glyph(((FT_Face)face)->glyph, FT_RENDER_MODE_LIGHT);
	return 0;
}

jlong nFreetypeGetGlyphWidth(jlong face) {
	return ((FT_Face)face)->glyph->bitmap.width;
}

jlong nFreetypeGetGlyphHeight(jlong face) {
	return ((FT_Face)face)->glyph->bitmap.rows;
}

jobject nFreetypeGetGlyphData(JNIEnv* env, jlong face) {
	auto data = ((FT_Face)face)->glyph->bitmap.buffer;
	return env->NewDirectByteBuffer(data, (jlong)((FT_Face)face)->glyph->bitmap.width * (jlong)((FT_Face)face)->glyph->bitmap.rows);
}

jint nFreetypeGetBearingX(jlong face) {
	return (jint)((FT_Face)face)->glyph->bitmap_left;
}

jint nFreetypeGetBearingY(jlong face) {
	return (jint)((FT_Face)face)->glyph->bitmap_top;
}