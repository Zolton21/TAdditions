package net.tadditions.mod.helper;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

import java.util.List;

public interface IConsoleHelp {

   List<RegistryKey<World>> getAvailable();
   boolean getCloakState();
   void setCloakState(boolean state);

   void removeAvailable(RegistryKey<World> type);
   void addAvailable(RegistryKey<World> type);

}
