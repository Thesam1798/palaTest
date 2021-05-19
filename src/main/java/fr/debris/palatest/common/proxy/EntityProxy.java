/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 18/05/2021 : 14:42
 */
package fr.debris.palatest.common.proxy;

import cpw.mods.fml.common.registry.EntityRegistry;
import fr.debris.palatest.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class EntityProxy {

    public EntityProxy(Class<? extends Entity> entity, String name, int solidColor, int sopColor) {
        int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entity, name, id);
        EntityRegistry.registerModEntity(entity, name, id, Main.instance, 64, 1, true);

        if (EntityList.entityEggs != null) {
            EntityList.entityEggs.put(id, new EntityList.EntityEggInfo(id, solidColor, sopColor));
        }
    }
}
