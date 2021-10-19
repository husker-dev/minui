cmake_minimum_required(VERSION 3.20)
set(CMAKE_BUILD_TYPE Release)
set(CMAKE_CXX_STANDARD 11)

project(minui_win_native)

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ../out)



add_library(win SHARED
        src/base.cpp
        src/clipboard.cpp
        src/window.cpp
        src/popup_menu.cpp)

target_link_libraries(win PRIVATE "Dwmapi.lib" "uxtheme.lib" )

