package hortonworks.hdp.refapp.falcon.management;

public class FalconDeploymentParams {

	private boolean scheduleIndexingDataPipeline;
	private boolean schedulePhoenixUpdateDataPipeline;

	public void setSchedulePhoenixUpdateDataPipeline(
			boolean schedulePhoenixUpdateDataPipeline) {
		this.schedulePhoenixUpdateDataPipeline = schedulePhoenixUpdateDataPipeline;
	}

	public void setScheduleIndexingDataPipeline(boolean scheduleIndexingDataPipeline) {
		this.scheduleIndexingDataPipeline = scheduleIndexingDataPipeline;
	}

	public boolean isScheduleIndexingDataPipeline() {
		return this.scheduleIndexingDataPipeline;
	}

	public boolean isSchedulePhoenixUpdateDataPipeline() {
		return this.schedulePhoenixUpdateDataPipeline;
	}

	
}
