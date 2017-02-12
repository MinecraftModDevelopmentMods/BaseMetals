node {
    stage('Init') {
    echo 'Running ${env.BUILD_ID} on ${env.JENKINS_URL}'
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'coded-github', url: 'https://github.com/MinecraftModDevelopment/BaseMetals/']]])
        sh 'rm -rf build/libs/'
    }
    stage('Build') {
        sh 'chmod +x gradlew'
        sh './gradlew clean build'
    }
    stage('Archive') {
        currentBuild.result == 'SUCCESS' {
            fingerprint 'build/libs/*.jar'
            archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
        }
    }
    stage('Deploy') {
        sh './gradlew publish'
        sh './gradlew curseforge'
    }
}