package br.com.futechat.discord.strategy;

public enum DataFetchingStrategyTypes {
	
	CACHE_AND_API(DataFetchingStrategyTypes.CACHE_AND_API_NAME),
	API_ALWAYS(DataFetchingStrategyTypes.API_ALWAYS_NAME);

	public static final String CACHE_AND_API_NAME = "cacheAndApi";
	public static final String API_ALWAYS_NAME = "apiAlways";

	String strategyName;

	DataFetchingStrategyTypes(String strategyName) {
		this.strategyName = strategyName;
	}

	public String getStrategyName() {
		return strategyName;
	}

}
