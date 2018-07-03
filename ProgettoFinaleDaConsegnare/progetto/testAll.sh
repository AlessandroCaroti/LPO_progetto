#!/bin/bash

SRC_ROOT='./interpreter'
TEST_INPUT='./testInput'
TEST_FALITURE='./failure'

OUT_TEST="./testOutput"

JAVA_COMPILER='javac'
PACKAGE='interpreter'

mkdir -p ${OUT_TEST}

printf "%s %s/Main.java\n" "${JAVA_COMPILER}" "${SRC_ROOT}"
${JAVA_COMPILER} ${SRC_ROOT}/Main.java

for filename in ${TEST_INPUT}/*.txt; do
    name=${filename##*/}
    base=${name%.txt}
    printf "java interpreter.Main -i %s -o %s\n" "$filename" "${OUT_TEST}/${base}_OUT.txt"
    java interpreter.Main -i $filename -o "${OUT_TEST}/${base}_OUT.txt"
done

for filename in ${TEST_FALITURE}/*.txt; do
    name=${filename##*/}
    base=${name%.txt}
    printf "java interpreter.Main -i %s -o %s\n" "$filename" "${OUT_TEST}/${base}_OUT.txt"
    java interpreter.Main -i $filename -o "${OUT_TEST}/${base}_OUT.txt"
done