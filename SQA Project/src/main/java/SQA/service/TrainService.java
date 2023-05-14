package SQA.service;

import SQA.model.OneWayList;
import SQA.model.SearchContent;

public interface TrainService {
	
	public boolean verfiyDateAndTime(SearchContent content, OneWayList result);
	
	public void searchOneWay(SearchContent content, OneWayList result);
	
	

}
