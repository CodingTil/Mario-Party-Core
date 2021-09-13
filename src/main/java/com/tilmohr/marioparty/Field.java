package com.tilmohr.marioparty;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public record Field(@NotNull Location location, int value, String name, int index) {
}
