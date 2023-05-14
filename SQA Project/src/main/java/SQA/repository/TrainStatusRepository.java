package SQA.repository;

import SQA.model.Train;
import SQA.model.TrainStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainStatusRepository extends JpaRepository<TrainStatus, Long>{
	
	TrainStatus findByTrainAndDate(Train train, String date);

}
