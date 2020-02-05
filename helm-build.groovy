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
                        
                       
                        sh "sed 's/<imageid>/$BUILD_NUMBER/g' ./celler-local/Chart.yaml > Chart-tmp.yaml"
                       
                        sh "mv Chart-tmp.yaml ./celler-local/Chart.yaml"
                        sh 'chmod 777 helm-build.sh'
                        sh 'sh helm-build.sh'
                       
						
                       
                    }
                }
            }
     	 stage('Integration Test') {
            steps {
                
                    container('python') {
                       
                   
                    sh 'pip install behave-webdriver'
                    sh 'chmod 777 run-sh.sh'
                    sh 'sh run-sh.sh'
				    
				  
                       
                    }
                }
            }
			stage('Publish helm repo') {
            steps {
                
                    container('kubectl') {
                        
                       
                        sh "helm repo add chartmuseum http://artifact:8080"
                       
                        sh "helm package ./celler-local"
                       
                        sh 'curl --data-binary "@celler-local-$BUILD_NUMBER.tgz" http://artifact:8080/api/charts'
                       
						
                       
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