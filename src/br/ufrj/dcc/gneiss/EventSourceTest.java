package br.ufrj.dcc.gneiss;

import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class EventSourceTest extends TestCase {
	private EventSource event;

	@Before
	public void setUp() throws Exception {
		event = new EventSource();
	}

	@Test
	public void shouldSaveWhenEmpty() {
		assertEquals("{}\n", event.save());
	}
	
	@Test
	public void shouldSaveWhenSingleField() {
		event.set("key0", "value0");
		
		assertEquals("{ \"key0\": \"value0\" }\n", event.save());
	}
	
	@Test
	public void shouldSaveWhenMultipleFields() {
		event.set("key0", "value0");
		event.set("key1", 1);
		event.set("key2", true);
		event.set("key3", false);
		
		assertEquals("{ \"key0\": \"value0\", \"key1\": 1, \"key2\": true, \"key3\": false }\n", event.save());
	}
	
	class FakeOutputStream extends OutputStream {
		StringBuffer buffer = new StringBuffer();
		
		@Override
		public void write(int arg) throws IOException {
			int raw[] = { arg };
			
			buffer.append(new String(raw, 0, 1));
		}

		public String getOutput() {
			return buffer.toString();
		}
	}
	
	@Test
	public void shouldFlushToOutputStream() {
		event.set("key0", 0);
		
		FakeOutputStream out = new FakeOutputStream();
		assertTrue(event.flush(out));
		assertEquals("{ \"key0\": 0 }\n", out.getOutput());
	}
	
	@Test
	public void shouldSaveMultipleEvents() {
		event.set("key0", 0);
		assertEquals("{ \"key0\": 0 }\n", event.save());
		
		event.set("key1", 1);
		assertEquals("{ \"key1\": 1 }\n", event.save());
		
		event.set("key2", 2);
		assertEquals("{ \"key2\": 2 }\n", event.save());
	}
}