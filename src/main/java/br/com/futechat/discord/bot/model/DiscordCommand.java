package br.com.futechat.discord.bot.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscordCommand(String id, @JsonProperty("application_id") String applicationId, String version,
		@JsonProperty("default_permission") boolean defaultPermission,
		@JsonProperty("default_member_permissions") Object defaultMemberPermissions, int type, String name,
		String description, @JsonProperty("dm_permission") boolean dmPermission, List<Option> options) {
}
