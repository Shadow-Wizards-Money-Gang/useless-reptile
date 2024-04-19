package nordmods.uselessreptile.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import nordmods.uselessreptile.UselessReptile;
import nordmods.uselessreptile.common.entity.base.URDragonEntity;
import nordmods.uselessreptile.common.gui.URDragonScreenHandler;

public abstract class URDragonScreen<T extends ScreenHandler> extends HandledScreen<T> {
    protected static final Identifier TEXTURE = new Identifier(UselessReptile.MODID,"textures/gui/dragon_inventory.png");
    private int mouseX;
    private int mouseY;
    private final URDragonEntity entity;
    private int i;
    private int j;
    protected boolean hasArmor = false;
    protected boolean hasSaddle = false;
    protected boolean hasBanner = false;
    public static int entityToRenderID;
    protected URDragonScreenHandler.StorageSize storageSize = URDragonScreenHandler.StorageSize.NONE;

    public URDragonScreen(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        PlayerEntity player = inventory.player;
        entity = (URDragonEntity) player.getWorld().getEntityById(entityToRenderID);
    }

    @Override
    protected void drawBackground(MatrixStack context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        i = (width - backgroundWidth) / 2;
        j = (height - backgroundHeight) / 2;
        drawTexture(context, i, j, 0, 0, backgroundWidth, backgroundHeight);
        drawSaddle(context);
        drawBanner(context);
        drawArmor(context);
        drawStorage(context);
        drawEntity(context);
    }

    @Override
    public void render(MatrixStack context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected void drawSaddle(MatrixStack context) {
        if (hasSaddle) drawTexture(context, i + 7, j + 35 - 18, 0, backgroundHeight + 54 - (entity.getEquippedStack(EquipmentSlot.FEET).isEmpty() ? 0 : 18), 18, 18); //saddle
    }

    protected void drawArmor(MatrixStack context) {
        if (hasArmor) {
            drawTexture(context, i + 7 + 18 + 54, j + 35 - 18, 18, backgroundHeight + 54 - (entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty() ? 0 : 18), 18, 18); //head
            drawTexture(context, i + 7 + 18 + 54, j + 35, 18 * 2, backgroundHeight + 54 - (entity.getEquippedStack(EquipmentSlot.CHEST).isEmpty() ? 0 : 18), 18, 18); //body
            drawTexture(context, i + 7 + 18 + 54, j + 35 + 18, 18 * 3, backgroundHeight + 54 - (entity.getEquippedStack(EquipmentSlot.LEGS).isEmpty() ? 0 : 18), 18, 18); //tail
        }
    }

    protected void drawEntity(MatrixStack context) {
        if (entity != null) drawEntity(context, i + 26, j + 18, i + 78, j + 70, 13, this.mouseX, this.mouseY, this.entity);
    }

    private void drawEntity(MatrixStack context, int x1, int y1, int x2, int y2, int size, float mouseX, float mouseY, LivingEntity entity) {
        float centerX = (x1 + x2) / 2f;
        float centerY = (y1 + y2) / 2f;
        float dx = (float)Math.atan((centerX - mouseX) / 40f);
        float dy = (float) Math.atan((centerY - mouseY) / 40f);
        float tickDelta = MinecraftClient.getInstance().getTickDelta();

        context.push();

        context.translate(centerX, centerY, 50);
        context.scale(size, size, -size);
        RenderSystem.applyModelViewMatrix();
        context.translate(0, entity.getHeight() / 2f + 0.4f, 0);
        context.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-dy * 20 + 180));
        context.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-dx * 40 + entity.getYaw(tickDelta)));

        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadows(false);
        DiffuseLighting.method_34742();
        entityRenderDispatcher.render(entity, 0, 0, 0, 0, tickDelta, context, immediate, 15728880);
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        context.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

    protected void drawStorage(MatrixStack context) {
        int size = storageSize.getSize()/3;
        int offset = hasArmor ? 1 : 0;
        drawTexture(context, i + 79 + 18 * offset, j + 17, 0, this.backgroundHeight, size * 18, 54);
    }

    protected void drawBanner(MatrixStack context) {
        if (hasBanner) drawTexture(context, i + 7, j + 35, 18 * 4, backgroundHeight + 54 - (entity.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty() ? 0 : 18), 18, 18); //banner
    }
}
