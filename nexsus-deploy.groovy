
pipeline {
  agent {
    kubernetes {
      label 'grpc-go-course'
    
      yaml """
apiVersion: v1
kind: Pod
metadata:
labels:
  component: ci
spec:
  # Use service account that can deploy to all namespaces
  serviceAccountName: cicd
  containers:
  - name: kubectl
    image: lachlanevenson/k8s-kubectl:v1.8.0
    command:
    - cat
    tty: true
  - name: golang
    image: cpattanayak/golang:latest
    command:
    - cat
    tty: true
  - name: python
    image: cpattanayak/python:v3
    command:
    - cat
    tty: true
  - name: docker
    image: docker:latest
    command:
    - cat
    tty: true
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: docker-sock
  volumes:
    - name: docker-sock
      hostPath:
        path: /var/run/docker.sock
  
"""
}
   }
   environment {
        KUBE_API_EP = ''
		KUBE_API_TOKEN = ''
    }
  stages {
    
   
    stage('Build project') {
      steps {
        container('golang') {
		 git 'https://github.com/CPattanayak/celler.git'

         
          script {
                sh 'cd ${GOPATH}/src'
                sh 'mkdir -p ${GOPATH}/src/celler'

                // Copy all files in our Jenkins workspace to our project directory.                
                sh 'cp -r ${WORKSPACE}/* ${GOPATH}/src/celler'

                sh 'cd ${GOPATH}/src/celler'
              
               // sh 'go get -u github.com/swaggo/swag/cmd/swag'
              
               
                sh 'go build'
              
               
              }
          
        }
      }
    }
	 stage('Build local Docker') {
      steps {
        container('docker') {
         
          script {
      
               
              def image = docker.build("artifact:8085/celler:$BUILD_NUMBER")
                        docker.withRegistry( 'http://artifact:8085/v1', "nexsus") {
                            image.push()
							}
               
              
              
               
              }
          
        }
      }
    }
	  
	 stage('Deploy dev') {
            steps {
                
                    container('kubectl') {
                       
                        sh "sed 's/<imageid>/$BUILD_NUMBER/g' deployment-dev.yaml > deploy.yaml"
                        sh "cat deploy.yaml"
                        sh "kubectl apply -f deploy.yaml"
                        sh 'chmod 777 depl-sh.sh'
                        sh 'sh depl-sh.sh'
                       
						
                       
                    }
                }
            }
     	 stage('Integration Test') {
            steps {
                
                    container('python') {
                       
                   // sh 'allure --version'
                   // sh 'java --version'
                    sh 'pip install behave-webdriver'
                    sh 'chmod 777 run-sh.sh'
                    sh 'sh run-sh.sh'
				    
				  
                       
                    }
                }
            }
	stage('Build Prod Docker') {
      steps {
        container('docker') {
         
          script {
      
               
              def image = docker.build("cpattanayak/celler:$BUILD_NUMBER")
                        docker.withRegistry( '', "dockerhubid") {
                            image.push()
							}
               
              
              
               
              }
          
        }
      }
    }
     stage('Deploy Stage') {
            steps {
                
                    container('kubectl') {
					//kubectl config set-cluster k8s --server=$KUBE_API_EP --certificate-authority=deploy.crt --embed-certs=true
                        sh "kubectl config set-cluster k8s --server=$KUBE_API_EP --insecure-skip-tls-verify=true"
						sh "kubectl config set-credentials k8s-deployer --token=$KUBE_API_TOKEN"
						sh "kubectl config set-context k8s --cluster k8s --user k8s-deployer"
						sh "kubectl config use-context k8s"
						sh "kubectl get pods"
                        sh "sed 's/<imageid>/$BUILD_NUMBER/g' deployment.yaml > deploy-stage.yaml"
                        sh "cat deploy-stage.yaml"
                        sh "kubectl apply -f deploy-stage.yaml --validate=false"
                        sh 'chmod 777 depl-sh.sh'
                        sh 'sh depl-sh.sh'
                       
						
                       
                    }
                }
            }      

    }
	post {
        always {
            archiveArtifacts artifacts: 'allure-report.tar.gz', fingerprint: true
            
        }
    }
     
      
}
// helm plugin install https://github.com/chartmuseum/helm-push/
//helm repo add chartmuseum http://localhost:8080
//helm package .
//curl --data-binary "@mychart-0.1.0.tgz" http://localhost:8080/api/charts
//kubectl create secret docker-registry regcred1 --docker-server='https://host.docker.internal:8082' --docker-username=admin --docker-password=Ayushi_123 --docker-email=chandan.manh@gmail.com
//kubectl create secret generic regcred4 --from-file=.dockerconfigjson=config.json --type=kubernetes.io/dockerconfigjson
