package br.com.futechat.discord.api.model;

import java.util.List;

public record ApiFootballResponse<R> (String get, Object parameters, Object errors, int results,
		ApiFootballPaging paging, List<R> response) {
}
