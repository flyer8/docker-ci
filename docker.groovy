// Docker Pipeline Plugin shoul be installed
node('jenkins-slave') {
// Previously Nexus private docker registry was configured.
def nexusRegistry = "nexus.flylab.local:8082"
  docker.withServer("tcp://192.168.0.104:4243") {
// 'docker-registry-login' - Jenkins Credentials ID for Nexus
    docker.withRegistry("https://${nexusRegistry}/", 'docker-registry-login') {

stage 'Checkout and build image'
    git branch: 'master', url: 'https://github.com/flyer8/tomcat8.git'
    def tomcatImg = docker.build("${nexusRegistry}/tomcat8:${env.BUILD_NUMBER}")

stage 'Push image to Nexus private registry'
    tomcatImg.push()

stage 'Docker container running'
    sh 'docker rm -f tomcat || true'
// Assumes the existence of nginx reverse proxy and ci_default network.
    tomcatImg.run ("--rm --name tomcat -p 8888:8080 --link nginx:nginx --net ci_default")
    }
  }
}
