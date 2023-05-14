package SQA.service;

import java.util.List;

import SQA.model.Train;

public interface AdminService {
	
	public void populateTrainTable();
	
	public void populateTrainStatus();
	
	public void trainCancel (String trainName, String date);
	
	public void updateTrainCapacity(int capacity);
	
	public void reset();
	
	public List<Train> showTrainCapacity();
	
	
}
