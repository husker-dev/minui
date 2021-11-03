#include <jni.h>
#include <iostream>

#include "ft2build.h"
#include FT_FREETYPE_H
#include FT_SFNT_NAMES_H

extern "C" {

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nInitFreetype(JNIEnv *, jobject) {
        FT_Library ft;
        if (FT_Init_FreeType(&ft))
            std::cout << "ERROR::FREETYPE: Could not init FreeType Library" << std::endl;
        return (jlong) ft;
    }

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nLoadFaceFile(JNIEnv *env, jobject, jlong ft, jstring path) {
        FT_Face face;
        if (FT_New_Face((FT_Library) ft, env->GetStringUTFChars(path, nullptr) , 0, &face)){
            std::cout << "ERROR::FREETYPE: Failed to load font" << std::endl;
            return -1;
        }
        return (jlong) face;
    }

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nDoneFace(JNIEnv *env, jobject, jlong face) {
        if (FT_Done_Face((FT_Face) face)){
            std::cout << "ERROR::FREETYPE: Failed to load font" << std::endl;
            return -1;
        }
        return 0;
    }

    JNIEXPORT jlong JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nLoadFace(JNIEnv *env, jobject, jlong ft, jbyteArray jdata) {
        FT_Face face;
        auto* data = (FT_Byte *) env->GetByteArrayElements(jdata, nullptr);
        FT_Long size = env->GetArrayLength(jdata);

        if (FT_New_Memory_Face((FT_Library) ft, data, size, 0, &face)){
            std::cout << "ERROR::FREETYPE: Failed to load font" << std::endl;
            return -1;
        }
        return (jlong) face;
    }

    JNIEXPORT void JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nSetFaceSize(JNIEnv *env, jobject, jlong face, jint size) {
        if (FT_Set_Pixel_Sizes((FT_Face) face, 0, (FT_UInt) size))
            std::cout << "ERROR::FREETYPE: Failed to set font size" << std::endl;
    }

    JNIEXPORT jint JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nGetFaceNameCount(JNIEnv *env, jobject, jlong face) {
        return (jint) FT_Get_Sfnt_Name_Count((FT_Face) face);
    }

    JNIEXPORT jobjectArray JNICALL Java_com_husker_minui_natives_impl_BaseLibrary_nGetFaceName(JNIEnv *env, jobject, jlong face, jint id) {
        FT_SfntName name;
        FT_Get_Sfnt_Name((FT_Face) face, id, &name);

        auto byteArray = env->NewByteArray((jsize) name.string_len);
        env->SetByteArrayRegion(byteArray, 0, (jsize) name.string_len, (jbyte*) name.string);

        auto encodingArray = env->NewIntArray(3);
        int encoding[] = {name.platform_id, name.encoding_id, name.language_id};
        env->SetIntArrayRegion(encodingArray, 0, 3, (jint*) encoding);

        jobjectArray out = env->NewObjectArray(2, env->FindClass("java/lang/Object"), nullptr);
        env->SetObjectArrayElement(out, 0, byteArray);
        env->SetObjectArrayElement(out, 1, encodingArray);

        return out;
    }
}
