package qbit.samples.recommendationworkers;

import java.util.ArrayList;
import java.util.List;

/**
 * to-be-completed
 */
public class RecommendationService {
	
	private UserDataServiceClient userDataServiceClient;
	
	public RecommendationService(UserDataServiceClient userDataServiceClient) {
		this.userDataServiceClient = userDataServiceClient;
	}
	
	List<Recommendation> recommend(String username) {
		return new ArrayList<>(0);
	}
	
}
