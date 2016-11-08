cmake_minimum_required(VERSION 2.8) 

find_package(Java REQUIRED)
include(UseJava)

project(java-abettor)

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")
set(JAVA_SOURCE_DIR ${CMAKE_SOURCE_DIR}/abettor4j/src)
set(CMAKE_JAVA_INCLUDE_PATH ${JAVA_SOURCE_DIR})

file(GLOB_RECURSE source_files
  "${JAVA_SOURCE_DIR}/*.java"
)

add_jar(abettor4j
  ${source_files}
)
