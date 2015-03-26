package edu.csupomona.cs585.awsmetrics;

import java.util.Arrays;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

public class CloudWatchFactory {
	
	private static CloudWatchFactory INSTANCE;

	public static CloudWatchFactory get() {
		if (INSTANCE == null) {
			INSTANCE = new CloudWatchFactory();
		}
		return INSTANCE;
	}

	/** CloudWatch client */
	private final AmazonCloudWatch cloudWatchClient;

	private CloudWatchFactory() {
		this.cloudWatchClient = new AmazonCloudWatchClient(
    			new BasicAWSCredentials("", ""));
	}

	/**
	 * Publish the server API metrics to CloudWatch
	 *
	 * @param apiName the API path name
	 * @param timeElapsedInMS the time elapsed in milliseconds
	 * @param isSuccess whether the API is executed successfully
	 */
	public void publishAPIMetrics(String apiName, double timeElapsedInMS, boolean isSuccess) {
		MetricDatum timeDatum = new MetricDatum();
		timeDatum.setMetricName(apiName + "-TimeElapsedInMS");
		timeDatum.setUnit(StandardUnit.Milliseconds);
		timeDatum.setValue(timeElapsedInMS);
		timeDatum.setDimensions(Arrays.asList(
				new Dimension()
				.withName("AutomatedMetrics")
				.withValue("Annotation-aop")));

		// form the success/failure count metrics data
		MetricDatum successDatum = new MetricDatum();
		successDatum.setUnit(StandardUnit.Count);
		successDatum.setValue(1.0);
		successDatum.setDimensions(Arrays.asList(
				new Dimension()
				.withName("AutomatedMetrics")
				.withValue("Annotation-aop")));

		if (isSuccess) {
			successDatum.setMetricName(apiName + "-SuccessCount");
		} else {
			successDatum.setMetricName(apiName + "-FailureCount");
		}

		// publish the metrics to Cloud Watch
		PutMetricDataRequest putMetricDataRequest = new PutMetricDataRequest();
		putMetricDataRequest.setNamespace("iphoto-web");
		putMetricDataRequest.setMetricData(Arrays.asList(timeDatum, successDatum));

		// send the request
		cloudWatchClient.putMetricData(putMetricDataRequest);
	}

}



