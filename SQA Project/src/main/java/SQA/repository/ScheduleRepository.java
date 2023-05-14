package SQA.repository;

import java.util.List;

import SQA.model.Station;
import SQA.model.Train;
import SQA.model.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepository extends JpaRepository<TrainSchedule, Long>{
	
	
	TrainSchedule findByStopAndTrain(Station stop, Train train);
	
	List<TrainSchedule> findByStopAndDepartTimeGreaterThanEqual(Station stop, String departTime);
	
	List<TrainSchedule> findByStopAndArrivalTimeGreaterThan(Station stop, String departTime);
	
	List<TrainSchedule> findByStopAndDepartTime(Station stop, String departTime);
	
}
