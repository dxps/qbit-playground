package qbit.samples.recommendationworkers;

import io.advantageous.qbit.reactive.Callback;

import java.util.List;

/**
 * This is assumed to be a CPU intensive service.
 *
 * TODO: to-be-completed
 */
public interface RecommendationServiceClient {
	
	void recommend(final Callback<List<Recommendation>> callback, String username);
	
}
