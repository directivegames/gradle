package gradlebuild.performance

class PerformanceTestIntegrationTest extends AbstractIntegrationTest {
    def "honors branch name in channel"() {
        buildFile << """
            plugins {
                id 'java-library'
                id 'gradlebuild.module-identity'
                id 'gradlebuild.dependency-modules'
            }
            ext {
                libraries = ['junit5Vintage': [coordinates: 'org.junit.vintage:junit-vintage-engine', version: '5.6.2']]
            }
            subprojects {
                apply plugin: 'java'
            }
            apply plugin: 'gradlebuild.performance-test'

            def distributedPerformanceTests = tasks.withType(gradlebuild.performance.tasks.DistributedPerformanceTest)
            distributedPerformanceTests.all {
                // resolve these tasks
            }
            task assertChannel {
                doLast {
                    distributedPerformanceTests.each { distributedPerformanceTest ->
                        assert distributedPerformanceTest.channel.endsWith("-myBranch")
                    }
                }
            }
        """

        file("version.txt") << '6.5'
        settingsFile << """
            include 'internalIntegTesting', 'internalPerformanceTesting', 'docs', 'launcher', 'apiMetadata', 'distributionsFull'
        """
        expect:
        build("assertChannel")
    }
}
