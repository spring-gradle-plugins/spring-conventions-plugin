/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.spring.gradle.convention

import io.spring.gradle.testkit.junit.rules.TestKit
import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ShowcaseITest extends Specification {
	@Rule final TestKit testKit = new TestKit()

	def "build"() {
		when:
		BuildResult result = testKit.withProjectResource("samples/showcase/")
				.withArguments('build','--stacktrace')
				.forwardOutput()
				.build();
		then: 'entire build passes'
		result.output.contains("BUILD SUCCESSFUL")

		and: 'javadoc api works'

		and: 'integration tests run'
		new File(testKit.getRootDir(), 'samples/sgbcs-sample-war/build/integration-test-results/').exists()
		new File(testKit.getRootDir(), 'samples/sgbcs-sample-war/build/reports/integration-tests/').exists()
	}

	def "springio"() {
		when:
		BuildResult result = testKit.withProjectResource("samples/showcase/")
				.withArguments('springIoTest','--stacktrace')
				.forwardOutput()
				.build();
		then:
		result.output.contains("SUCCESS")
	}

	def "install"() {
		when:
		BuildResult result = testKit.withProjectResource("samples/showcase/")
				.withArguments('install','--stacktrace')
				.build();
		then:
		result.output.contains("SUCCESS")

		and: 'pom contains dependency management'
		File pom = new File(testKit.getRootDir(), 'sgbcs-core/build/poms/pom-default.xml')
		pom.exists()
		pom.getText().contains("<dependencyManagement>")
	}
}
