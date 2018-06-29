#!/bin/bash

if [[ $# != 4 ]] ; then printf "\nNumero argomenti insufficenti.(NOME_ESEGUIBILE SERVER_IP_ADDRES SERVER_PORTA OUTPUT_FILE_NAME)\n" ; exit 1 ; fi

readonly BIN_DIR='../../bin'
readonly ROOT_DIR='../../www-root'
readonly FILE_OUT="$4"
readonly SCRIPT_CLIENTHTTP='./clienthttpScript.bash'
readonly SERVER_PORT="$3"
readonly SERVER_ADDR="$2"
readonly EXEC="$1"
readonly STD_OUT='/dev/null'

printf "sudo chown root %s\n" "$EXEC";
sudo chown root "${EXEC}"
printf "sudo chmod u+s %s\n" "$EXEC";
sudo chmod u+s "${EXEC}"
printf "sudo chmod a+x %s\n\n" "$EXEC";
sudo chmod a+x "${EXEC}"

printf "%s %s %s >%s &" "$EXEC" "$ROOT_DIR" "$SERVER_PORT" "$STD_OUT";

"${EXEC}" "${ROOT_DIR}" "${SERVER_PORT}" >"$STD_OUT" &
"${SCRIPT_CLIENTHTTP}" "${SERVER_ADDR}" "${SERVER_PORT}" "${FILE_OUT}" &
wait %2
pkill -en incapache
