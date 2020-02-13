#!/bin/bash
#
# Script to download and deploy and openwhisk actions
#

helm install celler ./celler-local --set image.repository=$IMAGE_NAME 
if [[ $? -ne 0 ]]; then
    
	helm upgrade celler ./celler-local --set image.repository=$IMAGE_NAME 
fi

