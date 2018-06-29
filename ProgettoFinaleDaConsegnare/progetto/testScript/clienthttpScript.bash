#!/bin/bash

if [[ $# != 3 ]] ; then printf "\nArgomenti insufficenti. SERVER_IP_ADDRES SERVER_PORT FILE_OUTPUT\n" ; exit 1; fi

CLIENT_HTTP_TEST='./test'
STD_OUT='/dev/null'
CC='gcc'

"$CC" -Wall -pedantic "${CLIENT_HTTP_TEST}".c -o "${CLIENT_HTTP_TEST}"

printf "%s %s %s %s >%s\n" "$CLIENT_HTTP_TEST" "$1" "$2" "$3" "$STD_OUT";

"${CLIENT_HTTP_TEST}" "$1" "$2" "$3" > "$STD_OUT"

