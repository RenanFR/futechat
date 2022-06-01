package br.com.futechat.discord.api.model;

import java.util.List;
import java.util.Map;

public record ApiFootballResponse<T> (String get, Map<String, String> parameters, List<String> errors, int results,
		ApiFootballPaging paging, List<T> response) {
}
