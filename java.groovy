node('jenkins-slave') {
  docker.withServer("tcp://192.168.0.104:4243") {

stage 'Running maven for building'
   docker.image('flyer8/maven-git').inside {
   git 'https://github.com/flyer8/webapp1.git'
   sh 'mvn -B clean deploy'

stage 'Deploying to Tomcat apps server'
   sh 'curl -T "target/webapp1.war" "http://tomcat:tomcat@192.168.0.104:8888/manager/text/deploy?path=/webapp1&update=true"'
  }
 }
}
