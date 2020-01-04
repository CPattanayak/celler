
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

                // Copy all files in our Jenkins workspace to our project directory.                
                sh 'cp -r ${WORKSPACE}/* ${GOPATH}/src/celler'

                sh 'cd ${GOPATH}/src/celler'
              
               // sh 'go get -u github.com/swaggo/swag/cmd/swag'
              
               
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
	 stage('Blue Deploy') {
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
                       
                  
                    sh 'behave -f allure_behave.formatter:AllureFormatter -o allure-results behaveTest/features/feature/*.feature'
                    sh 'allure generate allure-results -o allure-report'
                    sh 'tar -zcvf allure-report.tar.gz -C allure-report .'
				    
				  
                       
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

