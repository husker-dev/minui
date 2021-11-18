#include "harfbuzz.h"

jlong nHarfBuzzCreateBuffer() {
	hb_buffer_t* buf;
	buf = hb_buffer_create();

	hb_buffer_set_direction(buf, HB_DIRECTION_LTR);
	hb_buffer_set_script(buf, HB_SCRIPT_LATIN);
	hb_buffer_set_language(buf, hb_language_from_string("en", -1));
	return (jlong)buf;
}

void nHarfBuzzSetBufferText(jlong buffer, jbyte* text) {
	auto buf = (hb_buffer_t*)buffer;
	hb_buffer_reset(buf);
	hb_buffer_set_direction(buf, HB_DIRECTION_LTR);
	hb_buffer_set_script(buf, HB_SCRIPT_LATIN);
	hb_buffer_set_language(buf, hb_language_from_string("en", -1));
	hb_buffer_add_utf8((hb_buffer_t*)buffer, (char*)text, -1, 0, -1);
}

jlong nHarfBuzzCreateFont(jlong ftFace) {
	return (jlong)hb_ft_font_create((FT_Face)ftFace, nullptr);
}

void nHarfBuzzShape(jlong font, jlong buffer) {
	hb_shape((hb_font_t*)font, (hb_buffer_t*)buffer, NULL, 0);
}

jint nHarfBuzzGetGlyphCount(jlong buffer) {
	unsigned int glyph_count;
	hb_buffer_get_glyph_infos((hb_buffer_t*)buffer, &glyph_count);
	return (jint)glyph_count;
}

jlong nHarfBuzzGetGlyphInfo(jlong buffer) {
	return (jlong)hb_buffer_get_glyph_infos((hb_buffer_t*)buffer, nullptr);
}

jlong nHarfBuzzGetGlyphPositions(jlong buffer) {
	return (jlong)hb_buffer_get_glyph_positions((hb_buffer_t*)buffer, nullptr);
}

jint nHarfBuzzGetGlyphId(jlong info, jint index) {
	return ((hb_glyph_info_t*)info)[index].codepoint;
}

jint nHarfBuzzGetXOffset(jlong positions, jint index) {
	return ((hb_glyph_position_t*)positions)[index].x_offset;
}

jint nHarfBuzzGetYOffset(jlong positions, jint index) {
	return ((hb_glyph_position_t*)positions)[index].y_offset;
}

jint nHarfBuzzGetXAdvance(jlong positions, jint index) {
	return ((hb_glyph_position_t*)positions)[index].x_advance;
}

jint nHarfBuzzGetYAdvance(jlong positions, jint index) {
	return ((hb_glyph_position_t*)positions)[index].y_advance;
}