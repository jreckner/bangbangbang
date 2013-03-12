grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        // runtime 'mysql:mysql-connector-java:5.1.20'
        //compile "org.apache.shiro:shiro-core:1.2.1"
        //compile "org.apache.shiro:shiro-web:1.2.1"
        //compile "org.apache.shiro:shiro-spring:1.2.1"

        // workaround for code-coverage breakage in Grails 2.2.x
        // ref: https://jira.grails.org/browse/GPCODECOVERAGE-50
        test 'net.sourceforge.cobertura:cobertura:1.9.4.1'
        test 'org.easymock:easymock:3.1'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        runtime ":resources:1.1.6"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.4"

        build ":tomcat:$grailsVersion"

        runtime ":database-migration:1.2.1"
        runtime ':twitter-bootstrap:2.2.2'
        runtime ':fields:1.3'

        compile ':cache:1.0.1'
        compile ":shiro:1.1.4"
        compile ":lesscss-resources:1.3.1"

        compile ":mail:1.0.1"
	    test ":code-coverage:1.2.5"
    }
}

coverage {
    enabledByDefault = true
    nopost = false
    xml = false
    exclusions = [
            "**/com/wireblend/Abstract*",
            "**/taglib/**",
            "**/twitter/bootstrap/scaffolding/**",
            "**/com/wireblend/DbRealm*",
            "**/com/wireblend/SecurityFilters*",
            "**/com/wireblend/AuthController*"
    ]
}
