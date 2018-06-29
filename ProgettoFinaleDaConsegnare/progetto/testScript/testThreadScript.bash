#!/bin/bash

if [[ $# != 3 ]] ; then printf "\nArgomenti insufficenti. SERVER_IP_ADDRES SERVER_PORT FILE_OUTPUT\n" ; exit 1; fi

readonly CC='gcc'

readonly ROOT_DIR='../../www-root'
readonly BIN_DIR='../../bin'
readonly STD_OUT='/dev/null'
readonly CLIENT_HTTP_TEST='./test'
readonly SERVER_ADDR="$1"
readonly SERVER_PORT="$2"
readonly FILE_OUT="$3"

make -C ../../../incapache-students

"${BIN_DIR}/incapache" "${ROOT_DIR}" "${SERVER_PORT}" &
"$CC" -Wall -pedantic "${CLIENT_HTTP_TEST}".c -o "${CLIENT_HTTP_TEST}"

for value in {1..5}
do
   echo $value;
   "${CLIENT_HTTP_TEST}" "${SERVER_ADDR}" "${SERVER_PORT}" "${value}_${FILE_OUT}" "TEST_PIPELINE" &
done
