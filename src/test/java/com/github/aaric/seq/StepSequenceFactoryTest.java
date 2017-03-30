package com.github.aaric.seq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bocom.jump.bp.service.id.seq.StepSequenceFactory;

/**
 * StepSequenceFactory测试类
 * 
 * @author Aaric
 * @since 2017-03-30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context-Test.xml", "classpath:spring-seq-Test.xml" })
public class StepSequenceFactoryTest {

	@Autowired
	@Qualifier("type01StepSequenceFactory")
	private StepSequenceFactory stepSequenceFactory;

	@Test
	public void testGen() throws Exception {
		for(int i = 0; i < 50; i++) {
			System.err.println(stepSequenceFactory.create());
		}
	}

}
