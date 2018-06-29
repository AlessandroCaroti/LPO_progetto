#!/bin/bash

set -e

if [[ $# != 3 ]] ; then printf "\nArgomenti insufficenti. SERVER_IP_ADDRES SERVER_PORT OUTPUT_FILE_NAME\n" ; exit 1 ; fi


readonly ROOT_DIR='../../www-root'
readonly BIN_DIR='../../bin'
readonly FILE_OUT="$3"
readonly SCRIPT_CLIENTHTTP='./clienthttpScript.bash'
readonly SERVER_ADDR="$1"
readonly SERVER_PORT="$2"
readonly STD_OUT='/dev/null'

make -C ../../../incapache-students

printf "\n%s/incapache %s %s >%s &\n" "$BIN_DIR" "$ROOT_DIR" "$SERVER_PORT" "$STD_OUT" ;

"${BIN_DIR}/incapache" "${ROOT_DIR}" "${SERVER_PORT}" > "${STD_OUT}" &
"${SCRIPT_CLIENTHTTP}" "${SERVER_ADDR}" "${SERVER_PORT}" "${FILE_OUT}" &
wait %2
pkill -en incapache