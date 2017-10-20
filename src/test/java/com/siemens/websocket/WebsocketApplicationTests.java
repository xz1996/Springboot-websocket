package com.siemens.websocket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebsocketApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void stringTest() {

		String s = "aaaa#gggghh";
		String[] s1 = s.split("#");
		for (String it : s.split("#")) {
			System.out.println(it);
		}
	}

}
