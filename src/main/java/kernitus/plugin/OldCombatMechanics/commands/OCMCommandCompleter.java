/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kernitus.plugin.OldCombatMechanics.commands.OCMCommandHandler.Subcommand;

/**
 * Provides tab completion for OCM commands
 */
public class OCMCommandCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length < 2) {
            completions.addAll(Arrays.stream(Subcommand.values())
                    .filter(arg -> arg.toString().startsWith(args[0]))
                    .filter(arg -> OCMCommandHandler.checkPermissions(sender, arg))
                    .map(Enum::toString).collect(Collectors.toList()));
        }

        return completions;
    }
}
