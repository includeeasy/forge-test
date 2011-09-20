package net.flowas.mock.jdbc;


import java.io.InputStream;

import org.junit.Test;

public class Hello {

	@Test
	public void test() {
		InputStream tables = DatabaseIntializer.class.getResourceAsStream("/tables.sql");
		InputStream in = DatabaseIntializer.class.getResourceAsStream("/data.sql");
		DatabaseIntializer.clean();
		DatabaseIntializer.init(new InputStream[]{tables,in});
	}

}
