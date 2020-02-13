#!/bin/bash
#
# Script to download and deploy and openwhisk actions
#

helm upgrade celler ./celler-local --set image.repository=$IMAGE_NAME --set imagePullSecrets[0].name=regcred4
if [[ $? -ne 0 ]]; then
    
	helm upgrade celler ./celler-local --set image.repository=$IMAGE_NAME --set imagePullSecrets[0].name=regcred4
fi
