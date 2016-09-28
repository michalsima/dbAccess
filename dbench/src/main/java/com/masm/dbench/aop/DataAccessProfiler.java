package com.masm.dbench.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.masm.dbench.TableHelper;

@Aspect
public class DataAccessProfiler {

	@Pointcut("execution(* com.masm.dbench.Benchmark.*(..))")
	public void businessMethods() {
	}

	@Around("businessMethods()")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {

		long start = System.currentTimeMillis();

		System.out.println("############# START");
		Object output = pjp.proceed();
		long elapsedTime = System.currentTimeMillis() - start;

		TableHelper.addIterationValue2Table(new Double(elapsedTime));
		System.out.println("############# STOP - execution time: " + elapsedTime + " milliseconds.");
		return output;
	}

}