#!/bin/bash
#
# Script to download and deploy and openwhisk actions
#

behave -f allure_behave.formatter:AllureFormatter -o allure-results behaveTest/features/feature/*.feature
if [[ $? -ne 0 ]]; then
    allure generate allure-results -o allure-report
    exit 1
fi
allure generate allure-results -o allure-report
tar -zcvf allure-report.tar.gz -C allure-report .
