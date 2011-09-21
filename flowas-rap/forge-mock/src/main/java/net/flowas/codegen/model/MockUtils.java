package net.flowas.codegen.model;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.JavaClass;
public class MockUtils {
	public static void main(String[] args) {
		System.out.println(getConstructorText(getFirstConstructor(File.class)));
	}

	public static List<String> addTo(List<String> list, String imports) {
		if (null != imports && imports.contains("\n")) {
			String[] pairs = imports.split("\n");
			for (String imp : pairs) {
				imp = imp.replace("import", "").replace(";", "").trim();
				if (!list.contains(imp)) {
					list.add(imp);
				}
			}
		}else if(null != imports){
			imports = imports.replace("import", "").replace(";", "").trim();
			if (!list.contains(imports)) {
				list.add(imports);
			}
		}
		return list;
	}

	public static Method getInstanceMethod(Class clasz) {
		for (Method m : clasz.getDeclaredMethods()) {
			if (m.getReturnType().equals(clasz)) {
				return m;
			}
		}
		return null;
	}

	public static Constructor getFirstConstructor(Class clasz) {
		for (Constructor m : clasz.getDeclaredConstructors()) {
			if (m.getModifiers() != Modifier.PRIVATE) {
				return m;
			}
		}
		return null;
	}

	public static String getConstructorText(Constructor constructor) {
		String var = "";
		String paramText = "";
		Class[] params = constructor.getParameterTypes();
		if(params.length>0){
			var="Object[] arguments = invocation.getArguments();\n";
		}
		for (int i = 0; i < params.length; i++) {
			//var += params[i].getName() + " cParam" + i + " = null;\n";
			//paramText += "cParam" + i + ",";
			paramText += "("+params[i].getName()+")arguments[" + i + "],";
		}
		
		String simpleName = constructor.getName();
		if (simpleName.contains(".")) {
			String[] pkgs = simpleName.split("\\.");
			simpleName = pkgs[pkgs.length - 1];
		}
		if (paramText.endsWith(",")) {
			paramText = paramText.substring(0, paramText.length() - 1);
		}
		return var + simpleName + " mock" + simpleName + " = new " + simpleName
				+ "(" + paramText + ");";
	}
	public static String getPointcutCode(Constructor constructor) {
		String hasArg="PowerMockito.whenNew(File.class).withArguments(Mockito.anyString()).thenAnswer(answerFile);";
		return null;		
	}
	public static void prepareFor(JavaClass cl, String className) {
		String simpleName = className;
		if (simpleName.contains(".")) {
			String[] pkgs = simpleName.split("\\.");
			simpleName = pkgs[pkgs.length - 1];
		}
		if (cl.hasAnnotation("org.powermock.core.classloader.annotations.PrepareForTest")) {
			Annotation<JavaClass> prepare = cl
					.getAnnotation("org.powermock.core.classloader.annotations.PrepareForTest");
			String value = prepare.getLiteralValue();
			if (!value.contains(simpleName)) {
				if (value.contains("{")) {
					value = value.replace("{", "");
					value = value.replace("}", "");
				}
				value = "{" + value + "," + simpleName + ".class}";
				prepare.setLiteralValue(value);
			}			
		} else {
			cl.addAnnotation(
					"org.powermock.core.classloader.annotations.PrepareForTest")
					.setLiteralValue("{" + simpleName + ".class}");
		}
	}
}
