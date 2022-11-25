#!/bin/sh

mvn -DwithHistory test-compile org.pitest:pitest-maven:mutationCoverage
