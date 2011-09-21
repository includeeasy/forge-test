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
package net.flowas.codegen.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jboss.forge.project.Project;
import org.jboss.forge.shell.Shell;

@Named
// @ApplicationScoped
public class OperationUtils {
	private final Project project;
	private final Shell shell;

	@Inject
	public OperationUtils(final Project project, final Shell shell) {
		this.project = project;
		this.shell = shell;
	}

	private File openFile() {
		JFileChooser jfChooser = new JFileChooser(project.getProjectRoot()
				.getFullyQualifiedName());
		jfChooser.setDialogTitle("打开文件");
		jfChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith("java") || f.getName().endsWith("xml")
						|| f.isDirectory())
					return true;
				return false;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "数值型数据(*.java,*.xml)";
			}
		});
		int result = jfChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) { // 确认打开
			File fileIn = jfChooser.getSelectedFile();
			return fileIn;
		} else {
			return null;
		}
	}

	public File getFile(String ui, String fileName) {
		if (fileName == null || null != ui) {
			return openFile();
		}
		File file = new File(fileName);
		return file;
	}

	public void unzip(InputStream in) throws IOException {
		ZipInputStream zi = new ZipInputStream(in);
		ZipEntry entry;
		while ((entry = zi.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				continue;
			}
			File file = new File(project.getProjectRoot().getFullyQualifiedName() + "/"
					+ entry.getName());
			if (!file.exists()) {
				file.mkdirs();
			}
			file.delete();
			FileOutputStream out = new FileOutputStream(file);
			int count;
			byte data[] = new byte[1000];
			while ((count = zi.read(data, 0, 1000)) != -1) {
				out.write(data, 0, count);
			}
			out.close();
			System.out.println(file.getAbsolutePath());
		}
		System.out.println("project:"+project.getProjectRoot().getFullyQualifiedName());
	}

	public void unzip(InputStream in, UnzipFilter fillter) throws IOException {
		ZipInputStream zi = new ZipInputStream(in);
		ZipEntry entry;
		while ((entry = zi.getNextEntry()) != null) {
			String path = entry.getName();
			if (entry.isDirectory()) {
				continue;
			}
			ByteArrayOutputStream bitout = new ByteArrayOutputStream();
			int count;
			byte data[] = new byte[1000];
			while ((count = zi.read(data, 0, 1000)) != -1) {
				bitout.write(data, 0, count);
			}
			bitout.flush();
			byte[] filtedData;
			if (fillter != null) {
				filtedData = fillter.filte(entry.getName(),
						bitout.toByteArray());
				if (filtedData == null) {
					continue;
				} else {
					filtedData = bitout.toByteArray();
					path = fillter.getFileName();
				}
			} else {
				filtedData = bitout.toByteArray();
			}
			bitout.close();
			File file = new File(project.getProjectRoot().getFullyQualifiedName() + "/" + path);
			if (!file.exists()) {
				file.mkdirs();
			}
			file.delete();
			FileOutputStream out = new FileOutputStream(file);
			out.write(filtedData);
			out.close();
			System.out.println(path);
		}
	}

	interface UnzipFilter {
		String getFileName();

		byte[] filte(String fileName, byte[] data);
	}
}
