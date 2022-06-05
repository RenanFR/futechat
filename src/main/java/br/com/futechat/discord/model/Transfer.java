package br.com.futechat.discord.model;

import java.time.LocalDate;

public record Transfer(LocalDate date, String type, String teamIn, String teamOut) {

}
