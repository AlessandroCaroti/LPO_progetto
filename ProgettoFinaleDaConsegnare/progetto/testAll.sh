#!/bin/bash

SRC_ROOT='./interpreter'
TEST_IN_ROOT='./testInput'

OUT_ROOT='./out'
OUT_CLASS="${OUT_ROOT}/class"
OUT_TEST="${OUT_ROOT}/test"

JAVA_COMPILER='javac'
PACKAGE='interpreter'

mkdir -p "$OUT_CLASS"
mkdir -p "$OUT_TEST"

printf "%s -d %s %s/Main.java\n" "${JAVA_COMPILER}" "${OUT_CLASS}" "${SRC_ROOT}"
${JAVA_COMPILER} ${SRC_ROOT}/Main.java

printf "java interpreter.Main\n"
java interpreter.Main
