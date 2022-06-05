package br.com.futechat.discord.model;

import java.util.List;

public record PlayerTransferHistory(Player player, List<Transfer> transfers) {

}
