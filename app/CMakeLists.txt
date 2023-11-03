cmake_minimum_required(VERSION 3.22.1)

project(CatBreedExplorer)

add_library(

        native-lib

        SHARED

        src/main/cpp/libnative-lib.cpp

)

find_library( # Defines the name of the path variable that stores the
        # location of the NDK library.
        log-lib

        # Specifies the name of the NDK library that
        # CMake needs to locate.
        log)

# Links your native library against one or more other native libraries.
target_link_libraries( # Specifies the target library.
        native-lib

        # Links the log library to the target library.
        ${log-lib})