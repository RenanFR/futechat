package br.com.futechat.discord.api.model;

public record ApiFootballPlayer(int id, String name, String firstname, String lastname, int age, ApiFootballPlayerBirth birth, String nationality,
		String height, String weight, boolean injured, String photo) {

}
