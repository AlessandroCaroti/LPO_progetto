#!/bin/bash

printf "Removing .class files and test output files...\n\n"
find . -name "*.class" -type f -delete

rm -rf ./testOutput/