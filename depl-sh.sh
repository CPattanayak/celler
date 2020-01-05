#!/bin/bash
#
# Script to download and deploy and openwhisk actions
#


COUNT=$(kubectl get pods | grep -c celler-deployment)
while [[ "$COUNT" != 1 ]]; do
 COUNT=$(kubectl get pods | grep -c celler-deployment)
 kubectl get pods
 sleep 5
done
 
