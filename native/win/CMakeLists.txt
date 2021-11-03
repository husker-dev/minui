cmake_minimum_required(VERSION 3.20)
set(CMAKE_BUILD_TYPE Release)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ../build)
project(minui_win_native)
set(LIBS ${PROJECT_SOURCE_DIR}/libs)

# JNI
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

file(GLOB sources CONFIGURE_DEPENDS "src/*.h" "src/*.cpp")
add_library(minui-win SHARED ${sources} src/registry.cpp)
