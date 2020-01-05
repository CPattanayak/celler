
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
    image: cpattanayak/python:v2
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

                           
                sh 'cp -r ${WORKSPACE}/* ${GOPATH}/src/celler'

                sh 'cd ${GOPATH}/src/celler'
              
               
              
               
                sh 'go build'
              
               
              }
          
        }
      }
    }
	  stage('Build Docker') {
      steps {
        container('docker') {
         
          script {
      
               
              def image = docker.build("cpattanayak/celler:$BUILD_NUMBER")
                        docker.withRegistry( '', "dockerhubid") {
                            image.push()
							}
               
               // sh './golang-build'
              
               
              }
          
        }
      }
    }
	 stage('Deploy') {
            steps {
                
                    container('kubectl') {
                       
                     
                        sh "sed 's/<imageid>/$BUILD_NUMBER/g' deployment.yaml > deploy.yaml"
                        sh "cat deploy.yaml"
                        sh "kubectl apply -f deploy.yaml"
                        sh "kubectl get pods"
						
                       
                    }
                }
            }
     	 stage('Integration Test') {
            steps {
                
                    container('python') {
                       
                   
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

