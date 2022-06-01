package br.com.futechat.discord.api.model;

import java.time.LocalDate;

public record ApiFootballSeason(int year, LocalDate start, LocalDate end, boolean current, ApiFootballCoverage coverage) {

}
