#!/bin/bash
#
# Script to download and deploy and openwhisk actions
#

helm install celler ./celler-local
if [[ $? -ne 0 ]]; then
    helm delete celler ./celler-local
	helm install celler ./celler-local
fi

