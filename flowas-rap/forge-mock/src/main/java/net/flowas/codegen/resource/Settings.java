/**
 * Copyright (C) 2010 http://flowas.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You can write to flowas@gmial.com for more customer requirement.
 */
package net.flowas.codegen.resource;
import java.util.List;
public class Settings {	
	private static GenEnum testFramework=GenEnum.JUNIT;
	private static GenEnum mockFramework=GenEnum.POWERMOCK;
	public static GenEnum getTestFramework() {
		return testFramework;
	}
	public static void setTestFramework(GenEnum testFramework) {
		Settings.testFramework = testFramework;
	}
    


	public static GenEnum getMockFramework() {
		return mockFramework;
	}
	public static void setMockFramework(GenEnum mockFramework) {
		Settings.mockFramework = mockFramework;
	}
	
}
