#!/bin/bash

SRC_ROOT='./interpreter'
TEST_IN_ROOT='./testInput'

OUT_ROOT='./out'
OUT_CLASS="${OUT_ROOT}/class"
OUT_TEST="${OUT_ROOT}/test"

JAVA_COMPILER='javac'

mkdir -p "$OUT_CLASS"

printf "%s -d %s %s/Main.java\n" "${JAVA_COMPILER}" "${OUT_CLASS}" "${SRC_ROOT}"
${JAVA_COMPILER} -d ${OUT_CLASS} ${SRC_ROOT}/Main.java
