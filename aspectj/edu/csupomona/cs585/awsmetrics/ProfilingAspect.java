package edu.csupomona.cs585.awsmetrics;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class ProfilingAspect {
	long startTime;
	long endTime;
	String annotElement;
	String methodName;
	
	@Before("execution(* *.*(..)) && @annotation(edu.csupomona.cs585.awsmetrics.ProfiledAOP)")
	public void startMetric(JoinPoint joinPoint){
		methodName = joinPoint.getSignature().getName();
		System.out.print("********	Method: " + methodName);
		startTime = System.currentTimeMillis();
	}
	
	@After("execution(* *.*(..)) && @annotation(edu.csupomona.cs585.awsmetrics.ProfiledAOP)")
	public void endMetrics(JoinPoint joinPoint){
		endTime = System.currentTimeMillis() - startTime;
		System.out.println(" with execution time: " + endTime);
	}
}