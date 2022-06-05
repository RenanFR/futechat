package br.com.futechat.discord.api.model;

import java.util.List;

public record ApiFootballResponse<R, P> (String get, P parameters, Object errors, int results,
		ApiFootballPaging paging, List<R> response) {
}
