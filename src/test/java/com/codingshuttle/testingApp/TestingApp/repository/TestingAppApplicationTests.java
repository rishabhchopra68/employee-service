package com.codingshuttle.testingApp.TestingApp.repository;

import com.codingshuttle.testingApp.TestingApp.TestContainerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(TestContainerConfiguration.class)
class TestingAppApplicationTests {

	int add(int a, int b){
		return a+b;
	}

	double divideNumbers(int a, int b){
		try {
			return a/b;
		} catch(ArithmeticException e){
			log.info("error dividing");
			throw new ArithmeticException("error dividing");
		}
	}

	@Test
	@DisplayName("testing add function")
	void firstTest(){
		log.info("test one running ...");
		int a = 10;
		int b = 20;
//		Assertions.assertEquals(30, add(10,20));
		Assertions.assertThat(add(10,20)).isEqualTo(30);
	}

	@Test
	void divideNumbers_whenDenominatorCanBeZero(){
		log.info("running division test");
		int a = 5;
		int b = 0;
//		double result = divideNumbers(5,10);
		Assertions.assertThatThrownBy(() -> divideNumbers(a,b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("error dividing");
	}


	@Test
	void contextLoads() {
	}

}
