/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 18/05/2021 : 14:34
 */
package fr.debris.palatest.client.model;

import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.entity.EntityGolem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderProxy extends RenderLiving {
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/entity/golem.png");

    public RenderProxy(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityGolem entity) {
        return texture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((EntityGolem) entity);
    }
}
