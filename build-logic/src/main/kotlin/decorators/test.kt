package decorators

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestResult.ResultType
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.gradle.kotlin.dsl.withType

fun Project.configureTest() {
    tasks.withType<Test>().all {
        useJUnitPlatform {
            systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
        }

        testLogging.run {
            events = setOf(FAILED, PASSED, SKIPPED, STANDARD_OUT)
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true

            debug {
                events = setOf(STARTED, FAILED, PASSED, SKIPPED, STANDARD_ERROR, STANDARD_OUT)
                exceptionFormat = TestExceptionFormat.FULL
            }

            info.events = debug.events
            info.exceptionFormat = debug.exceptionFormat

            val failedTests = mutableListOf<TestDescriptor>()
            val skippedTests = mutableListOf<TestDescriptor>()
            fun MutableList<TestDescriptor>.reportIfNeeded(message: String) {
                if (isEmpty()) return

                logger.lifecycle(message)
                asSequence().forEach { tesDescriptor ->
                    parent?.let { parent ->
                        logger.lifecycle("\t\t${parent.name} - ${tesDescriptor.name}")
                    } ?: logger.lifecycle("\t\t${tesDescriptor.name}")
                }
            }

            addTestListener(object : TestListener {
                override fun beforeSuite(suite: TestDescriptor) = Unit
                override fun beforeTest(testDescriptor: TestDescriptor) = Unit
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                    when (result.resultType) {
                        ResultType.FAILURE -> failedTests.add(testDescriptor)
                        ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                        else -> Unit
                    }
                }

                override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    if (suite.parent == null) { // root suite
                        logger.lifecycle("----")
                        logger.lifecycle("Test result: ${result.resultType}")
                        logger.lifecycle(
                            "Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped"
                        )

                        failedTests.reportIfNeeded("\tFailed Tests:")
                        skippedTests.reportIfNeeded("\tSkipped Tests:")
                    }
                }
            })
        }
    }
}
