#!/bin/bash

set -e

if [[ $# != 1 ]] ; then printf "\nArgomenti insufficenti. SERVER_IP_ADDRES\n" ; exit 1 ; fi

readonly DIR_BIN='../../bin'
readonly DIR_ROOT='../../www-root'
readonly STUDENTS_SERVER="${DIR_BIN}/incapache"
readonly DEBUG_SERVER="${DIR_BIN}/incapache2.3-debub"
readonly STUDENTS_PORT='8010'
readonly DEBUG_PORT='8005'
readonly STUDENTS_FILE_OUT='output.students'
readonly DEBUG_FILE_OUT='output.debug'
readonly FILE_OUT_DIFF='output.diff'
readonly SCRIPT_DEBUG='./debugScript.bash'
readonly SCRIPT_STUDENTSSERVER='./studentserverScript.bash'
readonly SCRIPT_CLIENTHTTP='./clienthttpScript.bash'
readonly SCRIPT_TESTPIPELINE='./testThreadScript.bash'
readonly SERVER_ADDR="$1"

rm -f "${STUDENTS_FILE_OUT}" "${DEBUG_FILE_OUT}" "${FILE_OUT_DIFF}"

#ESECUZIONE_TEST_SUL_SERVER-DEBUG
printf "\nTESTING DEBUG HTTP SERVER...\n\n";
"${SCRIPT_DEBUG}" "${DEBUG_SERVER}" "${SERVER_ADDR}" "${DEBUG_PORT}" "${DEBUG_FILE_OUT}"
printf "\n                         ...TESTING DONE.\n\n";

#ESECUZIONE_TEST_SUL_MY-SERVER
printf "\nTESTING STUDENTS HTTP SERVER...\n\n";
"${SCRIPT_STUDENTSSERVER}" "${SERVER_ADDR}" "${STUDENTS_PORT}" "${STUDENTS_FILE_OUT}"
printf "\n                              ...TESTING DONE.\n\n";   

diff "${STUDENTS_FILE_OUT}" "${DEBUG_FILE_OUT}" > "${FILE_OUT_DIFF}"
printf "\n\nOUTPUT RESULT:\n\n";
printf " -The http answers of \"%s\" server are saved on the file \"%s\".\n" "$DEBUG_SERVER" "$DEBUG_FILE_OUT" ;
printf " -The http answers of \"%s\" server are saved on th file \"%s\".\n" "$STUDENTS_SERVER" "$STUDENTS_FILE_OUT" ;
printf " -Differences between the http answer of the servers \"%s\" and \"%s\" are saved on \"%s\" file.\n\n\n" "$STUDENTS_SERVER" "$DEBUG_SERVER" "$FILE_OUT_DIFF" ;
