
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
    image: dtzar/helm-kubectl:latest
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
                        
                        sh "cd celler-local"
                        sh "sed 's/<imageid>/$BUILD_NUMBER/g' Chart.yaml > Chart-tmp.yaml"
                        sh "mv Chart-tmp.yaml Chart.yaml"
                        sh 'cd ..'
                        sh 'helm install --dry-run --debug ./celler-local'
                       
						
                       
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
//kubectl create secret docker-registry regcred --docker-server='http://artifact:8085/v1' --docker-username=admin --docker-password=<pass> --docker-email=chandan.manh@gmail.com
