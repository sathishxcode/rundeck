/*
 * The following allows grails to leverage a different url setting for maven central. This would
 * typically be passed along as a -D parameter to grails, ie: grails -Dmaven.central.url=http://...
 */
def mavenCentralUrl = "http://repo1.maven.org/maven2/"
if (System.properties["maven.central.url"]) {
    mavenCentralUrl = System.properties["maven.central.url"]
}
println "Maven Central: ${mavenCentralUrl}"

Boolean mavenCredsDefined = false
def mavenRealm
def mavenHost
def mavenUser
def mavenPassword

// TODO: System.env["mavenRealm"] is a hack.  See comments below.
if (System.env["mavenRealm"] && System.properties["maven.host"] && System.properties["maven.user"] && System.properties["maven.password"]) {
    mavenCredsDefined = true

    /*
     * There's a bug in grails 1.3.7 where system properties (e.g. -Dmaven.realm="Sonatype Nexus Repository Manager") 
     * are truncated at the first space (e.g. System.properties["maven.realm"] is "Sonatype")
     */
    // mavenRealm = System.properties["maven.realm"]

    /*
     * Fortunately, the bug doesn't affect reading environment variables.
     * TODO: This is a hack until grails is fixed
     */
    mavenRealm = System.env["mavenRealm"]

    mavenHost = System.properties["maven.host"]
    mavenUser = System.properties["maven.user"]
    mavenPassword = System.properties["maven.password"]

    println "Maven credentials:\n\tRealm: ${mavenRealm}\n\tHost: ${mavenHost}\n\tUser: ${mavenUser}"
}

def grailsLocalRepo = "grails-app/plugins"
if (System.properties["grails.local.repo"]) {
        grailsLocalRepo = System.properties["grails.local.repo"]
}
println "Grails Local Repo: ${grailsLocalRepo}"

grails.plugin.location.webrealms = "webrealms"
grails.plugin.location.metricsweb = "metricsweb"

grails.project.dependency.resolution = {
    inherits "global" // inherit Grails' default dependencies
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        useOrigin true
        mavenLocal()
        flatDir name:'grailsLocalRepo', dirs:"${grailsLocalRepo}"
        grailsHome()
        grailsPlugins()
        mavenRepo mavenCentralUrl
        grailsCentral()
    }

    if (mavenCredsDefined) {
        credentials {
            realm = mavenRealm
            host = mavenHost
            username = mavenUser
            password = mavenPassword
        }
    }


    rundeckVersion = System.getProperty("RUNDECK_VERSION", appVersion)
    println "Application Version: ${rundeckVersion}"
    plugins {
        runtime ":hibernate:$grailsVersion"
        compile ":code-coverage:1.2.6"
        build ':jetty:2.0.3'
        compile ":twitter-bootstrap:3.0.3"
        compile ":asset-pipeline:1.3.3"
        compile ":less-asset-pipeline:1.2.1"
    }
    dependencies {
        
        test 'org.yaml:snakeyaml:1.9', 'org.apache.ant:ant:1.7.1', 'org.apache.ant:ant-jsch:1.7.1', 
             'com.jcraft:jsch:0.1.50', 'log4j:log4j:1.2.16', 'commons-collections:commons-collections:3.2.1',
             'commons-codec:commons-codec:1.5', 'com.fasterxml.jackson.core:jackson-databind:2.0.2',
                'com.google.guava:guava:15.0'
        test("org.rundeck:rundeck-core:${rundeckVersion}"){
            changing=true
        }
             
        compile 'org.yaml:snakeyaml:1.9', 'org.apache.ant:ant:1.7.1', 'org.apache.ant:ant-jsch:1.7.1', 
                'com.jcraft:jsch:0.1.50','log4j:log4j:1.2.16','commons-collections:commons-collections:3.2.1',
                'commons-codec:commons-codec:1.5', 'com.fasterxml.jackson.core:jackson-databind:2.0.2',
                'com.codahale.metrics:metrics-core:3.0.1',
                'com.google.guava:guava:15.0'
        compile("org.rundeck:rundeck-core:${rundeckVersion}") {
            changing = true
            excludes("xalan")
        }
        compile("org.rundeck:rundeck-storage-filesys:${rundeckVersion}")

        runtime 'org.yaml:snakeyaml:1.9', 'org.apache.ant:ant:1.7.1', 'org.apache.ant:ant-launcher:1.7.1',
                'org.apache.ant:ant-jsch:1.7.1','com.jcraft:jsch:0.1.50', 'org.springframework:spring-test:3.0.5.RELEASE',
                'log4j:log4j:1.2.16' ,'commons-collections:commons-collections:3.2.1','commons-codec:commons-codec:1.5', 
                'com.fasterxml.jackson.core:jackson-databind:2.0.2', 'postgresql:postgresql:9.1-901.jdbc4',
                'com.google.guava:guava:15.0'
        runtime("org.rundeck:rundeck-core:${rundeckVersion}") {
            changing = true
        }
        runtime("org.rundeck:rundeck-jetty-server:${rundeckVersion}") {
            changing = true
        }
    }
}
grails.war.resources = { stagingDir, args ->
    delete(file: "${stagingDir}/WEB-INF/lib/jetty-all-7.6.0.v20120127.jar")
    delete(file: "${stagingDir}/WEB-INF/lib/rundeck-jetty-server-${rundeckVersion}.jar")
    delete(file: "${stagingDir}/WEB-INF/lib/servlet-api-2.5.jar")
    if(System.getProperty('rundeck.war.additional')!=null){
        copy(todir: stagingDir ){
            fileset(dir: System.getProperty('rundeck.war.additional'))
        }
    }
}
