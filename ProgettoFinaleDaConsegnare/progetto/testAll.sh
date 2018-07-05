#!/bin/bash

SRC_ROOT='./interpreter'
TEST_INPUT='./test/correct'
TEST_FALITURE='./test/failure'

OUT_TEST="./testOutput"

JAVA_COMPILER='javac'
PACKAGE='interpreter'

./clean.sh

mkdir -p ${OUT_TEST}

printf "%s %s/Main.java\n" "${JAVA_COMPILER}" "${SRC_ROOT}"
${JAVA_COMPILER} ${SRC_ROOT}/Main.java

printf "\n\n\n************************************************\nTEST CORRECT\n\n"
for filename in ${TEST_INPUT}/*.txt; do
    name=${filename##*/}
    base=${name%.txt}
    printf "java interpreter.Main -i %s -o %s\n" "$filename" "${OUT_TEST}/${base}_OUT.txt"
    java interpreter.Main -i $filename -o "${OUT_TEST}/${base}_OUT.txt"
done

printf "\n\n\n************************************************\nTEST FALITURE\n\n"
for filename in ${TEST_FALITURE}/*.txt; do
    name=${filename##*/}
    base=${name%.txt}
    printf "java interpreter.Main -i %s -o %s\n" "$filename" "${OUT_TEST}/${base}_OUT.txt"
    java interpreter.Main -i $filename -o "${OUT_TEST}/${base}_OUT.txt"
done