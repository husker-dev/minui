cmake_minimum_required(VERSION 3.20)
project(win)

set(CMAKE_BUILD_TYPE Release)
set(CMAKE_C_STANDARD 99)

find_package(JNI REQUIRED)

include_directories(${JNI_INCLUDE_DIRS})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
set(SOURCE_FILES
        src/win.c
        src/win.h)

set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/out)

add_library(win SHARED ${SOURCE_FILES})

