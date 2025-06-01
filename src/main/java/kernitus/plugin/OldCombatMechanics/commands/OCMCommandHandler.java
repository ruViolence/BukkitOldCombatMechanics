/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.commands;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import kernitus.plugin.OldCombatMechanics.utilities.Config;
import kernitus.plugin.OldCombatMechanics.utilities.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class OCMCommandHandler implements CommandExecutor {
    private static final String NO_PERMISSION = "&cYou need the permission '%s' to do that!";

    private final OCMMain plugin;

    enum Subcommand {reload }

    public OCMCommandHandler(OCMMain instance) {
        this.plugin = instance;
    }

    private void help(OCMMain plugin, CommandSender sender) {
        final PluginDescriptionFile description = plugin.getDescription();

        Messenger.sendNoPrefix(sender, ChatColor.DARK_GRAY + Messenger.HORIZONTAL_BAR);
        Messenger.sendNoPrefix(sender, "&6&lOldCombatMechanics&e by &ckernitus&e and &cRayzr522&e version &6%s", description.getVersion());

        if (checkPermissions(sender, Subcommand.reload))
            Messenger.sendNoPrefix(sender, "&eYou can use &c/ocm reload&e to reload the config file");

        Messenger.sendNoPrefix(sender, ChatColor.DARK_GRAY + Messenger.HORIZONTAL_BAR);
    }

    private void reload(CommandSender sender) {
        Config.reload();
        Messenger.sendNoPrefix(sender, "&6&lOldCombatMechanics&e config file reloaded");
    }

    /*
    private void test(OCMMain plugin, CommandSender sender) {
        final Location location = sender instanceof Player ?
                ((Player) sender).getLocation() :
                sender.getServer().getWorlds().get(0).getSpawnLocation();

        new InGameTester(plugin).performTests(sender, location);
    }
     */

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length < 1) {
            help(plugin, sender);
        } else {
            try {
                try {
                    final Subcommand subcommand = Subcommand.valueOf(args[0].toLowerCase(Locale.ROOT));
                    if (checkPermissions(sender, subcommand, true)) {
                        switch (subcommand) {
                            case reload:
                                reload(sender);
                                break;
                                /*
                            case test:
                                test(plugin, sender);
                                break;
                                 */
                            default:
                                throw new CommandNotRecognisedException();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    throw new CommandNotRecognisedException();
                }
            } catch (CommandNotRecognisedException e) {
                Messenger.send(sender, "Subcommand not recognised!");
            }
        }
        return true;
    }

    private static class CommandNotRecognisedException extends IllegalArgumentException {
    }

    static boolean checkPermissions(CommandSender sender, Subcommand subcommand) {
        return checkPermissions(sender, subcommand, false);
    }

    static boolean checkPermissions(CommandSender sender, Subcommand subcommand, boolean sendMessage) {
        final boolean hasPermission = sender.hasPermission("oldcombatmechanics." + subcommand);
        if (sendMessage && !hasPermission)
            Messenger.sendNoPrefix(sender, NO_PERMISSION, "oldcombatmechanics." + subcommand);
        return hasPermission;
    }
}